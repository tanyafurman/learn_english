package edu.furman.english.utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

public class PropertyUtil {

	public static Properties loadProperty(URL url) {
		if (url == null) {
			//TODO: message
			throw new IllegalArgumentException();
		}
		Properties messages = new Properties();
		try {
			messages.load(new InputStreamReader(url.openStream()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return messages;
	}

}
