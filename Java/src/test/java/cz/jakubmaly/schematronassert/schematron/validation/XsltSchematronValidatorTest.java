package cz.jakubmaly.schematronassert.schematron.validation;

import static cz.jakubmaly.schematronassert.assertions.Assertions.*;
import static cz.jakubmaly.schematronassert.schematron.serialization.SchemaBuilder.*;
import static org.assertj.core.api.Assertions.*;

import java.io.*;

import javax.xml.transform.stream.*;

import org.apache.commons.io.*;
import org.apache.commons.io.output.*;
import org.junit.*;

import cz.jakubmaly.schematronassert.schematron.model.*;
import cz.jakubmaly.schematronassert.svrl.model.*;
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
			.withNamespace("bk", "http://www.example.com/books")
			.withPattern(pattern().withRule(rule().context("bk:books")
				.withAssert("bk:book", "There are some books")));

		ValidationOutput validationResult = validateResource(XML_BOOKS, schema);
		assertThat(validationResult).hasNoFailures();
	}

	@Test
	public void assert_should_fail() throws Exception {
		Schema schema = schema()
			.withNamespace("bk", "http://www.example.com/books")
			.withPattern(pattern().withRule(rule().context("bk:book")
				.withAssert("bk:author")));

		ValidationOutput validationResult = validateResource(XML_BOOKS, schema);
		assertThat(validationResult.getFailures()).haveAtLeast(1, withTest("bk:author"));
	}

	@Test
	public void assert_with_let() throws Exception {
		Schema schema = schema()
			.withNamespace("bk", "http://www.example.com/books")
			.withPattern(pattern().withRule(rule()
				.context("bk:book")
				.let("publisher", "bk:publisher")
				.withAssert("$publisher/bk:name | $publisher/bk:address", "Missing publisher details")));

		ValidationOutput validationResult = validateResource(XML_BOOKS, schema);
		assertThat(validationResult).hasNoFailures();
	}

	@Test
	public void assert_shoud_give_error_message() throws Exception {
		Schema schema = schema()
			.withNamespace("bk", "http://www.example.com/books")
			.withPattern(pattern().withRule(rule().context("bk:book")
				.withAssert("bk:author", "Book must have an author")));

		ValidationOutput validationResult = validateResource(XML_BOOKS, schema);
		assertThat(validationResult.getFailures()).haveAtLeast(1, withTest("bk:author"));
		assertThat(validationResult.getFailures()).haveAtLeast(1, withText("Book must have an author"));
	}

	@Test
	public void assert_shoud_give_error_message_with_text_value_template() throws Exception {
		Schema schema = schema()
			.withNamespace("bk", "http://www.example.com/books")
			.withPattern(pattern().withRule(rule().context("bk:book")
				.withAssert("bk:author", "Book '{./bk:title}' has no author")));

		ValidationOutput validationResult = validateResource(XML_BOOKS, schema);
		assertThat(validationResult.getFailures()).haveAtLeast(1, withTest("bk:author"));
		assertThat(validationResult.getFailures()).haveAtLeast(1, withText("Book 'Reason' has no author"));
	}

	@Test
	public void report_should_pass() throws Exception {
		Schema schema = schema()
			.withNamespace("bk", "http://www.example.com/books")
			.withPattern(pattern().withRule(rule().context("bk:book")
				.withReport("count(bk:author) > 1", "Only one author allowed")));

		ValidationOutput validationResult = validateResource(XML_BOOKS, schema);
		assertThat(validationResult).hasNoReports();
	}

	@Test
	public void report_should_fail() throws Exception {
		Schema schema = schema()
			.withNamespace("bk", "http://www.example.com/books")
			.withPattern(pattern().withRule(rule().context("bk:book")
				.withReport("not(bk:author)")));

		ValidationOutput validationResult = validateResource(XML_BOOKS, schema);
		assertThat(validationResult.getReports()).haveAtLeast(1, withTest("not(bk:author)"));
	}

	@Test
	public void report_should_give_error_message() throws Exception {
		Schema schema = schema()
			.withNamespace("bk", "http://www.example.com/books")
			.withPattern(pattern().withRule(rule().context("bk:book")
				.withReport("not(bk:author)", "Book must have an author")));

		ValidationOutput validationResult = validateResource(XML_BOOKS, schema);
		assertThat(validationResult.getReports()).haveAtLeast(1, withTest("not(bk:author)"));
		assertThat(validationResult.getReports()).haveAtLeast(1, withText("Book must have an author"));
	}

	@Test
	public void report_shoud_give_error_message_with_text_value_template() throws Exception {
		Schema schema = schema()
			.withNamespace("bk", "http://www.example.com/books")
			.withPattern(pattern().withRule(rule().context("bk:book")
				.withReport("not(bk:author)", "Book '{./bk:title}' must have an author")));

		ValidationOutput validationResult = validateResource(XML_BOOKS, schema);
		assertThat(validationResult.getReports()).haveAtLeast(1, withTest("not(bk:author)"));
		assertThat(validationResult.getReports()).haveAtLeast(1, withText("Book 'Reason' must have an author"));
	}

	@Test
	public void should_recognize_default_namespace() throws Exception {
		validator.setAutoRecognizeNamespaces(true);
		Schema schema = schema()
			.withPattern(pattern().withRule(rule().context("books")
				.withAssert("book", "There are some books")));

		ValidationOutput validationResult = validateResource(XML_BOOKS_WITH_DEFAULT_NAMESPACE, schema);
		assertThat(validationResult.getReportContents()).haveAtLeast(1, isFiredRule());
		assertThat(validationResult).hasNoFailures();
	}

	@Test
	public void should_recognize_namespace_prefix() throws Exception {
		validator.setAutoRecognizeNamespaces(true);
		Schema schema = schema()
			.withPattern(pattern().withRule(rule().context("bk:books")
				.withAssert("bk:book", "There are some books")));

		ValidationOutput validationResult = validateResource(XML_BOOKS_WITH_NAMESPACES, schema);
		assertThat(validationResult.getReportContents()).haveAtLeast(1, isFiredRule());
		assertThat(validationResult.getFailures()).isEmpty();
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

	private ValidationOutput validateResource(String resourceName, Schema schema) throws Exception {
		String xmlText = TestUtils.getResourceText(resourceName);
		return validator.validate(xmlText, schema);
	}
}
