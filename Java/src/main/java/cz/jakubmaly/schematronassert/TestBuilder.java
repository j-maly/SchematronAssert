package cz.jakubmaly.schematronassert;

import java.io.*;

import javax.xml.transform.stream.*;

import org.jdom.*;

import cz.jakubmaly.schematronassert.assertions.*;
import cz.jakubmaly.schematronassert.io.*;
import cz.jakubmaly.schematronassert.schematron.model.*;
import cz.jakubmaly.schematronassert.schematron.validation.*;
import cz.jakubmaly.schematronassert.svrl.model.*;

/**
 * Use test builder for quick creation of XML assertions. TestBuilder uses
 * {@link Schema} in the background. Use {@link #in} to get a new instance of
 * TestBuilder initialized for an input XML document. Then, you can use
 * {@link #forEvery}, {@link #check} and other convenience method to build a
 * schema. Calling {@link #validate()} runs validation against the input XML
 * document. The result of {@link #validate()} is {@link ValidationOutput}
 * object which you can use in you assertions.
 * <p>
 * Example:
 * 
 * <pre>
 * {
 * 	&#064;code
 * 	// validate that every &lt;a&gt; element has &lt;b&gt; child  
 * 	ValidationOutput r = in(&quot;&lt;root&gt;&lt;a&gt;&lt;b /&gt;&lt;/a&gt;&lt;/root&gt;&quot;)
 * 		.forEvery(&quot;a&quot;)
 * 		.check(&quot;b&quot;)
 * 		.validate();
 * 	assertThat(r).hasNoErrors();
 * }
 * </pre>
 */
public class TestBuilder {

	private Schema schema;
	private DocumentWrapper document;
	private boolean autoRecognizeNamespaces;

	protected TestBuilder() {
		schema = new Schema();
	}

	/**
	 * Creates a {@link TestBuilder} for an XML document (XML is passed as a raw
	 * string).
	 * 
	 * @param document
	 *            XML document, passed as <b>plain text</b>
	 * @return instance of {@link TestBuilder}
	 */
	public static TestBuilder in(String document) {
		TestBuilder testBuilder = new TestBuilder();
		testBuilder.setInput(document);
		return testBuilder;
	}

	/**
	 * Creates a {@link TestBuilder} for an XML document (XML is passed as a
	 * JDOM document).
	 * 
	 * @param document
	 *            XML document, passed as a <b>JDOM document</b>
	 * @return instance of {@link TestBuilder}
	 */
	public static TestBuilder in(org.jdom.Document document) {
		TestBuilder testBuilder = new TestBuilder();
		testBuilder.setInput(document);
		return testBuilder;
	}

	/**
	 * Creates a {@link TestBuilder} for an XML document (XML is passed as a
	 * JDOM element).
	 * 
	 * @param element
	 *            XML document, passed as a <b>JDOM element</b>
	 * @return instance of {@link TestBuilder}
	 */
	public static TestBuilder in(org.jdom.Element element) {
		TestBuilder testBuilder = new TestBuilder();
		testBuilder.setInput(element);
		return testBuilder;
	}

	/**
	 * Creates a {@link TestBuilder} for an XML document (XML is passed as a
	 * JDOM document).
	 * 
	 * @param document
	 *            XML document, passed as a <b>JDOM2 document</b>
	 * @return instance of {@link TestBuilder}
	 */
	public static TestBuilder in(org.jdom2.Document document) {
		TestBuilder testBuilder = new TestBuilder();
		testBuilder.setInput(document);
		return testBuilder;
	}

	/**
	 * Creates a {@link TestBuilder} for an XML document (XML is passed as a
	 * JDOM element).
	 * 
	 * @param element
	 *            XML document, passed as a <b>JDOM2 element</b>
	 * @return instance of {@link TestBuilder}
	 */
	public static TestBuilder in(org.jdom2.Element element) {
		TestBuilder testBuilder = new TestBuilder();
		testBuilder.setInput(element);
		return testBuilder;
	}

