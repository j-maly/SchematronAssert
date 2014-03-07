package cz.jakubmaly.schematronassert.schematron.model;

import java.util.*;

import javax.xml.bind.annotation.*;

/**
 * Represents either &lt;assert&gt; or &lt;report&gt; in Schematron schema
 */
public abstract class TestElement {
	@XmlAttribute(name = "test")
	public String test;

	@XmlAttribute(name = "flag")
	public String flag;

	@XmlAttribute(name = "id")
	public String id;

	@XmlAttribute(name = "diagnostics")
	public String diagnostics;

	private List<Object> mixedContent;

	@XmlElementRef(name = "value-of", type = ValueOf.class)
	@XmlMixed
	public List<Object> getMessage() {
		return mixedContent;
	}

	public void setMessage(List<Object> mixedContent) {
		this.mixedContent = mixedContent;
	}

	private static final java.util.regex.Pattern TEXT_VALUE_TEMPLATE_PATTERN = java.util.regex.Pattern
		.compile("(?<!\\{)\\{([^{}]*)\\}(?!\\})");

	public void addTemplatedMessage(String message) {
		if (message == null)
			return;
		java.util.regex.Matcher matcher = TEXT_VALUE_TEMPLATE_PATTERN.matcher(message);
		int lastIndex = 0;
		while (matcher.find()) {
			if (lastIndex < matcher.start())
				addMessage(message.substring(lastIndex, matcher.start()));
			addMessage(new ValueOf(matcher.group(1)));
			lastIndex = matcher.end();
		}
		if (lastIndex != message.length())
			addMessage(message.substring(lastIndex, message.length()));
	}

	public void addMessage(String message) {
		if (mixedContent == null) {
			mixedContent = new ArrayList<Object>();
		}
		this.mixedContent.add(message);
	}

	public void addMessage(ValueOf valueOf) {
		if (mixedContent == null) {
			mixedContent = new ArrayList<Object>();
		}
		this.mixedContent.add(valueOf);
	}

	public TestElement test(String test) {
		this.test = test;
		return this;
	}
}
