package cz.jakubmaly.schematronassert.schematron.validation;

import java.io.*;
import java.util.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.apache.commons.io.*;
import org.slf4j.*;

import cz.jakubmaly.schematronassert.io.*;
import cz.jakubmaly.schematronassert.schematron.model.*;

public class DocumentNamespaceLookup {
	private static Logger logger = LoggerFactory.getLogger(DocumentNamespaceLookup.class);

	private static Transformer detect_namespaces;

	static {
		try {
			detect_namespaces = XsltSchematronValidator.initializeTransformer("/xslt/detect_namespaces.xsl");
		} catch (TransformerException e) {
			logger.error("Failed DocumentNamespaceLookup initialization, failed to load detect_namespaces.xsl", e);
		}
	}

	public void addNamespacesFromDocumentToSchema(Schema schema, StreamSource xmlDocument) throws ValidationException {
		logExplicitNamespaces(schema);
		try {
			verifyUnderlyingStreamOrReaderIsResettable(xmlDocument);
			logger.debug("Running namespace scan in document...");
			StreamSource rereadableSource = Converter.createRereadableSource(xmlDocument);
			List<NamespacePrefixDeclaration> namespaces = detectNamespaces(rereadableSource);
			for (NamespacePrefixDeclaration ns : namespaces) {
				if (ns.prefix != null && ns.prefix.length() > 0) {
					logger.debug(String.format("Adding namespace with prefix %s:%s", ns.prefix, ns.uri));
					schema.withNamespace(ns);
				} else {
					logger.debug(String.format("Setting xpath default namespace: %s", ns.uri));
					schema.xpathDefaultNamespace(ns.uri);
				}
			}
		} catch (Exception e) {
			throw new ValidationException("Error during recognizing namespaces in the document.", e);
		}
	}

	public List<NamespacePrefixDeclaration> detectNamespaces(StreamSource xmlSource) throws TransformerException {
		StringWriter resultWriter = new StringWriter();
		StreamResult outputTarget = new StreamResult(resultWriter);
		List<NamespacePrefixDeclaration> result = new ArrayList<NamespacePrefixDeclaration>();
		try {
			detect_namespaces.transform(xmlSource, outputTarget);
			String[] declarations = resultWriter.toString().split(" \\| ");
			for (String declaration : declarations) {
				String[] parts = declaration.split(" : ");
				String prefix = parts[0];
				String uri = parts[1];
				result.add(new NamespacePrefixDeclaration(prefix, uri));
			}
		} finally {
			IOUtils.closeQuietly(resultWriter);
		}
		return result;
	}

	public void verifyUnderlyingStreamOrReaderIsResettable(StreamSource xmlSource) {
		boolean streamNotResettable = xmlSource.getInputStream() != null && !xmlSource.getInputStream().markSupported();
		boolean readerNotResettable = xmlSource.getReader() != null && !xmlSource.getReader().markSupported();
		if (streamNotResettable || readerNotResettable) {
			throw new ValidationException("Underlying stream/reader of xmlSource does not support reset(). "
					+ "For autoRecognizeNamespaces, the xml document must be read twice"
					+ "(once for namespace scan, once for validation), "
					+ "this will require creating a copy of the document in the memory. ");
		}
	}

	public void logExplicitNamespaces(Schema schema) {
		if (schema.namespacePrefixDeclarations == null ||
				schema.namespacePrefixDeclarations.size() == 0) {
			logger.debug("No explicitly defined namespaces.");
			return;
		}
		logger.debug("Explicitly defined namespaces: ");
		for (NamespacePrefixDeclaration ns : schema.namespacePrefixDeclarations) {
			logger.debug(String.format("%s:%s", ns.prefix, ns.uri));
		}
	}
}
