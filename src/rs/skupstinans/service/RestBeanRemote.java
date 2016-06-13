package rs.skupstinans.service;

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
	public List<Amandman> findBy(@QueryParam("username") String username,
			@QueryParam("notUsvojen") boolean notUsvojen);

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
	@Path("/nadjiSvePropise")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Propis> nadjiSvePropise();

	@GET
	@Path("/nadjiSveAmandmane")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Propis> nadjiSveAmandmane();

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
	@Path("/odbaciPrelogPropisa")
	@Consumes(MediaType.APPLICATION_JSON)
	public void odbaciPrelogPropisa(String id);

	@DELETE
	@Path("/odbaciPrelogAmandmana")
	@Consumes(MediaType.APPLICATION_JSON)
	public void odbaciPrelogAmandmana(String id);
}
