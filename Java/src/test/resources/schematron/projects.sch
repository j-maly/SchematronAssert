<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2"
    icon="http://www.ascc.net/xml/resource/schematron/bilby.jpg"
    defaultPhase="built">
    
    <!--<sch:p>This is an example schema for the <emph>Building Projects XML</emph> language.</sch:p>-->
    
    <!--
    <sch:phase id="underConstruction">
        <sch:active pattern="construction"></sch:active>
        <sch:active pattern="admin"></sch:active>
    </sch:phase>
    --> 
    
    <sch:phase id="built">        
        <sch:active pattern="completed">completed</sch:active>
        <sch:active pattern="admin">admin</sch:active>
    </sch:phase>
    
    <!--
    <sch:pattern id="construction">
        
        <sch:p>Constraints which are applied during construction</sch:p>
        
        <sch:rule context="house">
            <sch:assert test="count(wall) = 4">A house should have 4 walls</sch:assert>
            <sch:report test="not(roof)">The house is incomplete, it still needs a roof</sch:report>
            <sch:assert test="builder">An incomplete house must have
                a builder assigned to it</sch:assert>
            <sch:assert test="not(owner)">An incomplete house cannot have an owner</sch:assert>
        </sch:rule>
        
    </sch:pattern>
    -->
     
    <sch:pattern id="completed" abstract="false">
        <!--<sch:p>Constraints which are applied after construction</sch:p>-->
        
        <sch:rule context="house" abstract="false">
            <sch:assert test="count(wall) = 4">A house should have 4 walls</sch:assert>
            <sch:report test="roof">The house is incomplete, it still needs a roof</sch:report>
    <!--        <sch:assert test="owner">An incomplete house must have
                an owner</sch:assert>
            <sch:assert test="not(builder)">An incomplete house doesn't need a builder</sch:assert>-->
        </sch:rule>
        
    </sch:pattern>
    
    <sch:pattern id="admin" abstract="false">
        <!--
        <sch:p>Adminstrative constraints which are <sch:emph>always</sch:emph> applied</sch:p>
        
        <sch:rule context="house">
            <sch:assert test="address">A house must have an address</sch:assert>
        </sch:rule>
        
        <sch:rule context="address">
            <sch:assert test="count(*) = count(street) + count(town) + count(postcode)">
                An address may only include street, town and postcode elements.
            </sch:assert>
            <sch:assert test="street">An address must include the street details</sch:assert>
            <sch:assert test="town">An address must identify the town</sch:assert>
            <sch:assert test="postcode">An address must have a postcode</sch:assert>
        </sch:rule>
        -->
        
        <sch:rule abstract="true" id="nameChecks">
            <sch:assert test="firstname">A name element must have a first name</sch:assert>
            <!--<sch:assert test="lastname">A <name/> element must have a last name</sch:assert>-->
        </sch:rule>
        
        <sch:rule context="builder" abstract="false">
            <sch:extends rule="nameChecks"></sch:extends>
            <sch:assert test="certification">A name must be certified</sch:assert>
        </sch:rule>
        <!--
        <sch:rule context="owner">
            <sch:extends rule="nameChecks"></sch:extends>
            <sch:assert test="telephone">An <name/> must have a telephone</sch:assert>
        </sch:rule>
        
        <sch:rule context="certification">
            <sch:assert test="@number">Certification numbers must be recorded
                in the number attribute</sch:assert>
        </sch:rule>
        -->
    </sch:pattern>
</sch:schema>