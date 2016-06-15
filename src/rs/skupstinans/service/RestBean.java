package rs.skupstinans.service;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
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
import rs.skupstinans.util.ElementConstants;
import rs.skupstinans.util.ElementFinder;
import rs.skupstinans.util.Query;
import rs.skupstinans.util.TransformHelper;

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

	public Stav test(Stav stav) {
		boolean test = false;
		if (test) {
			database.clearDatabase();
		} else {
			DocumentMetadataHandle metadata = new DocumentMetadataHandle();
			JAXBContext context;
			try {
				context = JAXBContext.newInstance(Propis.class.getPackage().getName());
				JAXBHandle<Propis> handle = new JAXBHandle<>(context);
				database.read("/propisi/1", metadata, handle);
				Propis propis = handle.get();
				File file = new File("C:/Test.pdf");
				TransformHelper.transformToPDF(propis, file);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
		return stav;
	}

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
						amandman.setId(
								amandmani.getReferences() + "/"
										+ (Integer
												.parseInt(amandmani.getAmandman()
														.get(amandmani.getAmandman().size() - 1).getId().split("/")[1])
												+ 1));
					}
					amandman.setUsernameDonosioca(user.getUsername());
					amandmani.getAmandman().add(amandman);
					ElementConstants.setAmandmaniNaziv(amandmani, propis);
					database.predlogAmandmana(amandmani);
				}
			} catch (JAXBException e) {
				e.printStackTrace();
			}

		} else

		{
			retVal.add("Zabranjena akcija");
		}
		return retVal;
	}

	public void povuciPredlogPropisa(@PathParam("id") String id) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null && user instanceof Odbornik) {
			database.deletePropis("/propisi/" + id, user);
		}
	}

	public void povuciPredlogAmandmana(@PathParam("id") String id, @PathParam("amendmentId") String amendmentId) {
		User user = (User) request.getSession().getAttribute("user");
		if (user != null && user instanceof Odbornik) {
			database.deleteAmendment(id, amendmentId, user);
		}
	}

	public List<Propis> nadjiSvePropise() {
		// TODO
		return null;
	}

	public List<Propis> nadjiSveAmandmane() {
		// TODO
		return null;
	}

	public void usvojiPropisUNacelu(String id) {
		// TODO
	}

	public void usvojiAmandman(String id) {
		// TODO
	}

	public void usvojiPropisUCelosti(String id) {
		// TODO
	}

	public void odbaciPrelogPropisa(String id) {
		// TODO
	}

	public void odbaciPrelogAmandmana(String id) {
		// TODO
	}

	@Override
	public void getPropisAsXML(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getPropisAsHTML(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getPropisAsPDF(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAmendmentsAsXML(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAmendmentsAsHTML(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAmendmentsAsPDF(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAmendmentAsXML(String id, String aid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAmendmentAsHTML(String id, String aid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getAmendmentAsPDF(String id, String aid) {
		// TODO Auto-generated method stub

	}

}
