package cz.jakubmaly.schematronassert;

import static cz.jakubmaly.schematronassert.TestBuilder.*;
import static org.assertj.core.api.Assertions.*;

import java.io.*;

import org.junit.*;

import cz.jakubmaly.schematronassert.schematron.model.*;
import cz.jakubmaly.schematronassert.schematron.model.Assert;
import cz.jakubmaly.schematronassert.schematron.model.Rule;
import cz.jakubmaly.schematronassert.svrl.model.*;
import cz.jakubmaly.schematronassert.test.*;

public class TestBuilderTest {

	private String booksDocument;
	private String booksDocumentWithNamespaces;

	@Test
	public void build_schema() {
		TestBuilder tb = in(booksDocument);
		assertThat(tb.getSchema()).isNotNull();
	}

	@Test
	public void add_pattern_rule_and_context() {
		TestBuilder tb = in(booksDocument)
			.forEvery("bk:book");

		assertThat(tb.getSchema().getLastRule().context)
			.isEqualTo("bk:book");
	}

	@Test
	public void add_check() {
		TestBuilder tb = in(booksDocument)
			.forEvery("bk:book")
			.check("bk:author");

		assertThat(tb.getSchema().getLastRule().assertionElements).hasSize(1);
		Assert a = (Assert) tb.getSchema().getLastRule().assertionElements.get(0);
		assertThat(a.test).isEqualTo("bk:author");
	}

	@Test
	public void add_check_with_message() {
		TestBuilder tb = in(booksDocument)
			.forEvery("bk:book")
			.check("bk:author", "Author is missing");

		assertThat(tb.getSchema().getLastRule().assertionElements).hasSize(1);
		Assert a = (Assert) tb.getSchema().getLastRule().assertionElements.get(0);
		assertThat(a.getMessage())
			.hasSize(1)
			.contains("Author is missing");
	}

	@Test
	public void add_bind_schema_scope() {
		TestBuilder tb = in(booksDocument)
			.bind("books", "//bk:book")
			.bind("authors", "//bk:author", BindScope.SCHEMA);
		assertThat(tb.getSchema().letDeclarations.get(0).name).isEqualTo("books");
		assertThat(tb.getSchema().letDeclarations.get(0).value).isEqualTo("//bk:book");
		assertThat(tb.getSchema().letDeclarations.get(1).name).isEqualTo("authors");
		assertThat(tb.getSchema().letDeclarations.get(1).value).isEqualTo("//bk:author");
	}

	@Test
	public void add_bind_pattern_scope() {
		TestBuilder tb = in(booksDocument);
		tb.getSchema().withPattern(new Pattern("p1"));
		tb.bind("authors", "bk:author");
		tb.getSchema().getLastPattern().withRule(new Rule(""));
		tb.bind("authors2", "bk:author", BindScope.PATTERN);
		assertThat(tb.getSchema().letDeclarations).isNullOrEmpty();
		assertThat(tb.getSchema().getLastPattern().letDeclarations.get(0).name).isEqualTo("authors");
		assertThat(tb.getSchema().getLastPattern().letDeclarations.get(0).value).isEqualTo("bk:author");
		assertThat(tb.getSchema().getLastPattern().letDeclarations.get(1).name).isEqualTo("authors2");
		assertThat(tb.getSchema().getLastPattern().letDeclarations.get(1).value).isEqualTo("bk:author");
	}

	@Test
	public void add_bind_rule_scope() {
		TestBuilder tb = in(booksDocument)
			.forEvery("bk:book")
			.bind("books", "bk:book")
			.bind("books2", "bk:book", BindScope.RULE);
		assertThat(tb.getSchema().letDeclarations).isNullOrEmpty();
		assertThat(tb.getSchema().getLastRule().letDeclarations.get(0).name).isEqualTo("books");
		assertThat(tb.getSchema().getLastRule().letDeclarations.get(0).value).isEqualTo("bk:book");
		assertThat(tb.getSchema().getLastRule().letDeclarations.get(1).name).isEqualTo("books2");
		assertThat(tb.getSchema().getLastRule().letDeclarations.get(1).value).isEqualTo("bk:book");
	}

	@Test
	public void run_validation() {
		ValidationOutput result = in(booksDocument)
			.withNamespace("bk", "http://www.example.com/books")
			.forEvery("bk:book")
			.check("bk:author")
			.validate();

		assertThat(result.getFailures()).hasSize(1);
	}

	@Test
	public void run_validation_with_auto_detect_namespaces() {
		ValidationOutput result = in(booksDocumentWithNamespaces)
			.forEvery("bk:book")
			.check("bk:author")
			.autoDetectNamespaces()
			.validate();

		assertThat(result.getFailures()).hasSize(1);
	}

	@Before
	public void setUp() throws IOException {
		booksDocument = TestUtils.getResourceText("/xml/books.xml");
		booksDocumentWithNamespaces = TestUtils.getResourceText("/xml/books_with_namespaces.xml");
	}
}