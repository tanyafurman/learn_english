package edu.english.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import edu.english.UserManager;

public class CreateUserAction extends AbstractAction {

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
