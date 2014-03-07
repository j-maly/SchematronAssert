package cz.jakubmaly.schematronassert.schematron.serialization;

import java.io.*;

import javax.xml.transform.stream.*;

import cz.jakubmaly.schematronassert.utils.*;

public class Converter {
	public static StreamSource createStreamSource(String xmlText) {
		StringReader schemaReader = new StringReader(xmlText);
		StreamSource xmlSource = new StreamSource(schemaReader);
		return xmlSource;
	}

	public static StreamSource createRereadableSource(StreamSource xmlSource) throws IOException {
		if (xmlSource.getReader() != null) {
			return new StreamSource(new RereadableReader(xmlSource.getReader()));
		} else if (xmlSource.getInputStream() != null) {
			return new StreamSource(new RereadableStream(xmlSource.getInputStream()));
		} else {
			throw new IOException(
					"Only implementations of StreamSource which are based on Reader or InputStream are supported. The underlying Reader/InputStream must allow mark/reset. ");
		}
	}
}
