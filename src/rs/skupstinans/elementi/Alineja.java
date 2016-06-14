//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.14 at 07:36:13 PM CEST 
//


package rs.skupstinans.elementi;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;group ref="{http://www.skupstinans.rs/elementi}ElementiTeksta"/>
 *       &lt;/choice>
 *       &lt;attribute ref="{http://www.skupstinans.rs/elementi}id use="required""/>
 *       &lt;attribute ref="{http://www.skupstinans.rs/elementi}rednaOznaka"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "content"
})
@XmlRootElement(name = "Alineja")
public class Alineja implements ElementInterface {

    @XmlElementRefs({
        @XmlElementRef(name = "StrucniIzraz", namespace = "http://www.skupstinans.rs/elementi", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "StranaRec", namespace = "http://www.skupstinans.rs/elementi", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Datum", namespace = "http://www.skupstinans.rs/elementi", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Referenca", namespace = "http://www.skupstinans.rs/elementi", type = Referenca.class, required = false),
        @XmlElementRef(name = "SkraceniNaziv", namespace = "http://www.skupstinans.rs/elementi", type = JAXBElement.class, required = false)
    })
    @XmlMixed
    protected List<Object> content;
    @XmlAttribute(name = "id", namespace = "http://www.skupstinans.rs/elementi", required = true)
    protected String id;
    @XmlAttribute(name = "rednaOznaka", namespace = "http://www.skupstinans.rs/elementi")
    protected String rednaOznaka;

    /**
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * {@link Referenca }
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link String }
     * 
     * 
     */
    public List<Object> getContent() {
        if (content == null) {
            content = new ArrayList<Object>();
        }
        return this.content;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the rednaOznaka property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRednaOznaka() {
        return rednaOznaka;
    }

    /**
     * Sets the value of the rednaOznaka property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRednaOznaka(String value) {
        this.rednaOznaka = value;
    }

}
