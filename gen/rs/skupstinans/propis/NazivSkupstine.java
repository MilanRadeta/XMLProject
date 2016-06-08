//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.08 at 10:57:10 PM CEST 
//


package rs.skupstinans.propis;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NazivSkupstine.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="NazivSkupstine">
 *   &lt;restriction base="{http://www.skupstinans.rs/elementi}Naziv">
 *     &lt;minLength value="1"/>
 *     &lt;maxLength value="50"/>
 *     &lt;pattern value="[A-Z�\u010c\u0106\u0110�]+[A-Z�\u0110\u010c\u0106�a-z�\u0111\u010d\u0107�0-9 ]*"/>
 *     &lt;enumeration value="Skup�tina Grada Novog Sada"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "NazivSkupstine")
@XmlEnum
public enum NazivSkupstine {

    @XmlEnumValue("Skup\u0161tina Grada Novog Sada")
    SKUP�TINA_GRADA_NOVOG_SADA("Skup\u0161tina Grada Novog Sada");
    private final String value;

    NazivSkupstine(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NazivSkupstine fromValue(String v) {
        for (NazivSkupstine c: NazivSkupstine.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
