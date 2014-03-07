package cz.jakubmaly.schematronassert.assertions;

import java.util.*;

import org.assertj.core.api.*;

import com.google.common.base.*;
import com.google.common.collect.*;

public abstract class FilterableIterableAssert<MemberType, ConcreteFilterableIterableAssert>
		extends IterableAssert<MemberType> {

	@SuppressWarnings("unchecked")
	public FilterableIterableAssert(@SuppressWarnings("rawtypes") Iterable actual) {
		super(actual);
	}

	@SuppressWarnings("unchecked")
	public FilterableIterableAssert(@SuppressWarnings("rawtypes") Iterator actual) {
		super(actual);
	}

	public abstract ConcreteFilterableIterableAssert createInstance(Iterable<MemberType> actual);

	public ConcreteFilterableIterableAssert filteredBy(final Condition<MemberType> condition) {
		Iterable<MemberType> filtered = Iterables.filter(actual, new Predicate<MemberType>() {
			public boolean apply(MemberType arg) {
				return condition.matches(arg);
			}
		});

		return this.createInstance(filtered);
	}

	public ConcreteFilterableIterableAssert filteredBy(final org.hamcrest.Matcher<MemberType> condition) {
		Iterable<MemberType> filtered = Iterables.filter(actual, new Predicate<MemberType>() {
			public boolean apply(MemberType arg) {
				return condition.matches(arg);
			}
		});
		return this.createInstance(filtered);
	}
}