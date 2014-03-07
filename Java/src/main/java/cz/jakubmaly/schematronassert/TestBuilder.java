package cz.jakubmaly.schematronassert;

import java.io.*;

import org.apache.commons.io.*;
import org.xml.sax.*;

import cz.jakubmaly.schematronassert.schematron.model.*;

public class TestBuilder {

	private Schema schema;

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	public TestBuilder() {
		schema = new Schema();
	}

	public static TestBuilder inEvery(String context) {
		return new TestBuilder();
	}

	public static TestBuilder bind(String context) {
		return new TestBuilder();
	}

	public void check(String condition) {
		throw new AssertionError("Not implemented");
	}

	public void check(String condition, String message) {
		throw new AssertionError("Not implemented");
	}

	public static InputSource createInputSource(org.jdom.Document jdom1document) throws IOException {
		ByteArrayOutputStream docOutputStream = new ByteArrayOutputStream();
		org.jdom.output.XMLOutputter xmlOutput = new org.jdom.output.XMLOutputter();
		xmlOutput.output(jdom1document, docOutputStream);
		ByteArrayInputStream docInputStream = new
				ByteArrayInputStream(docOutputStream.toByteArray());
		docOutputStream.close();
		IOUtils.closeQuietly(docOutputStream);
		return new InputSource(docInputStream);
	}

	public static InputSource createInputSource(org.jdom2.Document jdom2document) throws IOException {
		ByteArrayOutputStream docOutputStream = new ByteArrayOutputStream();
		org.jdom2.output.XMLOutputter xmlOutput = new org.jdom2.output.XMLOutputter();
		xmlOutput.output(jdom2document, docOutputStream);
		ByteArrayInputStream docInputStream = new
				ByteArrayInputStream(docOutputStream.toByteArray());
		docOutputStream.close();
		IOUtils.closeQuietly(docOutputStream);
		return new InputSource(docInputStream);
	}
}
