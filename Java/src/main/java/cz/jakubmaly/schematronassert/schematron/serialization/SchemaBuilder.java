package cz.jakubmaly.schematronassert.schematron.serialization;

import cz.jakubmaly.schematronassert.schematron.model.*;

public class SchemaBuilder {

	public static Schema schema() {
		return new Schema();
	}

	public static Rule rule() {
		return new Rule();
	}

	public static Rule rule(String id) {
		return new Rule(id);
	}

	public static Pattern pattern() {
		return new Pattern();
	}

	public static Pattern pattern(String id) {
		return new Pattern().id(id);
	}

	public static Phase phase(String id) {
		return new Phase().id(id);
	}

}
