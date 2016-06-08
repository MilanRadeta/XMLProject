package rs.skupstina.util;

import java.util.HashMap;
import java.util.List;

import rs.skupstinans.elementi.Clan;
import rs.skupstinans.elementi.Deo;
import rs.skupstinans.elementi.ElementInterface;
import rs.skupstinans.elementi.Glava;
import rs.skupstinans.elementi.Odeljak;
import rs.skupstinans.elementi.Pododeljak;
import rs.skupstinans.propis.Preambula;
import rs.skupstinans.propis.Propis;

public class Checker {

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

	public void checkPropis(List<String> messages, Propis propis) {
		Preambula preambula = propis.getPreambula();
		checkString(messages, preambula.getPravniOsnov().getFormalniPravniOsnov(),
				"Nedostaje formalni pravni osnov u preambuli");
		checkString(messages, preambula.getPravniOsnov().getMaterijalniPravniOsnov(),
				"Nedostaje materijalni pravni osnov u preambuli");
		checkString(messages, preambula.getDonosilacPropisa().getNaziv(), "Nedostaje donosilac propisa");
		if (preambula.getSaglasnost() != null) {
			checkString(messages, preambula.getSaglasnost().getNaziv(), "Nedostaje naziv saglasnog organa");
			checkString(messages, preambula.getSaglasnost().getNaznaka(), "Nedostaje naznaka saglasnosti");
		}
		checkString(messages, propis.getNaziv(), "Nedostaje naziv propisa");

		checkPropisContent(messages, propis);

		checkString(messages, propis.getObrazlozenje(), "Nedostaje obrazloženje za predlaganje propisa");
	}

	public void checkPropisContent(List<String> messages, Propis propis) {
		if (propis.getClan().isEmpty() && propis.getDeo().isEmpty() && propis.getGlava().isEmpty()) {
			messages.add("Nedostaje sadržaj propisa");
		} else if (!propis.getClan().isEmpty()) {
			System.out.println("Propis se sastoji isključivo od članova");
			if (propis.getDeo().isEmpty() && propis.getGlava().isEmpty()) {
				if (propis.getClan().size() < 20) {
					messages.add("Propis može da se sastoji isključivo od članova ako ih ima manje od 20");
				}
				for (int i = 0; i < propis.getClan().size(); i++) {
					checkClan(messages, propis.getClan().get(i), "Član " + clanCounter + ".", false);
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
				if (propis.getDeo().size() == 1) {
					messages.add("Propis može da se sastoji od barem 2 glave");
				}
				for (int i = 0; i < propis.getGlava().size(); i++) {
					checkGroup(messages, propis.getGlava().get(i), String.format("Glava %s.", i + 1));
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
				checkGroup(messages, deo.getGlava().get(j), String.format("%s Glava %s.", messagePrefix, j + 1));
			}
		}
	}

	public void checkGroup(List<String> messages, Object group, String messagePrefix) {
		ElementInterface element = (ElementInterface) group;
		if (group instanceof Glava) {
			glavaCounter++;
			odeljakCounter = 0;
			if (deoCounter == 0) {
				element.setId(String.format("g%s", glavaCounter));	
			}	
			else {
				element.setId(String.format("d%s/%s", deoCounter, glavaCounter));
			}
		}
		else if (group instanceof Odeljak) {
			odeljakCounter++;
			pododeljakCounter = 0;
			if (deoCounter == 0) {
				element.setId(String.format("g%s/%s", glavaCounter, odeljakCounter));	
			}	
			else {
				element.setId(String.format("d%s/%s/%s", deoCounter, glavaCounter, odeljakCounter));
			}
			
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
							innerClanCounter == 1);
				} else if (obj instanceof Odeljak) {
					odeljakCounter++;
					checkGroup(messages, obj, String.format("%s Odeljak %s.", messagePrefix, odeljakCounter));
				} else if (obj instanceof Pododeljak) {
					pododeljakCounter++;
					checkGroup(messages, obj, String.format("%s Pododeljak %s.", messagePrefix, pododeljakCounter));
				}
			}
			if (!isClanFirst) {
				if (odeljakCounter == 1) {
					messages.add(String.format("%s - mora da se sastoji od barem 2 odeljka ili od članova i odeljaka",
							messagePrefix));
				} else if (pododeljakCounter == 1) {
					messages.add(
							String.format("%s - mora da se sastoji od barem 2 pododeljka ili od članova i pododeljaka",
									messagePrefix));
				}
			}
		}
	}

	public void checkPododeljak(List<String> messages, Pododeljak pododeljak, String messagePrefix) {
		pododeljakCounter++;
		if (deoCounter == 0) {
			pododeljak.setId(String.format("g%s/%s/%s", glavaCounter, odeljakCounter, pododeljakCounter));	
		}	
		else {
			pododeljak.setId(String.format("d%s/%s/%s/%s", deoCounter, glavaCounter, odeljakCounter, pododeljakCounter));
		}
		checkString(messages, pododeljak.getNaziv(), String.format("%s - nedostaje naziv", messagePrefix));
		if (pododeljak.getClan().isEmpty()) {
			messages.add(String.format("%s - nedostaju članovi", messagePrefix));
		} else {
			for (Clan clan : pododeljak.getClan()) {
				checkClan(messages, clan, String.format("%s Član %s.", messagePrefix, clanCounter),
						pododeljak.getClan().size() == 1);
			}
		}
	}

	public void checkClan(List<String> messages, Clan clan, String messagePrefix, boolean withoutNaziv) {
		clanCounter++;
		stavCounter = 0;
		clan.setId("cl" + clanCounter);
		if (withoutNaziv) {
			clan.setNaziv(null);
		} else {
			checkString(messages, clan.getNaziv(), String.format("%s - nedostaje naziv", messagePrefix));
		}
		if (clan.getStav().isEmpty()) {
			messages.add(String.format("%s - nedostaju stavovi", messagePrefix));
		} else {
			for (int j = 0; j < clan.getStav().size(); j++) {
				checkSubElements(messages, clan.getStav().get(j), String.format("%s. Stav %s.", messagePrefix, j + 1));
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void checkSubElements(List<String> messages, Object parent, String messagePrefix) {
		ElementInterface element = (ElementInterface) parent;
		switch (parent.getClass().getSimpleName()) {
		case "Stav":
			tackaCounter = 0;
			stavCounter++;
			element.setId(String.format("cl%s/%s", clanCounter, stavCounter));
			break;
		case "Tacka":
			podtackaCounter = 0;
			tackaCounter++;
			element.setId(String.format("cl%s/%s/%s", clanCounter, stavCounter, tackaCounter));
			break;
		case "Podtacka":
			alinejaCounter = 0;
			podtackaCounter++;
			element.setId(String.format("cl%s/%s/%s/%s", clanCounter, stavCounter, tackaCounter, podtackaCounter));
			break;
		case "Alineja":
			alinejaCounter++;
			element.setId(String.format("cl%s/%s/%s/%s/%s", clanCounter, stavCounter, tackaCounter, podtackaCounter, alinejaCounter));
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
							obj.getClass().getSimpleName(), viewIndexes.get(cl)));
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
}
