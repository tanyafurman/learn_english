package edu.english.ui;

import java.awt.Component;
import java.awt.ScrollPane;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import edu.english.Application;
import edu.english.model.StatusModel;

public class StatusPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public StatusPanel(Application app) {
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);
		{ // create label
			JLabel tableName = new JLabel("Status");
			tableName.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.add(tableName);
		}
		{// create table
			ScrollPane scrollable = new ScrollPane();
			this.add(scrollable);
			scrollable.add(new JTable(app.getAdapter(StatusModel.class)));
		}
	}

}
