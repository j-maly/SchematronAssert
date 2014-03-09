package cz.jakubmaly.schematronassert.schematron.model;

import java.util.*;

import javax.xml.bind.annotation.*;

import cz.jakubmaly.schematronassert.schematron.validation.*;

@XmlRootElement(name = "schema")
public class Schema {

	public static final String SCHEMATRON_NAMESPACE = "http://purl.oclc.org/dsdl/schematron";

	@XmlAttribute(name = "id")
	public String id;

	@XmlAttribute(name = "schemaVersion")
	public String schemaVersion;

	@XmlAttribute(name = "defaultPhase")
	public String defaultPhase;

	@XmlAttribute(name = "icon")
	public String icon;

	@XmlAttribute(name = "queryBinding")
	public String queryBinding = "xslt2";

	@XmlElement(name = "include")
	public List<Include> includes;

	@XmlElement(name = "title")
	public String title;

	@XmlElement(name = "ns")
	public List<NamespacePrefixDeclaration> namespacePrefixDeclarations;

	@XmlElement(name = "let")
	public List<LetDeclaration> letDeclarations;

	@XmlElement(name = "phase")
	public Collection<Phase> phases;

	@XmlElement(name = "pattern")
	public List<Pattern> patterns;

	@XmlTransient
	/**
	 * URI of the default namespace used in xpath expressions. 
	 * Used to set xpath-default-namespace attribute in the validation stylesheet. 
	 * Example: 
	 *  	when xpathDefaultNamespace = "http://mydomain.org"
	 *  	then expression: "a/b > 1"
	 * 		is equivalent to p:a/p:b with <ns prefix="p" uri="http://mydomain.org" /> in the schema.
	 *  
	 * Note: this is not a standard path of Schematron and may not work for other validators. 
	 * ISO template iso_schematron_skeleton_for_saxon.xsl was modified to support this.  
	 */
	public String xpathDefaultNamespace;

	public Schema title(String title) {
		this.title = title;
		return this;
	}

	public Schema queryBinding(String queryBinding) {
		this.queryBinding = queryBinding;
		return this;
	}

	public Schema icon(String icon) {
		this.icon = icon;
		return this;
	}

	public Schema defaultPhase(String defaultPhase) {
		this.defaultPhase = defaultPhase;
		return this;
	}

	public Schema withPattern(Pattern pattern) {
		if (patterns == null)
			patterns = new ArrayList<Pattern>();
		patterns.add(pattern);
		return this;
	}

	public Schema withNamespace(String prefix, String uri) {
		NamespacePrefixDeclaration ns = new NamespacePrefixDeclaration(prefix, uri);
		return withNamespace(ns);
	}

	public Schema withNamespace(NamespacePrefixDeclaration ns) {
		if (namespacePrefixDeclarations == null)
			namespacePrefixDeclarations = new ArrayList<NamespacePrefixDeclaration>();
		namespacePrefixDeclarations.add(ns);
		return this;
	}

	public Schema withPhase(Phase phase) {
		if (phases == null)
			phases = new ArrayList<Phase>();
		phases.add(phase);
		return this;
	}

	public Schema let(String name, String value) {
		if (letDeclarations == null) {
			this.letDeclarations = new ArrayList<LetDeclaration>();
		}
		LetDeclaration l = new LetDeclaration()
			.name(name)
			.value(value);
		letDeclarations.add(l);
		return this;
	}

	public void validate() throws InvalidSchemaException {
		validatePrefixes();
	}

	private void validatePrefixes() throws InvalidSchemaException {
		if (namespacePrefixDeclarations == null)
			return;
		HashMap<String, NamespacePrefixDeclaration> map = new HashMap<String, NamespacePrefixDeclaration>();
		for (NamespacePrefixDeclaration ns : namespacePrefixDeclarations) {
			if (ns.uri == null || ns.uri.length() == 0) {
				throw new InvalidSchemaException(String.format("Empty namespace uri is not allowed (%s)", ns.prefix));
			}
			if (ns.prefix == null || ns.prefix.length() == 0) {
				throw new InvalidSchemaException(String.format(
						"Empty namespace prefix is not allowed (%s), consider using Schema.xpathDefaultNamespace. ", ns.uri));
			}

			if (map.containsKey(ns.prefix) && !map.get(ns.prefix).uri.equals(ns.uri)) {
				throw new InvalidSchemaException(String.format("Namespace prefix %s is defined multiple times. (%s, %s)",
						ns.prefix, map.get(ns.prefix).uri, ns.uri));
			}
			map.put(ns.prefix, ns);
		}
	}

	public void xpathDefaultNamespace(String uri) {
		xpathDefaultNamespace = uri;
	}

	/**
	 * Convenience method that returns the last pattern in the schema
	 */
	public Pattern getLastPattern() {
		if (patterns != null && patterns.size() > 0) {
			Pattern pattern = patterns.get(patterns.size() - 1);
			return pattern;
		}
		return null;
	}

	/**
	 * Convenience method that returns the last rule in the last pattern in the
	 * schema
	 */
	public Rule getLastRule() {
		if (getLastPattern() != null) {
			Pattern pattern = getLastPattern();
			if (pattern.rules != null && pattern.rules.size() > 0) {
				Rule rule = pattern.rules.get(pattern.rules.size() - 1);
				return rule;
			}
		}
		return null;
	}
}
