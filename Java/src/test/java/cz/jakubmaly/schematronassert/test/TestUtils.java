package cz.jakubmaly.schematronassert.test;

import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import net.sf.saxon.*;

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
		SvrlDeserializer deserializer = new SvrlDeserializerImpl();
		Source source = new StreamSource(stream);
		ValidationOutput output = deserializer.deserializeSvrlOutput(source);
		IOUtils.closeQuietly(stream);
		return output;
	}

	public static String getXmlAsString(Source source) throws TransformerConfigurationException, TransformerException,
			TransformerFactoryConfigurationError {
		StringWriter outputWriter = new StringWriter();
		StreamResult output = new StreamResult(outputWriter);
		createIdentityTransformer().transform(source, output);
		String outputString = outputWriter.toString();
		IOUtils.closeQuietly(outputWriter);
		return outputString;
	}

	public static Transformer createIdentityTransformer() throws TransformerConfigurationException, TransformerFactoryConfigurationError {
		Transformer t = TransformerFactoryImpl.newInstance().newTransformer();
		return t;
	}
}
