package cz.jakubmaly.schematronassert.schematron.validation;

import static cz.jakubmaly.schematronassert.schematron.serialization.SchemaBuilder.*;
import static org.assertj.core.api.Assertions.*;

import java.io.*;

import javax.xml.transform.stream.*;

import org.apache.commons.io.*;
import org.apache.commons.io.output.*;
import org.junit.*;

import cz.jakubmaly.schematronassert.schematron.model.*;
import cz.jakubmaly.schematronassert.test.*;

public class XsltSchematronValidatorTest extends XsltSchematronValidator {

	private static final String XML_BOOKS = "/xml/books.xml";
	private static final String XML_BOOKS_WITH_NAMESPACES = "/xml/books_with_namespaces.xml";
	private static final String XML_BOOKS_WITH_DEFAULT_NAMESPACE = "/xml/books_with_default_namespace.xml";
	private XsltSchematronValidator validator;

	@Before
	public void setUp() {
		validator = new XsltSchematronValidator();
		validator.setAutoRecognizeNamespaces(false);
	}

	@Test
	public void assert_should_pass() throws Exception {
		Schema schema = schema()
			.withNs("bk", "http://www.example.com/books")
			.withPattern(pattern().withRule(rule().context("bk:books")
				.withAssert("bk:book", "There are some books")));

		String validationResult = validateResource(XML_BOOKS, schema);
		assertThat(validationResult).doesNotContain("failed-assert");
	}

	private String validateResource(String resourceName, Schema schema) throws Exception {
		String xmlText = TestUtils.getResourceText(resourceName);
		String validationResult = validator.validate(schema, xmlText);
		return validationResult;
	}

	@Test
	public void assert_should_fail() throws Exception {
		Schema schema = schema()
			.withNs("bk", "http://www.example.com/books")
			.withPattern(pattern().withRule(rule().context("bk:book")
				.withAssert("bk:author")));

		String validationResult = validateResource(XML_BOOKS, schema);
		assertThat(validationResult).contains("failed-assert test=\"bk:author\"");
	}

	@Test
	public void assert_with_let() throws Exception {
		Schema schema = schema()
			.withNs("bk", "http://www.example.com/books")
			.withPattern(pattern().withRule(rule()
				.context("bk:book")
				.let("publisher", "bk:publisher")
				.withAssert("$publisher/bk:name | $publisher/bk:address", "Missing publisher details")));

		String validationResult = validateResource(XML_BOOKS, schema);
		assertThat(validationResult).doesNotContain("failed-assert");
	}

	@Test
	public void assert_shoud_give_error_message() throws Exception {
		Schema schema = schema()
			.withNs("bk", "http://www.example.com/books")
			.withPattern(pattern().withRule(rule().context("bk:book")
				.withAssert("bk:author", "Book must have an author")));

		String validationResult = validateResource(XML_BOOKS, schema);
		assertThat(validationResult).contains("failed-assert test=\"bk:author\"");
		assertThat(validationResult).contains("Book must have an author");
	}

	@Test
	public void assert_shoud_give_error_message_with_text_value_template() throws Exception {
		Schema schema = schema()
			.withNs("bk", "http://www.example.com/books")
			.withPattern(pattern().withRule(rule().context("bk:book")
				.withAssert("bk:author", "Book '{./bk:title}' has no author")));

		String validationResult = validateResource(XML_BOOKS, schema);
		assertThat(validationResult).contains("failed-assert test=\"bk:author\"");
		assertThat(validationResult).contains("Book 'Reason' has no author");
	}

	@Test
	public void report_should_pass() throws Exception {
		Schema schema = schema()
			.withNs("bk", "http://www.example.com/books")
			.withPattern(pattern().withRule(rule().context("bk:book")
				.withReport("count(bk:author) > 1", "Only one author allowed")));

		String validationResult = validateResource(XML_BOOKS, schema);
		assertThat(validationResult).doesNotContain("successful-report");
	}

	@Test
	public void report_should_fail() throws Exception {
		Schema schema = schema()
			.withNs("bk", "http://www.example.com/books")
			.withPattern(pattern().withRule(rule().context("bk:book")
				.withReport("not(bk:author)")));

		String validationResult = validateResource(XML_BOOKS, schema);
		assertThat(validationResult).contains("successful-report test=\"not(bk:author)\"");
	}

	@Test
	public void report_should_give_error_message() throws Exception {
		Schema schema = schema()
			.withNs("bk", "http://www.example.com/books")
			.withPattern(pattern().withRule(rule().context("bk:book")
				.withReport("not(bk:author)", "Book must have an author")));

		String validationResult = validateResource(XML_BOOKS, schema);
		assertThat(validationResult).contains("successful-report test=\"not(bk:author)\"");
		assertThat(validationResult).contains("Book must have an author");
	}

	@Test
	public void report_shoud_give_error_message_with_text_value_template() throws Exception {
		Schema schema = schema()
			.withNs("bk", "http://www.example.com/books")
			.withPattern(pattern().withRule(rule().context("bk:book")
				.withReport("not(bk:author)", "Book '{./bk:title}' must have an author")));

		String validationResult = validateResource(XML_BOOKS, schema);
		assertThat(validationResult).contains("successful-report test=\"not(bk:author)\"");
		assertThat(validationResult).contains("Book 'Reason' must have an author");
	}

	@Test
	public void should_recognize_default_namespace() throws Exception {
		validator.setAutoRecognizeNamespaces(true);
		Schema schema = schema()
			.withPattern(pattern().withRule(rule().context("books")
				.withAssert("book", "There are some books")));

		String validationResult = validateResource(XML_BOOKS_WITH_DEFAULT_NAMESPACE, schema);
		assertThat(validationResult).contains("fired-rule");
		assertThat(validationResult).doesNotContain("failed-assert");
	}

	@Test
	public void should_recognize_namespace_prefix() throws Exception {
		validator.setAutoRecognizeNamespaces(true);
		Schema schema = schema()
			.withPattern(pattern().withRule(rule().context("bk:books")
				.withAssert("bk:book", "There are some books")));

		String validationResult = validateResource(XML_BOOKS_WITH_NAMESPACES, schema);
		assertThat(validationResult).doesNotContain("failed-assert");
	}

	@Test
	public void should_validate_using_raw_schema() throws Exception {
		InputStream schemaStream = getClass().getResourceAsStream("/schematron/books.sch");
		InputStream xmlStream = getClass().getResourceAsStream(XML_BOOKS);

		StreamSource schemaSource = new StreamSource(schemaStream);
		StreamSource xmlSource = new StreamSource(xmlStream);

		StreamResult output = new StreamResult(NullOutputStream.NULL_OUTPUT_STREAM);
		validator.validate(xmlSource, schemaSource, output);

		IOUtils.closeQuietly(xmlStream);
		IOUtils.closeQuietly(schemaStream);
	}
}
