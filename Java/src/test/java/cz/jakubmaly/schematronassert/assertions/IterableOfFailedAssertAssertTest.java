package cz.jakubmaly.schematronassert.assertions;

import static cz.jakubmaly.schematronassert.assertions.Assertions.*;

import java.util.*;

import org.apache.commons.lang.*;
import org.assertj.core.api.Condition;
import org.hamcrest.*;
import org.junit.*;

import com.google.common.collect.*;

import cz.jakubmaly.schematronassert.svrl.model.*;

public class IterableOfFailedAssertAssertTest {

	private List<FailedAssert> asserts;
	private FailedAssert a1;
	private FailedAssert a2;
	private FailedAssert a3;
	private FailedAssert a4;
	private FiredRule r1;
	private FiredRule r2;
	private ActivePattern p;

	@Before
	public void setUp() throws Exception {
		r1 = new FiredRule("r1");
		r2 = new FiredRule("r2");
		p = new ActivePattern("p1");
		a1 = new FailedAssert("a1");
		a1.setFlag("warning");
		a1.setLocation("place1");
		a1.setText("this is a1");
		a1.setRole("role1");
		a1.setFiredRule(r1);
		a2 = new FailedAssert("a2");
		a2.setFlag("error");
		a2.setText("check this");
		a2.setFiredRule(r1);
		a3 = new FailedAssert("a3");
		a3.setFlag("error");
		a3.setLocation("place1");
		a3.setText("check this");
		a3.setRole("role1");
		a3.setFiredRule(r2);
		a3.setPattern(p);
		a4 = new FailedAssert("a4");
		a4.setFlag("warning");
		a4.setLocation("place4");
		a4.setText("this is a4");
		a4.setRole("role4");
		a4.setFiredRule(r1);
		asserts = Lists.newArrayList(a1, a2, a3, a4);
	}

	@Test
	public void should_filter_asserts_by_condition() {
		Condition<FailedAssert> condition = new Condition<FailedAssert>() {
			@Override
			public boolean matches(FailedAssert failedAssert) {
				return failedAssert.getFlag() != null && failedAssert.getFlag().equals("warning");
			}
		};
		assertThat(asserts).filteredBy(condition).containsOnly(a1, a4);
	}

	@Test
	public void should_filter_asserts_using_matcher() {
		org.hamcrest.Matcher<FailedAssert> matcher = new BaseMatcher<FailedAssert>() {
			@Override
			public boolean matches(Object item) {
				return ((FailedAssert) item) != null && ObjectUtils.equals(((FailedAssert) item).getFlag(), "warning");
			}

			@Override
			public void describeTo(Description description) {
			}
		};
		assertThat(asserts).filteredBy(matcher).containsOnly(a1, a4);
	}

	@Test
	public void testWithText() throws Exception {
		assertThat(asserts).withText("check this").containsOnly(a2, a3);
	}

	@Test
	public void testWithId() throws Exception {
		assertThat(asserts).withId("a2").containsOnly(a2);
		assertThat(asserts).withId("a3").containsOnly(a3);
		assertThat(asserts).withId("xxx").isEmpty();
	}

	@Test
	public void testWithLocation() throws Exception {
		assertThat(asserts).withLocation("place1").containsOnly(a1, a3);
	}

	@Test
	public void testWithRole() throws Exception {
		assertThat(asserts).withRole("role1").containsOnly(a1, a3);
		assertThat(asserts).withRole("role4").containsOnly(a4);
	}

	@Test
	public void testFromRule() throws Exception {
		assertThat(asserts).fromRule(r1).containsOnly(a1, a2, a4);
		assertThat(asserts).fromRule(r2).containsOnly(a3);
	}

	@Test
	public void testFromPattern() throws Exception {
		assertThat(asserts).fromPattern(p).containsOnly(a3);
	}
}
