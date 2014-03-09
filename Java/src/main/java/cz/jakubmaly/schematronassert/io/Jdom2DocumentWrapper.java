package cz.jakubmaly.schematronassert.io;

import java.io.*;

import javax.xml.transform.stream.*;

import org.jdom2.*;

public class Jdom2DocumentWrapper implements DocumentWrapper {

	private Document document;
	private Element element;

	public Jdom2DocumentWrapper(Document document) {
		this.document = document;
	}

	public Jdom2DocumentWrapper(Element element) {
		this.element = element;
	}

	@Override
	public StreamSource getStreamSource() throws IOException {
		if (document != null)
			return Converter.createStreamSource(document);
		else
			return Converter.createStreamSource(element);
	}
}
