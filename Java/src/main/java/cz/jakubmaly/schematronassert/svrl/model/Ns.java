package cz.jakubmaly.schematronassert.svrl.model;

import javax.xml.bind.annotation.*;

import com.google.common.base.*;

@XmlRootElement(name = "ns")
@XmlAccessorType(XmlAccessType.FIELD)
public class Ns {

	@XmlAttribute(name = "prefix", required = true)
	private String prefix;
	@XmlAttribute(name = "uri", required = true)
	private String uri;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
			.add("prefix", prefix)
			.add("uri", uri)
			.toString();
	}
}
