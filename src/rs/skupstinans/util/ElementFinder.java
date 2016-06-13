package rs.skupstinans.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.dom.DOMResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rs.skupstinans.amandman.Amandman;
import rs.skupstinans.amandman.Amandmani;
import rs.skupstinans.propis.Propis;

public class ElementFinder {
	
	public static Object findPropisElementById(String id, Propis propis) {
		Map<String, String> namespaceMappings = new HashMap<String, String>();
		namespaceMappings.put("elem", "http://www.skupstinans.rs/elementi");
		namespaceMappings.put("p", "http://www.skupstinans.rs/propisi");
		return findElement("//*[@elem:id='" + id + "']", propis, namespaceMappings).get(0);
	}

	public static List<Amandman> findAmendmentByUsername(String username, Amandmani amandmani) {
		Map<String, String> namespaceMappings = new HashMap<String, String>();
		namespaceMappings.put("elem", "http://www.skupstinans.rs/elementi");
		namespaceMappings.put("am", "http://www.skupstinans.rs/amandmani");
		return findElement("//*[@elem:usernameDonosioca='" + username + "']", amandmani, namespaceMappings);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> findElement(String expression, Object object, Map<String, String> namespaceMappings) {
		List<T> retVal = new ArrayList<>();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(object.getClass().getPackage().getName());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			DOMResult res = new DOMResult();

			marshaller.marshal(object, res);
			Document doc = (Document) res.getNode();

			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			xPath.setNamespaceContext(new NamespaceContext(namespaceMappings));

			XPathExpression xPathExpression;
			xPathExpression = xPath.compile(expression);

			NodeList nodeList = (NodeList) xPathExpression.evaluate(doc, XPathConstants.NODESET);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Node node;
			
			for (int i = 0; i < nodeList.getLength(); i++) {
				node = nodeList.item(i);
				retVal.add((T) unmarshaller.unmarshal(node));	
			}

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}

		return retVal;
	}
}
