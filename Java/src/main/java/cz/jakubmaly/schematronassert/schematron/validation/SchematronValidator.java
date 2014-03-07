package cz.jakubmaly.schematronassert.schematron.validation;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

public interface SchematronValidator {
	void validate(StreamSource xmlSource, StreamSource schemaSource, Result outputTarget)
			throws ValidationException;

	void validate(StreamSource xmlSource, StreamSource schemaSource, Result outputTarget, String xpathDefaultNamespace)
			throws ValidationException;

	public boolean getAutoRecognizeNamespaces();

	public void setAutoRecognizeNamespaces(boolean b);
}
