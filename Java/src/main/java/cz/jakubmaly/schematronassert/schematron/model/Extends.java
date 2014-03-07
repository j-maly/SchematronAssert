package cz.jakubmaly.schematronassert.schematron.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "extends")
public class Extends {

	@XmlAttribute(name = "rule")
	public String rule;

	public Extends() {
		super();
	}

	public Extends(String extendedRuleId) {
		this();
		this.rule = extendedRuleId;
	}

}
