package cz.jakubmaly.schematronassert.schematron.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "let")
public class LetDeclaration {

	@XmlAttribute(name = "name")
	public String name;

	@XmlAttribute(name = "value")
	public String value;

	public LetDeclaration name(String name) {
		this.name = name;
		return this;
	}

	public LetDeclaration value(String value) {
		this.value = value;
		return this;
	}
}
