//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.12 at 10:06:18 PM CEST 
//


package rs.skupstinans.propis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{http://www.skupstinans.rs/propis}MaterijalniPravniOsnov"/>
 *         &lt;element ref="{http://www.skupstinans.rs/propis}FormalniPravniOsnov"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "materijalniPravniOsnov",
    "formalniPravniOsnov"
})
@XmlRootElement(name = "PravniOsnov")
public class PravniOsnov {

    @XmlElement(name = "MaterijalniPravniOsnov", required = true)
    protected String materijalniPravniOsnov;
    @XmlElement(name = "FormalniPravniOsnov", required = true)
    protected String formalniPravniOsnov;

    /**
     * Gets the value of the materijalniPravniOsnov property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaterijalniPravniOsnov() {
        return materijalniPravniOsnov;
    }

    /**
     * Sets the value of the materijalniPravniOsnov property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaterijalniPravniOsnov(String value) {
        this.materijalniPravniOsnov = value;
    }

    /**
     * Gets the value of the formalniPravniOsnov property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormalniPravniOsnov() {
        return formalniPravniOsnov;
    }

    /**
     * Sets the value of the formalniPravniOsnov property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormalniPravniOsnov(String value) {
        this.formalniPravniOsnov = value;
    }

}
