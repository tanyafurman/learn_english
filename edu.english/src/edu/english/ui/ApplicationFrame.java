package edu.english.ui;

import java.awt.Dimension;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

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
		tabbed.addTab("Words", new UserVocabularyPane(app));
		tabbed.addTab("Test", new TestPanel(this));
		tabbed.addTab("Status", new StatusPanel(application));
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

	private static void addNewItem(JMenu menu, Action action) {
		JMenuItem item = new JMenuItem();
		menu.add(item);
		item.setAction(action);
	}
}
