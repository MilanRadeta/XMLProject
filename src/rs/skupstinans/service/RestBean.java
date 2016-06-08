package rs.skupstinans.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import rs.skupstinans.amandman.Amandmani;
import rs.skupstinans.elementi.Clan;
import rs.skupstinans.elementi.Stav;
import rs.skupstinans.elementi.Tacka;
import rs.skupstinans.propis.Preambula;
import rs.skupstinans.propis.Propis;

/**
 * Session Bean implementation class RestBean
 */
@Stateless
@LocalBean
@Path("/act")
public class RestBean implements RestBeanRemote {
	
	@POST
	@Path("/test")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_XML)
	public Stav test(Stav stav) {
		System.out.println(stav);
		System.out.println(stav.getContent());
		return stav;
	}
	
	@POST
	@Path("/findBy")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Propis> findBy(String query) {
		// TODO
		return null;
	}

	private void checkString(List<String> messages, String stringToCheck, String message) {
		if (stringToCheck == null || stringToCheck.equals("")) {
			messages.add(message);
		}
	}
	
	private void checkClan(List<String> messages, Propis propis, int i) {
		Clan clan = propis.getClan().get(i);
		checkString(messages, clan.getNaziv(), String.format("Članu %s. nedostaje naziv", i+1));
		if (clan.getStav().isEmpty()) {
			messages.add(String.format("Članu %s. nedostaju stavovi", i+1));
		}
		else {
			for (int j = 0; j < clan.getStav().size(); j++) {
				checkStav(messages, clan, j, String.format("U članu %s.", i));
			}
		}
	}
	
	private void checkStav(List<String> messages, Clan clan, int i, String messagePrefix) {
		Stav stav = clan.getStav().get(i);
		if (stav.getContent().isEmpty()) {
			messages.add(String.format("%s stavu %s. nedostaje sadržaj", messagePrefix, i+1));
		}
		else {
			int tackaCount = 0;
			for (int j = 0; j < stav.getContent().size(); j++) {
				Object obj = stav.getContent().get(j);
				if (obj instanceof Tacka) {
					checkTacka(messages, stav, j, ++tackaCount, String.format("%s u stavu %s", messagePrefix, i));
				}
				else if (!(obj instanceof String)) {
					/*	TODO:
					        @XmlElementRef(name = "Tacka", namespace = "http://www.skupstinans.rs/elementi", type = Tacka.class, required = false),
					        @XmlElementRef(name = "SkraceniNaziv", namespace = "http://www.skupstinans.rs/elementi", type = JAXBElement.class, required = false),
					        @XmlElementRef(name = "Referenca", namespace = "http://www.skupstinans.rs/elementi", type = Referenca.class, required = false),
					        @XmlElementRef(name = "Datum", namespace = "http://www.skupstinans.rs/elementi", type = JAXBElement.class, required = false),
					        @XmlElementRef(name = "StrucniIzraz", namespace = "http://www.skupstinans.rs/elementi", type = JAXBElement.class, required = false),
					        @XmlElementRef(name = "StranaRec", namespace = "http://www.skupstinans.rs/elementi", type = JAXBElement.class, required = false)
					*/
				}
			}
		}
	}
	
	private void checkTacka(List<String> messages, Stav stav, int i, int tackaCount, String messagePrefix) {
		// TODO:
	}
	
	private void checkPropisContent(List<String> messages, Propis propis) {
		// TODO: Check propis content
		if (propis.getClan().isEmpty() && propis.getDeo().isEmpty() && propis.getDeoBezNaziva().isEmpty() && propis.getGlava().isEmpty()) {
			messages.add("Nedostaje sadržaj propisa");
		}
		else if (!propis.getClan().isEmpty()) {
			System.out.println("Propis se sastoji isključivo od članova");
			if (propis.getDeo().isEmpty() && propis.getDeoBezNaziva().isEmpty() && propis.getGlava().isEmpty()) {
				for (int i = 0; i < propis.getClan().size(); i++) {
					checkClan(messages, propis, i);
				}
			}
			else {
				messages.add("Raspored elemenata nije dobar");
			}
		}

		else if (!propis.getDeo().isEmpty()) {
			System.out.println("Propis se sastoji od delova");
			if (propis.getClan().isEmpty() && propis.getDeoBezNaziva().isEmpty() && propis.getGlava().isEmpty()) {
				
			}
			else {
				messages.add("Raspored elemenata nije dobar");
			}
		}
		else if (!propis.getDeoBezNaziva().isEmpty()) {
			System.out.println("Propis se sastoji od delova bez naziva");
			if (propis.getDeo().isEmpty() && propis.getClan().isEmpty() && propis.getGlava().isEmpty()) {
				
			}
			else {
				messages.add("Raspored elemenata nije dobar");
			}
		}
		else if (!propis.getGlava().isEmpty()) {
			System.out.println("Propis se sastoji od glava");
			if (propis.getDeo().isEmpty() && propis.getClan().isEmpty() && propis.getDeoBezNaziva().isEmpty()) {
				
			}
			else {
				messages.add("Raspored elemenata nije dobar");
			}
		}
		
	}
	
	@POST
	@Path("/predlogPropisa")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> predlogPropisa(Propis propis) {
		List<String> retVal = new ArrayList<String>();
		Preambula preambula = propis.getPreambula();
		checkString(retVal, preambula.getPravniOsnov().getFormalniPravniOsnov(), "Nedostaje formalni pravni osnov u preambuli");
		checkString(retVal, preambula.getPravniOsnov().getMaterijalniPravniOsnov(), "Nedostaje materijalni pravni osnov u preambuli");
		checkString(retVal, preambula.getDonosilacPropisa().getNazivOrgana(), "Nedostaje donosilac propisa");
		if (preambula.getSaglasnost() != null) {
			checkString(retVal, preambula.getSaglasnost().getNazivOrgana(), "Nedostaje naziv saglasnog organa");
			checkString(retVal, preambula.getSaglasnost().getNaznaka(), "Nedostaje naznaka saglasnosti");
		}
		checkString(retVal, propis.getNazivPropisa(), "Nedostaje naziv propisa");
		
		checkPropisContent(retVal, propis);
		
		checkString(retVal, propis.getObrazlozenje(), "Nedostaje obrazloženje za predlaganje propisa");
		return retVal;
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
