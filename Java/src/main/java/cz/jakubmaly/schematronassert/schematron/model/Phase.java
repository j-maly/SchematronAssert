package cz.jakubmaly.schematronassert.schematron.model;

import java.util.*;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "phase")
public class Phase {
	@XmlAttribute(name = "id")
	public String id;

	@XmlElement(name = "include")
	public Collection<Include> includes;

	@XmlElement(name = "let")
	public Collection<LetDeclaration> let;

	@XmlElement(name = "active")
	public Collection<Active> activePatterns;

	public Phase id(String id) {
		this.id = id;
		return this;
	}

	public Phase withActivePattern(String patternId) {
		return withActivePattern(patternId, null);
	}

	public Phase withActivePattern(String patternId, String text) {
		if (this.activePatterns == null)
			this.activePatterns = new ArrayList<Active>();
		this.activePatterns.add(new Active(patternId, text));
		return this;
	}
}
