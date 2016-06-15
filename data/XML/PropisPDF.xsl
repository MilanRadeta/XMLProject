<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:ns1="http://www.skupstinans.rs/propis"
    xmlns:ns2="http://www.skupstinans.rs/elementi"
    xmlns:ns3="http://www.skupstinans.rs/amandman"
    xmlns:fo="http://www.w3.org/1999/XSL/Format" 
    exclude-result-prefixes="xs"
    version="2.0">
    <xsl:output method="xml" encoding="UTF-8"/>
    <xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyzčšđćžабвгдђежзијклљмнњопрстћуфхцчџш'" />
    <xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZČŠĐĆŽАБВГДЂЕЖЗИЈКЛЉМНЊОПРСТЋУФХЦЧЏЊ'" />
    <xsl:variable name="latin" select="'abcdefghijklmnoprstuvzčšđćžABCDEFGHIJKLMNOPRSTUVZČŠĐĆŽ'" />
    <xsl:variable name="cyrilic" select="'абцдефгхијклмнопрстувзчшђћжАБЦДЕФГХИЈКЛМНОПРСТУВЗЧЂЂЋЖ'" />
    
    <xsl:template match="ns1:Propis">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master
                    master-name="propis-page"
                    page-height="29.7cm"
                    page-width="21cm"
                    margin-top="1cm"
                    margin-bottom="2cm"
                    margin-left="2.5cm"
                    margin-right="2.5cm">>
                    <fo:region-body margin="1in"/>
                </fo:simple-page-master>
                <fo:page-sequence-master master-name="propis-pages">
                    <fo:repeatable-page-master-reference master-reference="propis-page" maximum-repeats="no-limit" />
                </fo:page-sequence-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="propis-pages" initial-page-number="1" country="rs" language="sr">
                <fo:flow flow-name="xsl-region-body" font-family="Arial">
                    <xsl:apply-templates select="ns1:Preambula" />
                    <xsl:apply-templates select="ns2:Obrazlozenje" />
                    <fo:block text-align="center" font-family="Arial" font-size="32px" font-weight="bold" padding="30px">
                        <xsl:value-of select="translate(translate(ns2:Naziv, $smallcase, $uppercase), $latin, $cyrilic)"></xsl:value-of>
                    </fo:block>
                    <xsl:apply-templates select="ns2:Deo" />
                    <xsl:apply-templates select="ns2:Glava" />
                    <xsl:apply-templates select="ns2:Clan" />
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:template match="ns1:Preambula">
        <fo:block space-after="12pt" space-before="12pt" text-align="justify">
            <xsl:value-of select="translate(ns1:PravniOsnov, $latin, $cyrilic)" />,
            <xsl:apply-templates select="ns1:Saglasnost" />
        </fo:block>
        <fo:block space-after="12pt" space-before="12pt" text-align="justify">
            <xsl:value-of select="translate(ns1:DonosilacPropisa/ns2:Naziv, $latin, $cyrilic)" />
            доноси
        </fo:block>
    </xsl:template>
    <xsl:template match="ns2:Obrazlozenje">
        <fo:block text-align="center" font-family="Arial" font-size="18px" font-weight="bold">Образложење</fo:block>
        <fo:block space-after="12pt" space-before="12pt" text-align="justify">
            <xsl:value-of select="translate(., $latin, $cyrilic)" />
        </fo:block>
    </xsl:template>
    <xsl:template match="ns1:Saglasnost">
        , уз сагласност коју доноси
        <xsl:value-of select="translate(ns2:Naziv, $latin, $cyrilic)" />
        са следећом назнаком
        <fo:block space-after="12pt" space-before="12pt" text-align="justify">
            <xsl:value-of select="translate(ns1:Naznaka, $latin, $cyrilic)" />
        </fo:block>
        
    </xsl:template>
    <xsl:template match="ns2:Clan">
        <fo:block text-align="center" font-family="Arial" font-size="20px" font-weight="bold"><xsl:value-of select="translate(ns2:Naziv, $latin, $cyrilic)" /></fo:block>
        <fo:block text-align="center" font-family="Arial" font-size="20px" font-weight="bold">Члан <xsl:value-of select="@ns2:rednaOznaka" />.</fo:block>
        <xsl:apply-templates select="ns2:Stav" />
    </xsl:template>
    <xsl:template match="ns2:Deo">
        <fo:block text-align="center" font-family="Arial" font-size="24px" font-weight="bold">Део <xsl:value-of select="translate(@ns2:rednaOznaka, $latin, $cyrilic)" /></fo:block>
        <fo:block text-align="center" font-family="Arial" font-size="24px" font-weight="bold"><xsl:value-of select="translate(translate(ns2:Naziv, $latin, $cyrilic), $smallcase, $uppercase)" /></fo:block>
        <xsl:apply-templates select="ns2:Glava" />
    </xsl:template>
    <xsl:template match="ns2:Glava">
        <fo:block text-align="center" font-family="Arial" font-size="22px" font-weight="bold">
            <xsl:value-of select="@ns2:rednaOznaka" />.
            <xsl:value-of select="translate(translate(ns2:Naziv, $latin, $cyrilic), $smallcase, $uppercase)" />
        </fo:block>
        <xsl:apply-templates select="ns2:Odeljak" />
        <xsl:apply-templates select="ns2:Clan" />
    </xsl:template>
    <xsl:template match="ns2:Odeljak">
        <fo:block text-align="center" font-family="Arial" font-size="20px" font-weight="bold" font-style="italic">
            <xsl:value-of select="@ns2:rednaOznaka" />.
            <xsl:value-of select="translate(ns2:Naziv, $latin, $cyrilic)"/>
        </fo:block>
        <xsl:apply-templates select="ns2:Pododeljak" />
        <xsl:apply-templates select="ns2:Clan" />
    </xsl:template>
    <xsl:template match="ns2:Pododeljak">
        <fo:block text-align="center" font-family="Arial" font-size="20px" font-weight="bold" font-style="italic">
            <xsl:value-of select="translate(@ns2:rednaOznaka, $latin, $cyrilic)" />)
            <xsl:value-of select="translate(ns2:Naziv, $latin, $cyrilic)" />
        </fo:block>
        <xsl:apply-templates select="ns2:Clan" />
    </xsl:template>
    <xsl:template match="ns2:Stav">
        <fo:block space-after="12pt" space-before="12pt" text-align="justify">
            <xsl:apply-templates />
        </fo:block>
    </xsl:template>
    <xsl:template match="ns2:Stav//*">
        <xsl:copy>
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>
    <xsl:template match="ns2:Stav//text()">
        <xsl:if test="normalize-space(.)">
            <xsl:value-of select="translate(., $latin, $cyrilic)" />
        </xsl:if>
    </xsl:template>
    <xsl:template match="ns2:Stav//Tacka">
        <fo:list-block start-indent="12pt" end-indent="12pt">
                <xsl:apply-templates /> 
            
        </fo:list-block>
    </xsl:template>
    <xsl:template match="ns2:Tacka//*">
        <xsl:copy>
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>
    <xsl:template match="ns2:Tacka//text()">
        <xsl:if test="normalize-space(.)">
            <fo:list-item>
             <fo:list-item-label>
                 <fo:block>
                     <xsl:value-of select="@ns2:rednaOznaka" />) 
                 </fo:block>
             </fo:list-item-label>
             <fo:list-item-body>
                 <xsl:copy-of select="translate(., $latin, $cyrilic)" />
             </fo:list-item-body>
            </fo:list-item>
        </xsl:if>
    </xsl:template>
    <xsl:template match="ns2:Tacka//ns2:Podtacka">
        <fo:list-block start-indent="12pt" end-indent="12pt">
                <xsl:apply-templates />
        </fo:list-block>
    </xsl:template>
    <xsl:template match="ns2:Podtacka//*">
        <xsl:copy>
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>
    <xsl:template match="ns2:Podtacka//text()">
        <xsl:if test="normalize-space(.)">
            <fo:list-item>
             <fo:list-item-label>
                 <fo:block>
                     (<xsl:value-of select="@ns2:rednaOznaka" />)
                 </fo:block>
             </fo:list-item-label>
             <fo:list-item-body>
                 <xsl:copy-of select="translate(., $latin, $cyrilic)" />
             </fo:list-item-body>
            </fo:list-item>
        </xsl:if>
    </xsl:template>
    <xsl:template match="ns2:Podtacka//ns2:Alineja">
        <fo:list-block start-indent="12pt" end-indent="12pt">
            <xsl:apply-templates />
        </fo:list-block>
    </xsl:template>
    <xsl:template match="ns2:Alineja//text()">
        <xsl:if test="normalize-space(.)">
            <fo:list-item>
                <fo:list-item-label>
                    <fo:block>
                        -
                    </fo:block>
                </fo:list-item-label>
                <fo:list-item-body>
                    <xsl:value-of select="translate(., $latin, $cyrilic)" />
                </fo:list-item-body>
            </fo:list-item>
        </xsl:if>
    </xsl:template>
    <!-- default rule: ignore any unspecific text node -->
    <xsl:template match="text()" />
    
    <xsl:template match="ns3:Amandmani">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master
                    master-name="propis-page"
                    page-height="29.7cm"
                    page-width="21cm"
                    margin-top="1cm"
                    margin-bottom="2cm"
                    margin-left="2.5cm"
                    margin-right="2.5cm">>
                    <fo:region-body margin="1in"/>
                </fo:simple-page-master>
                <fo:page-sequence-master master-name="amandman-page">
                    <fo:repeatable-page-master-reference master-reference="propis-page" maximum-repeats="no-limit" />
                </fo:page-sequence-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="amandman-pages" initial-page-number="1" country="rs" language="sr">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block text-align="center" font-family="Arial" font-size="32px" font-weight="bold" padding="30px">
                        <xsl:value-of select="translate(translate(ns2:Naziv, $smallcase, $uppercase), $latin, $cyrilic)" />
                    </fo:block>
                    <xsl:apply-templates />
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    
    
    <xsl:template match="ns3:Amandmani/ns3:Amandman">
        <fo:block text-align="center" font-family="Arial" font-size="20px" font-weight="bold" padding="30px">
            АМАНДМАН <xsl:number format="I"/>.
        </fo:block>
        <xsl:apply-templates />
    </xsl:template>
    <xsl:template match="ns3:Amandman">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master
                    master-name="propis-page"
                    page-height="29.7cm"
                    page-width="21cm"
                    margin-top="1cm"
                    margin-bottom="2cm"
                    margin-left="2.5cm"
                    margin-right="2.5cm">>
                    <fo:region-body margin="1in"/>
                </fo:simple-page-master>
                <fo:page-sequence-master master-name="amandman-page">
                    <fo:repeatable-page-master-reference master-reference="propis-page" maximum-repeats="no-limit" />
                </fo:page-sequence-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="amandman-pages" initial-page-number="1" country="rs" language="sr">
                <fo:flow flow-name="xsl-region-body">
                    <xsl:apply-templates />
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:template match="ns3:Amandman//*">
        <xsl:copy>
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>
    <xsl:template match="ns3:Amandman//text()">
        <fo:block space-after="12pt" space-before="12pt" text-align="justify">
            <xsl:if test="normalize-space(.)">
                <xsl:value-of select="translate(., $latin, $cyrilic)" />
            </xsl:if>
        </fo:block>
    </xsl:template>
    <xsl:template match="ns3:Amandman//ns3:Izmena">
        <xsl:apply-templates />
    </xsl:template>
    <xsl:template match="ns3:Amandman//ns3:Dopuna">
        <xsl:apply-templates />
    </xsl:template>
    <xsl:template match="ns3:Amandman//ns3:Brisanje">
        <xsl:apply-templates />
    </xsl:template>
    <xsl:template match="ns3:Amandman//ns2:Obrazlozenje"><fo:block text-align="center" font-family="Arial" font-size="18px" font-weight="bold">Образложење</fo:block>
        <fo:block space-after="12pt" space-before="12pt" text-align="justify">
            <xsl:value-of select="translate(., $latin, $cyrilic)" />
        </fo:block>
    </xsl:template>
</xsl:stylesheet>