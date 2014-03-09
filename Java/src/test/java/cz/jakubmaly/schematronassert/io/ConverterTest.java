package cz.jakubmaly.schematronassert.io;

import static org.mockito.Mockito.*;

import java.io.*;

import javax.xml.transform.stream.*;

import org.custommonkey.xmlunit.*;
import org.junit.*;

import cz.jakubmaly.schematronassert.test.*;

public class ConverterTest {

	@Test
	public void testCreateStreamSource() throws Exception {
		// ARRANGE
		String text = "<test>Hello</test>";
		// ACT 
		StreamSource source = Converter.createStreamSource(text);
		// ASSERT
		String outputString = TestUtils.getXmlAsString(source);
		XMLAssert.assertXMLEqual(text, outputString);
	}

	@Test
	public void testCreateRereadableSource_from_reader() throws Exception {
		// ARRANGE
		String text = "<test>Hello</test>";
		StreamSource source = Converter.createStreamSource(text);
		// ACT 
		StreamSource rereadableSoruce = Converter.createRereadableSource(source);
		// ASSERT 
		String outputString = TestUtils.getXmlAsString(rereadableSoruce);
		XMLAssert.assertXMLEqual(text, outputString);
		// now use the source again - should give the same result  
		outputString = TestUtils.getXmlAsString(source);
		XMLAssert.assertXMLEqual(text, outputString);
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
		TestUtils.createIdentityTransformer().transform(rereadableSoruce, output);
		XMLAssert.assertXMLEqual(text, outputWriter.toString());
		// now use the source again - should give the same result  
		outputWriter = new StringWriter();
		output = new StreamResult(outputWriter);
		TestUtils.createIdentityTransformer().transform(rereadableSoruce, output);
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

	@Test
	public void testJdom1() throws Exception {
		org.jdom.Document jdom1document = new org.jdom.Document();
		org.jdom.Element root = new org.jdom.Element("root");
		jdom1document.setContent(root);
		// ACT
		StreamSource source = Converter.createStreamSource(jdom1document);
		String outputString = TestUtils.getXmlAsString(source);
		XMLAssert.assertXMLEqual("<root />", outputString);
	}

	@Test
	public void testJdom2() throws Exception {
		org.jdom2.Document jdom2document = new org.jdom2.Document();
		org.jdom2.Element root = new org.jdom2.Element("root");
		jdom2document.setContent(root);
		// ACT
		StreamSource source = Converter.createStreamSource(jdom2document);
		String outputString = TestUtils.getXmlAsString(source);
		XMLAssert.assertXMLEqual("<root />", outputString);
	}
}
