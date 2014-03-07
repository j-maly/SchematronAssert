package cz.jakubmaly.schematronassert.assertions;

import java.util.*;

import cz.jakubmaly.schematronassert.svrl.model.*;

public class IterableOfSuccessfulReportAssert
		extends IterableOfFoundTestElementAssert<SuccessfulReport, IterableOfSuccessfulReportAssert>
{

	public IterableOfSuccessfulReportAssert(Iterable<SuccessfulReport> actual) {
		super(actual);
	}

	public IterableOfSuccessfulReportAssert(Iterator<SuccessfulReport> actual) {
		super(actual);
	}

	@Override
	public IterableOfSuccessfulReportAssert createInstance(Iterable<SuccessfulReport> actual) {
		return new IterableOfSuccessfulReportAssert(actual);
	}
}
