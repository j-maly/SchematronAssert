package cz.jakubmaly.schematronassert.io;

import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.apache.commons.io.*;
import org.jdom.Element;
import org.jdom2.*;
import org.slf4j.*;

import cz.jakubmaly.schematronassert.utils.*;

public class Converter {
	private static Logger logger = LoggerFactory.getLogger(Converter.class);

	public static StreamSource createStreamSource(String xmlText) {
		StringReader schemaReader = new StringReader(xmlText);
		StreamSource xmlSource = new StreamSource(schemaReader);
		return xmlSource;
	}

	public static StreamSource createRereadableSource(StreamSource xmlSource) throws IOException {
		if (xmlSource.getReader() != null) {
			return new StreamSource(new RereadableReader(xmlSource.getReader()));
		} else if (xmlSource.getInputStream() != null) {
			return new StreamSource(new RereadableStream(xmlSource.getInputStream()));
		} else {
			throw new IOException(
					"Only implementations of StreamSource which are based on Reader or InputStream are supported. The underlying Reader/InputStream must allow mark/reset. ");
		}
	}

	public static StreamSource createStreamSource(org.jdom.Document jdom1document) throws IOException {
		ByteArrayOutputStream docOutputStream = null;
		try {
			docOutputStream = new ByteArrayOutputStream();
			org.jdom.output.XMLOutputter xmlOutput = new org.jdom.output.XMLOutputter();
			xmlOutput.output(jdom1document, docOutputStream);
			ByteArrayInputStream docInputStream = new ByteArrayInputStream(docOutputStream.toByteArray());
			return new StreamSource(docInputStream);
		} finally {
			IOUtils.closeQuietly(docOutputStream);
		}
	}

	public static StreamSource createStreamSource(org.jdom2.Document jdom2document) throws IOException {
		ByteArrayOutputStream docOutputStream = null;
		try {
			docOutputStream = new ByteArrayOutputStream();
			org.jdom2.output.XMLOutputter xmlOutput = new org.jdom2.output.XMLOutputter();
			xmlOutput.output(jdom2document, docOutputStream);
			ByteArrayInputStream docInputStream = new ByteArrayInputStream(docOutputStream.toByteArray());
			return new StreamSource(docInputStream);
		} finally {
			IOUtils.closeQuietly(docOutputStream);
		}
	}

	public static StreamSource createStreamSource(org.jdom.Element jdom1element) throws IOException {
		ByteArrayOutputStream docOutputStream = null;
		try {
			docOutputStream = new ByteArrayOutputStream();
			org.jdom.output.XMLOutputter xmlOutput = new org.jdom.output.XMLOutputter();
			xmlOutput.output(jdom1element, docOutputStream);
			ByteArrayInputStream docInputStream = new ByteArrayInputStream(docOutputStream.toByteArray());
			return new StreamSource(docInputStream);
		} finally {
			IOUtils.closeQuietly(docOutputStream);
		}
	}

	public static StreamSource createStreamSource(org.jdom2.Element jdom2element) throws IOException {
		ByteArrayOutputStream docOutputStream = null;
		try {
			docOutputStream = new ByteArrayOutputStream();
			org.jdom2.output.XMLOutputter xmlOutput = new org.jdom2.output.XMLOutputter();
			xmlOutput.output(jdom2element, docOutputStream);
			ByteArrayInputStream docInputStream = new ByteArrayInputStream(docOutputStream.toByteArray());
			return new StreamSource(docInputStream);
		} finally {
			IOUtils.closeQuietly(docOutputStream);
		}
	}

	public static String prettyPrintXml(Source source) {
		StringWriter resultWriter = null;
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			resultWriter = new StringWriter();
			StreamResult result = new StreamResult(resultWriter);
			transformer.transform(source, result);
			String xmlString = resultWriter.toString();
			return xmlString;
		} catch (Exception e) {
			logger.error("XML pretty print failed", e);
			return null;
		} finally {
			IOUtils.closeQuietly(resultWriter);
		}
	}

	public static String prettyPrintXml(String stringXml) {
		StringReader r = null;
		try {
			r = new StringReader(stringXml);
			StreamSource s = new StreamSource(r);
			String prettyPrint = prettyPrintXml(s);
			return prettyPrint;
		} finally {
			IOUtils.closeQuietly(r);
		}
	}
}
