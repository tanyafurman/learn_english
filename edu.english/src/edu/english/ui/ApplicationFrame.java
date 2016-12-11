package edu.english.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.text.NumberFormat;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;

import edu.english.Application;
import edu.english.UserManager;
import edu.english.data.User;
import edu.english.data.Word2Translate;

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
			userWordsPanel.add(new AbstractWordsPanel(new JTable(app.getAdapter(Application.UNKNOWN_WORDS_MODEL_ID)), "Unknown words"));
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
		JTable table = new JTable(model);
		AbstractWordsPanel panel = new AbstractWordsPanel(table, name);
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
	
	//////////////////////////////////////////////////////////////
	// UI
	//////////////////////////////////////////////////////////////
	private static class AbstractWordsPanel extends JPanel {

		public AbstractWordsPanel(JTable table, String name) {
			BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
			setLayout(layout);

			JLabel tableName = new JLabel(name);
			tableName.setAlignmentX(Component.CENTER_ALIGNMENT);
			add(tableName);

			ScrollPane scrollable = new ScrollPane();
			add(scrollable);
			scrollable.add(table);
		}
	}
	
	//////////////////////////////////////////////////////////////
	// ACTIONS
	//////////////////////////////////////////////////////////////
	private static class CreateUserAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		public CreateUserAction() {
			super("Create User");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String name = "";
			String surname = "";
			String login = "";
			String pass = "";
			
			JTextField nameField = new JTextField();
			JTextField surnameField = new JTextField();
			JTextField loginField = new JTextField();
			JTextField passField = new JTextField();

			JComponent[] components = new JComponent[]{
					new JLabel("Name"), nameField,
					new JLabel("Surname"), surnameField,
					new JLabel("Login"), loginField,
					new JLabel("Password"), passField,
			};

			boolean userCreated = false;
			while(!userCreated) {
				int result = JOptionPane.showConfirmDialog(null, components, "Create a New User", JOptionPane.PLAIN_MESSAGE);
				if (result != JOptionPane.OK_OPTION) {
					return;
				}
				name = nameField.getText().trim();
				surname = surnameField.getText().trim();
				login = loginField.getText().trim();
				pass = passField.getText().trim();

				if (login.length() == 0) {
					JOptionPane.showConfirmDialog(null, new JComponent[]{},"\"Login\" shouldn't be empty", JOptionPane.PLAIN_MESSAGE);
					continue;
				}

				userCreated = UserManager.getInstance().createUser(name, surname, login, pass);
				
				if (!userCreated) {
					JOptionPane.showConfirmDialog(null, new JComponent[]{},"User already exist", JOptionPane.PLAIN_MESSAGE);
					continue;
				}
			}
		}
	}
	
	private static class LoginAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		private final ApplicationFrame ui;

		public LoginAction(ApplicationFrame ui) {
			super("Login");
			this.ui = ui;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String login = "";
			String pass = "";
			
			JTextField loginField = new JTextField();
			JTextField passField = new JPasswordField();

			JComponent[] components = new JComponent[]{
					new JLabel("Login"), loginField,
					new JLabel("Password"), passField,
			};

			User u = null;
			while(u == null) {
				int result = JOptionPane.showConfirmDialog(null, components, "Create a New User", JOptionPane.PLAIN_MESSAGE);
				if (result != JOptionPane.OK_OPTION) {
					return;
				}
				login = loginField.getText().trim();
				pass = passField.getText();

				if (login.length() == 0) {
					JOptionPane.showConfirmDialog(null, new JComponent[]{},"\"Login\" shouldn't be empty", JOptionPane.PLAIN_MESSAGE);
					continue;
				}

				u = UserManager.getInstance().login(login, pass);

				if (u == null) {
					JOptionPane.showConfirmDialog(null, new JComponent[]{},"Login or pass is not valid", JOptionPane.PLAIN_MESSAGE);
					continue;
				} else {
					Application previousApp = ui.getApplication();
					if (previousApp != null) {
						previousApp.dispose();
					}
					ui.setApplication(new Application(u));
				}
			}
		}
	}
	
	private static class UpdateUserSettings extends AbstractAction {

		private static final long serialVersionUID = 1L;

		private ApplicationFrame app;

		public UpdateUserSettings(ApplicationFrame app) {
			super("Settings");
			this.app = app;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (app.getApplication() == null) {
				return;
			}
			JLabel repeatCountLabel = new JLabel("Repeat Count:");
			JLabel wordsAmountLabel = new JLabel("Unknown words size:");
			

			NumberFormat numberFormat = NumberFormat.getInstance();
			NumberFormatter formatter = new NumberFormatter(numberFormat);
			formatter.setValueClass(Integer.class);
			formatter.setMinimum(1);
			formatter.setMaximum(Integer.MAX_VALUE);

			JSpinner repeatCountText = new JSpinner(createModel(app.getApplication().getRepeatCount()));
			JSpinner wordsAmountText = new JSpinner (createModel(app.getApplication().getUnknownWordsSize()));

			int result = JOptionPane.showConfirmDialog(null, new JComponent[]{repeatCountLabel, repeatCountText, wordsAmountLabel, wordsAmountText}, "Change user settings", JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				app.getApplication().setRepeatCount(getSpinnerValue(repeatCountText));
				app.getApplication().setUnknownWordsAmount(getSpinnerValue(wordsAmountText));
			}
		}

		private int getSpinnerValue(JSpinner spinner) {
			return Integer.parseInt(((JSpinner.NumberEditor)spinner.getEditor()).getTextField().getText());
		}
		
		private SpinnerNumberModel createModel(int initValue) {
			return new SpinnerNumberModel(initValue, 1, Integer.MAX_VALUE, 1);
		}
	}
}
