package rs.skupstinans.session;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.namespace.QName;

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
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;
import com.marklogic.client.query.StructuredQueryBuilder;
import com.marklogic.client.query.StructuredQueryDefinition;

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

	private static int transactionCounter = 0;

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
	}

	@PreDestroy
	void destroy() {
		System.out.println("DESTROY");
		for (Transaction t : transactions) {
			t.rollback();
		}
		client.release();
	}

	private Transaction createTransaction() {

		Transaction t = client.openTransaction("Trans-" + (transactionCounter++), 10);
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

	public void predlogPropisa(Propis propis) {
		Transaction t = createTransaction();
		DOMHandle content = new DOMHandle();
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		DocumentDescriptor desc = xmlManager.exists("/brojPropisa", t);
		int brojPropisa;
		if (desc == null) {
			brojPropisa = 0;
		} else {
			xmlManager.read("/brojPropisa", metadata, content);
			brojPropisa = Integer.parseInt(content.get().getElementsByTagName("brojPropisa").item(0).getTextContent());
		}
		brojPropisa++;
		propis.setBrojPropisa(new BigInteger("" + brojPropisa));
		StringHandle stringHandle = new StringHandle("<brojPropisa>" + brojPropisa + "</brojPropisa>");
		xmlManager.write("/brojPropisa", stringHandle, t);
		write("/propisi/" + brojPropisa, "/predlozi", propis, t);
		commitTransaction(t);
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

	public void write(String URI, Propis propis) {
		xmlManager.write(URI, getPropisHandle(propis));
	}

	public void write(String URI, Propis propis, Transaction t) {
		xmlManager.write(URI, getPropisHandle(propis), t);
	}

	public void write(String URI, String collectionID, Propis propis) {
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		metadata.getCollections().add(collectionID);
		xmlManager.write(URI, metadata, getPropisHandle(propis));
	}

	public void write(String URI, String collectionID, Propis propis, Transaction t) {
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		metadata.getCollections().add(collectionID);
		xmlManager.write(URI, metadata, getPropisHandle(propis), t);
	}

	public void read(String URI, DocumentMetadataHandle metadata, JAXBHandle<? extends Object> handle) {
		xmlManager.read(URI, metadata, handle);
	}

	public void read(String URI, DocumentMetadataHandle metadata, DOMHandle handle) {
		xmlManager.read(URI, metadata, handle);
	}

	public SearchHandle query(String query, String collection) {

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

	public void test() {
		if (false) {
			// TODO: cleanup all acts and reset act counter
			Transaction t = createTransaction();
			DOMHandle content = new DOMHandle();
			DocumentMetadataHandle metadata = new DocumentMetadataHandle();
			DocumentDescriptor desc = xmlManager.exists("/brojPropisa", t);
			int brojPropisa;
			if (desc == null) {
				brojPropisa = 0;
			} else {
				xmlManager.read("/brojPropisa", metadata, content);
				brojPropisa = Integer
						.parseInt(content.get().getElementsByTagName("brojPropisa").item(0).getTextContent());
			}
			brojPropisa++;
			// propis.setBrojPropisa(new BigInteger("" + brojPropisa));
			StringHandle stringHandle = new StringHandle("<brojPropisa>" + brojPropisa + "</brojPropisa>");
			xmlManager.write("/brojPropisa", stringHandle, t);
			// write("/propisi/" + brojPropisa, "/predlozi", propis, t);
			commitTransaction(t);
		} else {
			System.out.println("TEST");
			try {

				List<Propis> propisi = new ArrayList<>();
				JAXBHandle<Propis> handle;
				JAXBContext context = JAXBContext.newInstance(Propis.class.getPackage().getName());
				handle = new JAXBHandle<>(context);

				QueryManager queryManager = client.newQueryManager();
				StructuredQueryBuilder qb = queryManager.newStructuredQueryBuilder();

				String namespace = Propis.class.getPackage().getAnnotation(XmlSchema.class).namespace();
				StructuredQueryDefinition queryDef = qb.value(
						qb.elementAttribute(qb.element(new QName(namespace, "Propis")),
								qb.attribute(new QName(namespace, "usernameDonosioca"))),
						"odbornik");
				System.out.println(queryDef.serialize());
				SearchHandle results = queryManager.search(queryDef, new SearchHandle());
				MatchDocumentSummary[] summaries = results.getMatchResults();
				for (MatchDocumentSummary summary : summaries) {
					System.out.println(summary.getUri());

					DocumentMetadataHandle metadata = new DocumentMetadataHandle();
					read(summary.getUri(), metadata, handle);
				}
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public SearchHandle query(String query) {

		/*
		 * QueryManager queryManager = client.newQueryManager();
		 * 
		 * // Query definition is used to specify Google-style query string
		 * StringQueryDefinition queryDefinition =
		 * queryManager.newStringDefinition();
		 * queryDefinition.setCriteria(query);
		 * 
		 * return queryManager.search(queryDefinition, new DOMHandle());
		 */

		QueryManager queryManager = client.newQueryManager();
		StructuredQueryBuilder qb = queryManager.newStructuredQueryBuilder();

		StructuredQueryDefinition queryDef = qb
				.word(qb.elementAttribute(qb.element("Propis"), qb.attribute("username")), query);

		return queryManager.search(queryDef, new SearchHandle());
	}

}
