package rs.skupstinans.service;

import java.util.List;

import javax.ejb.Remote;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import rs.skupstinans.amandman.Amandmani;
import rs.skupstinans.propis.Propis;

@Remote
public interface RestBeanRemote {

	@POST
	@Path("/findBy")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Propis> findBy(String query);

	@POST
	@Path("/predlogPropisa")
	@Consumes(MediaType.APPLICATION_JSON)
	public void predlogPropisa(Propis propis);

	@POST
	@Path("/predlogAmandmana")
	@Consumes(MediaType.APPLICATION_JSON)
	public void predlogAmandmana(Amandmani amandmani);

	@GET
	@Path("/nadjiSvePropise")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Propis> nadjiSvePropise();

	@GET
	@Path("/nadjiSveAmandmane")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Propis> nadjiSveAmandmane();
}
