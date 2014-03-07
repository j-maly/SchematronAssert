package cz.jakubmaly.schematronassert.svrl.serialization;

import java.io.*;

import javax.xml.bind.*;
import javax.xml.transform.*;

import cz.jakubmaly.schematronassert.schematron.serialization.*;
import cz.jakubmaly.schematronassert.svrl.model.*;

public class SvrlDeserializer {

	private Unmarshaller unmarshaller;
	private Marshaller marshaller;

	public SvrlDeserializer() {
		try {
			JAXBContext context = JAXBContext.newInstance(ValidationOutput.class);
			unmarshaller = context.createUnmarshaller();
			marshaller = context.createMarshaller();
			unmarshaller.setListener(new MyListener());
		} catch (JAXBException e) {
			throw new SvrlDeserializationException("Failed to create SVRL unmarshaller.", e);
		}
	}

	public ValidationOutput deserializeSvrlOutput(Source output) throws SvrlDeserializationException {
		try {
			ValidationOutput result = (ValidationOutput) unmarshaller.unmarshal(output);
			return result;
		} catch (JAXBException e) {
			throw new SvrlDeserializationException("Failed to deserialize SVRL.", e);
		}
	}

	public void serializeSvrlOutput(ValidationOutput svrl, Writer outputWriter) throws SvrlDeserializationException,
			SchemaSerializationException {
		try {
			marshaller.marshal(svrl, outputWriter);
		} catch (JAXBException e) {
			throw new SchemaSerializationException("Error during schema serialization. ", e);
		}
	}

	public class MyListener extends Unmarshaller.Listener {

		private ActivePattern activePattern;
		private FiredRule firedRule;

		@Override
		public void afterUnmarshal(Object target, Object parent) {
			super.afterUnmarshal(target, parent);
			if (target instanceof ActivePattern) {
				activePattern = (ActivePattern) target;
			} else if (target instanceof FiredRule) {
				firedRule = (FiredRule) target;
			} else if (target instanceof FoundTestElement) {
				((FoundTestElement) target).setPattern(activePattern);
				((FoundTestElement) target).setFiredRule(firedRule);
			}
		}
	}
}
