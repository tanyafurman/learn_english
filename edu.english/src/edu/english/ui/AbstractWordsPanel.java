package edu.english.ui;

import java.awt.Component;
import java.awt.ScrollPane;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import edu.english.model.AbstractWordsModel;

public class AbstractWordsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTable table;

	public <T extends AbstractWordsModel> AbstractWordsPanel(T model, String name) {
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);

		JLabel tableName = new JLabel(name);
		tableName.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(tableName);

		ScrollPane scrollable = new ScrollPane();
		add(scrollable);
		table = new JTable(model);
		scrollable.add(table);
	}

	public JTable getTable() {
		return table;
	}
}
