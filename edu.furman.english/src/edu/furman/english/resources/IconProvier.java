package edu.furman.english.resources;

import javax.swing.ImageIcon;

public class IconProvier {

	public static final ImageIcon TRUE_ICON = getIcon("true.gif");
	
	public static final ImageIcon FALSE_ICON = getIcon("false.gif");

	public static final ImageIcon DEFAULT_ICON = getIcon("default.gif");

	private static ImageIcon getIcon(String name) {
		return new ImageIcon(IconProvier.class.getResource(name), name);
	}
}
