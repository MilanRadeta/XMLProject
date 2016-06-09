package rs.skupstinans.session;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.Transaction;
import com.marklogic.client.document.DocumentDescriptor;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.JAXBHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;

import rs.skupstinans.propis.Propis;
import rs.skupstinans.xmldb.util.Util;
import rs.skupstinans.xmldb.util.Util.ConnectionProperties;

/**
 * Session Bean implementation class DatabaseBean
 */
@Stateless
@LocalBean
public class DatabaseBean implements DatabaseBeanRemote {

	private DatabaseClient client;
	private XMLDocumentManager xmlManager;

	private List<Transaction> transactions = new ArrayList<>();

	private TransformerFactory transformerFactory;

	@PostConstruct
	void init() {
		ConnectionProperties props;
		try {
			props = Util.loadProperties();
			client = DatabaseClientFactory.newClient(props.host, props.port, props.database, props.user, props.password,
					props.authType);
			xmlManager = client.newXMLDocumentManager();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		transformerFactory = TransformerFactory.newInstance();
	}

	@PreDestroy
	void destroy() {
		System.out.println("DESTROY");
		for (Transaction t : transactions) {
			t.rollback();
		}
		client.release();
	}

	public void write(String URI, Propis propis) {
		xmlManager.write(URI, getPropisHandle(propis));
	}

	private Transaction createTransaction() {

		Transaction t = client.openTransaction();
		transactions.add(t);
		return t;
	}

	private void commitTransaction(Transaction t) {
		t.commit();
		transactions.remove(t);
	}

	private void rollbackTransaction(Transaction t) {
		t.rollback();
		transactions.remove(t);
	}

	public void testBrojPropisa() {
		Transaction t = createTransaction();
		System.out.println("testBrojPropisa");
		DOMHandle content = new DOMHandle();
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		// xmlManager.read("/brojPropisa", metadata, content, transaction);
		DocumentDescriptor desc = xmlManager.exists("/brojPropisa", t);
		System.out.println("BROJ PROPISA DESC: " + desc);
		if (desc == null) {
			StringHandle stringHandle = new StringHandle("<brojPropisa>1</brojPropisa>");
			xmlManager.write("/brojPropisa", stringHandle, t);
		} else {
			/*
			DOMHandle handle = new DOMHandle();
			xmlManager.read("/brojPropisa", handle, t);
			handle.get().getElementsByTagName("brojPropisa").item(0)
			*/
		}
		commitTransaction(t);
		desc = xmlManager.exists("/brojPropisa");
		System.out.println("BROJ PROPISA DESC: " + desc);
		// Document doc = content.get();
		// transform(doc, System.out);
	}

	public void predlogPropisa(Propis propis) {
		DOMHandle content = new DOMHandle();
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		xmlManager.read("/brojPropisa", metadata, content);
		Document doc = content.get();
		transform(doc, System.out);
		// xmlManager.write("/propisi/" + propis.getBrojPropisa(), "/predlozi",
		// propis);
	}

	private void transform(Node node, OutputStream out) {
		try {

			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(node);
			StreamResult result = new StreamResult(out);
			transformer.transform(source, result);

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	private JAXBHandle<Propis> getPropisHandle(Propis propis) {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(propis.getClass().getPackage().getName());
			JAXBHandle<Propis> handle = new JAXBHandle<>(context);
			handle.set(propis);
			return handle;
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void delete(String URI, JAXBHandle<? extends Object> handle) {
		xmlManager.delete(URI);
	}

	public void write(String URI, String collectionID, Propis propis) {
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		metadata.getCollections().add(collectionID);
		xmlManager.write(URI, metadata, getPropisHandle(propis));
	}

	public void read(String URI, DocumentMetadataHandle metadata, JAXBHandle<? extends Object> handle) {
		xmlManager.read(URI, metadata, handle);
	}

	public SearchHandle query(String query, String collection, String context) {

		QueryManager queryManager = client.newQueryManager();

		// Query definition is used to specify Google-style query string
		StringQueryDefinition queryDefinition = queryManager.newStringDefinition();
		queryDefinition.setCriteria(query);

		if (collection != null) {
			// Search within a specific collection
			queryDefinition.setCollections(collection);
		}
		return queryManager.search(queryDefinition, new SearchHandle());
	}

}
