package cz.jakubmaly.schematronassert.io;

import java.io.*;

import javax.xml.transform.stream.*;

import org.jdom.*;

public class Jdom1DocumentWrapper implements DocumentWrapper {

	private Document document;
	private Element element;

	public Jdom1DocumentWrapper(Document document) {
		this.document = document;
	}

	public Jdom1DocumentWrapper(Element element) {
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
