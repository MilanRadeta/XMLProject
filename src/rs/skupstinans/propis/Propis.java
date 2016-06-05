//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.04 at 07:25:37 PM CEST 
//


package rs.skupstinans.propis;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import rs.skupstinans.elementi.Clan;
import rs.skupstinans.elementi.Deo;
import rs.skupstinans.elementi.DeoBezNaziva;
import rs.skupstinans.elementi.Glava;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.skupstinans.rs/propis}Preambula"/>
 *         &lt;element ref="{http://www.skupstinans.rs/propis}NazivPropisa"/>
 *         &lt;choice>
 *           &lt;element ref="{http://www.skupstinans.rs/elementi}Clan" maxOccurs="19"/>
 *           &lt;element ref="{http://www.skupstinans.rs/elementi}Deo" maxOccurs="unbounded"/>
 *           &lt;element ref="{http://www.skupstinans.rs/elementi}DeoBezNaziva" maxOccurs="unbounded"/>
 *           &lt;element ref="{http://www.skupstinans.rs/elementi}Glava" maxOccurs="unbounded"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://www.skupstinans.rs/propis}Prilozi"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://www.skupstinans.rs/propis}danStupanjaNaSnagu"/>
 *       &lt;attribute ref="{http://www.skupstinans.rs/propis}nazivSluzbenogGlasila"/>
 *       &lt;attribute ref="{http://www.skupstinans.rs/propis}godinaOdKojeSePrimenjuje"/>
 *       &lt;attribute ref="{http://www.skupstinans.rs/propis}rokVazenja"/>
 *       &lt;attribute ref="{http://www.skupstinans.rs/propis}brojPropisa use="required""/>
 *       &lt;attribute ref="{http://www.skupstinans.rs/propis}datumDonosenjaPropisa use="required""/>
 *       &lt;attribute ref="{http://www.skupstinans.rs/propis}preciscen"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "preambula",
    "nazivPropisa",
    "clan",
    "deo",
    "deoBezNaziva",
    "glava",
    "prilozi"
})
@XmlRootElement(name = "Propis")
public class Propis {

