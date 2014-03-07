package cz.jakubmaly.schematronassert.assertions;

import static cz.jakubmaly.schematronassert.assertions.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.*;
import org.junit.*;
import org.junit.rules.*;

import cz.jakubmaly.schematronassert.svrl.model.*;
import cz.jakubmaly.schematronassert.test.*;

public class ValidationOutputAssertTest {

	static ValidationOutput outputWithAsserts;
	static ValidationOutput outputWithReports;
	static ValidationOutput outputWithManyAsserts;

	@Rule
	public ExpectedException exception = ExpectedException.none().handleAssertionErrors();

	@Test
	public void should_construct_ValidationResultAssert() throws Exception {
		ValidationOutputAssert result = Assertions.assertThat(outputWithAsserts);
		assertThat(result).isNotNull();
		assertThat(result.getValidationOutput()).isEqualTo(outputWithAsserts);
	}

	@Test
	public void should_find_failuers() throws Exception {
		// ARRANGE
		exception.expect(AssertionError.class);
		exception.expectMessage("[ValidationOutput{failures:1, reports:0}]");
		exception.expectMessage("A book must have at least one author");
		// ACT
		assertThat(outputWithAsserts).hasNoFailures();
	}

	@Test
	public void should_find_no_failuers() throws Exception {
		assertThat(outputWithReports).hasNoFailures();
	}

	@Test
	public void should_find_reports() throws Exception {
		// ARRANGE
		exception.expect(AssertionError.class);
		exception.expectMessage("[ValidationOutput{failures:0, reports:1}]");
		exception.expectMessage("A book must have at least one author");
		// ACT
		assertThat(outputWithReports).hasNoReports();
	}

	@Test
	public void should_find_asserts_as_errors() throws Exception {
		// ARRANGE
		exception.expect(AssertionError.class);
		exception.expectMessage("[ValidationOutput{failures:1, reports:0}]");
		exception.expectMessage("A book must have at least one author");
		// ACT
		assertThat(outputWithAsserts).hasNoErrors();
	}

	@Test
	public void should_find_reports_not_as_errors() throws Exception {
		assertThat(outputWithReports).hasNoErrors();
	}

	@Test
	public void should_find_reports_as_errors() {
		// ARRANGE
		exception.expect(AssertionError.class);
		exception.expectMessage("[ValidationOutput{failures:0, reports:1}]");
		exception.expectMessage("A book must have at least one author");
		// ACT
		assertThat(outputWithReports).reportsAsErrors().hasNoErrors();
	}

	@Test
	public void should_filter_asserts_by_condition() {
		Condition<FailedAssert> condition = new Condition<FailedAssert>() {
			@Override
			public boolean matches(FailedAssert failedAssert) {
				return failedAssert.getFlag() != null && failedAssert.getFlag().equals("warning");
			}
		};
		assertThat(outputWithManyAsserts.getFailures())
			.filteredBy(condition)
			.are(condition);
	}

	// TODO: ideas
	// ValidationResult result = validate();
	// result.failedAsserts().withFlag()...
	// fromLocation(), withName(), withId(), withRole(), fromRule()
	// fromLocation(contains("") - string matcher here)

	@Before
	public void loadSampleOutput() {
		outputWithAsserts = TestUtils.loadSvrlFromResource("/svrl/books.svrl");
		outputWithReports = TestUtils.loadSvrlFromResource("/svrl/books_with_reports.svrl");
		outputWithManyAsserts = TestUtils.loadSvrlFromResource("/svrl/article.svrl");
	}

}
