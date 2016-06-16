<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:ns1="http://www.skupstinans.rs/propis"
    xmlns:ns2="http://www.skupstinans.rs/elementi"
    xmlns:ns3="http://www.skupstinans.rs/amandman"
    exclude-result-prefixes="xs"
    version="2.0">
    <xsl:output method="html" />
    <xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyzčšđćžабвгдђежзијклљмнњопрстћуфхцчџш'" />
    <xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZČŠĐĆŽАБВГДЂЕЖЗИЈКЛЉМНЊОПРСТЋУФХЦЧЏЊ'" />
    <xsl:variable name="latin" select="'abcdefghijklmnoprstuvzčšđćžABCDEFGHIJKLMNOPRSTUVZČŠĐĆŽ'" />
    <xsl:variable name="cyrilic" select="'абцдефгхијклмнопрстувзчшђћжАБЦДЕФГХИЈКЛМНОПРСТУВЗЧЂЂЋЖ'" />
    
    <xsl:template match="ns1:Propis">
        <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
                <meta charset="utf-8" />
                <title><xsl:value-of select="translate(translate(ns2:Naziv, $smallcase, $uppercase), $latin, $cyrilic)" /></title>
            </head>
            <body style="margin:50px">
                <xsl:apply-templates select="ns1:Preambula" />
                <xsl:apply-templates select="ns2:Obrazlozenje" />
                <h1 align="center">
                    <xsl:value-of select="translate(translate(ns2:Naziv, $smallcase, $uppercase), $latin, $cyrilic)"></xsl:value-of>
                </h1>
                <xsl:apply-templates select="ns2:Deo" />
                <xsl:apply-templates select="ns2:Glava" />
                <xsl:apply-templates select="ns2:Clan" />
            </body>
        </html>
    </xsl:template>
    <xsl:template match="ns1:Preambula">
        <p align="justify">
            <xsl:value-of select="translate(ns1:PravniOsnov, $latin, $cyrilic)" />,
            <xsl:apply-templates select="ns1:Saglasnost" />
        </p>
        <p align="justify">
            <xsl:value-of select="translate(ns1:DonosilacPropisa/ns2:Naziv, $latin, $cyrilic)" />
            доноси
        </p>
    </xsl:template>
    <xsl:template match="ns2:Obrazlozenje">
        <h4 align="center">Образложење</h4>
        <p align="justify">
            <xsl:value-of select="translate(., $latin, $cyrilic)" />
        </p>
    </xsl:template>
    <xsl:template match="ns1:Saglasnost">
      , уз сагласност коју доноси
      <xsl:value-of select="translate(ns2:Naziv, $latin, $cyrilic)" />
      са следећом назнаком
      <p align="justify">
          <xsl:value-of select="translate(ns1:Naznaka, $latin, $cyrilic)" />
      </p>
        
    </xsl:template>
    <xsl:template match="ns2:Clan">
        <a name="{@ns2:id}" />
        <h2 align="center"><xsl:value-of select="translate(ns2:Naziv, $latin, $cyrilic)" /></h2>
        <h2 align="center">Члан <xsl:value-of select="@ns2:rednaOznaka" />.</h2>
        <xsl:apply-templates select="ns2:Stav" />
    </xsl:template>
    <xsl:template match="ns2:Deo">
        <a name="{@ns2:id}" />
        <h2 align="center">Део <xsl:value-of select="translate(@ns2:rednaOznaka, $latin, $cyrilic)" /></h2>
        <h2 align="center"><xsl:value-of select="translate(translate(ns2:Naziv, $latin, $cyrilic), $smallcase, $uppercase)" /></h2>
        <xsl:apply-templates select="ns2:Glava" />
    </xsl:template>
    <xsl:template match="ns2:Glava">
        <a name="{@ns2:id}" />
        <h2 align="center">
            <xsl:value-of select="@ns2:rednaOznaka" />.
            <xsl:value-of select="translate(translate(ns2:Naziv, $latin, $cyrilic), $smallcase, $uppercase)" />
        </h2>
        <xsl:apply-templates select="ns2:Odeljak" />
        <xsl:apply-templates select="ns2:Clan" />
    </xsl:template>
    <xsl:template match="ns2:Odeljak">
        <a name="{@ns2:id}" />
        <h2 align="center">
            <xsl:value-of select="@ns2:rednaOznaka" />.
            <xsl:value-of select="translate(ns2:Naziv, $latin, $cyrilic)"/>
        </h2>
        <xsl:apply-templates select="ns2:Pododeljak" />
        <xsl:apply-templates select="ns2:Clan" />
    </xsl:template>
    <xsl:template match="ns2:Pododeljak">
        <a name="{@ns2:id}" />
        <h2 align="center">
            <xsl:value-of select="translate(@ns2:rednaOznaka, $latin, $cyrilic)" />) <xsl:value-of select="translate(ns2:Naziv, $latin, $cyrilic)" />
        </h2>
        <xsl:apply-templates select="ns2:Clan" />
    </xsl:template>
    <xsl:template match="ns2:Stav">
        <a name="{@ns2:id}" />
        <p align="justify">
            <xsl:apply-templates />
        </p>
    </xsl:template>
    <xsl:template match="ns2:Stav/*">
        <xsl:copy>
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>
    <xsl:template match="ns2:Stav/text()">
        <xsl:if test="normalize-space(.)">
            <xsl:value-of select="translate(., $latin, $cyrilic)" />
        </xsl:if>
    </xsl:template>
    <xsl:template match="ns2:Stav/ns2:Tacka">
        <a name="{@ns2:id}" />
        <ul style="list-style-type:none">
            <xsl:apply-templates /> 
        </ul>
    </xsl:template>
    <xsl:template match="ns2:Tacka/*">
        <xsl:copy>
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>
    <xsl:template match="ns2:Tacka/text()">
        <xsl:if test="normalize-space(.)">
            <li><xsl:value-of select="../@ns2:rednaOznaka" />) <xsl:copy-of select="translate(., $latin, $cyrilic)" /></li>
        </xsl:if>
    </xsl:template>
    <xsl:template match="ns2:Tacka/ns2:Podtacka">
        <a name="{@ns2:id}" />
        <ul style="list-style-type:none">
            <xsl:apply-templates /> 
        </ul>
    </xsl:template>
    <xsl:template match="ns2:Podtacka/*">
        <xsl:copy>
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>
    <xsl:template match="ns2:Podtacka/text()">
        <xsl:if test="normalize-space(.)">
            <li>(<xsl:value-of select="../@ns2:rednaOznaka" />) <xsl:copy-of select="translate(., $latin, $cyrilic)" /></li>
        </xsl:if>
    </xsl:template>
    <xsl:template match="ns2:Podtacka/ns2:Alineja">
        <a name="{@ns2:id}" />
        <ul style="list-style-type:none">
            <xsl:apply-templates /> 
        </ul>
    </xsl:template>
    <xsl:template match="ns2:Alineja/text()">
        <xsl:if test="normalize-space(.)">
            <li>- <xsl:value-of select="translate(., $latin, $cyrilic)" /></li>
        </xsl:if>
    </xsl:template>
    <!-- default rule: ignore any unspecific text node -->
    <xsl:template match="text()" />
    
    <xsl:template match="ns3:Amandmani">
        <html>
            <head>
                <meta charset="utf-8" />
                <title><xsl:value-of select="translate(translate(ns2:Naziv, $smallcase, $uppercase), $latin, $cyrilic)" /></title>
            </head>
            <body style="margin:50px">
                <h1 align="center">
                    <xsl:value-of select="translate(translate(ns2:Naziv, $smallcase, $uppercase), $latin, $cyrilic)"></xsl:value-of>
                </h1>
                <xsl:apply-templates select="ns3:Amandman" />
            </body>
        </html>
    </xsl:template>
    
    
    <xsl:template match="ns3:Amandmani/ns3:Amandman">
        <h2 align="center">АМАНДМАН <xsl:number format="I"/>.</h2>
        <xsl:apply-templates />
    </xsl:template>
    <xsl:template match="ns3:Amandman">
        <html>
            <head>
                <meta charset="utf-8" />
                <title><xsl:value-of select="translate(translate(ns2:Naziv, $smallcase, $uppercase), $latin, $cyrilic)" /></title>
            </head>
            <body style="margin:50px">
                <xsl:apply-templates />
            </body>
        </html>
    </xsl:template>
    <xsl:template match="ns3:Amandman/text()">
        <p align="justify"> 
           <xsl:if test="normalize-space(.)">
               <xsl:value-of select="translate(., $latin, $cyrilic)" />
           </xsl:if>
        </p>
    </xsl:template>
    <xsl:template match="ns3:Izmena">
        <xsl:apply-templates />
    </xsl:template>
    <xsl:template match="ns3:Dopuna">
        <xsl:apply-templates />
    </xsl:template>
    <xsl:template match="ns3:Brisanje">
        <xsl:apply-templates />
    </xsl:template>
</xsl:stylesheet>