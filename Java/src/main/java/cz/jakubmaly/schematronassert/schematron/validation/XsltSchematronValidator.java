package cz.jakubmaly.schematronassert.schematron.validation;

import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.apache.commons.io.*;
import org.slf4j.*;

import cz.jakubmaly.schematronassert.schematron.model.*;
import cz.jakubmaly.schematronassert.schematron.serialization.*;
import cz.jakubmaly.schematronassert.utils.*;

public class XsltSchematronValidator implements SchematronValidator {

	private static Logger logger = LoggerFactory.getLogger(XsltSchematronValidator.class);
	private static TransformerFactory transformerFactory = new net.sf.saxon.TransformerFactoryImpl();
	private static Transformer iso_dsdl_include;
	private static Transformer iso_abstract_expand;
	private static Transformer iso_svrl_for_xslt2;

	private boolean autoRecognizeNamespaces;
	private DocumentNamespaceLookup documentNamespaceLookup = new DocumentNamespaceLookup();

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

	static {
		transformerFactory.setURIResolver(new ResourceURIResolver("/xslt/"));
		try {
			iso_dsdl_include = initializeTransformer("/xslt/iso_dsdl_include.xsl");
			iso_abstract_expand = initializeTransformer("/xslt/iso_abstract_expand.xsl");
			iso_svrl_for_xslt2 = initializeTransformer("/xslt/iso_svrl_for_xslt2.xsl");
		} catch (TransformerException e) {
			logger.error("Failed XsltSchematronValidator initialization, failed to load Schematron XSLTs", e);
		}
	}

	static Transformer initializeTransformer(String xsltFilePath) throws TransformerException {
		InputStream xsltStream = XsltSchematronValidator.class.getResourceAsStream(xsltFilePath);
		try {
			StreamSource xsltSource = new StreamSource(xsltStream);
			Transformer transformer = transformerFactory.newTransformer(xsltSource);
			return transformer;
		} finally {
			IOUtils.closeQuietly(xsltStream);
		}
	}

	public void validate(StreamSource xmlSource, StreamSource schemaSource, Result outputTarget)
			throws ValidationException {
		validate(xmlSource, schemaSource, outputTarget, null);
	}

	public void validate(StreamSource xmlSource, StreamSource schemaSource, Result outputTarget, String xpathDefaultNamespace)
			throws ValidationException {
		// TODO: better exception checks, consider removing throw
		// TransformerException entirely
		StringWriter step1writer = new StringWriter();
		StringWriter step2writer = new StringWriter();
		StringWriter step3writer = new StringWriter();
		StringReader step4Reader = null;
		try {
			iso_dsdl_include.transform(schemaSource, new StreamResult(step1writer));
			String step1string = step1writer.toString();

			performSchematronStep(iso_abstract_expand, step1string, step2writer, null);
			String step2string = step2writer.toString();

			performSchematronStep(iso_svrl_for_xslt2, step2string, step3writer, xpathDefaultNamespace);
			String step3string = step3writer.toString();
			step4Reader = new StringReader(step3string);
			StreamSource step4source = new StreamSource(step4Reader);
			Transformer finalTransformer = transformerFactory.newTransformer(step4source);
			finalTransformer.transform(xmlSource, outputTarget);
		} catch (TransformerException e) {
			throw new ValidationException("Error during validation", e);
		} finally {
			IOUtils.closeQuietly(step1writer);
			IOUtils.closeQuietly(step2writer);
			IOUtils.closeQuietly(step3writer);
			IOUtils.closeQuietly(step4Reader);
		}
	}

	public String validate(Schema schema, StreamSource xmlSource) throws ValidationException {
		StringWriter outputWriter = null;
		StringReader schemaReader = null;
		try {
			outputWriter = new StringWriter();
			StreamResult output = new StreamResult(outputWriter);
			validate(schema, xmlSource, output);
			return outputWriter.toString();
		} finally {
			IOUtils.closeQuietly(outputWriter);
			IOUtils.closeQuietly(schemaReader);
		}
	}

	public void validate(Schema schema, StreamSource xmlSource, Result outputTarget) throws ValidationException {
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
			schemaReader = new StringReader(schemaString);
			StreamSource schemaSource = new StreamSource(schemaReader);
			validate(xmlSource, schemaSource, outputTarget, schema.xpathDefaultNamespace);
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
			throw new ValidationException("Error during validation", e);
		} finally {
			IOUtils.closeQuietly(inputReader);
		}
	}

	public String validate(Schema schema, String xmlText) throws ValidationException {
		StreamSource xmlStream = Converter.createStreamSource(xmlText);
		return validate(schema, xmlStream);
	}
}
