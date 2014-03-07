package cz.jakubmaly.schematronassert.assertions;

import java.util.*;

import org.apache.commons.lang.*;
import org.assertj.core.api.*;

import cz.jakubmaly.schematronassert.svrl.model.*;

public abstract class IterableOfFoundTestElementAssert<MemberType extends FoundTestElement, ConcreteFilterableIterableAssert>
		extends FilterableIterableAssert<MemberType, ConcreteFilterableIterableAssert> {

	protected IterableOfFoundTestElementAssert(@SuppressWarnings("rawtypes") Iterable actual) {
		super(actual);
	}

	protected IterableOfFoundTestElementAssert(@SuppressWarnings("rawtypes") Iterator actual) {
		super(actual);
	}

	public ConcreteFilterableIterableAssert withText(final String text) {
		Condition<MemberType> condition = new Condition<MemberType>() {
			@Override
			public boolean matches(MemberType examined) {
				return examined.getText() != null && StringUtils.equals(text, examined.getText().getText());
			}
		};
		return withText(condition);
	}

	public ConcreteFilterableIterableAssert withText(Condition<MemberType> textCondition) {
		return filteredBy(textCondition);
	}

	public ConcreteFilterableIterableAssert withId(final String id) {
		Condition<MemberType> condition = new Condition<MemberType>() {
			@Override
			public boolean matches(MemberType examined) {
				return StringUtils.equals(id, examined.getId());
			}
		};
		return withId(condition);
	}

	public ConcreteFilterableIterableAssert withId(Condition<MemberType> condition) {
		return filteredBy(condition);
	}

	public ConcreteFilterableIterableAssert withLocation(final String location) {
		Condition<MemberType> condition = new Condition<MemberType>() {
			@Override
			public boolean matches(MemberType examined) {
				return StringUtils.equals(location, examined.getLocation());
			}
		};
		return withLocation(condition);
	}

	public ConcreteFilterableIterableAssert withLocation(Condition<MemberType> condition) {
		return filteredBy(condition);
	}

	public ConcreteFilterableIterableAssert withName(final String test) {
		Condition<MemberType> condition = new Condition<MemberType>() {
			@Override
			public boolean matches(MemberType examined) {
				return StringUtils.equals(test, examined.getTest());
			}
		};
		return withName(condition);
	}

	public ConcreteFilterableIterableAssert withName(Condition<MemberType> condition) {
		return filteredBy(condition);
	}

	public ConcreteFilterableIterableAssert withRole(final String role) {
		Condition<MemberType> condition = new Condition<MemberType>() {
			@Override
			public boolean matches(MemberType examined) {
				return StringUtils.equals(role, examined.getRole());
			}
		};
		return withRole(condition);
	}

	public ConcreteFilterableIterableAssert withRole(Condition<MemberType> condition) {
		return filteredBy(condition);
	}

	public ConcreteFilterableIterableAssert withFlag(final String flag) {
		Condition<MemberType> condition = new Condition<MemberType>() {
			@Override
			public boolean matches(MemberType examined) {
				return StringUtils.equals(flag, examined.getFlag());
			}
		};
		return withFlag(condition);
	}

	public ConcreteFilterableIterableAssert withFlag(Condition<MemberType> condition) {
		return filteredBy(condition);
	}

	public ConcreteFilterableIterableAssert fromRule(final FiredRule rule) {
		Condition<MemberType> condition = new Condition<MemberType>() {
			@Override
			public boolean matches(MemberType examined) {
				return ObjectUtils.equals(examined.getFiredRule(), rule);
			}
		};
		return fromRule(condition);
	}

	public ConcreteFilterableIterableAssert fromRule(Condition<MemberType> condition) {
		return filteredBy(condition);
	}

	public ConcreteFilterableIterableAssert fromPattern(final ActivePattern pattern) {
		Condition<MemberType> condition = new Condition<MemberType>() {
			@Override
			public boolean matches(MemberType examined) {
				return ObjectUtils.equals(examined.getPattern(), pattern);
			}
		};
		return fromPattern(condition);
	}

	public ConcreteFilterableIterableAssert fromPattern(Condition<MemberType> condition) {
		return filteredBy(condition);
	}
}