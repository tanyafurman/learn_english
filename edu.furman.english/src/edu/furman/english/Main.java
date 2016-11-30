package edu.furman.english;

import java.awt.Dimension;

import javax.swing.JFrame;

import edu.furman.english.ui.ApplicationFrame;

public class Main {

	public static void main(String[] args) {
		ApplicationFrame frame = new ApplicationFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setMinimumSize(new Dimension(300, 300));
		frame.setVisible(true);
	}

}
