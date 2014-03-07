package cz.jakubmaly.schematronassert.assertions;

import java.util.*;

import cz.jakubmaly.schematronassert.svrl.model.*;

/**
 * Entry point for assertion of different data types. Each method in this class
 * is a static factory for the type-specific assertion objects.
 */
public class Assertions {

	/**
	 * Creates a new instance of <code>{@link SchematronOutputAssert}</code>.
	 * 
	 */
	public static ValidationOutputAssert assertThat(ValidationOutput svrlOutput) {
		return new ValidationOutputAssert(svrlOutput);
	}

	/**
	 * Creates a new instance of <code>{@link ActivePatternAssert}</code>.
	 * 
	 * @param actual
	 *            the actual value.
	 * @return the created assertion object.
	 */
	public static ActivePatternAssert assertThat(ActivePattern actual) {
		return new ActivePatternAssert(actual);
	}

	/**
	 * Creates a new instance of <code>{@link DiagnosticReferenceAssert}</code>.
	 * 
	 * @param actual
	 *            the actual value.
	 * @return the created assertion object.
	 */
	public static DiagnosticReferenceAssert assertThat(DiagnosticReference actual) {
		return new DiagnosticReferenceAssert(actual);
	}

	/**
	 * Creates a new instance of <code>{@link FailedAssertAssert}</code>.
	 * 
	 * @param actual
	 *            the actual value.
	 * @return the created assertion object.
	 */
	public static FailedAssertAssert assertThat(FailedAssert actual) {
		return new FailedAssertAssert(actual);
	}

	/**
	 * Creates a new instance of <code>{@link FiredRuleAssert}</code>.
	 * 
	 * @param actual
	 *            the actual value.
	 * @return the created assertion object.
	 */
	public static FiredRuleAssert assertThat(FiredRule actual) {
		return new FiredRuleAssert(actual);
	}

	/**
	 * Creates a new instance of <code>{@link FoundTestElementAssert}</code>.
	 * 
	 * @param actual
	 *            the actual value.
	 * @return the created assertion object.
	 */
	public static FoundTestElementAssert assertThat(FoundTestElement actual) {
		return new FoundTestElementAssert(actual);
	}

	/**
	 * Creates a new instance of <code>{@link NsAssert}</code>.
	 * 
	 * @param actual
	 *            the actual value.
	 * @return the created assertion object.
	 */
	public static NsAssert assertThat(Ns actual) {
		return new NsAssert(actual);
	}

	/**
	 * Creates a new instance of
	 * <code>{@link NsPrefixInAttributeValuesAssert}</code>.
	 * 
	 * @param actual
	 *            the actual value.
	 * @return the created assertion object.
	 */
	public static NsPrefixInAttributeValuesAssert assertThat(NsPrefixInAttributeValues actual) {
		return new NsPrefixInAttributeValuesAssert(actual);
	}

	/**
	 * Creates a new instance of <code>{@link SuccessfulReportAssert}</code>.
	 * 
	 * @param actual
	 *            the actual value.
	 * @return the created assertion object.
	 */
	public static SuccessfulReportAssert assertThat(SuccessfulReport actual) {
		return new SuccessfulReportAssert(actual);
	}

	/**
	 * Creates a new instance of <code>{@link TextAssert}</code>.
	 * 
	 * @param actual
	 *            the actual value.
	 * @return the created assertion object.
	 */
	public static TextAssert assertThat(Text actual) {
		return new TextAssert(actual);
	}

	protected Assertions() {
	}

	public static void fail(String format, Object... args) {
		String message = String.format(format, args);
		org.assertj.core.api.Assertions.fail(message);
	}

	public static IterableOfFailedAssertAssert assertThat(List<FailedAssert> failedAsserts) {
		return new IterableOfFailedAssertAssert(failedAsserts);
	}

	public static IterableOfSuccessfulReportAssert assertThat(List<SuccessfulReport> failedAsserts) {
		return new IterableOfSuccessfulReportAssert(failedAsserts);
	}
}
