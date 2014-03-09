package cz.jakubmaly.schematronassert.assertions;

import static org.assertj.core.api.Assertions.*;

import java.util.*;

import org.assertj.core.api.*;
import org.junit.*;

import com.google.common.collect.*;

import cz.jakubmaly.schematronassert.svrl.model.*;

public class IterableOfSuccessfulReportAssertTest {

	private List<SuccessfulReport> reports;
	private SuccessfulReport r1;
	private SuccessfulReport r2;
	private SuccessfulReport r3;
	private SuccessfulReport r4;

	@Before
	public void setUp() throws Exception {
		r1 = new SuccessfulReport("a1");
		r1.setFlag("warning");
		r2 = new SuccessfulReport("a2");
		r2.setFlag("error");
		r3 = new SuccessfulReport("a3");
		r3.setFlag("error");
		r4 = new SuccessfulReport("a4");
		r4.setFlag("warning");
		reports = Lists.newArrayList(r1, r2, r3, r4);
	}

	@Test
	public void should_filter_asserts_by_condition() {
		Condition<SuccessfulReport> condition = new Condition<SuccessfulReport>() {
			@Override
			public boolean matches(SuccessfulReport failedAssert) {
				return failedAssert.getFlag() != null && failedAssert.getFlag().equals("warning");
			}
		};
		assertThat(filter(reports).having(condition).get()).containsOnly(r1, r4);
	}

}
