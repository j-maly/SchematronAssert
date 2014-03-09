package cz.jakubmaly.schematronassert.svrl.serialization;

import java.io.*;

import javax.xml.transform.*;

import cz.jakubmaly.schematronassert.schematron.serialization.*;
import cz.jakubmaly.schematronassert.svrl.model.*;

public interface SvrlDeserializer {

	public abstract ValidationOutput deserializeSvrlOutput(Source output) throws SvrlDeserializationException;

	public abstract void serializeSvrlOutput(ValidationOutput svrl, Writer outputWriter) throws SvrlDeserializationException,
			SchemaSerializationException;

}