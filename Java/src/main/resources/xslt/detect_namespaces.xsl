<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
    <xsl:output method="text"/>

    <xsl:template match="/">
        <xsl:variable name="allNs" as="element()">
            <tmp>
              <xsl:for-each select="//namespace::*">
                  <ns prefix="{local-name()}" uri="{string(.)}" />            
              </xsl:for-each>
            </tmp>
        </xsl:variable>   
        
        <xsl:for-each select="$allNs//ns[not(./@prefix = preceding::*/@prefix)]">            
            <xsl:value-of select="@prefix"/>
            <xsl:text> : </xsl:text>
            <xsl:value-of select="@uri"/>
            <xsl:text> | </xsl:text>
        </xsl:for-each>        
    </xsl:template>
</xsl:stylesheet>