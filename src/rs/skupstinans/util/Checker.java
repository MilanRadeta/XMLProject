package rs.skupstinans.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import rs.skupstinans.amandman.Amandman;
import rs.skupstinans.amandman.Brisanje;
import rs.skupstinans.amandman.Dopuna;
import rs.skupstinans.amandman.Izmena;
import rs.skupstinans.elementi.Alineja;
import rs.skupstinans.elementi.Clan;
import rs.skupstinans.elementi.Deo;
import rs.skupstinans.elementi.ElementInterface;
import rs.skupstinans.elementi.Glava;
import rs.skupstinans.elementi.Odeljak;
import rs.skupstinans.elementi.Pododeljak;
import rs.skupstinans.elementi.Podtacka;
import rs.skupstinans.elementi.Stav;
import rs.skupstinans.elementi.Tacka;
import rs.skupstinans.propis.Propis;
import rs.skupstinans.session.DatabaseBean;

@Stateless
@LocalBean
public class Checker {

	@EJB
	private DatabaseBean database;

	private int clanCounter = 0;
	private int deoCounter = 0;
	private int glavaCounter = 0;
	private int odeljakCounter = 0;
	private int pododeljakCounter = 0;
	private int stavCounter = 0;
	private int tackaCounter = 0;
	private int podtackaCounter = 0;
	private int alinejaCounter = 0;

	public void checkString(List<String> messages, String stringToCheck, String message) {
		if (stringToCheck == null || stringToCheck.trim().equals("")) {
			messages.add(message);
		}
	}

	private void resetCounters() {
		clanCounter = 0;
		deoCounter = 0;
		glavaCounter = 0;
		odeljakCounter = 0;
		pododeljakCounter = 0;
		stavCounter = 0;
		tackaCounter = 0;
		podtackaCounter = 0;
		alinejaCounter = 0;
	}

	public void checkPropis(List<String> messages, Propis propis) {
		resetCounters();
		/* TODO: ovo treba da se vrši samo kada se propis usvaja u celini
		Preambula preambula = propis.getPreambula();
		checkString(messages, preambula.getPravniOsnov(), "Nedostaje pravni osnov u preambuli");
		checkString(messages, preambula.getDonosilacPropisa().getNaziv(), "Nedostaje donosilac propisa");
		if (preambula.getSaglasnost() != null) {
			checkString(messages, preambula.getSaglasnost().getNaziv(), "Nedostaje naziv saglasnog organa");
			checkString(messages, preambula.getSaglasnost().getNaznaka(), "Nedostaje naznaka saglasnosti");
		}*/
		checkString(messages, propis.getNaziv(), "Nedostaje naziv propisa");

		checkPropisContent(messages, propis);

		checkString(messages, propis.getObrazlozenje(), "Nedostaje obrazloženje za predlaganje propisa");
	}

	public void checkPropisContent(List<String> messages, Propis propis) {
		if (propis.getClan().isEmpty() && propis.getDeo().isEmpty() && propis.getGlava().isEmpty()) {
			messages.add("Propis mora da se sastoji od �?lanova, delova ili glava.");
		} else if (!propis.getClan().isEmpty()) {
			System.out.println("Propis se sastoji isklju�?ivo od �?lanova");
			if (propis.getDeo().isEmpty() && propis.getGlava().isEmpty()) {
				if (propis.getClan().size() >= 20) {
					messages.add("Propis može da se sastoji isklju�?ivo od �?lanova ako ih ima manje od 20");
				}
				for (int i = 0; i < propis.getClan().size(); i++) {
					checkClan(messages, propis.getClan().get(i), "Član " + (clanCounter + 1) + ".", false, "");
				}
			} else {
				messages.add("Raspored elemenata nije dobar");
			}
		}

		else if (!propis.getDeo().isEmpty()) {
			System.out.println("Propis se sastoji od delova");
			if (propis.getClan().isEmpty() && propis.getGlava().isEmpty()) {
				boolean withoutNaziv = false;
				if (propis.getDeo().size() == 1) {
					messages.add("Propis može da se sastoji od barem 2 dela");
				}
				for (int i = 0; i < propis.getDeo().size(); i++) {
					Deo deo = propis.getDeo().get(i);
					if (i == 0) {
						withoutNaziv = deo.getNaziv().equals("");
					} else if (withoutNaziv != deo.getNaziv().equals("")) {
						withoutNaziv = false;
					}
				}
				for (int i = 0; i < propis.getDeo().size(); i++) {
					checkDeo(messages, propis.getDeo().get(i), withoutNaziv, String.format("Deo %s.", i + 1));
				}
			} else {
				messages.add("Raspored elemenata nije dobar");
			}
		} else if (!propis.getGlava().isEmpty()) {
			System.out.println("Propis se sastoji od glava");
			if (propis.getDeo().isEmpty() && propis.getClan().isEmpty()) {
				if (propis.getGlava().size() == 1) {
					messages.add("Propis može da se sastoji od barem 2 glave");
				}
				for (int i = 0; i < propis.getGlava().size(); i++) {
					checkGroup(messages, propis.getGlava().get(i), String.format("Glava %s.", i + 1), "");
				}
			} else {
				messages.add("Raspored elemenata nije dobar");
			}
		}

	}

