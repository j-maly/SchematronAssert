package cz.jakubmaly.schematronassert.schematron.serialization;

import static cz.jakubmaly.schematronassert.schematron.serialization.SchemaBuilder.*;

import java.io.*;

import org.custommonkey.xmlunit.*;
import org.junit.*;

import cz.jakubmaly.schematronassert.schematron.model.*;
import cz.jakubmaly.schematronassert.test.*;

public class SchemaSerializerTest {

	SchemaSerializer ser = new SchemaSerializer();

	@Test
	public void should_replicate_sample_books_schema() throws Exception {
		Schema schema = schema()
			.title("A Schema for Books")
			.withNamespace("bk", "http://www.example.com/books")
			.withPattern(pattern("authorTests")
				.withRule(rule()
					.context("bk:book")
					.let("authors", "bk:author")
					.withAssert("count($authors) != 0", "A book must have at least one author")))
			.withPattern(pattern("onLoanTests")
				.withRule(rule()
					.context("bk:book")
					.withReport("@on-loan and not(@return-date)", "Every book that is on loan must have a return date")));

		StringWriter outWriter = new StringWriter();
		ser.serializeSchema(schema, outWriter);
		String control = TestUtils.getResourceText("/schematron/books.sch");
		XMLAssert.assertXMLEqual(control, outWriter.toString());
	}

	@Test
	public void should_replicate_sample_projects_schema() throws Exception {
		Schema schema = schema().queryBinding("xslt2")
			.icon("http://www.ascc.net/xml/resource/schematron/bilby.jpg")
			.defaultPhase("built")
			.withPhase(phase("built")
				.withActivePattern("completed", "completed")
				.withActivePattern("admin", "admin"))
			.withPattern(pattern("completed")
				.withRule(rule().context("house")
					.withAssert("count(wall) = 4", "A house should have 4 walls")
					.withReport("roof", "The house is incomplete, it still needs a roof")))
			.withPattern(pattern("admin")
				.withRule(rule("nameChecks")
					.abstractAttribute(true)
					.withAssert("firstname", "A name element must have a first name"))
				.withRule(rule().context("builder")
					.extendsRule("nameChecks")
					.withAssert("certification", "A name must be certified")));

		StringWriter outWriter = new StringWriter();
		ser.serializeSchema(schema, outWriter);
		String control = TestUtils.getResourceText("/schematron/projects.sch");
		XMLUnit.setIgnoreWhitespace(true);
		XMLAssert.assertXMLEqual(control, outWriter.toString());
	}

	private boolean wasIgnoreWhitespace;
	private boolean wasIgnoreComments;

	@Before
	public void setUpXmlUnit() {
		wasIgnoreWhitespace = XMLUnit.getIgnoreWhitespace();
		wasIgnoreComments = XMLUnit.getIgnoreComments();
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreComments(true);
	}

	@After
	public void restoreXmlUnit() {
		XMLUnit.setIgnoreWhitespace(wasIgnoreWhitespace);
		XMLUnit.setIgnoreComments(wasIgnoreComments);
	}
}