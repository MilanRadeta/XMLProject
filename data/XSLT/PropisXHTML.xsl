<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
    <xsl:template match="/Propis">
        <html>
            <head>
                <meta charset="utf-8" />
                <title><xsl:value-of>/Naziv</xsl:value-of></title>
            </head>
            <body>
                <p>
                    <xsl:value-of select="/Preambula/PravniOsnov"></xsl:value-of>
                </p>
                <p>
                    <xsl:value-of select="/Preambula/DonosilacPropisa/Naziv"></xsl:value-of>
                    donosi
                </p>
                <h1>
                    <xsl:value-of select="/Naziv"></xsl:value-of>
                </h1>
                <xsl:for-each select="/Clan">
                    <h2><xsl:value-of select="/Naziv"></xsl:value-of></h2>
                    <h2>Član XX.</h2>
                    <xsl:for-each select="/Stav">
                        <h2><xsl:value-of select="/Naziv"></xsl:value-of></h2>
                        <h2>Član XX.</h2>
                    </xsl:for-each>
                </xsl:for-each>
                <xsl:for-each select="/Deo">
                    <h2>Deo xxxx</h2>
                    <h2><xsl:value-of select="/Naziv"></xsl:value-of></h2>
                    <xsl:for-each select="/Glava">
                        <h2>XX. <xsl:value-of select="/Naziv"></xsl:value-of></h2>
                        <xsl:for-each select="/Odeljak">
                            <h2>XX. <xsl:value-of select="/Naziv"></xsl:value-of></h2>
                            <xsl:for-each select="/Pododeljak">
                                <h2>x) <xsl:value-of select="/Naziv"></xsl:value-of></h2>
                                <xsl:for-each select="/Clan">
                                    <h2><xsl:value-of select="/Naziv"></xsl:value-of></h2>
                                    <h2>Član XX.</h2>
                                </xsl:for-each>
                            </xsl:for-each>
                            <xsl:for-each select="/Clan">
                                <h2><xsl:value-of select="/Naziv"></xsl:value-of></h2>
                                <h2>Član XX.</h2>
                            </xsl:for-each>
                        </xsl:for-each>
                        <xsl:for-each select="/Clan">
                            <h2><xsl:value-of select="/Naziv"></xsl:value-of></h2>
                            <h2>Član XX.</h2>
                        </xsl:for-each>
                    </xsl:for-each>
                </xsl:for-each>
                <xsl:for-each select="/Glava">
                    <h2>XX. <xsl:value-of select="/Naziv"></xsl:value-of></h2>
                    <xsl:for-each select="/Odeljak">
                        <h2>XX. <xsl:value-of select="/Naziv"></xsl:value-of></h2>
                        <xsl:for-each select="/Pododeljak">
                            <h2>x) <xsl:value-of select="/Naziv"></xsl:value-of></h2>
                            <xsl:for-each select="/Clan">
                                <h2><xsl:value-of select="/Naziv"></xsl:value-of></h2>
                                <h2>Član XX.</h2>
                            </xsl:for-each>
                        </xsl:for-each>
                        <xsl:for-each select="/Clan">
                            <h2><xsl:value-of select="/Naziv"></xsl:value-of></h2>
                            <h2>Član XX.</h2>
                        </xsl:for-each>
                    </xsl:for-each>
                    <xsl:for-each select="/Clan">
                        <h2><xsl:value-of select="/Naziv"></xsl:value-of></h2>
                        <h2>Član XX.</h2>
                    </xsl:for-each>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>