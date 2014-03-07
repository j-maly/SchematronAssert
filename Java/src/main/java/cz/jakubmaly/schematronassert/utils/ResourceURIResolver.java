package cz.jakubmaly.schematronassert.utils;

import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

public class ResourceURIResolver implements URIResolver {
	private String resourceFolder;

	public ResourceURIResolver(String resourceFolder) {
		super();
		this.resourceFolder = resourceFolder;
	}

	public Source resolve(String href, String base) throws TransformerException {
		InputStream inputStream = this.getClass().getResourceAsStream(resourceFolder + href);
		return new StreamSource(inputStream);
	}
}