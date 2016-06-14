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

import rs.skupstinans.amandman.Amandman;
import rs.skupstinans.propis.Propis;

public abstract class TransformPrinter {
	// TODO: rename

	private static TransformerFactory transformerFactory;
	private static FopFactory fopFactory;

	static {
		transformerFactory = TransformerFactory.newInstance();
		/*
		try {
			// TODO: get resource
			fopFactory = FopFactory.newInstance(new File("src/fop.xconf"));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
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

	public static void transformToPDF(OutputStream out) {
		// TODO: parametrize
		// TODO: change filepaths
		try {
			// TODO: get resource
			File xsltFile = new File("data/xsl-fo/bookstore_fo.xsl");
			StreamSource transformSource = new StreamSource(xsltFile);
			// TODO: get resource
			StreamSource source = new StreamSource(new File("data/xsl-fo/bookstore.xml"));
			FOUserAgent userAgent = fopFactory.newFOUserAgent();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			Transformer xslFoTransformer = transformerFactory.newTransformer(transformSource);
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, outStream);
			Result res = new SAXResult(fop.getDefaultHandler());
			xslFoTransformer.transform(source, res);
			// TODO: get resource
			File pdfFile = new File("gen/bookstore_result.pdf");
			out = new BufferedOutputStream(new FileOutputStream(pdfFile));
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
		}
	}

	public static <T> void transformToXHTML(Object obj, OutputStream out) {
		try {
			String xslFilepath = ""; 
			if (obj instanceof Propis) {
				xslFilepath = TransformPrinter.class.getClassLoader().getResource("PropisXHTML.xsl").getPath();
			}
			else if (obj instanceof Amandman) {	
				xslFilepath = TransformPrinter.class.getClassLoader().getResource("AmandmanXHTML.xsl").getPath();
			} 

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
