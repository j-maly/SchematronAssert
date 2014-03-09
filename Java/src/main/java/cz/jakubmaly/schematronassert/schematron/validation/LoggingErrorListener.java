package cz.jakubmaly.schematronassert.schematron.validation;

import java.util.*;

import javax.xml.transform.*;

import org.slf4j.*;

public class LoggingErrorListener implements ErrorListener {
	static Logger logger = LoggerFactory.getLogger(LoggingErrorListener.class);

	List<TransformerException> exceptions = new ArrayList<TransformerException>();

	@Override
	public void warning(TransformerException te) throws TransformerException {
		logger.warn("Fatal error during transformation: ", te);
		exceptions.add(te);
	}

	@Override
	public void fatalError(TransformerException te) throws TransformerException {
		logger.error("Fatal error during transformation: ", te);
		exceptions.add(te);
	}

	@Override
	public void error(TransformerException te) throws TransformerException {
		logger.error("Error during transformation: ", te);
		exceptions.add(te);
	}

	public void flushTo(StringBuffer message) {
		String nl = System.getProperty("line.separator");
		if (exceptions.size() > 0) {
			message.append(nl);
			message.append("Total number of errors/warnings: " + exceptions.size());
			message.append(nl);
		}
		for (TransformerException e : exceptions) {
			message.append(e.getLocalizedMessage());
			message.append(nl);
		}
	}
}
