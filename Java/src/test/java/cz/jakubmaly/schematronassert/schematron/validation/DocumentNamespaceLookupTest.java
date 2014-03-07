package cz.jakubmaly.schematronassert.schematron.validation;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.*;
import org.powermock.modules.junit4.*;
import org.powermock.reflect.*;
import org.slf4j.*;

import cz.jakubmaly.schematronassert.schematron.model.*;
import cz.jakubmaly.schematronassert.schematron.serialization.*;
import cz.jakubmaly.schematronassert.test.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ LoggerFactory.class })
@SuppressStaticInitializationFor("cz.jakubmaly.schematronassert.schematron.validation.DocumentNamespaceLookup")
public class DocumentNamespaceLookupTest {

	DocumentNamespaceLookup documentNamespaceLookup;

	@Mock
	Logger logger;

	private Comparator<NamespacePrefixDeclaration> nsComparator;

	@Test
	public void should_detect_default_namespace_and_2_others() throws Exception {
		// ARRANGE
		StreamSource xmlSource = Converter.createStreamSource(TestUtils.getResourceText("/xml/books_with_default_namespace.xml"));
		Schema schema = SchemaBuilder.schema();
		// ACT
		documentNamespaceLookup.addNamespacesFromDocumentToSchema(schema, xmlSource);
		// ASSERT
		assertThat(schema.namespacePrefixDeclarations).usingElementComparator(nsComparator).containsOnly(
				new NamespacePrefixDeclaration("xml", "http://www.w3.org/XML/1998/namespace"),
				new NamespacePrefixDeclaration("h", "http://html")
			);
		assertThat(schema.xpathDefaultNamespace).isEqualTo("http://www.example.com/books");
		verify(logger).debug("Running namespace scan in document...");
		verify(logger).debug("Adding namespace with prefix %s:%s", "xml", "http://www.w3.org/XML/1998/namespace");
		verify(logger).debug("Adding namespace with prefix %s:%s", "h", "http://html");
		verify(logger).debug("Setting xpath default namespace: %s", "http://www.example.com/books");
	}

	@Test
	public void should_detect_3_namespaces() throws Exception {
		// ARRANGE
		StreamSource xmlSource = Converter.createStreamSource(TestUtils.getResourceText("/xml/books_with_namespaces.xml"));
		Schema schema = SchemaBuilder.schema();
		// ACT
		documentNamespaceLookup.addNamespacesFromDocumentToSchema(schema, xmlSource);
		// ASSERT
		assertThat(schema.namespacePrefixDeclarations).usingElementComparator(nsComparator).containsOnly(
				new NamespacePrefixDeclaration("bk", "http://www.example.com/books"),
				new NamespacePrefixDeclaration("xml", "http://www.w3.org/XML/1998/namespace"),
				new NamespacePrefixDeclaration("h", "http://html")
			);

		assertThat(schema.xpathDefaultNamespace).isNull();
	}

	@Test
	public void should_detect_3_namespaces_in_a_document() throws Exception {
		// ARRANGE
		StreamSource xmlSource = Converter.createStreamSource(TestUtils.getResourceText("/xml/books_with_namespaces.xml"));
		// ACT
		List<NamespacePrefixDeclaration> result = documentNamespaceLookup.detectNamespaces(xmlSource);
		// ASSERT
		assertThat(result).usingElementComparator(nsComparator).containsOnly(
				new NamespacePrefixDeclaration("bk", "http://www.example.com/books"),
				new NamespacePrefixDeclaration("xml", "http://www.w3.org/XML/1998/namespace"),
				new NamespacePrefixDeclaration("h", "http://html")
			);
	}

	@Test
	public void resetable_reader_should_pass() throws Exception {
		// ARRANGE
		StreamSource xmlSource = mock(StreamSource.class);
		Reader reader = mock(Reader.class);
		when(xmlSource.getReader()).thenReturn(reader);
		when(reader.markSupported()).thenReturn(true);
		// ACT
		documentNamespaceLookup.verifyUnderlyingStreamOrReaderIsResettable(xmlSource);
	}

	@Test
	public void resetable_stream_should_pass() throws Exception {
		// ARRANGE
		StreamSource xmlSource = mock(StreamSource.class);
		InputStream inputStream = mock(InputStream.class);
		when(xmlSource.getInputStream()).thenReturn(inputStream);
		when(inputStream.markSupported()).thenReturn(true);
		// ACT
		documentNamespaceLookup.verifyUnderlyingStreamOrReaderIsResettable(xmlSource);
	}

	@Test(expected = ValidationException.class)
	public void non_resetable_reader_should_fail() throws Exception {
		// ARRANGE
		StreamSource xmlSource = mock(StreamSource.class);
		Reader reader = mock(Reader.class);
		when(xmlSource.getReader()).thenReturn(reader);
		when(reader.markSupported()).thenReturn(false);
		// ACT
		documentNamespaceLookup.verifyUnderlyingStreamOrReaderIsResettable(xmlSource);
	}

	@Test(expected = ValidationException.class)
	public void non_resetable_stream_should_fail() throws Exception {
		// ARRANGE
		StreamSource xmlSource = mock(StreamSource.class);
		InputStream inputStream = mock(InputStream.class);
		when(xmlSource.getInputStream()).thenReturn(inputStream);
		when(inputStream.markSupported()).thenReturn(false);
		// ACT
		documentNamespaceLookup.verifyUnderlyingStreamOrReaderIsResettable(xmlSource);
	}

	@Test
	public void should_log_two_namespaces() throws Exception {
		// ARRANGE
		Schema schema = SchemaBuilder.schema();
		schema.withNs("p1", "uri1");
		schema.withNs("p2", "uri2");
		// ACT
		documentNamespaceLookup.logExplicitNamespaces(schema);
		// ASSERT
		verify(logger).debug("Explicitly defined namespaces: ");
		verify(logger).debug("%s: %s", "p1", "uri1");
		verify(logger).debug("%s: %s", "p2", "uri2");
	}

	@Test
	public void should_show_no_namespaces() throws Exception {
		// ARRANGE
		Schema schema = SchemaBuilder.schema();
		// ACT
		documentNamespaceLookup.logExplicitNamespaces(schema);
		// ASSERT
		verify(logger, only()).debug("No explicitly defined namespaces.");
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		documentNamespaceLookup = new DocumentNamespaceLookup();
		Whitebox.setInternalState(DocumentNamespaceLookup.class, logger);
		Transformer detect_namespaces = XsltSchematronValidator.initializeTransformer("/xslt/detect_namespaces.xsl");
		Whitebox.setInternalState(DocumentNamespaceLookup.class, detect_namespaces);

		nsComparator = new Comparator<NamespacePrefixDeclaration>() {
			public int compare(NamespacePrefixDeclaration o1, NamespacePrefixDeclaration o2) {
				if (o1 != null && o2 != null) {
					return o1.uri.compareTo(o2.uri) + o1.prefix.compareTo(o2.prefix);
				}
				return 1;
			}
		};
	}
}
