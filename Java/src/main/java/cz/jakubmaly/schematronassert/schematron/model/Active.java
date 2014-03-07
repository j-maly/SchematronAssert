package cz.jakubmaly.schematronassert.schematron.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "active")
public class Active {

	@XmlAttribute(name = "pattern")
	public String patternId;

	@XmlValue
	public String text;

	public Active() {
		super();
	}

	public Active(String patternId) {
		this();
		this.patternId = patternId;
	}

	public Active(String patternId, String text) {
		this(patternId);
		this.text = text;
	}
}