    @XmlElement(name = "Preambula", required = true)
    protected Preambula preambula;
    @XmlElement(name = "NazivPropisa", required = true)
    protected String nazivPropisa;
    @XmlElement(name = "Clan", namespace = "http://www.skupstinans.rs/elementi")
    protected List<Clan> clan;
    @XmlElement(name = "Deo", namespace = "http://www.skupstinans.rs/elementi")
    protected List<Deo> deo;
    @XmlElement(name = "DeoBezNaziva", namespace = "http://www.skupstinans.rs/elementi")
    protected List<DeoBezNaziva> deoBezNaziva;
    @XmlElement(name = "Glava", namespace = "http://www.skupstinans.rs/elementi")
    protected List<Glava> glava;
    @XmlElement(name = "Prilozi", required = true)
    protected Prilozi prilozi;
    @XmlAttribute(name = "danStupanjaNaSnagu", namespace = "http://www.skupstinans.rs/propis")
    protected BigInteger danStupanjaNaSnagu;
    @XmlAttribute(name = "nazivSluzbenogGlasila", namespace = "http://www.skupstinans.rs/propis")
    protected String nazivSluzbenogGlasila;
    @XmlAttribute(name = "godinaOdKojeSePrimenjuje", namespace = "http://www.skupstinans.rs/propis")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger godinaOdKojeSePrimenjuje;
    @XmlAttribute(name = "rokVazenja", namespace = "http://www.skupstinans.rs/propis")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar rokVazenja;
    @XmlAttribute(name = "brojPropisa", namespace = "http://www.skupstinans.rs/propis", required = true)
    protected BigInteger brojPropisa;
    @XmlAttribute(name = "datumDonosenjaPropisa", namespace = "http://www.skupstinans.rs/propis", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datumDonosenjaPropisa;
    @XmlAttribute(name = "preciscen", namespace = "http://www.skupstinans.rs/propis")
    protected Boolean preciscen;

    /**
     * Gets the value of the preambula property.
     * 
     * @return
     *     possible object is
     *     {@link Preambula }
     *     
     */
    public Preambula getPreambula() {
        return preambula;
    }

    /**
     * Sets the value of the preambula property.
     * 
     * @param value
     *     allowed object is
     *     {@link Preambula }
     *     
     */
    public void setPreambula(Preambula value) {
        this.preambula = value;
    }

    /**
     * Gets the value of the nazivPropisa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNazivPropisa() {
        return nazivPropisa;
    }

    /**
     * Sets the value of the nazivPropisa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNazivPropisa(String value) {
        this.nazivPropisa = value;
    }

    /**
     * Gets the value of the clan property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the clan property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClan().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Clan }
     * 
     * 
     */
    public List<Clan> getClan() {
        if (clan == null) {
            clan = new ArrayList<Clan>();
        }
        return this.clan;
    }

    /**
     * Gets the value of the deo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the deo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Deo }
     * 
     * 
     */
    public List<Deo> getDeo() {
        if (deo == null) {
            deo = new ArrayList<Deo>();
        }
        return this.deo;
    }

    /**
     * Gets the value of the deoBezNaziva property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the deoBezNaziva property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeoBezNaziva().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DeoBezNaziva }
     * 
     * 
     */
    public List<DeoBezNaziva> getDeoBezNaziva() {
        if (deoBezNaziva == null) {
            deoBezNaziva = new ArrayList<DeoBezNaziva>();
        }
        return this.deoBezNaziva;
    }

    /**
     * Gets the value of the glava property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the glava property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGlava().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Glava }
     * 
     * 
     */
    public List<Glava> getGlava() {
        if (glava == null) {
            glava = new ArrayList<Glava>();
        }
        return this.glava;
    }

    /**
     * Gets the value of the prilozi property.
     * 
     * @return
     *     possible object is
     *     {@link Prilozi }
     *     
     */
    public Prilozi getPrilozi() {
        return prilozi;
    }

    /**
     * Sets the value of the prilozi property.
     * 
     * @param value
     *     allowed object is
     *     {@link Prilozi }
     *     
     */
    public void setPrilozi(Prilozi value) {
        this.prilozi = value;
    }

    /**
     * Gets the value of the danStupanjaNaSnagu property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getDanStupanjaNaSnagu() {
        return danStupanjaNaSnagu;
    }

    /**
     * Sets the value of the danStupanjaNaSnagu property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setDanStupanjaNaSnagu(BigInteger value) {
        this.danStupanjaNaSnagu = value;
    }

    /**
     * Gets the value of the nazivSluzbenogGlasila property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNazivSluzbenogGlasila() {
        return nazivSluzbenogGlasila;
    }

    /**
     * Sets the value of the nazivSluzbenogGlasila property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNazivSluzbenogGlasila(String value) {
        this.nazivSluzbenogGlasila = value;
    }

    /**
     * Gets the value of the godinaOdKojeSePrimenjuje property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGodinaOdKojeSePrimenjuje() {
        return godinaOdKojeSePrimenjuje;
    }

    /**
     * Sets the value of the godinaOdKojeSePrimenjuje property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGodinaOdKojeSePrimenjuje(BigInteger value) {
        this.godinaOdKojeSePrimenjuje = value;
    }

    /**
     * Gets the value of the rokVazenja property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRokVazenja() {
        return rokVazenja;
    }

    /**
     * Sets the value of the rokVazenja property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRokVazenja(XMLGregorianCalendar value) {
        this.rokVazenja = value;
    }

    /**
     * Gets the value of the brojPropisa property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBrojPropisa() {
        return brojPropisa;
    }

    /**
     * Sets the value of the brojPropisa property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBrojPropisa(BigInteger value) {
        this.brojPropisa = value;
    }

    /**
     * Gets the value of the datumDonosenjaPropisa property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDatumDonosenjaPropisa() {
        return datumDonosenjaPropisa;
    }

    /**
     * Sets the value of the datumDonosenjaPropisa property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDatumDonosenjaPropisa(XMLGregorianCalendar value) {
        this.datumDonosenjaPropisa = value;
    }

    /**
     * Gets the value of the preciscen property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isPreciscen() {
        if (preciscen == null) {
            return true;
        } else {
            return preciscen;
        }
    }

    /**
     * Sets the value of the preciscen property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPreciscen(Boolean value) {
        this.preciscen = value;
    }

}