package cz.jakubmaly.schematronassert.svrl.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "text")
@XmlAccessorType(XmlAccessType.FIELD)
public class Text {

	@XmlValue
	private String text;

	public Text() {

	}

	public Text(String text) {
		this();
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
