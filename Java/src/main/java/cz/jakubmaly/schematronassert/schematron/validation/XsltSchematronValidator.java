package cz.jakubmaly.schematronassert.schematron.validation;

import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.apache.commons.io.*;
import org.apache.commons.lang.*;
import org.slf4j.*;

import cz.jakubmaly.schematronassert.*;
import cz.jakubmaly.schematronassert.io.*;
import cz.jakubmaly.schematronassert.schematron.model.*;
import cz.jakubmaly.schematronassert.schematron.serialization.*;
import cz.jakubmaly.schematronassert.svrl.model.*;
import cz.jakubmaly.schematronassert.svrl.serialization.*;
import cz.jakubmaly.schematronassert.utils.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XsltSchematronValidator implements SchematronValidator {

	private static Logger logger = LoggerFactory.getLogger(XsltSchematronValidator.class);
	private static Logger svrlTraceLogger = LoggerFactory.getLogger(
		TestBuilder.class.getPackage().getName() + ".intermediate.svrl");
	private static Logger schemaTraceLogger = LoggerFactory
		.getLogger(TestBuilder.class.getPackage().getName() + ".intermediate.schematron");

	private static net.sf.saxon.TransformerFactoryImpl pipelineTransformerFactory;
	private static Transformer iso_dsdl_include;
	private static Transformer iso_abstract_expand;
	private static Transformer iso_svrl_for_xslt2;

	private boolean autoRecognizeNamespaces;
	private String xpathDefaultNamespace;
	private DocumentNamespaceLookup documentNamespaceLookup = new DocumentNamespaceLookup();
	private SvrlDeserializer svrlDeserializer = new SvrlDeserializerImpl();
	private TransformerFactory transformerFactory;
	private LoggingErrorListener errorListener;

    private boolean useCache = false;
    private URIResolver uriResolver;
    private static final Map<String, Transformer> transformerCache = new ConcurrentHashMap<String, Transformer>();
    
	static {
		pipelineTransformerFactory = new net.sf.saxon.TransformerFactoryImpl();
		pipelineTransformerFactory.setURIResolver(new ResourceURIResolver("/xslt/"));

		try {
			iso_dsdl_include = initializeTransformer("/xslt/iso_dsdl_include.xsl");
			iso_abstract_expand = initializeTransformer("/xslt/iso_abstract_expand.xsl");
			iso_svrl_for_xslt2 = initializeTransformer("/xslt/iso_svrl_for_xslt2.xsl");
		} catch (TransformerException ex) {
			logger.error("Failed XsltSchematronValidator initialization, failed to load Schematron XSLTs", ex);
		}
	}

	static Transformer initializeTransformer(String xsltFilePath) throws TransformerException {
		InputStream xsltStream = XsltSchematronValidator.class.getResourceAsStream(xsltFilePath);      
		try {
			StreamSource xsltSource = new StreamSource(xsltStream);
			Transformer transformer = pipelineTransformerFactory.newTransformer(xsltSource);
			return transformer;
		} finally {
			IOUtils.closeQuietly(xsltStream);
		}
	}

    public XsltSchematronValidator(boolean useCache, URIResolver uriResoolver) {
        this();
        this.useCache = useCache;
        this.uriResolver = uriResoolver;
    }
    
	public XsltSchematronValidator() {
		transformerFactory = new net.sf.saxon.TransformerFactoryImpl();
		errorListener = new LoggingErrorListener();
		transformerFactory.setErrorListener(errorListener);

	}

    /**
     * Allow a user to clear the caches.
     */
    public static void clearCaches() {
        transformerCache.clear();
    }
    
	public void validate(StreamSource xmlSource, StreamSource schemaSource, Result outputTarget)
			throws ValidationException {
        
		StringWriter step1writer = new StringWriter();
		StringWriter step2writer = new StringWriter();
		StringWriter step3writer = new StringWriter();
		StringReader step4Reader = null;
		try {
            
            if (useCache 
                    && schemaSource.getSystemId() != null && !schemaSource.getSystemId().isEmpty() 
                    && transformerCache.containsKey(schemaSource.getSystemId())) {
                logger.debug(">>>>> Found cached transformer for key " + schemaSource.getSystemId());
                Transformer finalTransformer = transformerCache.get(schemaSource.getSystemId());
                synchronized (finalTransformer) {
                    finalTransformer.transform(xmlSource, outputTarget);
                }                
            } else {
                iso_dsdl_include.transform(schemaSource, new StreamResult(step1writer));
                String step1string = step1writer.toString();

                performSchematronStep(iso_abstract_expand, step1string, step2writer, null);
                String step2string = step2writer.toString();

                performSchematronStep(iso_svrl_for_xslt2, step2string, step3writer, xpathDefaultNamespace);
                String step3string = step3writer.toString();
                step4Reader = new StringReader(step3string);
                StreamSource step4source = new StreamSource(step4Reader);

                Transformer finalTransformer = transformerFactory.newTransformer(step4source);
                if (uriResolver != null) {
                    finalTransformer.setURIResolver(uriResolver);
                }
                finalTransformer.transform(xmlSource, outputTarget);
                if (useCache
                        && schemaSource.getSystemId() != null && !schemaSource.getSystemId().isEmpty()) {
                    transformerCache.put(schemaSource.getSystemId(), finalTransformer);
                }
            }
		} catch (TransformerException e) {
			StringBuffer message = new StringBuffer("Error during validation");
			errorListener.flushTo(message);
			throw new ValidationException(message.toString(), e);
		} finally {
			IOUtils.closeQuietly(step1writer);
			IOUtils.closeQuietly(step2writer);
			IOUtils.closeQuietly(step3writer);
			IOUtils.closeQuietly(step4Reader);
		}
	}

	public ValidationOutput validate(StreamSource xmlSource, Schema schema) throws ValidationException {
		StringWriter outputWriter = null;
		StringReader schemaReader = null;
		try {
			outputWriter = new StringWriter();
			StreamResult output = new StreamResult(outputWriter);
			validate(xmlSource, schema, output);
			svrlTraceLogger.trace("SVRL output: ");
			svrlTraceLogger.trace(Converter.prettyPrintXml(outputWriter.toString()));
			ValidationOutput validationOutput = svrlDeserializer.deserializeSvrlOutput(
				Converter.createStreamSource(outputWriter.toString()));
			return validationOutput;
		} finally {
			IOUtils.closeQuietly(outputWriter);
			IOUtils.closeQuietly(schemaReader);
		}
	}

	public void validate(StreamSource xmlSource, Schema schema, Result outputTarget) throws ValidationException {
		Writer outputWriter = null;
		StringReader schemaReader = null;
		try {
			if (getAutoRecognizeNamespaces()) {
				getDocumentNamespaceLookup().addNamespacesFromDocumentToSchema(schema, xmlSource);
			}
			SchemaSerializer s = new SchemaSerializer();
			s.setPrettyPrint(false);
			outputWriter = new StringWriter();
			s.serializeSchema(schema, outputWriter);
			String schemaString = outputWriter.toString();
			schemaTraceLogger.trace("Schematron schema: ");
			schemaTraceLogger.trace(Converter.prettyPrintXml(schemaString));
			schemaReader = new StringReader(schemaString);
			StreamSource schemaSource = new StreamSource(schemaReader);
			if (!StringUtils.isEmpty(schema.xpathDefaultNamespace)) {
				setXpathDefaultNamespace(schema.xpathDefaultNamespace);
			}
			validate(xmlSource, schemaSource, outputTarget);
		} finally {
			IOUtils.closeQuietly(schemaReader);
			IOUtils.closeQuietly(outputWriter);
		}
	}

	private void performSchematronStep(Transformer transformer, String input, StringWriter resultWriter, String xpathDefaultNamespace)
			throws ValidationException {
		StringReader inputReader = new StringReader(input);
		try {
			StreamSource inputSource = new StreamSource(inputReader);
			transformer.clearParameters();
			if (xpathDefaultNamespace != null && xpathDefaultNamespace.length() > 0) {
				transformer.setParameter("xpath-default-namespace", xpathDefaultNamespace);
			}
			transformer.transform(inputSource, new StreamResult(resultWriter));
		} catch (TransformerException e) {
			StringBuffer message = new StringBuffer("Error during validation");
			errorListener.flushTo(message);
			throw new ValidationException(message.toString(), e);
		} finally {
			IOUtils.closeQuietly(inputReader);
		}
	}

	public ValidationOutput validate(String xmlText, Schema schema) throws ValidationException {
		StreamSource xmlStream = Converter.createStreamSource(xmlText);
		return validate(xmlStream, schema);
	}

	public boolean getAutoRecognizeNamespaces() {
		return this.autoRecognizeNamespaces;
	}

	public void setAutoRecognizeNamespaces(boolean autoRecognizeNamespaces) {
		this.autoRecognizeNamespaces = autoRecognizeNamespaces;
	}

	public DocumentNamespaceLookup getDocumentNamespaceLookup() {
		return documentNamespaceLookup;
	}

	public void setDocumentNamespaceLookup(DocumentNamespaceLookup documentNamespaceLookup) {
		this.documentNamespaceLookup = documentNamespaceLookup;
	}

	public String getXpathDefaultNamespace() {
		return xpathDefaultNamespace;
	}

	public void setXpathDefaultNamespace(String xpathDefaultNamespace) {
		this.xpathDefaultNamespace = xpathDefaultNamespace;
	}

	public SvrlDeserializer getSvrlDeserializer() {
		return svrlDeserializer;
	}

	public void setSvrlDeserializer(SvrlDeserializer svrlDeserializer) {
		this.svrlDeserializer = svrlDeserializer;
	}
}
