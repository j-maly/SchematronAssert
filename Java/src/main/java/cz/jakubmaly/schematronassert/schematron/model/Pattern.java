package cz.jakubmaly.schematronassert.schematron.model;

import java.util.*;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "pattern")
public class Pattern {
	@XmlElement(name = "include")
	public List<Include> includes;

	@XmlAttribute(name = "abstract")
	public boolean abstractAttribute;

	@XmlAttribute(name = "id")
	public String id;

	@XmlAttribute(name = "isA")
	public String isA;

	@XmlElement(name = "title")
	public String title;

	@XmlElement(name = "let")
	public List<LetDeclaration> letDeclarations;

	@XmlElement(name = "rule")
	public List<Rule> rules;

	public Pattern() {
		super();
	}

	public Pattern(String id) {
		this();
		id(id);
	}

	public Pattern withRule(Rule rule) {
		if (rules == null)
			rules = new ArrayList<Rule>();
		rules.add(rule);
		return this;
	}

	public Pattern id(String id) {
		this.id = id;
		return this;
	}

	public Pattern let(String name, String value) {
		if (letDeclarations == null) {
			this.letDeclarations = new ArrayList<LetDeclaration>();
		}
		LetDeclaration l = new LetDeclaration()
			.name(name)
			.value(value);
		letDeclarations.add(l);
		return this;
	}
}
