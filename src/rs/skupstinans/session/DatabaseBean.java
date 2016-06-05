package rs.skupstinans.session;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.JAXBHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;

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
		client.release();
	}

	public void write(String URI, JAXBHandle<? extends Object> handle) {
		xmlManager.write(URI, handle);
	}

	public void delete(String URI, JAXBHandle<? extends Object> handle) {
		xmlManager.delete(URI);
	}

	public void write(String URI, String collectionID, JAXBHandle<? extends Object> handle) {
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		metadata.getCollections().add(collectionID);
		xmlManager.write(URI, metadata, handle);
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
