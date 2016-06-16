package rs.skupstinans.service;

import java.io.InputStream;
import java.util.List;

import javax.ejb.Remote;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import rs.skupstinans.amandman.Amandman;
import rs.skupstinans.amandman.Amandmani;
import rs.skupstinans.elementi.Stav;
import rs.skupstinans.propis.Propis;

@Remote
public interface RestBeanRemote {

	@POST
	@Path("/test")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Stav test(Stav stav);

	@GET
	@Path("/findBy")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Propis> findBy(@QueryParam("username") String username, @QueryParam("predlog") boolean predlog,
			@QueryParam("inProcedure") boolean inProcedure);

	@GET
	@Path("/findAmendmentsBy")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Amandman> findBy(@QueryParam("username") String username, @QueryParam("notUsvojen") boolean notUsvojen);

	@POST
	@Path("/predlogPropisa")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> predlogPropisa(Propis propis);

	@POST
	@Path("/predlogAmandmana/{id}")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> predlogAmandmana(@PathParam("id") int propisId, Amandman amandman);

	@DELETE
	@Path("/povuciPredlogPropisa/{id}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void povuciPredlogPropisa(@PathParam("id") String id);

	@DELETE
	@Path("/povuciPredlogAmandmana/{id}/{amendmentId}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void povuciPredlogAmandmana(@PathParam("id") String id, @PathParam("amendmentId") String amendmentId);

	@GET
	@Path("/getAmendmentsForId/{id}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Amandmani getAmendmentsForId(@PathParam("id") String id);
	
	@POST
	@Path("/usvojiPropisUNacelu")
	@Consumes(MediaType.APPLICATION_JSON)
	public void usvojiPropisUNacelu(String id);

	@POST
	@Path("/usvojiAmandman")
	@Consumes(MediaType.APPLICATION_JSON)
	public void usvojiAmandman(String id);

	@POST
	@Path("/usvojiPropisUCelosti")
	@Consumes(MediaType.APPLICATION_JSON)
	public void usvojiPropisUCelosti(String id);

	@DELETE
	@Path("/odbaciPredlogPropisa/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void odbaciPredlogPropisa(@PathParam("id") String id);

	@DELETE
	@Path("/odbaciPredlogAmandmana/")
	@Consumes(MediaType.APPLICATION_JSON)
	public void odbaciPredlogAmandmana(String id);

	@GET
	@Path("/getPropisAsXML/{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Propis getPropisAsXML(@PathParam("id") String id);

	@GET
	@Path("/getPropisAsHTML/{id}")
	@Produces(MediaType.TEXT_HTML)
	public String getPropisAsHTML(@PathParam("id") String id);

	@GET
	@Path("/getPropisAsPDF/{id}")
	@Produces("application/pdf")
	public InputStream getPropisAsPDF(@PathParam("id") String id);

	@GET
	@Path("/getAmendmentsAsXML/{id}")
	@Produces(MediaType.APPLICATION_XML)
	public Amandmani getAmendmentsAsXML(@PathParam("id") String id);

	@GET
	@Path("/getAmendmentsAsHTML/{id}")
	@Produces(MediaType.TEXT_HTML)
	public String getAmendmentsAsHTML(@PathParam("id") String id);

	@GET
	@Path("/getAmendmentsAsPDF/{id}")
	@Produces("application/pdf")
	public InputStream getAmendmentsAsPDF(@PathParam("id") String id);

	@GET
	@Path("/getAmendmentAsXML/{id}/{aid}")
	@Produces(MediaType.APPLICATION_XML)
	public Amandman getAmendmentAsXML(@PathParam("id") String id, @PathParam("aid") String aid);

	@GET
	@Path("/getAmendmentAsHTML/{id}/{aid}")
	@Produces(MediaType.TEXT_HTML)
	public String getAmendmentAsHTML(@PathParam("id") String id, @PathParam("aid") String aid);

	@GET
	@Path("/getAmendmentAsPDF/{id}/{aid}")
	@Produces("application/pdf")
	public InputStream getAmendmentAsPDF(@PathParam("id") String id, @PathParam("aid") String aid);
}
