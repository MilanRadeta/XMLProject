//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.09 at 11:42:28 AM CEST 
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
 *         &lt;element ref="{http://www.skupstinans.rs/propis}PravniOsnov"/>
 *         &lt;element ref="{http://www.skupstinans.rs/propis}DonosilacPropisa"/>
 *         &lt;element ref="{http://www.skupstinans.rs/propis}Saglasnost" minOccurs="0"/>
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
    "pravniOsnov",
    "donosilacPropisa",
    "saglasnost"
})
@XmlRootElement(name = "Preambula")
public class Preambula {

    @XmlElement(name = "PravniOsnov", required = true)
    protected PravniOsnov pravniOsnov;
    @XmlElement(name = "DonosilacPropisa", required = true)
    protected DonosilacPropisa donosilacPropisa;
    @XmlElement(name = "Saglasnost")
    protected Saglasnost saglasnost;

    /**
     * Gets the value of the pravniOsnov property.
     * 
     * @return
     *     possible object is
     *     {@link PravniOsnov }
     *     
     */
    public PravniOsnov getPravniOsnov() {
        return pravniOsnov;
    }

    /**
     * Sets the value of the pravniOsnov property.
     * 
     * @param value
     *     allowed object is
     *     {@link PravniOsnov }
     *     
     */
    public void setPravniOsnov(PravniOsnov value) {
        this.pravniOsnov = value;
    }

    /**
     * Gets the value of the donosilacPropisa property.
     * 
     * @return
     *     possible object is
     *     {@link DonosilacPropisa }
     *     
     */
    public DonosilacPropisa getDonosilacPropisa() {
        return donosilacPropisa;
    }

    /**
     * Sets the value of the donosilacPropisa property.
     * 
     * @param value
     *     allowed object is
     *     {@link DonosilacPropisa }
     *     
     */
    public void setDonosilacPropisa(DonosilacPropisa value) {
        this.donosilacPropisa = value;
    }

    /**
     * Gets the value of the saglasnost property.
     * 
     * @return
     *     possible object is
     *     {@link Saglasnost }
     *     
     */
    public Saglasnost getSaglasnost() {
        return saglasnost;
    }

    /**
     * Sets the value of the saglasnost property.
     * 
     * @param value
     *     allowed object is
     *     {@link Saglasnost }
     *     
     */
    public void setSaglasnost(Saglasnost value) {
        this.saglasnost = value;
    }

}
