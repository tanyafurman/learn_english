package edu.furman.english.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import edu.furman.english.Application;
import edu.furman.english.UserManager;
import edu.furman.english.data.User;
import edu.furman.english.data.Vocabulary;
import edu.furman.english.ui.ApplicationFrame;

public class LoginAction extends AbstractAction {

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
				Vocabulary v = new Vocabulary();
				v.load();
				Application previousApp = ui.getApplication();
				if (previousApp != null) {
					previousApp.dispose();
				}
				ui.setApplication(new Application(u, v));
			}
		}
	}
}
