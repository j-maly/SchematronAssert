package cz.jakubmaly.schematronassert.assertions;

import static org.assertj.core.api.Assertions.*;

import org.apache.commons.lang.*;
import org.assertj.core.api.*;

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

	public static Condition<FoundTestElement> withFlag(final String flag) {
		Condition<FoundTestElement> condition = new Condition<FoundTestElement>() {
			@Override
			public boolean matches(FoundTestElement examined) {
				return StringUtils.equals(flag, examined.getFlag());
			}
		};
		return condition;
	}

	public static Condition<FoundTestElement> withLocation(final String location) {
		Condition<FoundTestElement> condition = new Condition<FoundTestElement>() {
			@Override
			public boolean matches(FoundTestElement examined) {
				return StringUtils.equals(location, examined.getLocation());
			}

		};
		return condition;
	}

	public static Condition<FoundTestElement> withName(final String test) {
		Condition<FoundTestElement> condition = new Condition<FoundTestElement>() {
			@Override
			public boolean matches(FoundTestElement examined) {
				return StringUtils.equals(test, examined.getTest());
			}
		};
		return condition;
	}

	public static Condition<FoundTestElement> withRole(final String role) {
		Condition<FoundTestElement> condition = new Condition<FoundTestElement>() {
			@Override
			public boolean matches(FoundTestElement examined) {
				return StringUtils.equals(role, examined.getRole());
			}
		};
		return condition;
	}

	public static Condition<FoundTestElement> withId(final String id) {
		Condition<FoundTestElement> condition = new Condition<FoundTestElement>() {
			@Override
			public boolean matches(FoundTestElement examined) {
				return StringUtils.equals(id, examined.getId());
			}
		};
		return condition;
	}

	public static Condition<FoundTestElement> withTest(final String test) {
		Condition<FoundTestElement> condition = new Condition<FoundTestElement>() {
			@Override
			public boolean matches(FoundTestElement examined) {
				return examined.getTest() != null && StringUtils.equals(test, examined.getTest());
			}
		};
		return condition;
	}

	public static Condition<FoundTestElement> withText(final String text) {
		Condition<FoundTestElement> condition = new Condition<FoundTestElement>() {
			@Override
			public boolean matches(FoundTestElement examined) {
				return examined.getText() != null && StringUtils.equals(text, examined.getText().getText());
			}
		};
		return condition;
	}

	public static Condition<FoundTestElement> fromRule(final FiredRule rule) {
		Condition<FoundTestElement> condition = new Condition<FoundTestElement>() {
			@Override
			public boolean matches(FoundTestElement examined) {
				return ObjectUtils.equals(examined.getFiredRule(), rule);
			}
		};
		return condition;
	}

	public static Condition<FoundTestElement> fromPattern(final ActivePattern pattern) {
		Condition<FoundTestElement> condition = new Condition<FoundTestElement>() {
			@Override
			public boolean matches(FoundTestElement examined) {
				return ObjectUtils.equals(examined.getPattern(), pattern);
			}
		};
		return condition;
	}

	public static Condition<Object> isFiredRule() {
		Condition<Object> condition = new Condition<Object>() {
			@Override
			public boolean matches(Object value) {
				return value instanceof FiredRule;
			}
		};
		return condition;
	}

	public static Condition<Object> isFailedAssert() {
		Condition<Object> condition = new Condition<Object>() {
			@Override
			public boolean matches(Object value) {
				return value instanceof FailedAssert;
			}
		};
		return condition;
	}

	public static Condition<Object> isSuccessfulReport() {
		Condition<Object> condition = new Condition<Object>() {
			@Override
			public boolean matches(Object value) {
				return value instanceof SuccessfulReport;
			}
		};
		return condition;
	}

	@SuppressWarnings("unchecked")
	public static Condition<Object> isFailedAssertOrSuccessfulReport() {
		return anyOf(isSuccessfulReport(), isFailedAssert());
	}
}
