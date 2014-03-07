package cz.jakubmaly.schematronassert.assertions;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.*;
import org.assertj.core.api.Assertions;

import cz.jakubmaly.schematronassert.svrl.model.*;

public class ValidationOutputAssert extends AbstractAssert<ValidationOutputAssert, ValidationOutput> {

	private boolean treatReportsAsErrors;

	/**
	 * Creates a new </code>{@link validationOutputAssert}</code> to make
	 * assertions on actual validationOutput.
	 * 
	 * @param actual
	 *            the validationOutput we want to make assertions on.
	 */
	public ValidationOutputAssert(ValidationOutput actual) {
		super(actual, ValidationOutputAssert.class);
	}

	public ValidationOutput getValidationOutput() {
		return actual;
	}

	public ValidationOutputAssert hasNoErrors() {
		assertThat(hasNoFailures());
		if (treatReportsAsErrors) {
			assertThat(hasNoReports());
		}
		return this;
	}

	/**
	 * Verifies that the actual ValidationOutput's failures contains the given
	 * FailedAssert elements.
	 * 
	 * @param failures
	 *            the given elements that should be contained in actual
	 *            ValidationOutput's failures.
	 * @return this assertion object.
	 * @throws AssertionError
	 *             if the actual ValidationOutput's failures does not contain
	 *             all given FailedAssert elements.
	 */
	public ValidationOutputAssert hasFailures(FailedAssert... failures) {
		// check that actual ValidationOutput we want to make assertions on is not null.
		isNotNull();

		// check that given FailedAssert varargs is not null.
		if (failures == null)
			throw new AssertionError("Expecting failures parameter not to be null.");

		// check with standard error message (see commented below to set your own message).
		Assertions.assertThat(actual.getFailures()).contains(failures);

		// To override the standard error message :
		// - remove the previous call to Assertions.assertThat(actual.getFailures().contains(failures)
		// - uncomment the line below and set your error message:
		// Assertions.assertThat(actual.getFailures()).overridingErrorMessage("\nmy error message %s", "arg1").contains(failures);

		// return the current assertion for method chaining
		return this;
	}

	/**
	 * Verifies that the actual ValidationOutput has no failures.
	 * 
	 * @return this assertion object.
	 * @throws AssertionError
	 *             if the actual ValidationOutput's failures is not empty.
	 */
	public ValidationOutputAssert hasNoFailures() {
		// check that actual ValidationOutput we want to make assertions on is not null.
		isNotNull();

		// we override the default error message with a more explicit one
		String assertjErrorMessage = "\nExpected :\n  <%s>\nnot to have failures but had :\n  <%s>";

		// check
		if (!actual.getFailures().isEmpty()) {
			failWithMessage(assertjErrorMessage, actual, actual.getFailures());
		}

		// return the current assertion for method chaining
		return this;
	}

	public ValidationOutputAssert reportsAsErrors() {
		treatReportsAsErrors = true;
		return this;
	}

	/**
	 * An entry point for ValidationResultAssert to follow AssertJ standard
	 * <code>assertThat()</code> statements.<br>
	 * With a static import, one's can write directly :
	 * <code>assertThat(myvalidationOutput)</code> and get specific assertion
	 * with code completion.
	 * 
	 * @param actual
	 *            the validationOutput we want to make assertions on.
	 * @return a new </code>{@link ValidationResultAssert}</code>
	 */

	/**
	 * Verifies that the actual validationOutput's ns contains the given Ns
	 * elements.
	 * 
	 * @param ns
	 *            the given elements that should be contained in actual
	 *            validationOutput's ns.
	 * @return this assertion object.
	 * @throws AssertionError
	 *             if the actual validationOutput's ns does not contain all
	 *             given Ns elements.
	 */
	public ValidationOutputAssert hasNs(Ns... ns) {
		// check that actual validationOutput we want to make assertions on is
		// not null.
		isNotNull();

		// check that given Ns varargs is not null.
		if (ns == null)
			throw new AssertionError("Expecting ns parameter not to be null.");

		// check with standard error message (see commented below to set your
		// own message).
		Assertions.assertThat(actual.getNs()).contains(ns);

		// To override the standard error message :
		// - remove the previous call to
		// Assertions.assertThat(actual.getNs().contains(ns)
		// - uncomment the line below and set your error message:
		// Assertions.assertThat(actual.getNs()).overridingErrorMessage("\nmy error message %s",
		// "arg1").contains(ns);

		// return the current assertion for method chaining
		return this;
	}

