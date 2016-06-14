package rs.skupstinans.util;

import java.util.HashMap;

import rs.skupstinans.amandman.Amandmani;
import rs.skupstinans.propis.Propis;

public class ElementConstants {
	public final static String[] Deo = { "prvi", "drugi", "treći", "četvrti", "peti", "šesti", "sedmi", "osmi",
			"deveti", "deseti" };
	public final static String[] Glava = { "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X" };
	public final static String[] Pododeljak = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j" };
	public final static String[] Amandman = { "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII",
			"XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX" };
	public final static String[] Parts = { "Deo", "Glava", "Odeljak", "Pododeljak", "Član", "Stav", "Tačka", "Podtačka",
			"Alineja" };

	public static void setAmandmaniNaziv(Amandmani amandmani, Propis propis) {
		HashMap<String, Integer> typesCount = ElementFinder.getTypesCount(amandmani);
		int dopunaCount = typesCount.get("Dopuna");
		int izmenaCount = typesCount.get("Izmena");
		izmenaCount += typesCount.get("Brisanje");
		switch (dopunaCount) {
		case 0:
			switch (izmenaCount) {
			case 0:
				break;
			case 1:
				amandmani.setNaziv(propis.getNaziv() + " - Propis o izmeni");
				break;
			default:
				amandmani.setNaziv(propis.getNaziv() + " - Propis o izmenama");
				break;
			}
			break;
		case 1:
			switch (izmenaCount) {
			case 0:
				amandmani.setNaziv(propis.getNaziv() + " - Propis o dopuni");
				break;
			case 1:
				amandmani.setNaziv(propis.getNaziv() + " - Propis o izmeni i dopuni");
				break;
			default:
				amandmani.setNaziv(propis.getNaziv() + " - Propis o izmenama i dopuni");
				break;

			}
			break;
		default:
			switch (izmenaCount) {
			case 0:
				amandmani.setNaziv(propis.getNaziv() + " - Propis o dopunama");
				break;
			case 1:
				amandmani.setNaziv(propis.getNaziv() + " - Propis o izmeni i dopunama");
				break;
			default:
				amandmani.setNaziv(propis.getNaziv() + " - Propis o izmenama i dopunama");
				break;

			}
			break;
		}
	}

	public static String getNameFromId(String id) {
		String formatString = "";
		int partIndex = -1;
		String[] splits = id.split("/");
		for (int index = 0; index < splits.length; index++) {
			String word = splits[index];
			if (index == 0) {
				switch (word.charAt(0)) {
				case 'd':
					formatString += "Deo " + Deo[Integer.parseInt(word.substring(1)) - 1];
					partIndex = 0;
					break;
				case 'g':
					formatString += "Glava " + Glava[Integer.parseInt(word.substring(1)) - 1] + ".";
					partIndex = 1;
					break;
				case 'c':
					formatString += "Član " + word.substring(1) + ".";
					partIndex = 4;
					break;
				}
			} else {
				if (word.startsWith("c")) {
					formatString = "Član " + word.substring(1) + ".";
				} else {
					formatString += " " + Parts[partIndex + index] + " " + splits[index].substring(1) + ".";
				}
			}
		}
		return formatString;
	}
}
