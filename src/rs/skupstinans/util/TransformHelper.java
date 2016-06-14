package rs.skupstinans.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public abstract class TransformHelper {
	private static TransformerFactory transformerFactory;
	private static FopFactory fopFactory;

	static {
		transformerFactory = TransformerFactory.newInstance();
		try {
			String FOPConfigFilepath = TransformHelper.class.getClassLoader().getResource("fop.xconf").getPath();
			fopFactory = FopFactory.newInstance(new File(FOPConfigFilepath));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void transform(Node node, OutputStream out) {
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

	public static void transformToPDF(Object obj, File file) {
		// TODO: make xsl for pdf
		try {
			String xslFilepath = TransformHelper.class.getClassLoader().getResource("PropisPDF.xsl").getPath();
			
			JAXBContext context = JAXBContext.newInstance(obj.getClass().getPackage().getName());
			JAXBSource source = new JAXBSource(context, obj);

			File xsltFile = new File(xslFilepath);
			StreamSource transformSource = new StreamSource(xsltFile);
			
			FOUserAgent userAgent = fopFactory.newFOUserAgent();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			Transformer xslFoTransformer = transformerFactory.newTransformer(transformSource);
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, outStream);
			Result res = new SAXResult(fop.getDefaultHandler());
			xslFoTransformer.transform(source, res);
			
			OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
			out.write(outStream.toByteArray());
			out.close();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (FOPException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static <T> void transformToXHTML(Object obj, OutputStream out) {
		// TODO: make xsl
		try {
			String xslFilepath = TransformHelper.class.getClassLoader().getResource("PropisXHTML.xsl").getPath();

			JAXBContext context = JAXBContext.newInstance(obj.getClass().getPackage().getName());
			JAXBSource source = new JAXBSource(context, obj);

			File xsltFile = new File(xslFilepath);
			StreamSource transformSource = new StreamSource(xsltFile);
			Transformer xslTransformer = transformerFactory.newTransformer(transformSource);
			StreamResult result = new StreamResult(System.out);
			xslTransformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}