	/**
	 * Verifies that the actual validationOutput has no ns.
	 * 
	 * @return this assertion object.
	 * @throws AssertionError
	 *             if the actual validationOutput's ns is not empty.
	 */
	public ValidationOutputAssert hasNoNs() {
		// check that actual validationOutput we want to make assertions on is
		// not null.
		isNotNull();

		// we override the default error message with a more explicit one
		String assertjErrorMessage = "\nExpected :\n  <%s>\nnot to have ns but had :\n  <%s>";

		// check
		if (!actual.getNs().isEmpty()) {
			failWithMessage(assertjErrorMessage, actual, actual.getNs());
		}

		// return the current assertion for method chaining
		return this;
	}

	/**
	 * Verifies that the actual validationOutput's nsPrefixInAttributeValues
	 * contains the given NsPrefixInAttributeValues elements.
	 * 
	 * @param nsPrefixInAttributeValues
	 *            the given elements that should be contained in actual
	 *            validationOutput's nsPrefixInAttributeValues.
	 * @return this assertion object.
	 * @throws AssertionError
	 *             if the actual validationOutput's nsPrefixInAttributeValues
	 *             does not contain all given NsPrefixInAttributeValues
	 *             elements.
	 */
	public ValidationOutputAssert hasNsPrefixInAttributeValues(NsPrefixInAttributeValues... nsPrefixInAttributeValues) {
		// check that actual validationOutput we want to make assertions on is
		// not null.
		isNotNull();

		// check that given NsPrefixInAttributeValues varargs is not null.
		if (nsPrefixInAttributeValues == null)
			throw new AssertionError("Expecting nsPrefixInAttributeValues parameter not to be null.");

		// check with standard error message (see commented below to set your
		// own message).
		Assertions.assertThat(actual.getNsPrefixInAttributeValues()).contains(nsPrefixInAttributeValues);

		// To override the standard error message :
		// - remove the previous call to
		// Assertions.assertThat(actual.getNsPrefixInAttributeValues().contains(nsPrefixInAttributeValues)
		// - uncomment the line below and set your error message:
		// Assertions.assertThat(actual.getNsPrefixInAttributeValues()).overridingErrorMessage("\nmy error message %s",
		// "arg1").contains(nsPrefixInAttributeValues);

		// return the current assertion for method chaining
		return this;
	}

	/**
	 * Verifies that the actual validationOutput has no
	 * nsPrefixInAttributeValues.
	 * 
	 * @return this assertion object.
	 * @throws AssertionError
	 *             if the actual validationOutput's nsPrefixInAttributeValues is
	 *             not empty.
	 */
	public ValidationOutputAssert hasNoNsPrefixInAttributeValues() {
		// check that actual validationOutput we want to make assertions on is
		// not null.
		isNotNull();

		// we override the default error message with a more explicit one
		String assertjErrorMessage = "\nExpected :\n  <%s>\nnot to have nsPrefixInAttributeValues but had :\n  <%s>";

		// check
		if (!actual.getNsPrefixInAttributeValues().isEmpty()) {
			failWithMessage(assertjErrorMessage, actual, actual.getNsPrefixInAttributeValues());
		}

		// return the current assertion for method chaining
		return this;
	}

	/**
	 * Verifies that the actual validationOutput's phase is equal to the given
	 * one.
	 * 
	 * @param phase
	 *            the given phase to compare the actual validationOutput's phase
	 *            to.
	 * @return this assertion object.
	 * @throws AssertionError
	 *             - if the actual validationOutput's phase is not equal to the
	 *             given one.
	 */
	public ValidationOutputAssert hasPhase(String phase) {
		// check that actual validationOutput we want to make assertions on is
		// not null.
		isNotNull();

		// overrides the default error message with a more explicit one
		String assertjErrorMessage = "\nExpected phase of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

		// null safe check
		String actualPhase = actual.getPhase();
		if (!org.assertj.core.util.Objects.areEqual(actualPhase, phase)) {
			failWithMessage(assertjErrorMessage, actual, phase, actualPhase);
		}

		// return the current assertion for method chaining
		return this;
	}

	/**
	 * Verifies that the actual ValidationOutput's reports contains the given
	 * SuccessfulReport elements.
	 * 
	 * @param reports
	 *            the given elements that should be contained in actual
	 *            ValidationOutput's reports.
	 * @return this assertion object.
	 * @throws AssertionError
	 *             if the actual ValidationOutput's reports does not contain all
	 *             given SuccessfulReport elements.
	 */
	public ValidationOutputAssert hasReports(SuccessfulReport... reports) {
		// check that actual ValidationOutput we want to make assertions on is not null.
		isNotNull();

		// check that given SuccessfulReport varargs is not null.
		if (reports == null)
			throw new AssertionError("Expecting reports parameter not to be null.");

		// check with standard error message (see commented below to set your own message).
		Assertions.assertThat(actual.getReports()).contains(reports);

		// To override the standard error message :
		// - remove the previous call to Assertions.assertThat(actual.getReports().contains(reports)
		// - uncomment the line below and set your error message:
		// Assertions.assertThat(actual.getReports()).overridingErrorMessage("\nmy error message %s", "arg1").contains(reports);

		// return the current assertion for method chaining
		return this;
	}

