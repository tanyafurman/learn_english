package edu.furman.english.resources;

import java.net.URL;
import java.util.Properties;

import edu.furman.english.utils.PropertyUtil;

public class Internalizer {

	private Properties messages;

	public Internalizer(String lang) {
		URL messagesURL = this.getClass().getResource("messages_" + lang + ".properties");
		messages = PropertyUtil.loadProperty(messagesURL);
	}

	public String getMessage(String massageName) {
		return messages.getProperty(massageName, "");
	}
}
