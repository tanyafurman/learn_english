package edu.english.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import edu.english.Application;
import edu.english.data.Word2Translate;
import edu.english.model.AbstractWordsModel;
import edu.english.model.KnownWordsModel;
import edu.english.model.UnknownWordsModel;
import edu.english.model.VocabularyModel;

public class UserVocabularyPane extends JPanel {

	private static final long serialVersionUID = 1L;

	Application app;

	public UserVocabularyPane(Application app) {
		this.app = app;
		setLayout(new GridLayout(0, 3, 5, 5));

		this.add(createKnownWordsPanel(app.getAdapter(KnownWordsModel.class),"Known words"));
		this.add(new AbstractWordsPanel(app.getAdapter(UnknownWordsModel.class), "Unknown words"));
		this.add(createKnownWordsPanel(app.getAdapter(VocabularyModel.class), "Vocabulary"));
	}

	private AbstractWordsPanel createKnownWordsPanel(AbstractWordsModel model, String name) {
		AbstractWordsPanel panel = new AbstractWordsPanel(model, name);
		JTable table = panel.getTable();
		JPopupMenu popup = createPopup(table);
		addSetAsUnknownAction(table, popup);
		table.setComponentPopupMenu(popup);
		return panel;
	}

	private void addSetAsUnknownAction(JTable table, JPopupMenu menu) {
		JMenuItem item = new JMenuItem("Set as unknown");
		menu.add(item);
		item.setAction(new AbstractAction("Set As Unknown") {
			
			private static final long serialVersionUID = -8590761989680241408L;

			@Override
			public void actionPerformed(ActionEvent e) {
				int[] rows = table.getSelectedRows();
				if (rows == null) return;
				
				for (int row: rows) {
					Word2Translate word = new Word2Translate((String)table.getModel().getValueAt(row, 0), (String)table.getModel().getValueAt(row, 1));
					app.addUnknownWord(word);
				}
			}
		});
	}

	private static JPopupMenu createPopup(JTable table) {
		JPopupMenu popup = new JPopupMenu();
		table.add(popup);
		return popup;
	}
}
