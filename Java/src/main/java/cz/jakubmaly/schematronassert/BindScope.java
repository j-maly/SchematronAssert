package cz.jakubmaly.schematronassert;

public enum BindScope {
	/**
	 * Will bind a variable valid in the whole schema. Only schema variables
	 * defined earlier can be used in the expression. This translates as a 'let'
	 * element added as a child of 'schema' element.
	 */
	SCHEMA,
	/**
	 * Will bind a variable for the last pattern. The binding expression can use
	 * schema variables and pattern variables defined for earlier for the same
	 * pattern. This translates as a 'let' element added as a child of the last
	 * 'pattern' element
	 */
	PATTERN,
	/**
	 * Will bind a variable for the last rule in the last pattern. The binding
	 * expression can use schema variables and pattern variables defined for
	 * earlier for the same pattern. This translates as a 'let' element added as
	 * a child of the last 'rule' element
	 */
	RULE
}
