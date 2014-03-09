package cz.jakubmaly.schematronassert.io;

import javax.xml.transform.stream.*;

public class StreamSourceDocumentWrapper implements DocumentWrapper {

	private StreamSource streamSource;

	public StreamSourceDocumentWrapper(StreamSource xmlText) {
		this.streamSource = xmlText;
	}

	@Override
	public StreamSource getStreamSource() {
		return streamSource;
	}
}
