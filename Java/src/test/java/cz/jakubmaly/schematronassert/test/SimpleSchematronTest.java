package cz.jakubmaly.schematronassert.test;

import static org.assertj.core.api.Assertions.*;

import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.junit.*;

import cz.jakubmaly.schematronassert.utils.*;

public class SimpleSchematronTest {

	private static TransformerFactory transformerFactory = new net.sf.saxon.TransformerFactoryImpl();

	static {
		transformerFactory.setURIResolver(new ResourceURIResolver("/xslt/"));
	}

	@Test
	public void should_run_simple_schematron_validation() throws TransformerException {

		InputStream schemaStream = getClass().getResourceAsStream("/schematron/books.sch");
		StreamSource schemaSource = new StreamSource(schemaStream);

		InputStream xmlStream = getClass().getResourceAsStream("/xml/books.xml");
		StreamSource xmlSource = new StreamSource(xmlStream);

		StringWriter r1 = transformation("/xslt/iso_dsdl_include.xsl", schemaSource);
		StreamSource ir1 = sourceFromStringWriter(r1);

		StringWriter r2 = transformation("/xslt/iso_abstract_expand.xsl", ir1);
		StreamSource ir2 = sourceFromStringWriter(r2);

		StringWriter r3 = transformation("/xslt/iso_svrl_for_xslt2.xsl", ir2);
		StreamSource ir3 = sourceFromStringWriter(r3);

		StringWriter r4 = transformation(ir3, xmlSource);
		String result = r4.toString();
		assertThat(result).contains("svrl:failed-assert test=\"count($authors) != 0\"");
	}

	private StringWriter transformation(String xsltFilePath, StreamSource inputSource) throws TransformerException {
		InputStream xsltStream = getClass().getResourceAsStream(xsltFilePath);
		StreamSource xsltSource = new StreamSource(xsltStream);
		return transformation(xsltSource, inputSource);
	}

	private StringWriter transformation(StreamSource xsltSource, StreamSource inputSource) throws TransformerConfigurationException,
			TransformerException {
		Transformer transformer = transformerFactory.newTransformer(xsltSource);
		StringWriter writer = new StringWriter();
		StreamResult xmlResult = new StreamResult(writer);
		transformer.transform(inputSource, xmlResult);
		return writer;
	}

	private StreamSource sourceFromStringWriter(StringWriter writer) {
		String string = writer.toString();
		StringReader reader = new StringReader(string);
		StreamSource source = new StreamSource(reader);
		return source;
	}

	// context("book").assert("count(autor)") > 0")
	// - esapi encoder to encode >
	// inEvery("book").assert("count(autor)") > 0")
	// inEvery("book").report("count(autor)") > 0")
	// context("book").assert("autor")
	// context("book/autor").node() != null
	// context("count(book/autor)").integer() > 0
	// context("book/autor").string().equals("John")

	// should handle namespaces
	// - explicitly (declared for context)
	// -- context("books:book").withPrefix("books", "http://...")
	// - handle no namespace
	// - handle default namespace
	// - smartly
	// -- context("books:book") -- will look for ns declarations in the XML
	// -- element and try to find out what each prefix means
	// --- be carefull with fn namespace, it may be built-in

	// some debugging (output schematron schemas, namespace resolution attempts,
	// svrl reports) - SLF4J?
	// and of course errors
}