	/**
	 * Creates a {@link TestBuilder} for an XML document (XML is passed as a
	 * StreamSource).
	 * 
	 * @param streamSource
	 *            XML document, passed as a <b>StreamSource object</b>
	 * @return instance of {@link TestBuilder}
	 */
	public static TestBuilder in(StreamSource streamSource) {
		TestBuilder testBuilder = new TestBuilder();
		testBuilder.setInput(streamSource);
		return testBuilder;
	}

	/**
	 * Creates a {@link TestBuilder} for an XML document (XML is passed as a
	 * Reader).
	 * 
	 * @param reader
	 *            XML document, passed as a <b>Reader object</b>
	 * @return instance of {@link TestBuilder}
	 */
	public static TestBuilder in(Reader reader) {
		TestBuilder testBuilder = new TestBuilder();
		testBuilder.setInput(reader);
		return testBuilder;
	}

	/**
	 * Creates a {@link TestBuilder} for an XML document (XML is passed as a
	 * File).
	 * 
	 * @param file
	 *            XML document, passed as a <b>file reference</b>
	 * @return instance of {@link TestBuilder}
	 */
	public static TestBuilder in(File file) {
		TestBuilder testBuilder = new TestBuilder();
		testBuilder.setInput(file);
		return testBuilder;
	}

	/**
	 * Creates a {@link TestBuilder} for an XML document (XML is passed as an
	 * InputStream).
	 * 
	 * @param inputStream
	 *            XML document, passed as an <b>input stream</b>
	 * @return instance of {@link TestBuilder}
	 */
	public static TestBuilder in(InputStream inputStream) {
		TestBuilder testBuilder = new TestBuilder();
		StreamSource streamSource = new StreamSource(inputStream);
		testBuilder.setInput(streamSource);
		return testBuilder;
	}

	/**
	 * Creates a {@link TestBuilder} for an XML document (XML is passed as an
	 * InputStream, systemId is specified for URI recognition).
	 * 
	 * @param inputStream
	 *            XML document, passed as an <b>input stream</b>
	 * @param systemId
	 *            URL used for resolving linked URLs
	 * @return instance of {@link TestBuilder}
	 */
	public static TestBuilder in(InputStream inputStream, String systemId) {
		TestBuilder testBuilder = new TestBuilder();
		testBuilder.setInput(inputStream, systemId);
		return testBuilder;
	}

	/**
	 * Creates a {@link TestBuilder} for an XML document (XML is passed as a
	 * Reader, systemId is specified for URI recognition).
	 * 
	 * @param reader
	 *            XML document, passed as a <b>reader object</b>
	 * @param systemId
	 *            URL used for resolving linked URLs
	 * @return
	 */
	public static TestBuilder in(Reader reader, String systemId) {
		TestBuilder testBuilder = new TestBuilder();
		testBuilder.setInput(reader, systemId);
		return testBuilder;
	}

	/**
	 * Sets the input XML to be validated.
	 * 
	 * @param document
	 *            input XML - raw string containing the XML fragment
	 */
	public void setInput(String document) {
		setDocument(new StringDocumentWrapper(document));
	}

	/**
	 * Sets the input XML to be validated.
	 * 
	 * @param document
	 *            input XML document
	 */
	public void setInput(org.jdom.Document document) {
		setDocument(new Jdom1DocumentWrapper(document));
	}

	/**
	 * Sets the input XML to be validated.
	 * 
	 * @param document
	 *            input XML document
	 */
	public void setInput(org.jdom2.Document document) {
		setDocument(new Jdom2DocumentWrapper(document));
	}

	/**
	 * Sets the input XML to be validated.
	 * 
	 * @param streamSource
	 *            input XML as a StreamSource
	 */
	public void setInput(StreamSource streamSource) {
		setDocument(new StreamSourceDocumentWrapper(streamSource));
	}

	/**
	 * Sets the input XML to be validated.
	 * 
	 * @param element
	 *            input XML fragment
	 */
	private void setInput(Element element) {
		setDocument(new Jdom1DocumentWrapper(element));
	}

	/**
	 * Sets the input XML to be validated.
	 * 
	 * @param element
	 *            input XML fragment
	 */
	private void setInput(org.jdom2.Element element) {
		setDocument(new Jdom2DocumentWrapper(element));
	}

