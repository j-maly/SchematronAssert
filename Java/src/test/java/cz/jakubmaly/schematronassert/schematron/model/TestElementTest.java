package cz.jakubmaly.schematronassert.schematron.model;

import static org.assertj.core.api.Assertions.*;

import java.util.*;

import org.junit.*;

public class TestElementTest {

	@Test
	public void should_create_simple_message() throws Exception {
		Assert a = new Assert();
		a.addTemplatedMessage("simple message");
	}

	@Test
	public void should_find_two_text_value_templates() throws Exception {
		Assert a = new Assert();
		a.addTemplatedMessage("one {template} and another {here}. ");

		assertThat(a.getMessage())
			.usingElementComparator(messagePartComparator)
			.containsExactly(
					"one ",
					new ValueOf("template"),
					" and another ",
					new ValueOf("here"),
					". "
			);
	}

	@Test
	public void should_find_two_neighbouring_templates() throws Exception {
		Assert a = new Assert();
		a.addTemplatedMessage("{template}{and another}");

		assertThat(a.getMessage())
			.usingElementComparator(messagePartComparator)
			.containsExactly(
					new ValueOf("template"),
					new ValueOf("and another")
			);
	}

	@Test
	public void should_recognize_escaping() throws Exception {
		Assert a = new Assert();
		a.addTemplatedMessage("simple message {{this is escaped}} without patterns");

		assertThat(a.getMessage())
			.usingElementComparator(messagePartComparator)
			.containsExactly("simple message {{this is escaped}} without patterns");
	}

	@Test
	@Ignore("escaping inside templates does not work at the moments, regex should be tweaked")
	public void should_recognize_escaping_inside_template() throws Exception {
		Assert a = new Assert();
		a.addTemplatedMessage("{one {{strange}} pattern}");

		assertThat(a.getMessage())
			.usingElementComparator(messagePartComparator)
			.containsExactly(new ValueOf("one {{strange}} pattern"));
	}

	Comparator<Object> messagePartComparator;

	@Before
	public void setup() {
		messagePartComparator = new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				if (o1 instanceof String && o2 instanceof String)
					return ((String) o1).compareTo((String) o2);
				else if (o1 instanceof ValueOf && o2 instanceof ValueOf)
					return ((ValueOf) o1).select.compareTo(((ValueOf) o2).select);
				else
					return -1;
			}
		};
	}
}
