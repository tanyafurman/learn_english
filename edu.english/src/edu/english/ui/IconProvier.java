package edu.english.ui;

import javax.swing.ImageIcon;

public class IconProvier {

	public static final ImageIcon TRUE_ICON = getIcon("plus.png");
	
	public static final ImageIcon FALSE_ICON = getIcon("cross.png");

	public static final ImageIcon DEFAULT_ICON = getIcon("minus.png");

	private static ImageIcon getIcon(String name) {
		return new ImageIcon(new ImageIcon(IconProvier.class.getResource(name), name).getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_DEFAULT));
	}
}
