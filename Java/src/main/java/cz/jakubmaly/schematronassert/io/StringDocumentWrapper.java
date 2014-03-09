package cz.jakubmaly.schematronassert.io;

import javax.xml.transform.stream.*;

public class StringDocumentWrapper implements DocumentWrapper {

	private String xmlText;

	public StringDocumentWrapper(String xmlText) {
		this.xmlText = xmlText;
	}

	@Override
	public StreamSource getStreamSource() {
		return Converter.createStreamSource(xmlText);
	}
}
