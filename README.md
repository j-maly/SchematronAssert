SchematronAssert
================

*SchematronAssert* provides [Schematron](http://schematron.com) capabilities in the form of a Java library. 
If you need to make more complex assertions about your XML data, you will soon find out the capabilities of the existing tools limiting. You are often stuck with crude tools working with XML as with string (regexes, looking for substrings etc.). In the better case, you will have XPath based checks, but it will most likely be XPath 1.0, which lacks expressive power. With *SchematronAssert*, you can make complex assertions, as you would do when validating your documents with *Schematron* schemas. 

Quick start
-------------

For start, you will need three static imports, one for buidling your queries and two for making AssertJ style assertions on the results. 

  import static cz.jakubmaly.schematronassert.TestBuilder.*;
  import static cz.jakubmaly.schematronassert.assertions.Assertions.*;
  import static org.assertj.core.api.Assertions.*;
  

Suppose you have an XML document like this one:

  <books>
      <book>
          <title>About the world</title>
          <author>Jim Smith</author>
          <publisher>
              <name>Albatros</name>
              <address>Czech Reublic</address>
          </publisher>
      </book>    
      <book>
          <title>Reason</title>
          <publisher>
              <name>Springer</name>
              <address>Germany</address>
          </publisher>
      </book>
  </books>

And you want to check that every `book` element has a child element `author`: 
  
  ValidationOutput result = in(booksDocument)
			.forEvery("book")
			.check("author")
			.validate();
	assertThat(result).hasNoErrors();

The parameter of `forEvery` - in this case `"book"` will be used as a *match pattern* (or *context*) (and you can use more complex XPath expression such as `//book[not(tag/text eq 'anonymous')])`). The validator will examine the input document and look for all nodes that match the pattern. In this case, all elements named `book` 

The parameter of `check` is an expression, that is evaluated for every node matching the pattern. If the expression evaluates to true, the check passes. In this case, expression `"author"` evaluates to true if at least one child element named "author" is found in the current context. 

The previous code will either pass or throw an AssertionError, similar to the following: 
  
    java.lang.AssertionError: 
  Expected :
    <[ValidationOutput{failures:1, reports:0}]>
  not to have failures but had :
    <[FailedAssert{, test=author, location=/books[1]/book[2]}]>
    
From the error, you immediately see, what went wrong: test "author" failed for the second book in the document. 

If you ara familiar with Schematron, the code above will in fact create this schema: 

  <schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
		<pattern abstract="false">
			<rule context="book" abstract="false">
				<assert test="author" />
			</rule>
		</pattern>
	</schema

### Better error messages

Schematron is really powerful when providing diagnostics, you can specify your error message like this: 

  ValidationOutput result = in(booksDocument)
			.forEvery("book")
			.check("author", "A book must have at least one author")
			.validate();
	assertThat(result).hasNoErrors();
	
There, you will obtain a more detailed message 

  <[FailedAssert{text=A book must have at least one author, test=author, location=/books[1]/book[2]}]>
  
You can also use { } syntax to add expressions that will be evaluated when error occurs. Suppose you modify the `check` line from the previous example to say: 

  .check("author", Book '{./title}' has no author")
  
Then the error message will say 

  <[FailedAssert{text=Book 'Reason' has no author, test=author, location=/books[1]/book[2]}]>
  
The check above will be translated into this Schematron code: 

  <assert test="author">
    Book <value-of select="./title"/> has no author
  </assert>
	
### Variables 

Sometimes, it might be handy to bind an expression to a variable, so that you can use it in several tests/contexts. This can be done using `bind` method

  .bind("titles", "//title")
  
The above will create a new variable `$titles`, which you can use in all following expressions. 


### Namespaces

Namespaces are a fundamental part of XML. However, they are also a pain to work with. But an XML library without support for namespaces would be limited at best. You have two options, either declare your namespaces explicitly: 

  .withNamespace("bk", "http://example.com/books)
  
And then, qualify all elements from the namespace with "bk" prefix in all your expressions, such as `bk:book/bk:author`. You have to use prefixes even when your input document utilizes default namespaces (without prefixes), i.e. the following form `<books xmlns="http://example.com/books"><author ... `. 

But if you use your namespaces in a 'sane' way (you only use one 'default' namespace, you do not undeclare namespaces etc.), you can get away with 
  
  .autoDetectNamespaces() 
  
In this case, you don't have to declare the namespace prefixes using `withNamespace` (and in your expressions, you will use the same prefixes as in your document). Moreover, because *SchematronAssert* is implemented using XSLT and this language allows you to define default namespace for XPath expressions, you don't have to qualify the elements from your default namespace - so you can write `book/author`. 

Working with the output
-----------------------

Method `validate` of TestBuilder (which is a convenience method for one of the `XsltValidator.validate` overloads) return ValidationOutput object. You can use this object as a parameter of `cz.jakubmaly.schematronAssert.assertions.Assertions.assertThat`. From that on, you can query the specific properties of the result. 

You have already seen the most basic: 

  assertThat(output).hasNoErrors()

Which is simple true/false check. But you can be more specific. If you know *AssertJ* (or *FEST*), you know what you can expect. 

You can e.g. assert that there is a failure in the output and the text of the failure says "Book 'Reason' has no author": 

assertThat(validationResult.getFailures()).haveAtLeast(1, withText("Book 'Reason' has no author"));

Detailed schema building
------------------------
If you know Schematron already and want to use its features without limitation, you can work directly with Schema object (instead of the convenience methods `forEvery`, `check` and `bind`). It allows you to build your schemas in a fluent manner: 

This piece: 

	Schema schema = schema()
		.title("A Schema for Books")
		.withNamespace("bk", "http://www.example.com/books")
		.withPattern(pattern("authorTests")
			.withRule(rule()
				.context("bk:book")
				.let("authors", "bk:author")
				.withAssert("count($authors) != 0", "A book must have at least one author")))
		.withPattern(pattern("onLoanTests")
			.withRule(rule()
				.context("bk:book")
				.withReport("@on-loan and not(@return-date)", "Every book that is on loan must have a return date")));

will create the following schema: 

	<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
		<title>A Schema for Books</title>
		<ns prefix="bk" uri="http://www.example.com/books" />
		<pattern abstract="false" id="authorTests">
			<rule context="bk:book" abstract="false">
				<let name="authors" value="bk:author" />
				<assert test="count($authors) != 0">
					A book must have at least one author
				</assert>
			</rule>
		</pattern>
		<pattern abstract="false" id="onLoanTests">
			<rule context="bk:book" abstract="false">
				<report test="@on-loan and not(@return-date)">
					Every book that is on loan must have a return date
				</report>
			</rule>
		</pattern>
	</schema>

Notes on Schematron implementation
----------------------------------

The library uses Schematron internally. Class XsltSchematronValidator uses the so called "skeleton" implementation, which is a pipeline of XSLT stylesheet applied to the source document and schema (see [Schematron](http://schematron.com) for more information about the skeleton pipeline. 

It uses the XPath 2.0 version, so you can use XPath 2.0 expressions in your implementation. Saxon HE is used for XSLT transformations. 

The output of the pipeline is an XML report using language called SVRL (Schematron Validation Report Language). You can use `XsltSchematronValidator.validate(StreamSource xmlSource, StreamSource schemaSource, Result outputTarget)` if you wish to work directly with the resulting report. An example of such a report might be: 

	<svrl:schematron-output xmlns:svrl="http://purl.oclc.org/dsdl/svrl"
							xmlns:xs="http://www.w3.org/2001/XMLSchema"
							xmlns:xsd="http://www.w3.org/2001/XMLSchema"
							xmlns:saxon="http://saxon.sf.net/"
							xmlns:schold="http://www.ascc.net/xml/schematron"
							xmlns:iso="http://purl.oclc.org/dsdl/schematron"
							xmlns:xhtml="http://www.w3.org/1999/xhtml"
							xmlns:bk="http://www.example.com/books"
							title="A Schema for Books"
							schemaVersion="">
			<svrl:ns-prefix-in-attribute-values uri="http://www.example.com/books" prefix="bk"/>
		<svrl:active-pattern document="file:/D:/GitHub/SchematronAssert/Java/src/test/resources/xml/books_with_namespaces.xml"
							id="authorTests"
							name="authorTests"/>
		<svrl:fired-rule context="bk:book"/>
		<svrl:fired-rule context="bk:book"/>
		<svrl:failed-assert test="count($authors) != 0"
						   location="/*:books[namespace-uri()='http://www.example.com/books'][1]/*:book[namespace-uri()='http://www.example.com/books'][2]">
			<svrl:text> A book must have at least one author </svrl:text>
		</svrl:failed-assert>
		<svrl:active-pattern document="file:/D:/GitHub/SchematronAssert/Java/src/test/resources/xml/books_with_namespaces.xml"
							id="onLoanTests"
							name="onLoanTests"/>
		<svrl:fired-rule context="bk:book"/>
		<svrl:fired-rule context="bk:book"/>
	</svrl:schematron-output>


Other overloads of `validate` will return `ValidationOutput`, which contains the contents of the report loaded into an object structure and provides convenience methods. 


Thanks
------
The library is using [AssertJ](https://github.com/joel-costigliola/assertj-core/) for its core functionality