	/**
	 * Sets the input XML to be validated.
	 * 
	 * @param streamSource
	 *            input XML as a Reader
	 */
	public void setInput(Reader reader) {
		StreamSource streamSource = new StreamSource(reader);
		setInput(streamSource);
	}

	/**
	 * Sets the input XML to be validated.
	 * 
	 * @param streamSource
	 *            input XML as a File
	 */
	public void setInput(File file) {
		StreamSource streamSource = new StreamSource(file);
		setInput(streamSource);
	}

	/**
	 * Sets the input XML to be validated.
	 * 
	 * @param inputStream
	 *            XML document, passed as an <b>input stream</b>
	 * @param systemId
	 *            URL used for resolving linked URLs
	 */
	public void setInput(InputStream inputStream, String systemId) {
		StreamSource streamSource = new StreamSource(inputStream, systemId);
		setInput(streamSource);
	}

	/**
	 * Sets the input XML to be validated.
	 * 
	 * @param inputStream
	 *            XML document, passed as a <b>reader</b>
	 * @param systemId
	 *            URL used for resolving linked URLs
	 */
	public void setInput(Reader reader, String systemId) {
		StreamSource streamSource = new StreamSource(reader, systemId);
		setInput(streamSource);
	}

	public TestBuilder forEvery(String context) {
		this.getSchema()
			.withPattern(new Pattern()
				.withRule(new Rule()
					.context(context)));
		return this;
	}

	/**
	 * Adds a variable binding. A scope is selected as Schema when no patterns
	 * are yet present, Pattern when no rules are yet present and Rule
	 * otherwise.
	 * <p>
	 * The scope selection works like this:
	 * <p>
	 * the scope will be {@link BindScope#SCHEMA} until
	 * {@link TestBuilder#forEvery} is called (which adds pattern with a rule),
	 * from that point on, {@link BindScope#RULE} context will be used as
	 * default.
	 * <p>
	 * This method corresponds to
	 * {@code <let name="variableName" value="expression">} in Schematron.
	 * 
	 * @param variableName
	 *            name of the variable
	 * @param expression
	 *            expression computing the value of the variable
	 * @return calling {@link TestBuilder} (fluent style)
	 */
	public TestBuilder bind(String variableName, String expression) {
		if (getSchema().getLastRule() != null) {
			return bind(variableName, expression, BindScope.RULE);
		} else if (getSchema().getLastPattern() != null) {
			return bind(variableName, expression, BindScope.PATTERN);
		} else {
			return bind(variableName, expression, BindScope.SCHEMA);
		}
	}

	/**
	 * Adds a variable binding with {@code scope} specified explicitly.
	 * Depending on {@code scope}, the variable is added either for the whole
	 * schema, in the last pattern or in the last rule of the last pattern.
	 * <p>
	 * This method corresponds to
	 * {@code <let name="variableName" value="expression">} in Schematron.
	 * 
	 * @param variableName
	 *            name of the variable
	 * @param expression
	 *            expression computing the value of the variable
	 * @param scope
	 *            scope of the variable
	 * @return calling {@link TestBuilder} (fluent style)
	 */
	public TestBuilder bind(String variableName, String expression, BindScope scope) {
		testSchemaDefined();
		if (scope == BindScope.RULE) {
			getSchema().getLastRule().let(variableName, expression);
		} else if (scope == BindScope.PATTERN) {
			getSchema().getLastPattern().let(variableName, expression);
		} else if (scope == BindScope.SCHEMA) {
			getSchema().let(variableName, expression);
		} else {
			throw new RuntimeException("Unknown scope: " + scope);
		}
		return this;
	}

	/**
	 * Adds a check for a condition. If the condition is not satisfied in the
	 * validated document, this will be reported as a <code>failure</code> in
	 * the validation output when {@link #validate()} is called.
	 * <p>
	 * This method corresponds to {@code <assert test="condition" />} in
	 * Schematron
	 * 
	 * @param condition
	 * @return calling {@link TestBuilder} (fluent style)
	 */
	public TestBuilder check(String condition) {
		testPatternAndRuleDefined();
		getSchema().getLastRule().withAssert(condition);
		return this;
	}

