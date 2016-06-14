<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:ns1="http://www.skupstinans.rs/propis"
    xmlns:ns2="http://www.skupstinans.rs/elementi"
    exclude-result-prefixes="xs"
    version="2.0">
    <xsl:output method="html" />
    <xsl:template match="ns1:Propis">
        <html>
            <head>
                <meta charset="utf-8" />
                <title><xsl:value-of select="ns2:Naziv" /></title>
            </head>
            <body>
                <h1>
                    <xsl:value-of select="ns2:Naziv"></xsl:value-of>
                </h1>
                <xsl:apply-templates />
            </body>
        </html>
    </xsl:template>
    <xsl:template match="ns1:Preambula">
        <p>
            <xsl:value-of select="ns1:PravniOsnov"></xsl:value-of>
        </p>
        <p>
            <xsl:value-of select="ns1:DonosilacPropisa/ns2:Naziv"></xsl:value-of>
            donosi
        </p>
    </xsl:template>
    <xsl:template match="ns2:Clan">
        <a name="{@ns2:id}" />
        <h2><xsl:value-of select="ns2:Naziv"></xsl:value-of></h2>
        <h2>Član XX.</h2>
        <xsl:apply-templates select="ns2:Stav" />
    </xsl:template>
    <xsl:template match="ns2:Deo">
        <a name="{@ns2:id}" />
        <h2>Deo xxxx</h2>
        <h2><xsl:value-of select="ns2:Naziv"></xsl:value-of></h2>
        <xsl:apply-templates select="ns2:Glava" />
    </xsl:template>
    <xsl:template match="ns2:Glava">
        <a name="{@ns2:id}" />
        <h2>XX. <xsl:value-of select="ns2:Naziv"></xsl:value-of></h2>
        <xsl:apply-templates select="ns2:Odeljak" />
        <xsl:apply-templates select="ns2:Clan" />
    </xsl:template>
    <xsl:template match="ns2:Odeljak">
        <a name="{@ns2:id}" />
        <h2>XX. <xsl:value-of select="ns2:Naziv"></xsl:value-of></h2>
        <xsl:apply-templates select="ns2:Pododeljak" />
        <xsl:apply-templates select="ns2:Clan" />
    </xsl:template>
    <xsl:template match="ns2:Pododeljak">
        <a name="{@ns2:id}" />
        <h2>x) <xsl:value-of select="ns2:Naziv"></xsl:value-of></h2>
        <xsl:apply-templates select="ns2:Clan" />
    </xsl:template>
    <xsl:template match="ns2:Stav">
        <a name="{@ns2:id}" />
        <p>
            <xsl:apply-templates />
        </p>
    </xsl:template>
    <xsl:template match="ns2:Stav//*">
        <xsl:copy>
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>
    <xsl:template match="ns2:Stav//text()">
        <xsl:copy-of select="." />
    </xsl:template>
    <xsl:template match="ns2:Stav//Tacka">
        <a name="{@ns2:id}" />
        <ul>
            <xsl:apply-templates /> 
        </ul>
    </xsl:template>
    <xsl:template match="ns2:Tacka//*">
        <xsl:copy>
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>
    <xsl:template match="ns2:Tacka//text()">
        <li><xsl:copy-of select="." /></li>
    </xsl:template>
    <xsl:template match="ns2:Tacka//ns2:Podtacka">
        <a name="{@ns2:id}" />
        <ul>
            <xsl:apply-templates /> 
        </ul>
    </xsl:template>
    <xsl:template match="ns2:Podtacka//*">
        <xsl:copy>
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>
    <xsl:template match="ns2:Podtacka//text()">
        <li><xsl:copy-of select="." /></li>
    </xsl:template>
    <xsl:template match="ns2:Podtacka//ns2:Alineja">
        <a name="{@ns2:id}" />
        <ul>
            <xsl:apply-templates /> 
        </ul>
    </xsl:template>
    <xsl:template match="ns2:Alineja">
        <li><xsl:copy-of select="." /></li>
    </xsl:template>
    <!-- default rule: ignore any unspecific text node -->
    <xsl:template match="text()" />
</xsl:stylesheet>