	/**
	 * Verifies that the actual ValidationOutput has no reports.
	 * 
	 * @return this assertion object.
	 * @throws AssertionError
	 *             if the actual ValidationOutput's reports is not empty.
	 */
	public ValidationOutputAssert hasNoReports() {
		// check that actual ValidationOutput we want to make assertions on is not null.
		isNotNull();

		// we override the default error message with a more explicit one
		String assertjErrorMessage = "\nExpected :\n  <%s>\nnot to have reports but had :\n  <%s>";

		// check
		if (!actual.getReports().isEmpty()) {
			failWithMessage(assertjErrorMessage, actual, actual.getReports());
		}

		// return the current assertion for method chaining
		return this;
	}

	/**
	 * Verifies that the actual validationOutput's schemaVersion is equal to the
	 * given one.
	 * 
	 * @param schemaVersion
	 *            the given schemaVersion to compare the actual
	 *            validationOutput's schemaVersion to.
	 * @return this assertion object.
	 * @throws AssertionError
	 *             - if the actual validationOutput's schemaVersion is not equal
	 *             to the given one.
	 */
	public ValidationOutputAssert hasSchemaVersion(String schemaVersion) {
		// check that actual validationOutput we want to make assertions on is
		// not null.
		isNotNull();

		// overrides the default error message with a more explicit one
		String assertjErrorMessage = "\nExpected schemaVersion of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

		// null safe check
		String actualSchemaVersion = actual.getSchemaVersion();
		if (!org.assertj.core.util.Objects.areEqual(actualSchemaVersion, schemaVersion)) {
			failWithMessage(assertjErrorMessage, actual, schemaVersion, actualSchemaVersion);
		}

		// return the current assertion for method chaining
		return this;
	}

	/**
	 * Verifies that the actual validationOutput's text contains the given Text
	 * elements.
	 * 
	 * @param text
	 *            the given elements that should be contained in actual
	 *            validationOutput's text.
	 * @return this assertion object.
	 * @throws AssertionError
	 *             if the actual validationOutput's text does not contain all
	 *             given Text elements.
	 */
	public ValidationOutputAssert hasText(Text... text) {
		// check that actual validationOutput we want to make assertions on is
		// not null.
		isNotNull();

		// check that given Text varargs is not null.
		if (text == null)
			throw new AssertionError("Expecting text parameter not to be null.");

		// check with standard error message (see commented below to set your
		// own message).
		Assertions.assertThat(actual.getText()).contains(text);

		// To override the standard error message :
		// - remove the previous call to
		// Assertions.assertThat(actual.getText().contains(text)
		// - uncomment the line below and set your error message:
		// Assertions.assertThat(actual.getText()).overridingErrorMessage("\nmy error message %s",
		// "arg1").contains(text);

		// return the current assertion for method chaining
		return this;
	}

	/**
	 * Verifies that the actual validationOutput has no text.
	 * 
	 * @return this assertion object.
	 * @throws AssertionError
	 *             if the actual validationOutput's text is not empty.
	 */
	public ValidationOutputAssert hasNoText() {
		// check that actual validationOutput we want to make assertions on is
		// not null.
		isNotNull();

		// we override the default error message with a more explicit one
		String assertjErrorMessage = "\nExpected :\n  <%s>\nnot to have text but had :\n  <%s>";

		// check
		if (!actual.getText().isEmpty()) {
			failWithMessage(assertjErrorMessage, actual, actual.getText());
		}

		// return the current assertion for method chaining
		return this;
	}

	/**
	 * Verifies that the actual validationOutput's title is equal to the given
	 * one.
	 * 
	 * @param title
	 *            the given title to compare the actual validationOutput's title
	 *            to.
	 * @return this assertion object.
	 * @throws AssertionError
	 *             - if the actual validationOutput's title is not equal to the
	 *             given one.
	 */
	public ValidationOutputAssert hasTitle(String title) {
		// check that actual validationOutput we want to make assertions on is
		// not null.
		isNotNull();

		// overrides the default error message with a more explicit one
		String assertjErrorMessage = "\nExpected title of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

		// null safe check
		String actualTitle = actual.getTitle();
		if (!org.assertj.core.util.Objects.areEqual(actualTitle, title)) {
			failWithMessage(assertjErrorMessage, actual, title, actualTitle);
		}

		// return the current assertion for method chaining
		return this;
	}

}
