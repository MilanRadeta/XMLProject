package rs.skupstinans.service;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import rs.skupstinans.amandman.Amandmani;
import rs.skupstinans.propis.Propis;

/**
 * Session Bean implementation class RestBean
 */
@ApplicationPath("/api")
@Stateless
@LocalBean
public class RestBean implements RestBeanRemote {

	@POST
	@Path("/findBy")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Propis> findBy(String query) {
		// TODO
		return null;
	}

	@POST
	@Path("/predlogPropisa")
	@Consumes(MediaType.APPLICATION_JSON)
	public void predlogPropisa(Propis propis) {
		// TODO
	}

	@POST
	@Path("/predlogAmandmana")
	@Consumes(MediaType.APPLICATION_JSON)
	public void predlogAmandmana(Amandmani amandmani) {
		// TODO
	}

	@DELETE
	@Path("/povuciPredlogPropisa")
	@Consumes(MediaType.APPLICATION_JSON)
	public void povuciPredlogPropisa(String id) {
		// TODO
	}


	@DELETE
	@Path("/povuciPredlogAmandmana")
	@Consumes(MediaType.APPLICATION_JSON)
	public void povuciPredlogAmandmana(String id) {
		// TODO
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
