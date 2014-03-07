package cz.jakubmaly.schematronassert.schematron.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "include")
public class Include {
	@XmlAttribute
	public String href;
}
