package cz.jakubmaly.schematronassert.svrl.model;

import javax.xml.bind.annotation.*;

import com.google.common.base.*;

@XmlRootElement(name = "diagnostic-reference")
@XmlAccessorType(XmlAccessType.FIELD)
public class DiagnosticReference {
	@XmlElement(name = "text")
	private Text text;
	@XmlAttribute(name = "diagnostic", required = true)
	private String diagnostic;

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public String getDiagnostic() {
		return diagnostic;
	}

	public void setDiagnostic(String diagnostic) {
		this.diagnostic = diagnostic;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("text", text)
			.add("diagnostic", diagnostic)
			.toString();
	}

}
