package cz.jakubmaly.schematronassert.svrl.model;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "successful-report")
@XmlAccessorType(XmlAccessType.FIELD)
public class SuccessfulReport extends FoundTestElement {

	public SuccessfulReport() {
		super();
	}

	public SuccessfulReport(String id) {
		super(id);
	}
}
