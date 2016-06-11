//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.11 at 01:16:14 PM CEST 
//


package rs.skupstinans.amandman;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the rs.skupstinans.amandman package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _PravniOsnov_QNAME = new QName("http://www.skupstinans.rs/amandman", "PravniOsnov");
    private final static QName _Brisanje_QNAME = new QName("http://www.skupstinans.rs/amandman", "Brisanje");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: rs.skupstinans.amandman
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Izmena }
     * 
     */
    public Izmena createIzmena() {
        return new Izmena();
    }

    /**
     * Create an instance of {@link Amandman }
     * 
     */
    public Amandman createAmandman() {
        return new Amandman();
    }

    /**
     * Create an instance of {@link Dopuna }
     * 
     */
    public Dopuna createDopuna() {
        return new Dopuna();
    }

    /**
     * Create an instance of {@link Amandmani }
     * 
     */
    public Amandmani createAmandmani() {
        return new Amandmani();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.skupstinans.rs/amandman", name = "PravniOsnov")
    public JAXBElement<String> createPravniOsnov(String value) {
        return new JAXBElement<String>(_PravniOsnov_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.skupstinans.rs/amandman", name = "Brisanje")
    public JAXBElement<Object> createBrisanje(Object value) {
        return new JAXBElement<Object>(_Brisanje_QNAME, Object.class, null, value);
    }

}
