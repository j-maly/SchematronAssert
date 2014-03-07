package cz.jakubmaly.schematronassert.svrl.model;

import java.util.*;

import javax.xml.bind.annotation.*;

import com.google.common.base.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class FoundTestElement {
	@XmlElement(name = "diagnostic-reference")
	private List<DiagnosticReference> diagnosticReference;
	@XmlElement(name = "text")
	private Text text;
	@XmlAttribute(name = "id")
	private String id;
	@XmlAttribute(name = "location", required = true)
	private String location;
	@XmlAttribute(name = "test", required = true)
	private String test;
	@XmlAttribute(name = "role")
	private String role;
	@XmlAttribute(name = "flag")
	private String flag;

	@XmlTransient
	private ActivePattern pattern;

	@XmlTransient
	private FiredRule firedRule;

	public FoundTestElement() {
	}

	public FoundTestElement(String id) {
		this();
		this.id = id;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.omitNullValues()
			.add("id", id)
			.add("flag", flag)
			.add("text", text)
			.add("test", test)
			.add("role", role)
			.add("location", location)
			.toString();
	}

	public List<DiagnosticReference> getDiagnosticReference() {
		return diagnosticReference;
	}

	public void setDiagnosticReference(List<DiagnosticReference> diagnosticReference) {
		this.diagnosticReference = diagnosticReference;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public void setText(String text) {
		this.text = new Text(text);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public ActivePattern getPattern() {
		return pattern;
	}

	public void setPattern(ActivePattern pattern) {
		this.pattern = pattern;
	}

	public FiredRule getFiredRule() {
		return firedRule;
	}

	public void setFiredRule(FiredRule firedRule) {
		this.firedRule = firedRule;
	}
}