package rs.skupstinans.util;

import java.util.List;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

public class PropisValidationEventHandler implements ValidationEventHandler {

	private List<String> messages;
	
	public PropisValidationEventHandler(List<String> messages) {
		super();
		this.messages = messages;
	}
	
	public boolean handleEvent(ValidationEvent event) {

		messages.add("Greška pri validaciji: " + event.getMessage());
		
		// Ako nije u pitanju WARNING metoda vraća false
		if (event.getSeverity() != ValidationEvent.WARNING) {
			ValidationEventLocator validationEventLocator = event.getLocator();
			System.out.println("ERROR: Line "
					+ validationEventLocator.getLineNumber() + ": Col"
					+ validationEventLocator.getColumnNumber() + ": "
					+ event.getMessage());
			
			
			// Dalje izvršavanje se prekida
			return false;
		} else {
			ValidationEventLocator validationEventLocator = event.getLocator();
			System.out.println("WARNING: Line "
					+ validationEventLocator.getLineNumber() + ": Col"
					+ validationEventLocator.getColumnNumber() + ": "
					+ event.getMessage());
			
			// Nastavlja se dalje izvršavanje
			return true;
		}
	}

}