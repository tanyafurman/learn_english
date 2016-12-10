package edu.english.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import edu.english.Application;
import edu.english.data.Word2Translate;
import edu.english.ui.actions.CreateUserAction;
import edu.english.ui.actions.LoginAction;
import edu.english.ui.actions.UpdateUserSettings;

public class ApplicationFrame extends JFrame {

	public static void main(String[] args) {
		ApplicationFrame frame = new ApplicationFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setMinimumSize(new Dimension(300, 300));
		frame.setVisible(true);
	}

	private static final long serialVersionUID = 1L;

	private Application application;

	private JMenu userMenu;

	public ApplicationFrame() {
		createMenu(this);
	}

	public void setApplication(Application app) {
		this.application = app;
		JTabbedPane tabbed = new JTabbedPane(3);
		createContent(app, tabbed);
		this.setContentPane(tabbed);

		if (userMenu == null) {
			getJMenuBar().add(createUserActions());
		}

		this.revalidate();
	}

	public Application getApplication() {
		return application;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (application != null) {
			application.dispose();
		}
	}

	private void createMenu(JFrame parent) {
		JMenuBar bar = new JMenuBar();
		parent.setJMenuBar(bar);

		JMenu userMenu = new JMenu("Login");
		bar.add(userMenu);
		addNewItem(userMenu, new CreateUserAction());
		addNewItem(userMenu, new LoginAction(this));
	}

	private JMenu createUserActions() {
		JMenu userMenu = new JMenu("Test");
		addNewItem(userMenu, new UpdateUserSettings(this));
		return userMenu;
	}

	private void createContent(Application app, JTabbedPane tabbed) {
		{// create User Words panel
			JPanel userWordsPanel = new JPanel();
			userWordsPanel.setLayout(new GridLayout(0, 3, 5, 5));

			userWordsPanel.add(createKnownWordsPanel(app.getAdapter(Application.KNOWN_WORDS_MODEL_ID),"Known words"));
			userWordsPanel.add(new AbstractWordsPanel(app.getAdapter(Application.UNKNOWN_WORDS_MODEL_ID), "Unknown words"));
			userWordsPanel.add(createKnownWordsPanel(app.getAdapter(Application.VOCABULARY_WORDS_MODEL_ID), "Vocabulary"));
			tabbed.addTab("Words", userWordsPanel);
		}
		{
			tabbed.addTab("Test", new TestPanel(this));
		}
		{ // create Status panel
			JPanel statusPanel = new JPanel();
			BoxLayout layout = new BoxLayout(statusPanel, BoxLayout.Y_AXIS);
			statusPanel.setLayout(layout);
			{ // create label
				JLabel tableName = new JLabel("Status");
				tableName.setAlignmentX(Component.CENTER_ALIGNMENT);
				statusPanel.add(tableName);
			}
			{// create table
				ScrollPane scrollable = new ScrollPane();
				statusPanel.add(scrollable);
				scrollable.add(new JTable(app.getAdapter(Application.STATUS_MODEL_ID)));
			}
			tabbed.addTab("Status", statusPanel);
		}
	}

	private AbstractWordsPanel createKnownWordsPanel(DefaultTableModel model, String name) {
		AbstractWordsPanel panel = new AbstractWordsPanel(model, name);
		JTable table = panel.getTable();
		JPopupMenu popup = new JPopupMenu();
		table.add(popup);
		addSetAsUnknownAction(table, popup, getApplication());
		table.setComponentPopupMenu(popup);
		return panel;
	}

	private static void addSetAsUnknownAction(JTable table, JPopupMenu menu, Application app) {
		JMenuItem item = new JMenuItem("Set as unknown");
		menu.add(item);
		item.setAction(new AbstractAction("Set As Unknown") {
			
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

	private static void addNewItem(JMenu menu, Action action) {
		JMenuItem item = new JMenuItem();
		menu.add(item);
		item.setAction(action);
	}
	
	private static class AbstractWordsPanel extends JPanel {

		private JTable table;

		public AbstractWordsPanel(DefaultTableModel model, String name) {
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
}
