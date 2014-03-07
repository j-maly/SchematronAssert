package cz.jakubmaly.schematronassert.svrl.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "failed-assert")
@XmlAccessorType(XmlAccessType.FIELD)
public class FailedAssert extends FoundTestElement {

	public FailedAssert() {
		super();
	}

	public FailedAssert(String id) {
		super(id);
	}
}
