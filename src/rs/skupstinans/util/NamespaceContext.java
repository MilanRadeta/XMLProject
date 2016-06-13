package rs.skupstinans.util;

import java.util.Iterator;
import java.util.Map;

/**
 * Klasa omogućava izvršavanje namespace-aware XPath izraza. 
 *
 */
public class NamespaceContext implements javax.xml.namespace.NamespaceContext {

	private Map<String, String> prefixes;
	
	public String getNamespaceURI(String prefix) {
		
		String uri = null;

		if (prefixes.containsKey(prefix))
			uri = prefixes.get(prefix);
		return uri;
		
	}

	@Override
	public String getPrefix(String arg0) {
		return null;
	}

	@Override
	public Iterator<?> getPrefixes(String arg0) {
		return null;
	}
	
	public void setPrefixes(Map<String, String> prefixes) {
		this.prefixes = prefixes;
	}

	public NamespaceContext() { }

	public NamespaceContext(Map<String, String> prefixes) { 
		this.setPrefixes(prefixes);
	}

};