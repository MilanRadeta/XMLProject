//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5.1 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.05 at 09:58:34 PM CEST 
//


package rs.skupstinans.propis;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the rs.skupstinans.propis package. 
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

    private final static QName _Naznaka_QNAME = new QName("http://www.skupstinans.rs/propis", "Naznaka");
    private final static QName _Prilog_QNAME = new QName("http://www.skupstinans.rs/propis", "Prilog");
    private final static QName _FormalniPravniOsnov_QNAME = new QName("http://www.skupstinans.rs/propis", "FormalniPravniOsnov");
    private final static QName _NazivPropisa_QNAME = new QName("http://www.skupstinans.rs/propis", "NazivPropisa");
    private final static QName _MaterijalniPravniOsnov_QNAME = new QName("http://www.skupstinans.rs/propis", "MaterijalniPravniOsnov");
    private final static QName _NazivOrgana_QNAME = new QName("http://www.skupstinans.rs/propis", "NazivOrgana");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: rs.skupstinans.propis
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PravniOsnov }
     * 
     */
    public PravniOsnov createPravniOsnov() {
        return new PravniOsnov();
    }

    /**
     * Create an instance of {@link Saglasnost }
     * 
     */
    public Saglasnost createSaglasnost() {
        return new Saglasnost();
    }

    /**
     * Create an instance of {@link Propis }
     * 
     */
    public Propis createPropis() {
        return new Propis();
    }

    /**
     * Create an instance of {@link Preambula }
     * 
     */
    public Preambula createPreambula() {
        return new Preambula();
    }

    /**
     * Create an instance of {@link DonosilacPropisa }
     * 
     */
    public DonosilacPropisa createDonosilacPropisa() {
        return new DonosilacPropisa();
    }

    /**
     * Create an instance of {@link Prilozi }
     * 
     */
    public Prilozi createPrilozi() {
        return new Prilozi();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.skupstinans.rs/propis", name = "Naznaka")
    public JAXBElement<String> createNaznaka(String value) {
        return new JAXBElement<String>(_Naznaka_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.skupstinans.rs/propis", name = "Prilog")
    public JAXBElement<Object> createPrilog(Object value) {
        return new JAXBElement<Object>(_Prilog_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.skupstinans.rs/propis", name = "FormalniPravniOsnov")
    public JAXBElement<String> createFormalniPravniOsnov(String value) {
        return new JAXBElement<String>(_FormalniPravniOsnov_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.skupstinans.rs/propis", name = "NazivPropisa")
    public JAXBElement<String> createNazivPropisa(String value) {
        return new JAXBElement<String>(_NazivPropisa_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.skupstinans.rs/propis", name = "MaterijalniPravniOsnov")
    public JAXBElement<String> createMaterijalniPravniOsnov(String value) {
        return new JAXBElement<String>(_MaterijalniPravniOsnov_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.skupstinans.rs/propis", name = "NazivOrgana")
    public JAXBElement<String> createNazivOrgana(String value) {
        return new JAXBElement<String>(_NazivOrgana_QNAME, String.class, null, value);
    }

}
