package cz.jakubmaly.schematronassert.test;

import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.apache.commons.io.*;

import cz.jakubmaly.schematronassert.schematron.serialization.*;
import cz.jakubmaly.schematronassert.svrl.model.*;
import cz.jakubmaly.schematronassert.svrl.serialization.*;

public class TestUtils {
	public static String getResourceText(String resourceName) throws IOException {
		InputStream resourceStream = null;
		resourceStream = TestUtils.class.getResourceAsStream(resourceName);
		String result = IOUtils.toString(resourceStream);
		IOUtils.closeQuietly(resourceStream);
		return result;
	}

	public static ValidationOutput loadSvrlFromResource(String resourceName) {
		InputStream stream = SchemaSerializer.class.getResourceAsStream(resourceName);
		SvrlDeserializer deserializer = new SvrlDeserializer();
		Source source = new StreamSource(stream);
		ValidationOutput output = deserializer.deserializeSvrlOutput(source);
		IOUtils.closeQuietly(stream);
		return output;
	}

	public static String prettyPrint(Source source) throws TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		StringWriter resultWriter = new StringWriter();
		StreamResult result = new StreamResult(resultWriter);
		transformer.transform(source, result);
		String xmlString = resultWriter.toString();
		IOUtils.closeQuietly(resultWriter);
		return xmlString;
	}

	public static String prettyPrint(String string) throws TransformerException {
		StringReader r = new StringReader(string);
		StreamSource s = new StreamSource(r);
		return prettyPrint(s);
	}
}
