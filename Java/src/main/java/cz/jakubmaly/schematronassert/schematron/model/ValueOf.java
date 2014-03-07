package cz.jakubmaly.schematronassert.schematron.model;

import javax.xml.bind.annotation.*;

import com.google.common.base.*;

@XmlRootElement(name = "value-of")
public class ValueOf {
	@XmlAttribute
	public String select;

	public ValueOf() {
		super();
	}

	public ValueOf(String select) {
		this();
		this.select = select;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("select", select)
			.toString();
	}
}
