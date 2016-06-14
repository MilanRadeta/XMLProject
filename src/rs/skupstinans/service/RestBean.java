package rs.skupstinans.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.JAXBHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.MatchDocumentSummary;

import rs.skupstinans.amandman.Amandman;
import rs.skupstinans.amandman.Amandmani;
import rs.skupstinans.elementi.Stav;
import rs.skupstinans.propis.Propis;
import rs.skupstinans.session.DatabaseBean;
import rs.skupstinans.users.Odbornik;
import rs.skupstinans.users.User;
import rs.skupstinans.util.Checker;
import rs.skupstinans.util.ElementFinder;
import rs.skupstinans.util.Query;
import rs.skupstinans.util.TransformPrinter;

/**
 * Session Bean implementation class RestBean
 */
@Stateless
@LocalBean
@Path("/act")
public class RestBean implements RestBeanRemote {

	@Context
	private HttpServletRequest request;

	@EJB
	private DatabaseBean database;

	@EJB
	private Checker checker;

	@POST
	@Path("/test")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Stav test(Stav stav) {
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(Propis.class.getPackage().getName());
			JAXBHandle<Propis> handle = new JAXBHandle<>(context);
			database.read("/propisi/1", metadata, handle);
			Propis propis = handle.get();
			File file = new File("C:/Test.html");
			OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
			TransformPrinter.transformToXHTML(propis, out);
			out.close();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stav;
	}

