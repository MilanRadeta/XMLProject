package rs.skupstina.util;

import java.util.HashMap;
import java.util.List;

import rs.skupstinans.elementi.Clan;
import rs.skupstinans.elementi.ElementInterface;
import rs.skupstinans.elementi.Podtacka;
import rs.skupstinans.elementi.Stav;
import rs.skupstinans.elementi.Tacka;
import rs.skupstinans.propis.Preambula;
import rs.skupstinans.propis.Propis;

public class Checker {

	public static void checkString(List<String> messages, String stringToCheck, String message) {
		if (stringToCheck == null || stringToCheck.equals("")) {
			messages.add(message);
		}
	}

	public static void checkPropis(List<String> messages, Propis propis) {
		Preambula preambula = propis.getPreambula();
		Checker.checkString(messages, preambula.getPravniOsnov().getFormalniPravniOsnov(),
				"Nedostaje formalni pravni osnov u preambuli");
		Checker.checkString(messages, preambula.getPravniOsnov().getMaterijalniPravniOsnov(),
				"Nedostaje materijalni pravni osnov u preambuli");
		Checker.checkString(messages, preambula.getDonosilacPropisa().getNazivOrgana(), "Nedostaje donosilac propisa");
		if (preambula.getSaglasnost() != null) {
			Checker.checkString(messages, preambula.getSaglasnost().getNazivOrgana(),
					"Nedostaje naziv saglasnog organa");
			Checker.checkString(messages, preambula.getSaglasnost().getNaznaka(), "Nedostaje naznaka saglasnosti");
		}
		Checker.checkString(messages, propis.getNazivPropisa(), "Nedostaje naziv propisa");

		Checker.checkPropisContent(messages, propis);

		Checker.checkString(messages, propis.getObrazlozenje(), "Nedostaje obrazloženje za predlaganje propisa");
	}

	public static void checkPropisContent(List<String> messages, Propis propis) {
		// TODO: Check propis content
		if (propis.getClan().isEmpty() && propis.getDeo().isEmpty() && propis.getDeoBezNaziva().isEmpty()
				&& propis.getGlava().isEmpty()) {
			messages.add("Nedostaje sadržaj propisa");
		} else if (!propis.getClan().isEmpty()) {
			System.out.println("Propis se sastoji isključivo od članova");
			if (propis.getDeo().isEmpty() && propis.getDeoBezNaziva().isEmpty() && propis.getGlava().isEmpty()) {
				for (int i = 0; i < propis.getClan().size(); i++) {
					checkClan(messages, propis, i);
				}
			} else {
				messages.add("Raspored elemenata nije dobar");
			}
		}

		else if (!propis.getDeo().isEmpty()) {
			System.out.println("Propis se sastoji od delova");
			if (propis.getClan().isEmpty() && propis.getDeoBezNaziva().isEmpty() && propis.getGlava().isEmpty()) {
				// TODO
			} else {
				messages.add("Raspored elemenata nije dobar");
			}
		} else if (!propis.getDeoBezNaziva().isEmpty()) {
			System.out.println("Propis se sastoji od delova bez naziva");
			if (propis.getDeo().isEmpty() && propis.getClan().isEmpty() && propis.getGlava().isEmpty()) {
				// TODO
			} else {
				messages.add("Raspored elemenata nije dobar");
			}
		} else if (!propis.getGlava().isEmpty()) {
			System.out.println("Propis se sastoji od glava");
			if (propis.getDeo().isEmpty() && propis.getClan().isEmpty() && propis.getDeoBezNaziva().isEmpty()) {
				// TODO
			} else {
				messages.add("Raspored elemenata nije dobar");
			}
		}

	}

	public static void checkClan(List<String> messages, Propis propis, int i) {
		Clan clan = propis.getClan().get(i);
		checkString(messages, clan.getNaziv(), String.format("Članu %s. nedostaje naziv", i + 1));
		if (clan.getStav().isEmpty()) {
			messages.add(String.format("Članu %s. nedostaju stavovi", i + 1));
		} else {
			for (int j = 0; j < clan.getStav().size(); j++) {
				checkSubElements(messages, clan.getStav().get(j), String.format("Član %s. Stav %s.", i + 1, j + 1));
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static void checkSubElements(List<String> messages, Object parent, String messagePrefix) {
		ElementInterface element = (ElementInterface) parent;
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
					checkSubElements(messages, obj, String.format("%s %s %s.", messagePrefix, obj.getClass().getSimpleName(), viewIndexes.get(cl)));
				}
			}
		}
	}

	public static void checkTextElements(List<String> messages, Object parent, String messagePrefix) {
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

	public static void checkTacka(List<String> messages, Stav stav, int i, int tackaCount, String messagePrefix) {
		Tacka tacka = (Tacka) stav.getContent().get(i);
		if (tacka.getContent().isEmpty()) {
			messages.add(String.format("%s tački %s. nedostaje sadržaj", messagePrefix, tackaCount));
		} else {
			int podtackaCount = 0;
			for (int j = 0; j < tacka.getContent().size(); j++) {
				Object obj = tacka.getContent().get(j);
				if (obj instanceof Podtacka) {
					checkPodtacka(messages, tacka, j, ++podtackaCount,
							String.format("%s u tački %s", messagePrefix, i));
				} else if (!(obj instanceof String)) {
					/*
					 * TODO:
					 * 
					 * @XmlElementRef(name = "Tacka", namespace =
					 * "http://www.skupstinans.rs/elementi", type = Tacka.class,
					 * required = false),
					 * 
					 * @XmlElementRef(name = "SkraceniNaziv", namespace =
					 * "http://www.skupstinans.rs/elementi", type =
					 * JAXBElement.class, required = false),
					 * 
					 * @XmlElementRef(name = "Referenca", namespace =
					 * "http://www.skupstinans.rs/elementi", type =
					 * Referenca.class, required = false),
					 * 
					 * @XmlElementRef(name = "Datum", namespace =
					 * "http://www.skupstinans.rs/elementi", type =
					 * JAXBElement.class, required = false),
					 * 
					 * @XmlElementRef(name = "StrucniIzraz", namespace =
					 * "http://www.skupstinans.rs/elementi", type =
					 * JAXBElement.class, required = false),
					 * 
					 * @XmlElementRef(name = "StranaRec", namespace =
					 * "http://www.skupstinans.rs/elementi", type =
					 * JAXBElement.class, required = false)
					 */
				}
			}
		}
	}

	public static void checkPodtacka(List<String> messages, Tacka tacka, int i, int podtackaCount,
			String messagePrefix) {

	}

}
