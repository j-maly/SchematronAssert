package cz.jakubmaly.schematronassert.schematron.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "ns")
public class NamespacePrefixDeclaration {

	@XmlAttribute(name = "uri")
	public String uri;

	@XmlAttribute(name = "prefix")
	public String prefix;

	public NamespacePrefixDeclaration() {
		super();
	}

	public NamespacePrefixDeclaration(String prefix, String uri) {
		this();
		this.uri = uri;
		this.prefix = prefix;
	}
}
