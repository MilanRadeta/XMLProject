package rs.skupstinans.session;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.dom.DOMResult;

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
import com.marklogic.client.query.StructuredQueryBuilder;
import com.marklogic.client.query.StructuredQueryDefinition;

import rs.skupstinans.amandman.Amandman;
import rs.skupstinans.amandman.Amandmani;
import rs.skupstinans.propis.DonosilacPropisa;
import rs.skupstinans.propis.Preambula;
import rs.skupstinans.propis.Propis;
import rs.skupstinans.users.User;
import rs.skupstinans.util.Checker;
import rs.skupstinans.util.ConnectionProperties;
import rs.skupstinans.util.ElementConstants;
import rs.skupstinans.util.ElementFinder;
import rs.skupstinans.util.Query;

/**
 * Session Bean implementation class DatabaseBean
 */
@Stateless
@LocalBean
public class DatabaseBean {

	@EJB
	private Checker checker;

	private DatabaseClient client;
	private XMLDocumentManager xmlManager;

	private List<Transaction> transactions = new ArrayList<>();

	private static int transactionCounter = 0;

	@PostConstruct
	void init() {
		ConnectionProperties props;
		try {
			props = ConnectionProperties.loadProperties();
			client = DatabaseClientFactory.newClient(props.host, props.port, props.database, props.user, props.password,
					props.authType);
			xmlManager = client.newXMLDocumentManager();
		} catch (IOException e) {
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

	public void predlogAmandmana(Amandmani amandmani) {
		Transaction t = createTransaction();
		predlogAmandmana(amandmani, t);
		commitTransaction(t);
	}

	public void predlogAmandmana(Amandmani amandmani, Transaction t) {
		write("/amandmani/" + amandmani.getReferences(), amandmani, t);
	}

	private JAXBHandle<Propis> getPropisHandle(Propis propis) {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(propis.getClass().getPackage().getName());
			JAXBHandle<Propis> handle = new JAXBHandle<>(context);
			handle.set(propis);
			return handle;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	private JAXBHandle<Amandmani> getAmandmaniHandle(Amandmani amandmani) {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(amandmani.getClass().getPackage().getName());
			JAXBHandle<Amandmani> handle = new JAXBHandle<>(context);
			handle.set(amandmani);
			return handle;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void delete(String URI) {
		xmlManager.delete(URI);
	}

	public void delete(String URI, Transaction t) {
		xmlManager.delete(URI, t);
	}

	public void deletePropis(String URI, User user) {
		Transaction t = createTransaction();
		try {
			JAXBContext context = JAXBContext.newInstance(Propis.class.getPackage().getName());
			JAXBHandle<Propis> handle = new JAXBHandle<>(context);
			DocumentMetadataHandle metadata = new DocumentMetadataHandle();
			read(URI, metadata, handle, t);
			Propis propis = handle.get();
			if (user == null || propis.getUsernameDonosioca().equals(user.getUsername())) {
				if (!propis.getStatus().equals("usvojen u celosti")) {
					delete(URI, t);
					delete("/amandmani/" + propis.getBrojPropisa(), t);
				}
			}
			commitTransaction(t);
		} catch (JAXBException e) {
			rollbackTransaction(t);
			e.printStackTrace();
		}
	}

	public void deletePropis(String URI) {
		deletePropis(URI, null);
	}

	public void deleteAmendment(String propisId, String amendmentId, User user) {

		Transaction t = createTransaction();
		try {
			JAXBContext context = JAXBContext.newInstance(Amandmani.class.getPackage().getName());
			JAXBHandle<Amandmani> handle = new JAXBHandle<>(context);
			DocumentMetadataHandle metadata = new DocumentMetadataHandle();
			read("/amandmani/" + propisId, metadata, handle, t);
			Amandmani amandmani = handle.get();
			for (Amandman amandman : amandmani.getAmandman()) {
				if (amandman.getId().equals(propisId + "/" + amendmentId)) {
					if (amandman.getUsernameDonosioca().equals(user.getUsername())) {
						if (!amandman.isUsvojen()) {
							amandmani.getAmandman().remove(amandman);
							context = JAXBContext.newInstance(Propis.class.getPackage().getName());
							JAXBHandle<Propis> propisHandle = new JAXBHandle<>(context);
							read("/propisi/" + propisId, metadata, propisHandle, t);
							ElementConstants.setAmandmaniNaziv(amandmani, propisHandle.get());
							predlogAmandmana(amandmani, t);
						}
					}
					break;
				}
			}

			commitTransaction(t);
		} catch (JAXBException e) {
			rollbackTransaction(t);
			e.printStackTrace();
		}
	}

	public void write(String URI, Propis propis) {
		Transaction t = createTransaction();
		xmlManager.write(URI, getPropisHandle(propis), t);
		commitTransaction(t);
	}

	public void write(String URI, Propis propis, Transaction t) {
		xmlManager.write(URI, getPropisHandle(propis), t);
	}

	public void write(String URI, Amandmani amandmani) {
		Transaction t = createTransaction();
		write(URI, amandmani, t);
		commitTransaction(t);
	}

	public void write(String URI, Amandmani amandmani, Transaction t) {
		xmlManager.write(URI, getAmandmaniHandle(amandmani), t);
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

	public boolean exists(String URI) {
		return xmlManager.exists(URI) != null;
	}

	public void read(String URI, DocumentMetadataHandle metadata, JAXBHandle<? extends Object> handle) {
		xmlManager.read(URI, metadata, handle);
	}

	public void read(String URI, DocumentMetadataHandle metadata, JAXBHandle<? extends Object> handle, Transaction t) {
		xmlManager.read(URI, metadata, handle, t);
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

	public void clearDatabase() {
		Transaction t = createTransaction();
		DocumentDescriptor desc = xmlManager.exists("/brojPropisa", t);
		if (desc != null) {
			xmlManager.delete(desc, t);
		}
		for (int i = 1; i <= 6; i++) {
			desc = xmlManager.exists("/propisi/" + i, t);
			if (desc != null) {
				xmlManager.delete(desc, t);
			}
			desc = xmlManager.exists("/amandmani/" + i, t);
			if (desc != null) {
				xmlManager.delete(desc, t);
			}
		}
		commitTransaction(t);
	}

	public SearchHandle query(Query query) {
		return query(query, null);
	}

	public SearchHandle query(Query query, String collection) {
		QueryManager queryManager = client.newQueryManager();
		StructuredQueryBuilder qb = queryManager.newStructuredQueryBuilder();
		StructuredQueryDefinition queryDef = query.createQueryDefinition(qb);

		if (collection != null) {
			queryDef.setCollections(collection);
		}

		if (queryDef != null) {
			return queryManager.search(queryDef, new SearchHandle());
		}
		return null;
	}

	public Amandmani findAmendmentsForPropis(Propis propis) {
		Amandmani retVal = null;
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(Amandmani.class.getPackage().getName());
			JAXBHandle<Amandmani> handle = new JAXBHandle<>(context);
			DocumentMetadataHandle metadata = new DocumentMetadataHandle();
			DocumentDescriptor desc = xmlManager.exists("/amandmani/" + propis.getBrojPropisa());
			if (desc == null) {
				retVal = new Amandmani();
				retVal.setReferences("" + propis.getBrojPropisa());
				retVal.setNaziv("Predlozi amandmana na " + propis.getNaziv());
			} else {
				read("/amandmani/" + propis.getBrojPropisa(), metadata, handle);
				retVal = handle.get();
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return retVal;
	}

	public void acceptActGenerally(String propisId) {
		try {
			Transaction t = createTransaction();
			JAXBContext context = JAXBContext.newInstance(Propis.class.getPackage().getName());
			JAXBHandle<Propis> handle = new JAXBHandle<>(context);
			DocumentMetadataHandle metadata = new DocumentMetadataHandle();
			read("/propisi/" + propisId, metadata, handle, t);
			Propis propis = handle.get();
			propis.setStatus("usvojen u nacelu");
			write("/propisi/" + propisId, propis, t);
			commitTransaction(t);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public void acceptActWithAmendments(String propisId, List<String> amandmani) {
		try {
			Transaction t = createTransaction();
			DocumentMetadataHandle metadata = new DocumentMetadataHandle();
			JAXBContext context = JAXBContext.newInstance(Amandmani.class.getPackage().getName());
			JAXBHandle<Amandmani> aHandle = new JAXBHandle<>(context);
			read("/amandmani/" + propisId, metadata, aHandle, t);
			Amandmani aDoc = aHandle.get();
			delete("/amandmani/" + propisId, t);
			context = JAXBContext.newInstance(Propis.class.getPackage().getName());
			JAXBHandle<Propis> handle = new JAXBHandle<>(context);
			read("/propisi/" + propisId, metadata, handle, t);
			Propis propis = handle.get();
			List<Amandman> toRemove = new ArrayList<>();
			for (Amandman a : aDoc.getAmandman()) {
				if (!amandmani.contains(a.getId())) {
					toRemove.add(a);
				} else {
					a.setUsvojen(true);
				}
			}
			aDoc.getAmandman().removeAll(toRemove);
			propis = primeniAmandmane(propis, aDoc);
			ArrayList<String> messages = new ArrayList<>();
			checker.checkPropis(messages, propis);
			if (messages.size() > 0) {
				rollbackTransaction(t);
				return;
			}
			propis.setStatus("usvojen u pojedinostima");
			write("/propisi/" + propisId, propis, t);
			commitTransaction(t);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public Propis primeniAmandmane(Propis propis, Amandmani amandmani) {
		try {
			JAXBContext amandmanContext = JAXBContext.newInstance(amandmani.getClass().getPackage().getName());
			Marshaller marshaller = amandmanContext.createMarshaller();

			DOMResult res = new DOMResult();

			marshaller.marshal(amandmani, res);
			Document amandmanDoc = (Document) res.getNode();

			JAXBContext propisContext = JAXBContext.newInstance(propis.getClass().getPackage().getName());
			marshaller = propisContext.createMarshaller();

			res = new DOMResult();

			marshaller.marshal(propis, res);
			Document propisDoc = (Document) res.getNode();

			for (Amandman am : amandmani.getAmandman()) {
				Node propisNode = ElementFinder.findNode("//*[@elem:id='" + am.getReferences() + "']", propisDoc);
				Node amNode = ElementFinder.findNode("//*[@elem:id='" + am.getId() + "']", amandmanDoc);
				for (int i = 0; i < amNode.getChildNodes().getLength(); i++) {
					Node childNode = amNode.getChildNodes().item(i);
					if (childNode.getNodeType() == Node.ELEMENT_NODE) {
						switch (childNode.getLocalName()) {
						case "Dopuna":
						case "Izmena":
							for (int j = 0; j < childNode.getChildNodes().getLength(); j++) {
								Node grandchildNode = childNode.getChildNodes().item(j);
								if (grandchildNode.getNodeType() == Node.ELEMENT_NODE) {
									if (childNode.getLocalName().equals("Dopuna")) {
										propisNode.getParentNode().insertBefore(
												propisDoc.importNode(grandchildNode, true),
												propisNode.getNextSibling());
									} else {
										propisNode.getParentNode()
												.replaceChild(propisDoc.importNode(grandchildNode, true), propisNode);
									}
									break;
								}
							}
							break;
						case "Brisanje":
							propisNode.getParentNode().removeChild(propisNode);
						}
						break;
					}
				}
			}
			Unmarshaller unmarshaller = propisContext.createUnmarshaller();
			propis = (Propis) unmarshaller.unmarshal(propisDoc);
			return propis;
			/*
			 * TODO: primeni dopune i izmene nad propis documentom konvertuj u
			 * propis objekat checkiraj propis uzimajući u obzir dopune primeni
			 * brisanje
			 * 
			 * NOTE: treba zbog referenci NOTE: preskociti, to bi trebalo da se
			 * kontroliše amandmanima
			 */

		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void acceptAct(String propisId) {
		try {
			Transaction t = createTransaction();
			String url = "/amandmani/" + propisId;
			if (exists(url)) {
				delete(url, t);
			}
			JAXBContext context = JAXBContext.newInstance(Propis.class.getPackage().getName());
			JAXBHandle<Propis> handle = new JAXBHandle<>(context);
			DocumentMetadataHandle metadata = new DocumentMetadataHandle();
			read("/propisi/" + propisId, metadata, handle, t);
			Propis propis = handle.get();
			Preambula preambula = new Preambula();
			preambula.setPravniOsnov(propis.getObrazlozenje());
			DonosilacPropisa donosilac = new DonosilacPropisa();
			donosilac.setNaziv("Skupština grada Novog Sada");
			preambula.setDonosilacPropisa(donosilac);
			propis.setPreambula(preambula);
			propis.setObrazlozenje(null);
			propis.setStatus("usvojen u celosti");
			write("/propisi/" + propisId, "/usvojeni", propis, t);
			commitTransaction(t);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public void test(String propisId) {
		try {
			Transaction t = createTransaction();
			JAXBContext context = JAXBContext.newInstance(Propis.class.getPackage().getName());
			JAXBHandle<Propis> handle = new JAXBHandle<>(context);
			DocumentMetadataHandle metadata = new DocumentMetadataHandle();
			read("/propisi/" + propisId, metadata, handle, t);
			Propis propis = handle.get();
			propis.setStatus("predlog");
			write("/propisi/" + propisId, propis, t);
			commitTransaction(t);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}
