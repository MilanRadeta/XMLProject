//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.11 at 01:16:14 PM CEST 
//


package rs.skupstinans.amandman;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import rs.skupstinans.elementi.Alineja;
import rs.skupstinans.elementi.Clan;
import rs.skupstinans.elementi.Podtacka;
import rs.skupstinans.elementi.Stav;
import rs.skupstinans.elementi.Tacka;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{http://www.skupstinans.rs/elementi}Clan"/>
 *         &lt;element ref="{http://www.skupstinans.rs/elementi}Stav"/>
 *         &lt;element ref="{http://www.skupstinans.rs/elementi}Tacka"/>
 *         &lt;element ref="{http://www.skupstinans.rs/elementi}Podtacka"/>
 *         &lt;element ref="{http://www.skupstinans.rs/elementi}Alineja"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "clan",
    "stav",
    "tacka",
    "podtacka",
    "alineja"
})
@XmlRootElement(name = "Izmena")
public class Izmena {

    @XmlElement(name = "Clan", namespace = "http://www.skupstinans.rs/elementi")
    protected Clan clan;
    @XmlElement(name = "Stav", namespace = "http://www.skupstinans.rs/elementi")
    protected Stav stav;
    @XmlElement(name = "Tacka", namespace = "http://www.skupstinans.rs/elementi")
    protected Tacka tacka;
    @XmlElement(name = "Podtacka", namespace = "http://www.skupstinans.rs/elementi")
    protected Podtacka podtacka;
    @XmlElement(name = "Alineja", namespace = "http://www.skupstinans.rs/elementi")
    protected Alineja alineja;

    /**
     * Gets the value of the clan property.
     * 
     * @return
     *     possible object is
     *     {@link Clan }
     *     
     */
    public Clan getClan() {
        return clan;
    }

    /**
     * Sets the value of the clan property.
     * 
     * @param value
     *     allowed object is
     *     {@link Clan }
     *     
     */
    public void setClan(Clan value) {
        this.clan = value;
    }

    /**
     * Gets the value of the stav property.
     * 
     * @return
     *     possible object is
     *     {@link Stav }
     *     
     */
    public Stav getStav() {
        return stav;
    }

    /**
     * Sets the value of the stav property.
     * 
     * @param value
     *     allowed object is
     *     {@link Stav }
     *     
     */
    public void setStav(Stav value) {
        this.stav = value;
    }

    /**
     * Gets the value of the tacka property.
     * 
     * @return
     *     possible object is
     *     {@link Tacka }
     *     
     */
    public Tacka getTacka() {
        return tacka;
    }

    /**
     * Sets the value of the tacka property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tacka }
     *     
     */
    public void setTacka(Tacka value) {
        this.tacka = value;
    }

    /**
     * Gets the value of the podtacka property.
     * 
     * @return
     *     possible object is
     *     {@link Podtacka }
     *     
     */
    public Podtacka getPodtacka() {
        return podtacka;
    }

    /**
     * Sets the value of the podtacka property.
     * 
     * @param value
     *     allowed object is
     *     {@link Podtacka }
     *     
     */
    public void setPodtacka(Podtacka value) {
        this.podtacka = value;
    }

    /**
     * Gets the value of the alineja property.
     * 
     * @return
     *     possible object is
     *     {@link Alineja }
     *     
     */
    public Alineja getAlineja() {
        return alineja;
    }

    /**
     * Sets the value of the alineja property.
     * 
     * @param value
     *     allowed object is
     *     {@link Alineja }
     *     
     */
    public void setAlineja(Alineja value) {
        this.alineja = value;
    }

}
