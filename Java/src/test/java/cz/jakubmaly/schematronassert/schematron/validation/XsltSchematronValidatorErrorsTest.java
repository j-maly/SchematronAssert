package cz.jakubmaly.schematronassert.schematron.validation;

import java.io.*;

import org.junit.*;
import org.junit.rules.*;

import cz.jakubmaly.schematronassert.schematron.model.*;
import cz.jakubmaly.schematronassert.schematron.model.Rule;
import cz.jakubmaly.schematronassert.test.*;

public class XsltSchematronValidatorErrorsTest {

	private XsltSchematronValidator validator;
	private static String booksXml;

	@org.junit.Rule
	public ExpectedException exception = ExpectedException.none().handleAssertionErrors();

	@Before
	public void setUp() throws IOException {
		validator = new XsltSchematronValidator();
		validator.setAutoRecognizeNamespaces(false);
		booksXml = TestUtils.getResourceText("/xml/books.xml");
	}

	@Test
	public void test_unknown_variable_in_xpath_test_returns_error_message() {
		// ARRANGE
		exception.expect(ValidationException.class);
		exception.expectMessage("Total number of errors/warnings: 2");
		exception.expectMessage("Variable i has not been declared");
		exception.expectMessage("Undeclared namespace prefix {bk}");
		Schema schema = new Schema()
			.withPattern(new Pattern()
				.withRule(new Rule()
					.context("bk:book")
					.withAssert("$i > $b")));
		// ACT - should throw 
		validator.validate(booksXml, schema);
	}
}