	public void checkDeo(List<String> messages, Deo deo, boolean withoutNaziv, String messagePrefix) {
		deoCounter++;
		glavaCounter = 0;
		deo.setId("d" + deoCounter);
		if (withoutNaziv) {
			deo.setNaziv(null);
		} else {
			checkString(messages, deo.getNaziv(), String.format("%s - nedostaje naziv", messagePrefix));
		}
		if (deo.getGlava().isEmpty()) {
			messages.add(String.format("%s - nedostaju glave", messagePrefix));
		} else {
			if (deo.getGlava().size() == 1) {
				messages.add(String.format("%s - deo mora da se sastoji od barem 2 glave", messagePrefix));
			}
			for (int j = 0; j < deo.getGlava().size(); j++) {
				checkGroup(messages, deo.getGlava().get(j), String.format("%s Glava %s.", messagePrefix, j + 1),
						deo.getId() + "/");
			}
		}
	}

	public void checkGroup(List<String> messages, Object group, String messagePrefix, String idPrefix) {
		ElementInterface element = (ElementInterface) group;
		if (group instanceof Glava) {
			glavaCounter++;
			odeljakCounter = 0;
			element.setId(String.format("%sg%s", idPrefix, glavaCounter));
		} else if (group instanceof Odeljak) {
			odeljakCounter++;
			pododeljakCounter = 0;
			element.setId(String.format("%so%s", idPrefix, odeljakCounter));
		}
		if (element.getContent().isEmpty()) {
			messages.add(String.format("%s - nedostaje sadržaj", messagePrefix));
		} else {
			checkString(messages, (String) element.getContent().get(0),
					String.format("%s - nedostaje naziv", messagePrefix));
			int odeljakCounter = 0;
			int pododeljakCounter = 0;
			boolean isClanFirst = false;
			int innerClanCounter = 0;
			for (int i = 0; i < element.getContent().size(); i++) {
				if (element.getContent().get(i) instanceof Clan) {
					innerClanCounter++;
				}
			}
			for (int i = 0; i < element.getContent().size(); i++) {
				Object obj = element.getContent().get(i);
				if (obj instanceof Clan) {
					if (i == 0) {
						isClanFirst = true;
					}
					checkClan(messages, (Clan) obj, String.format("%s Član %s.", messagePrefix, clanCounter),
							innerClanCounter == 1, element.getId() + "/");
				} else if (obj instanceof Odeljak) {
					odeljakCounter++;
					checkGroup(messages, obj, String.format("%s Odeljak %s.", messagePrefix, odeljakCounter),
							element.getId() + "/");
				} else if (obj instanceof Pododeljak) {
					pododeljakCounter++;
					checkPododeljak(messages, (Pododeljak) obj,
							String.format("%s Pododeljak %s.", messagePrefix, pododeljakCounter),
							element.getId() + "/");
				}
			}
			if (!isClanFirst) {
				if (odeljakCounter == 1) {
					messages.add(String.format("%s - mora da se sastoji od barem 2 odeljka ili od �?lanova i odeljaka",
							messagePrefix));
				} else if (pododeljakCounter == 1) {
					messages.add(
							String.format("%s - mora da se sastoji od barem 2 pododeljka ili od �?lanova i pododeljaka",
									messagePrefix));
				}
			}
		}
	}

	public void checkPododeljak(List<String> messages, Pododeljak pododeljak, String messagePrefix, String idPrefix) {
		pododeljakCounter++;
		pododeljak.setId(String.format("%sp%s", idPrefix, pododeljakCounter));
		checkString(messages, pododeljak.getNaziv(), String.format("%s - nedostaje naziv", messagePrefix));
		if (pododeljak.getClan().isEmpty()) {
			messages.add(String.format("%s - nedostaju �?lanovi", messagePrefix));
		} else {
			for (Clan clan : pododeljak.getClan()) {
				checkClan(messages, clan, String.format("%s Član %s.", messagePrefix, clanCounter),
						pododeljak.getClan().size() == 1, pododeljak.getId() + "/");
			}
		}
	}