	/**
	 * Adds a check for a condition with customized error message. If the
	 * condition is not satisfied in the validated document, this will be
	 * reported as a <code>failure</code> in the validation output when
	 * {@link #validate()} is called.
	 * <p>
	 * The message can use &#123; and &#125; for putting expressions in the
	 * message. E.g.: {@code check("author", "author is not defined for book
	 * }&#123;{@code ./title}&#125;{@code )}
	 * <p>
	 * This method corresponds to
	 * {@code <assert test="condition">message</assert>} in Schematron
	 * 
	 * @param condition
	 * @return
	 */
	public TestBuilder check(String condition, String message) {
		check(condition);
		getSchema().getLastRule().assertionElements.get(0).addMessage(message);
		return this;
	}

	private void testSchemaDefined() {
		if (getSchema() == null) {
			throw new SchemaBuilderException("Schema must be built first, use TestBuilder.in(...) or TestBuilder.setSchema(...).");
		}
	}

	private void testPatternAndRuleDefined() {
		if (getSchema().getLastRule() == null) {
			throw new SchemaBuilderException("Pattern/Rule must be built first, use TestBuilder.forEvery(...)");
		}
	}

	/**
	 * When this method is called, the underlying validator will try to
	 * automatically recognize the default namespace and namespace prefixes in
	 * the input XML document.
	 * <p>
	 * Note: for this, the input document must be read twice.
	 * 
	 * @return calling {@link TestBuilder} (fluent style)
	 * @see TestBuilder#withNamespace(String, String)
	 */
	public TestBuilder autoDetectNamespaces() {
		autoRecognizeNamespaces = true;
		return this;
	}

	public DocumentWrapper getDocument() {
		return document;
	}

	public void setDocument(DocumentWrapper document) {
		this.document = document;
	}

	public Schema getSchema() {
		return schema;
	}

	/**
	 * Set the Schematron schema explicitly.
	 * <p>
	 * Note: instead of passing the Schema object, the schema can be built using
	 * fluent interface methods {@link #in}, {@link #forEvery}, {@link #check},
	 * {@link #bind} and {@link #withNamespace}
	 * 
	 * @param schema
	 *            Schematron schema object
	 */
	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	/**
	 * Use the built schema to validate input document. The input document is
	 * specified in {@link TestBuilder#in} call or via
	 * {@link TestBuilder#setInput}.
	 * <p>
	 * The resulting ValidationOutput object contains deserialized SVRL
	 * document.
	 * 
	 * @return ValidationOutput object, which can be used in assertions
	 * 
	 * @see Assertions#assertThat(ValidationOutput)
	 */
	public ValidationOutput validate() {
		StreamSource streamSource = null;
		try {
			testDocumentPresent();
			streamSource = document.getStreamSource();
		} catch (IOException e) {
			throw new ValidationException("IO error during validation", e);
		}
		XsltSchematronValidator validator = new XsltSchematronValidator();
		if (autoRecognizeNamespaces)
			validator.setAutoRecognizeNamespaces(true);
		ValidationOutput validationOutput;
		validationOutput = validator.validate(streamSource, schema);
		return validationOutput;
	}

	private void testDocumentPresent() {
		if (document == null) {
			throw new ValidationException(
					"Test builder is not fully initialized, missing input. Use TestBuilder.in(...) or TestBuiler.setInput(...) to define input");
		}
	}

	/**
	 * Defines an XML namespace prefix binding. The prefix can then be used in
	 * all XPath expressions in conditions.
	 * 
	 * This method corresponds to {@code <ns prefix="prefix" uri="uri">} in
	 * Schematron.
	 * 
	 * @param prefix
	 *            namespace prefix
	 * @param uri
	 *            namespace URI
	 * @return calling {@link TestBuilder} (fluent style)
	 */
	public TestBuilder withNamespace(String prefix, String uri) {
		testSchemaDefined();
		getSchema().withNamespace(prefix, uri);
		return this;
	}
}
