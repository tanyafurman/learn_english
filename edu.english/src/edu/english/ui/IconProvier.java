package edu.english.ui;

import javax.swing.ImageIcon;
/**
 * Класс IconProvier предназначен для отображения иконок слева от слова во время прохождения теста.<br>
 * <br>
 * Данный класс содержит три изображения, которые дают знать пользователю результат своего овета:<br>
 * 1) иконка "галочка" означает правильный ответ.<br>
 * 2) иконка "крестик" означает неправильный ответ.<br>
 * 3) иконка "прочерк" означает то, что пользователь не дал никакого ответа.<br>
 */

public class IconProvier {

	public static final ImageIcon TRUE_ICON = getIcon("plus.png");
	
	public static final ImageIcon FALSE_ICON = getIcon("cross.png");

	public static final ImageIcon DEFAULT_ICON = getIcon("minus.png");

	private static ImageIcon getIcon(String name) {
		return new ImageIcon(new ImageIcon(IconProvier.class.getResource(name), name).getImage().getScaledInstance(32, 32, java.awt.Image.SCALE_DEFAULT));
	}
}
