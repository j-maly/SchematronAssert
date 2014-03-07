package cz.jakubmaly.schematronassert.assertions;

import java.util.*;

import cz.jakubmaly.schematronassert.svrl.model.*;

public class IterableOfFailedAssertAssert
		extends IterableOfFoundTestElementAssert<FailedAssert, IterableOfFailedAssertAssert>
{

	public IterableOfFailedAssertAssert(Iterable<FailedAssert> actual) {
		super(actual);
	}

	public IterableOfFailedAssertAssert(Iterator<FailedAssert> actual) {
		super(actual);
	}

	@Override
	public IterableOfFailedAssertAssert createInstance(Iterable<FailedAssert> actual) {
		return new IterableOfFailedAssertAssert(actual);
	}
}
