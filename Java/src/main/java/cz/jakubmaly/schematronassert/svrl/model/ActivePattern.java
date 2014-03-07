package cz.jakubmaly.schematronassert.svrl.model;

import javax.xml.bind.annotation.*;

import com.google.common.base.*;

@XmlRootElement(name = "active-pattern")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActivePattern {
	@XmlAttribute(name = "document")
	private String document;
	@XmlAttribute(name = "id")
	private String id;
	@XmlAttribute(name = "name")
	private String name;
	@XmlAttribute(name = "role")
	private String role;

	public ActivePattern() {
		super();
	}

	public ActivePattern(String id) {
		this();
		this.id = id;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("id", id)
			.add("name", name)
			.add("role", role)
			.add("document", document)
			.toString();
	}

}
