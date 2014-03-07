package cz.jakubmaly.schematronassert.schematron.model;

import java.util.*;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "rule")
public class Rule {
	@XmlAttribute(name = "abstract")
	public boolean abstractAttribute;

	@XmlAttribute(name = "id")
	public String id;

	@XmlAttribute(name = "context")
	public String context;

	@XmlElement(name = "let")
	public Collection<LetDeclaration> letDeclarations;

	@XmlElements(value = { @XmlElement(name = "assert", type = Assert.class), @XmlElement(name = "report", type = Report.class) })
	public Collection<TestElement> assertionElements;

	@XmlElement(name = "extends")
	private Extends extendsRule;

	public Rule() {
		super();
	}

	public Rule(String id) {
		this();
		this.id = id;
	}

	public Rule context(String context) {
		this.context = context;
		return this;
	}

	public Rule withAssert(String condition) {
		return withAssert(condition, null);
	}

	public Rule withAssert(String condition, String message) {
		if (assertionElements == null) {
			this.assertionElements = new ArrayList<TestElement>();
		}
		Assert a = new Assert();
		a.test(condition);
		a.addTemplatedMessage(message);
		assertionElements.add(a);
		return this;
	}

	public Rule withReport(String condition) {
		return withReport(condition, null);
	}

	public Rule withReport(String condition, String message) {
		if (assertionElements == null) {
			this.assertionElements = new ArrayList<TestElement>();
		}
		Report r = new Report();
		r.test(condition);
		r.addTemplatedMessage(message);
		assertionElements.add(r);
		return this;
	}

	public Rule let(String name, String value) {
		if (letDeclarations == null) {
			this.letDeclarations = new ArrayList<LetDeclaration>();
		}
		LetDeclaration l = new LetDeclaration()
			.name(name)
			.value(value);
		letDeclarations.add(l);
		return this;
	}

	public Rule abstractAttribute(boolean value) {
		this.abstractAttribute = value;
		return this;
	}

	public Rule extendsRule(String extendedRuleId) {
		this.extendsRule = new Extends(extendedRuleId);
		return this;
	}
}