	public void checkClan(List<String> messages, Clan clan, String messagePrefix, boolean withoutNaziv,
			String idPrefix) {
		clanCounter++;
		stavCounter = 0;
		clan.setId(String.format("%sc%s", idPrefix, clanCounter));
		if (withoutNaziv) {
			clan.setNaziv(null);
		} else {
			checkString(messages, clan.getNaziv(), String.format("%s - nedostaje naziv", messagePrefix));
		}
		if (clan.getStav().isEmpty()) {
			messages.add(String.format("%s - nedostaju stavovi", messagePrefix));
		} else {
			for (int j = 0; j < clan.getStav().size(); j++) {
				checkSubElements(messages, clan.getStav().get(j), String.format("%s. Stav %s.", messagePrefix, j + 1),
						clan.getId() + "/");
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void checkSubElements(List<String> messages, Object parent, String messagePrefix, String idPrefix) {
		ElementInterface element = (ElementInterface) parent;
		switch (parent.getClass().getSimpleName()) {
		case "Stav":
			tackaCounter = 0;
			stavCounter++;
			element.setId(String.format("%ss%s", idPrefix, stavCounter));
			break;
		case "Tacka":
			podtackaCounter = 0;
			tackaCounter++;
			element.setId(String.format("%sT%s", idPrefix, tackaCounter));
			break;
		case "Podtacka":
			alinejaCounter = 0;
			podtackaCounter++;
			element.setId(String.format("%st%s", idPrefix, podtackaCounter));
			break;
		case "Alineja":
			alinejaCounter++;
			element.setId(String.format("%sa%s", idPrefix, alinejaCounter));
			break;
		}
		if (element.getContent().isEmpty()) {
			messages.add(String.format("%s - nedostaje sadržaj", messagePrefix));
		} else {
			HashMap<Class, Integer> viewIndexes = new HashMap<>();
			for (int i = 0; i < element.getContent().size(); i++) {
				Object obj = element.getContent().get(i);
				Class cl = obj.getClass();
				if (!viewIndexes.containsKey(cl)) {
					viewIndexes.put(cl, 0);
				}
				viewIndexes.put(cl, viewIndexes.get(cl) + 1);
				switch (cl.getSimpleName()) {
				case "SkraceniNaziv":
				case "Referenca":
				case "Datum":
				case "StrucniIzraz":
				case "StranaRec":
					checkTextElements(messages, obj, messagePrefix);
					break;
				case "String":
					if (i == 0 && ((String) obj).trim().equals("")) {
						messages.add(String.format("%s - nedostaje sadržaj", messagePrefix));
					}
					break;
				default:
					if (i == 0) {
						messages.add(String.format("%s - nedostaje sadržaj", messagePrefix));
					}
					checkSubElements(messages, obj, String.format("%s %s %s.", messagePrefix,
							obj.getClass().getSimpleName(), viewIndexes.get(cl)), element.getId() + "/");
				}
			}
			for (Class cl : viewIndexes.keySet()) {
				switch (cl.getSimpleName()) {
				case "SkraceniNaziv":
				case "Referenca":
				case "Datum":
				case "StrucniIzraz":
				case "StranaRec":
				case "String":
					break;
				default:
					if (viewIndexes.get(cl) == 1) {
						messages.add(String.format("%s može da se deli na barem 2 podelementa tipa %s", messagePrefix,
								cl.getSimpleName()));
					}
				}
			}
		}
	}

	public void checkTextElements(List<String> messages, Object parent, String messagePrefix) {
		/*
		 * TODO:
		 * 
		 * @XmlElementRef(name = "SkraceniNaziv", namespace =
		 * "http://www.skupstinans.rs/elementi", type = JAXBElement.class,
		 * required = false),
		 * 
		 * @XmlElementRef(name = "Referenca", namespace =
		 * "http://www.skupstinans.rs/elementi", type = Referenca.class,
		 * required = false),
		 * 
		 * @XmlElementRef(name = "Datum", namespace =
		 * "http://www.skupstinans.rs/elementi", type = JAXBElement.class,
		 * required = false),
		 * 
		 * @XmlElementRef(name = "StrucniIzraz", namespace =
		 * "http://www.skupstinans.rs/elementi", type = JAXBElement.class,
		 * required = false),
		 * 
		 * @XmlElementRef(name = "StranaRec", namespace =
		 * "http://www.skupstinans.rs/elementi", type = JAXBElement.class,
		 * required = false)
		 */
	}

	@SuppressWarnings("unchecked")
	public void checkAmendment(List<String> messages, Amandman amandman, Propis propis) {
		resetCounters();
		checkString(messages, amandman.getReferences(), "Nedostaje referenca");
		String references = amandman.getReferences();
		if (references != null && references.length() > 0) {
			if (references.contains(" ")) {
				messages.add("Može da postoji samo jedna referenca");
			}
		}
		if (amandman.getContent().size() == 0) {
			messages.add("Nedostaje tim izmene");
			messages.add("Nedostaje obrazloženje za predlaganje amandmana");
		} else {
			if (amandman.getContent().size() == 2) {
				for (Object obj : amandman.getContent()) {
					switch (obj.getClass().getSimpleName()) {
					case "Dopuna":
					case "Izmena":
					case "Brisanje":
						checkAmandmanContent(messages, obj, propis, ElementFinder.findPropisElementById(references, propis));
						break;
					case "JAXBElement":
						checkString(messages, ((JAXBElement<String>) obj).getValue(), "Nedostaje obrazloženje");
					}
				}
			} else {
				Object obj = amandman.getContent().get(0);
				if (obj instanceof Dopuna || obj instanceof Izmena || obj instanceof Brisanje) {
					messages.add("Nedostaje obrazloženje za predlaganje amandmana");
				} else {
					messages.add("Nedostaje tim izmene");
				}
			}
		}
	}

	public void checkAmandmanContent(List<String> messages, Object parent, Propis propis, Object references) {
		if (references != null) {
			if (parent instanceof Izmena) {
				Izmena izmena = (Izmena) parent;
				if (izmena.getClan() != null) {
					if (izmena.getStav() == null && izmena.getTacka() == null && izmena.getPodtacka() == null
							&& izmena.getAlineja() == null) {
						if (references instanceof Clan) {
							boolean withoutNaziv = ((Clan) references).getNaziv() == null;
							checkClan(messages, izmena.getClan(), "Član", withoutNaziv, "");
						} else {
							messages.add("Članom se može izmeniti samo drugi član");
						}
					} else {
						messages.add("Pogrešno formatiran predlog");
					}
				} else if (izmena.getStav() != null) {
					if (izmena.getClan() == null && izmena.getTacka() == null && izmena.getPodtacka() == null
							&& izmena.getAlineja() == null) {
						if (references instanceof Stav) {
							checkSubElements(messages, izmena.getStav(), "Stav", "");
						} else {
							messages.add("Stavom se može izmeniti samo drugi stav");
						}
					} else {
						messages.add("Pogrešno formatiran predlog");
					}

				} else if (izmena.getTacka() != null) {
					if (izmena.getStav() == null && izmena.getClan() == null && izmena.getPodtacka() == null
							&& izmena.getAlineja() == null) {
						if (references instanceof Tacka) {
							checkSubElements(messages, izmena.getTacka(), "Tačka", "");
						} else {
							messages.add("Tačkom se može izmeniti samo druga tačka");
						}
					} else {
						messages.add("Pogrešno formatiran predlog");
					}

				} else if (izmena.getPodtacka() != null) {
					if (izmena.getStav() == null && izmena.getTacka() == null && izmena.getClan() == null
							&& izmena.getAlineja() == null) {
						if (references instanceof Podtacka) {
							checkSubElements(messages, izmena.getPodtacka(), "Podtačka", "");
						} else {
							messages.add("Podtačkom se može izmeniti samo druga podtačka");
						}
					} else {
						messages.add("Pogrešno formatiran predlog");
					}

				} else if (izmena.getAlineja() != null) {
					if (izmena.getStav() == null && izmena.getTacka() == null && izmena.getPodtacka() == null
							&& izmena.getClan() == null) {
						if (references instanceof Alineja) {
							checkSubElements(messages, izmena.getAlineja(), "Alineja", "");
						} else {
							messages.add("Alinejom se može izmeniti samo druga alineja");
						}
					} else {
						messages.add("Pogrešno formatiran predlog");
					}
				} else {
					messages.add("Nedostaje sadržaj izmene");
				}
			} else if (parent instanceof Dopuna) {

				Dopuna dopuna = (Dopuna) parent;
				if (dopuna.getClan() != null) {
					if (dopuna.getStav() == null && dopuna.getDeo() == null && dopuna.getGlava() == null
							&& dopuna.getOdeljak() == null && dopuna.getPododeljak() == null) {
						if (references instanceof Clan) {
							if (((Clan) references).getNaziv() == null) {
								messages.add(
										"Da bi se dodao novi član, član nakon koga se dodaje se mora izmeniti da ima naziv");
							} else {
								boolean withoutNaziv = false;
								checkClan(messages, dopuna.getClan(), "Član", withoutNaziv, "");
							}
						} else {
							messages.add("Novi član se može dodati jedino posle člana");
						}
					} else {
						messages.add("Pogrešno formatiran predlog");
					}
				} else if (dopuna.getStav() != null) {
					if (dopuna.getClan() == null && dopuna.getDeo() == null && dopuna.getGlava() == null
							&& dopuna.getOdeljak() == null && dopuna.getPododeljak() == null) {
						if (references instanceof Stav) {
							checkSubElements(messages, dopuna.getStav(), "Stav", "");
						} else {
							messages.add("Novi stav se može dodati jedino posle stava");
						}
					} else {
						messages.add("Pogrešno formatiran predlog");
					}

				} else if (dopuna.getDeo() != null) {
					if (dopuna.getStav() == null && dopuna.getClan() == null && dopuna.getGlava() == null
							&& dopuna.getOdeljak() == null && dopuna.getPododeljak() == null) {
						if (references instanceof Deo) {
							boolean withoutNaziv = false;
							withoutNaziv = propis.getDeo().get(0).getNaziv() == null;
							checkDeo(messages, dopuna.getDeo(), withoutNaziv, "Deo");
						} else {
							messages.add("Novi deo se može dodati jedino posle dela");
						}
					} else {
						messages.add("Pogrešno formatiran predlog");
					}

				} else if (dopuna.getGlava() != null) {
					if (dopuna.getStav() == null && dopuna.getDeo() == null && dopuna.getClan() == null
							&& dopuna.getOdeljak() == null && dopuna.getPododeljak() == null) {
						if (references instanceof Glava) {
							checkGroup(messages, dopuna.getGlava(), "Glava", "");
						} else {
							messages.add("Nova glava se može dodati jedino posle glave");
						}
					} else {
						messages.add("Pogrešno formatiran predlog");
					}
				} else if (dopuna.getOdeljak() != null) {
					if (dopuna.getStav() == null && dopuna.getDeo() == null && dopuna.getGlava() == null
							&& dopuna.getClan() == null && dopuna.getPododeljak() == null) {
						if (references instanceof Odeljak) {
							checkGroup(messages, dopuna.getOdeljak(), "Odeljak", "");
						} else {
							messages.add("Novi odeljak se može dodati jedino posle odeljka");
						}
					} else {
						messages.add("Pogrešno formatiran predlog");
					}
				} else if (dopuna.getPododeljak() != null) {
					if (dopuna.getStav() == null && dopuna.getDeo() == null && dopuna.getGlava() == null
							&& dopuna.getOdeljak() == null && dopuna.getClan() == null) {
						if (references instanceof Pododeljak) {
							checkPododeljak(messages, dopuna.getPododeljak(), "Pododeljak", "");
						} else {
							messages.add("Novi podeljak se može dodati jedino posle podeljka");
						}
					} else {
						messages.add("Pogrešno formatiran predlog");
					}
				} else {
					messages.add("Nedostaje sadržaj dopune");
				}
			}
		} else {
			messages.add("Referenca nije validna");
		}
	}
	

	public void validate(List<String> messages, Propis propis) {
		try {
			JAXBContext context = JAXBContext.newInstance(Propis.class.getPackage().getName());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			String path = getClass().getClassLoader().getResource("Propis.xsd").getPath();
			Schema schema = schemaFactory.newSchema(new File(path));
			marshaller.setSchema(schema);
			marshaller.setEventHandler(new PropisValidationEventHandler(messages));

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	

	public void validate(List<String> messages, Amandman amandman) {
		try {
			JAXBContext context = JAXBContext.newInstance(Amandman.class.getPackage().getName());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			String path = getClass().getClassLoader().getResource("Amandman.xsd").getPath();
			Schema schema = schemaFactory.newSchema(new File(path));
			marshaller.setSchema(schema);
			marshaller.setEventHandler(new PropisValidationEventHandler(messages));

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
}
