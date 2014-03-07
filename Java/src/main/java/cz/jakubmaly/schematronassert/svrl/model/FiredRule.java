package cz.jakubmaly.schematronassert.svrl.model;

import javax.xml.bind.annotation.*;

import com.google.common.base.*;

@XmlRootElement(name = "fired-rule")
@XmlAccessorType(XmlAccessType.FIELD)
public class FiredRule {
	@XmlAttribute(name = "id")
	private String id;
	@XmlAttribute(name = "context", required = true)
	private String context;
	@XmlAttribute(name = "role")
	private String role;
	@XmlAttribute(name = "flag")
	private String flag;

	public FiredRule() {
		super();
	}

	public FiredRule(String id) {
		this();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
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

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("id", id)
			.add("flag", flag)
			.add("role", role)
			.add("context", context)
			.toString();
	}

}