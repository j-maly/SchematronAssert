package cz.jakubmaly.schematronassert.io;

import java.io.*;

import javax.xml.transform.stream.*;

public interface DocumentWrapper {

	StreamSource getStreamSource() throws IOException;

}
