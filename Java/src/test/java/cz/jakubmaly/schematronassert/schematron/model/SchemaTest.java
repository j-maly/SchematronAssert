package cz.jakubmaly.schematronassert.schematron.model;

import org.junit.*;

import cz.jakubmaly.schematronassert.schematron.validation.*;

public class SchemaTest {

	@Test
	public void should_pass_validation() throws Exception {
		Schema s = new Schema();
		s.withNs("p1", "uri1");
		s.withNs("p2", "uri2");
		s.validate();
	}

	@Test(expected = InvalidSchemaException.class)
	public void should_report_duplicate_prefix() throws Exception {
		Schema s = new Schema();
		s.withNs("p1", "uri1");
		s.withNs("p1", "uri2");
		s.validate();
	}

	@Test(expected = InvalidSchemaException.class)
	public void should_report_empty_namespace_prefix() throws Exception {
		Schema s = new Schema();
		s.withNs("", "uri1");
		s.validate();
	}
}
