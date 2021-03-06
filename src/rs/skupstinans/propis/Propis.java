//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.16 at 03:31:00 AM CEST 
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
 *         &lt;element ref="{http://www.skupstinans.rs/propis}Preambula" minOccurs="0"/>
 *         &lt;element ref="{http://www.skupstinans.rs/elementi}Naziv"/>
 *         &lt;choice>
 *           &lt;element ref="{http://www.skupstinans.rs/elementi}Clan" maxOccurs="19"/>
 *           &lt;element ref="{http://www.skupstinans.rs/elementi}Deo" maxOccurs="unbounded" minOccurs="2"/>
 *           &lt;element ref="{http://www.skupstinans.rs/elementi}Glava" maxOccurs="unbounded" minOccurs="2"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://www.skupstinans.rs/propis}Prilozi" minOccurs="0"/>
 *         &lt;element ref="{http://www.skupstinans.rs/elementi}Obrazlozenje" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{http://www.skupstinans.rs/propis}nazivSluzbenogGlasila"/>
 *       &lt;attribute ref="{http://www.skupstinans.rs/propis}brojPropisa use="required""/>
 *       &lt;attribute ref="{http://www.skupstinans.rs/propis}preciscen"/>
 *       &lt;attribute ref="{http://www.skupstinans.rs/propis}status use="required""/>
 *       &lt;attribute ref="{http://www.skupstinans.rs/propis}datumPredlaganjaPropisa"/>
 *       &lt;attribute ref="{http://www.skupstinans.rs/propis}datumUsvajanjaPropisa"/>
 *       &lt;attribute ref="{http://www.skupstinans.rs/propis}datumStupanjaNaSnagu"/>
 *       &lt;attribute ref="{http://www.skupstinans.rs/propis}datumPrimenjivanja"/>
 *       &lt;attribute ref="{http://www.skupstinans.rs/propis}rokVazenja"/>
 *       &lt;attribute ref="{http://www.skupstinans.rs/propis}datumIzdavanjaUSluzbenomGlasilu"/>
 *       &lt;attribute ref="{http://www.skupstinans.rs/elementi}usernameDonosioca"/>
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
    "naziv",
    "clan",
    "deo",
    "glava",
    "prilozi",
    "obrazlozenje"
})
@XmlRootElement(name = "Propis")
public class Propis {

    @XmlElement(name = "Preambula")
    protected Preambula preambula;
    @XmlElement(name = "Naziv", namespace = "http://www.skupstinans.rs/elementi", required = true)
    protected String naziv;
    @XmlElement(name = "Clan", namespace = "http://www.skupstinans.rs/elementi")
    protected List<Clan> clan;
    @XmlElement(name = "Deo", namespace = "http://www.skupstinans.rs/elementi")
    protected List<Deo> deo;
    @XmlElement(name = "Glava", namespace = "http://www.skupstinans.rs/elementi")
    protected List<Glava> glava;
    @XmlElement(name = "Prilozi")
    protected Prilozi prilozi;
    @XmlElement(name = "Obrazlozenje", namespace = "http://www.skupstinans.rs/elementi")
    protected String obrazlozenje;
    @XmlAttribute(name = "nazivSluzbenogGlasila", namespace = "http://www.skupstinans.rs/propis")
    protected String nazivSluzbenogGlasila;
    @XmlAttribute(name = "brojPropisa", namespace = "http://www.skupstinans.rs/propis", required = true)
    protected BigInteger brojPropisa;
    @XmlAttribute(name = "preciscen", namespace = "http://www.skupstinans.rs/propis")
    protected Boolean preciscen;
    @XmlAttribute(name = "status", namespace = "http://www.skupstinans.rs/propis", required = true)
    protected String status;
    @XmlAttribute(name = "datumPredlaganjaPropisa", namespace = "http://www.skupstinans.rs/propis")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datumPredlaganjaPropisa;
    @XmlAttribute(name = "datumUsvajanjaPropisa", namespace = "http://www.skupstinans.rs/propis")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datumUsvajanjaPropisa;
    @XmlAttribute(name = "datumStupanjaNaSnagu", namespace = "http://www.skupstinans.rs/propis")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datumStupanjaNaSnagu;
    @XmlAttribute(name = "datumPrimenjivanja", namespace = "http://www.skupstinans.rs/propis")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datumPrimenjivanja;
    @XmlAttribute(name = "rokVazenja", namespace = "http://www.skupstinans.rs/propis")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar rokVazenja;
    @XmlAttribute(name = "datumIzdavanjaUSluzbenomGlasilu", namespace = "http://www.skupstinans.rs/propis")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datumIzdavanjaUSluzbenomGlasilu;
    @XmlAttribute(name = "usernameDonosioca", namespace = "http://www.skupstinans.rs/elementi")
    @XmlSchemaType(name = "anySimpleType")
    protected String usernameDonosioca;

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
     * Gets the value of the naziv property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNaziv() {
        return naziv;
    }

    /**
     * Sets the value of the naziv property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNaziv(String value) {
        this.naziv = value;
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
     * Gets the value of the obrazlozenje property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObrazlozenje() {
        return obrazlozenje;
    }

    /**
     * Sets the value of the obrazlozenje property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObrazlozenje(String value) {
        this.obrazlozenje = value;
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
     * Gets the value of the preciscen property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isPreciscen() {
        if (preciscen == null) {
            return false;
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

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the datumPredlaganjaPropisa property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDatumPredlaganjaPropisa() {
        return datumPredlaganjaPropisa;
    }

    /**
     * Sets the value of the datumPredlaganjaPropisa property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDatumPredlaganjaPropisa(XMLGregorianCalendar value) {
        this.datumPredlaganjaPropisa = value;
    }

    /**
     * Gets the value of the datumUsvajanjaPropisa property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDatumUsvajanjaPropisa() {
        return datumUsvajanjaPropisa;
    }

    /**
     * Sets the value of the datumUsvajanjaPropisa property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDatumUsvajanjaPropisa(XMLGregorianCalendar value) {
        this.datumUsvajanjaPropisa = value;
    }

    /**
     * Gets the value of the datumStupanjaNaSnagu property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDatumStupanjaNaSnagu() {
        return datumStupanjaNaSnagu;
    }

    /**
     * Sets the value of the datumStupanjaNaSnagu property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDatumStupanjaNaSnagu(XMLGregorianCalendar value) {
        this.datumStupanjaNaSnagu = value;
    }

    /**
     * Gets the value of the datumPrimenjivanja property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDatumPrimenjivanja() {
        return datumPrimenjivanja;
    }

    /**
     * Sets the value of the datumPrimenjivanja property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDatumPrimenjivanja(XMLGregorianCalendar value) {
        this.datumPrimenjivanja = value;
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
     * Gets the value of the datumIzdavanjaUSluzbenomGlasilu property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDatumIzdavanjaUSluzbenomGlasilu() {
        return datumIzdavanjaUSluzbenomGlasilu;
    }

    /**
     * Sets the value of the datumIzdavanjaUSluzbenomGlasilu property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDatumIzdavanjaUSluzbenomGlasilu(XMLGregorianCalendar value) {
        this.datumIzdavanjaUSluzbenomGlasilu = value;
    }

    /**
     * Gets the value of the usernameDonosioca property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsernameDonosioca() {
        return usernameDonosioca;
    }

    /**
     * Sets the value of the usernameDonosioca property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsernameDonosioca(String value) {
        this.usernameDonosioca = value;
    }

}