	@GET
	@Path("/findBy")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Propis> findBy(@QueryParam("username") String username, @QueryParam("predlog") boolean predlog,
			@QueryParam("inProcedure") boolean inProcedure) {
		List<Propis> propisi = new ArrayList<>();
		Query query = new Query();
		query.setPredlog(predlog);
		query.setInProcedure(inProcedure);

		User user = (User) request.getSession().getAttribute("user");
		if (user != null && user instanceof Odbornik && user.getUsername().equals(username)) {
			query.setUsername(username);
		}
		try {
			JAXBContext context = JAXBContext.newInstance(Propis.class.getPackage().getName());
			JAXBHandle<Propis> handle = new JAXBHandle<>(context);
			DocumentMetadataHandle metadata = new DocumentMetadataHandle();
			SearchHandle results = database.query(query);
			if (results != null) {
				MatchDocumentSummary[] summaries = results.getMatchResults();
				for (MatchDocumentSummary summary : summaries) {
					database.read(summary.getUri(), metadata, handle);
					propisi.add(handle.get());
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return propisi;
	}

	@GET
	@Path("/findAmendmentsBy")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Amandman> findBy(@QueryParam("username") String username,
			@QueryParam("notUsvojen") boolean notUsvojen) {
		List<Amandman> retVal = new ArrayList<>();

		Query query = new Query();
		query.setType(Query.AMANDMAN);
		query.setNotUsvojen(notUsvojen);
		User user = (User) request.getSession().getAttribute("user");
		if (user != null && user instanceof Odbornik && user.getUsername().equals(username)) {
			query.setUsername(username);
		}
		try {
			JAXBContext context = JAXBContext.newInstance(Amandmani.class.getPackage().getName());
			JAXBHandle<Amandmani> handle = new JAXBHandle<>(context);
			SearchHandle results = database.query(query);
			if (results != null) {
				MatchDocumentSummary[] summaries = results.getMatchResults();
				for (MatchDocumentSummary summary : summaries) {
					DocumentMetadataHandle metadata = new DocumentMetadataHandle();
					database.read(summary.getUri(), metadata, handle);
					retVal.addAll(ElementFinder.findAmendmentByUsername(username, handle.get()));
				}
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return retVal;
	}

	@POST
	@Path("/predlogPropisa")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> predlogPropisa(Propis propis) {
		List<String> retVal = new ArrayList<String>();
		User user = (User) request.getSession().getAttribute("user");
		if (user != null && user instanceof Odbornik) {
			checker.checkPropis(retVal, propis);
			propis.setPreciscen(false);
			propis.setBrojPropisa(new BigInteger("1"));
			propis.setStatus("predlog");
			propis.setUsernameDonosioca(user.getUsername());
			GregorianCalendar gcal = new GregorianCalendar();
			XMLGregorianCalendar xgcal;
			try {
				xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
				propis.setDatumPredlaganjaPropisa(xgcal);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
			if (retVal.size() == 0) {
				checker.validate(retVal, propis);
				if (retVal.size() == 0) {
					database.predlogPropisa(propis);
				}
			}
		} else {
			retVal.add("Zabranjena akcija");
		}
		return retVal;
	}

	@POST
	@Path("/predlogAmandmana/{id}")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> predlogAmandmana(@PathParam("id") int propisId, Amandman amandman) {
		List<String> retVal = new ArrayList<String>();
		User user = (User) request.getSession().getAttribute("user");
		if (user != null && user instanceof Odbornik) {
			amandman.setId(propisId + "/1");

			try {
				JAXBContext context = JAXBContext.newInstance(Propis.class.getPackage().getName());
				JAXBHandle<Propis> handle = new JAXBHandle<>(context);
				DocumentMetadataHandle metadata = new DocumentMetadataHandle();
				database.read("/propisi/" + propisId, metadata, handle);
				Propis propis = handle.get();
				checker.checkAmendment(retVal, amandman, propis);
				if (retVal.size() == 0) {
					checker.validate(retVal, amandman);
					Amandmani amandmani = database.findAmendmentsForPropis(propis);
					if (!amandmani.getAmandman().isEmpty()) {
						amandman.setId(amandmani.getReferences() + "/"
								+ (Integer.parseInt(amandmani.getAmandman().get(amandmani.getAmandman().size() - 1).getId().split("/")[1]) + 1));
					}
					amandman.setUsernameDonosioca(user.getUsername());
					amandmani.getAmandman().add(amandman);
					database.predlogAmandmana(amandmani);
				}
			} catch (JAXBException e) {
				e.printStackTrace();
			}

		} else {
			retVal.add("Zabranjena akcija");
		}
		return retVal;
	}

	@DELETE
	@Path("/povuciPredlogPropisa/{id}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void povuciPredlogPropisa(@PathParam("id") String id) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null && user instanceof Odbornik) {
			database.deletePropis("/propisi/" + id, user);
		}
	}

	@DELETE
	@Path("/povuciPredlogAmandmana/{id}/{amendmentId}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void povuciPredlogAmandmana(@PathParam("id") String id, @PathParam("amendmentId") String amendmentId) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null && user instanceof Odbornik) {
			database.deleteAmendment(id, amendmentId, user);
		}
	}

	@GET
	@Path("/nadjiSvePropise")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Propis> nadjiSvePropise() {
		// TODO
		return null;
	}

	@GET
	@Path("/nadjiSveAmandmane")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Propis> nadjiSveAmandmane() {
		// TODO
		return null;
	}

	@POST
	@Path("/usvojiPropisUNacelu")
	@Consumes(MediaType.APPLICATION_JSON)
	public void usvojiPropisUNacelu(String id) {
		// TODO
	}

	@POST
	@Path("/usvojiAmandman")
	@Consumes(MediaType.APPLICATION_JSON)
	public void usvojiAmandman(String id) {
		// TODO
	}

	@POST
	@Path("/usvojiPropisUCelosti")
	@Consumes(MediaType.APPLICATION_JSON)
	public void usvojiPropisUCelosti(String id) {
		// TODO
	}

	@DELETE
	@Path("/odbaciPrelogPropisa")
	@Consumes(MediaType.APPLICATION_JSON)
	public void odbaciPrelogPropisa(String id) {
		// TODO
	}

	@DELETE
	@Path("/odbaciPrelogAmandmana")
	@Consumes(MediaType.APPLICATION_JSON)
	public void odbaciPrelogAmandmana(String id) {
		// TODO
	}

}
