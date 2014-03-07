package cz.jakubmaly.schematronassert.schematron.serialization;

import java.io.*;

import javax.xml.bind.*;

import com.sun.xml.bind.marshaller.*;

import cz.jakubmaly.schematronassert.schematron.model.*;
import cz.jakubmaly.schematronassert.schematron.validation.*;

public class SchemaSerializer {

	private Marshaller marshaller;

	public void setPrettyPrint(boolean prettyPrint) {
		try {
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, prettyPrint);
			NamespacePrefixMapper prefixMapper = new NamespacePrefixMapper() {
				@Override
				public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
					if (namespaceUri.equals(Schema.SCHEMATRON_NAMESPACE)) {
						return "sch";
					}
					else {
						return suggestion;
					}
				}
			};
			marshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", prefixMapper);
		} catch (PropertyException e) {
		}
	}

	public SchemaSerializer() {
		try {
			JAXBContext context = JAXBContext.newInstance(Schema.class);
			marshaller = context.createMarshaller();
			setPrettyPrint(true);
		} catch (JAXBException e) {
			throw new SchemaSerializationException("Failed to create Schematron marshaller.", e);
		}
	}

	public void serializeSchema(Schema schema, Writer outputWriter) throws InvalidSchemaException, SchemaSerializationException {
		try {
			schema.validate();
			marshaller.marshal(schema, outputWriter);
		} catch (JAXBException e) {
			throw new SchemaSerializationException("Error during schema serialization. ", e);
		}
	}
}
