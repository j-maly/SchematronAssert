package cz.jakubmaly.schematronassert.schematron.serialization;

import static org.mockito.Mockito.*;

import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import net.sf.saxon.*;

import org.custommonkey.xmlunit.*;
import org.junit.*;

public class ConverterTest {

	@Test
	public void testCreateStreamSource() throws Exception {
		// ARRANGE
		String text = "<test>Hello</test>";
		// ACT 
		StreamSource source = Converter.createStreamSource(text);
		// ASSERT
		StringWriter outputWriter = new StringWriter();
		StreamResult output = new StreamResult(outputWriter);
		createIdentityTransformer().transform(source, output);
		XMLAssert.assertXMLEqual(text, outputWriter.toString());
	}

	private Transformer createIdentityTransformer() throws TransformerConfigurationException, TransformerFactoryConfigurationError {
		Transformer t = TransformerFactoryImpl.newInstance().newTransformer();
		return t;
	}

	@Test
	public void testCreateRereadableSource_from_reader() throws Exception {
		// ARRANGE
		String text = "<test>Hello</test>";
		StreamSource source = Converter.createStreamSource(text);
		// ACT 
		StreamSource rereadableSoruce = Converter.createRereadableSource(source);
		// ASSERT 
		StringWriter outputWriter = new StringWriter();
		StreamResult output = new StreamResult(outputWriter);
		createIdentityTransformer().transform(rereadableSoruce, output);
		XMLAssert.assertXMLEqual(text, outputWriter.toString());
		// now use the source again - should give the same result  
		outputWriter = new StringWriter();
		output = new StreamResult(outputWriter);
		createIdentityTransformer().transform(rereadableSoruce, output);
		XMLAssert.assertXMLEqual(text, outputWriter.toString());
	}

	@Test
	public void testCreateRereadableSource_from_stream() throws Exception {
		// ARRANGE
		String text = "<test>Hello</test>";
		InputStream stream = new ByteArrayInputStream(text.getBytes());
		StreamSource source = new StreamSource(stream);
		// ACT 
		StreamSource rereadableSoruce = Converter.createRereadableSource(source);
		// ASSERT 
		StringWriter outputWriter = new StringWriter();
		StreamResult output = new StreamResult(outputWriter);
		createIdentityTransformer().transform(rereadableSoruce, output);
		XMLAssert.assertXMLEqual(text, outputWriter.toString());
		// now use the source again - should give the same result  
		outputWriter = new StringWriter();
		output = new StreamResult(outputWriter);
		createIdentityTransformer().transform(rereadableSoruce, output);
		XMLAssert.assertXMLEqual(text, outputWriter.toString());
	}

	@Test(expected = IOException.class)
	public void testCreateRereadableSource_fails_on_not_supported_implementation() throws Exception {
		// ARRANGE
		StreamSource source = mock(StreamSource.class);
		when(source.getReader()).thenReturn(null);
		when(source.getInputStream()).thenReturn(null);
		// ACT
		Converter.createRereadableSource(source);
	}
}
