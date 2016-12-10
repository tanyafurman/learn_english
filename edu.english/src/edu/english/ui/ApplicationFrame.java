package edu.english.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.ScrollPane;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import edu.english.Application;
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
		tabbed.addTab("Words", new UserVocabularyPane(app));
		tabbed.addTab("Test", new TestPanel(this));
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

	private static void addNewItem(JMenu menu, Action action) {
		JMenuItem item = new JMenuItem();
		menu.add(item);
		item.setAction(action);
	}
}
