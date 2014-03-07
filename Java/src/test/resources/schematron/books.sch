<?xml version="1.0" encoding="UTF-8"?>
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