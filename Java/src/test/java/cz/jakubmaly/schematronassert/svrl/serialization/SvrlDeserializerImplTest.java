package cz.jakubmaly.schematronassert.svrl.serialization;

import static cz.jakubmaly.schematronassert.assertions.Assertions.*;

import java.io.*;

import org.apache.commons.io.*;
import org.custommonkey.xmlunit.*;
import org.junit.*;

import cz.jakubmaly.schematronassert.svrl.model.*;
import cz.jakubmaly.schematronassert.test.*;

public class SvrlDeserializerImplTest {

	SvrlDeserializer deserializer;

	@Test
	public void test_svrl_serialization_roundtrip() throws Exception {
		ValidationOutput svrlResult = TestUtils.loadSvrlFromResource("/svrl/books.svrl");
		assertThat(svrlResult).isNotNull();

		Writer outputWriter = new StringWriter();
		deserializer.serializeSvrlOutput(svrlResult, outputWriter);
		XMLAssert.assertXMLEqual(outputWriter.toString(), TestUtils.getResourceText("/svrl/books.svrl"));
		IOUtils.closeQuietly(outputWriter);
	}

	@Test
	public void test_pattern_links_deserialized_correctly() {
		ValidationOutput svrlResult = TestUtils.loadSvrlFromResource("/svrl/books.svrl");
		FailedAssert f1 = svrlResult.getFailures().get(0);
		assertThat(f1.getPattern()).hasId("authorTests");
		assertThat(f1.getFiredRule()).hasContext("bk:book");
	}

	@Test
	public void test_rule_links_deserialized_correctly() {

	}

	private boolean wasIgnoreWhitespace;

	private boolean wasIgnoreComments;

	@Before
	public void setUpXmlUnit() {
		wasIgnoreWhitespace = XMLUnit.getIgnoreWhitespace();
		wasIgnoreComments = XMLUnit.getIgnoreComments();
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreComments(true);
	}

	@After
	public void restoreXmlUnit() {
		XMLUnit.setIgnoreWhitespace(wasIgnoreWhitespace);
		XMLUnit.setIgnoreComments(wasIgnoreComments);
	}

	@Before
	public void setUp() {
		deserializer = new SvrlDeserializerImpl();
	}
}
