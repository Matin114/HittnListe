package desktop.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginPanel extends JPanel {

	private static final long serialVersionUID = 5090884733566617290L;

	private static LoginPanel ref;

	private static JTextField userNameField;
	private static JTextField passwordField;

	private LoginPanel(GridBagLayout gridBagLayout) {
		super(gridBagLayout);
	}

	public static LoginPanel initLoginPanel() {

		if (ref == null) {
			ref = new LoginPanel(new GridBagLayout());
			createLoginPanel();
		}
		return ref;
	}

	private static void createLoginPanel() {

		JLabel userNameLabel = new JLabel("User Name:");
		GridBagConstraints gbcNameLbl = new GridBagConstraints();
		gbcNameLbl.gridx = 0;
		gbcNameLbl.gridy = 0;
		gbcNameLbl.weightx = 0.5;
		gbcNameLbl.weighty = 0.1;
		gbcNameLbl.anchor = GridBagConstraints.WEST;
		gbcNameLbl.insets = new Insets(25, 10, 5, 25);

		userNameField = new JTextField();
		userNameField.setPreferredSize(new Dimension(200, 25));
		GridBagConstraints gbcNameField = new GridBagConstraints();
		gbcNameField.gridx = 0;
		gbcNameField.gridy = 1;
		gbcNameField.weightx = 0.5;
		gbcNameField.weighty = 0.1;
		gbcNameField.anchor = GridBagConstraints.WEST;
		gbcNameField.insets = new Insets(5, 10, 5, 25);

		JLabel passwordLabel = new JLabel("Password:");
		GridBagConstraints gbcpwdLbl = new GridBagConstraints();
		gbcpwdLbl.gridx = 0;
		gbcpwdLbl.gridy = 2;
		gbcpwdLbl.weightx = 0.5;
		gbcpwdLbl.weighty = 0.1;
		gbcpwdLbl.anchor = GridBagConstraints.WEST;
		gbcpwdLbl.insets = new Insets(5, 10, 5, 25);

		passwordField = new JTextField();
		passwordField.setPreferredSize(new Dimension(200, 25));
		GridBagConstraints gbcpwdField = new GridBagConstraints();
		gbcpwdField.gridx = 0;
		gbcpwdField.gridy = 3;
		gbcpwdField.weightx = 0.5;
		gbcpwdField.weighty = 0.1;
		gbcpwdField.anchor = GridBagConstraints.WEST;
		gbcpwdField.insets = new Insets(5, 10, 25, 25);

		ref.add(userNameLabel, gbcNameLbl);
		ref.add(userNameField, gbcNameField);
		ref.add(passwordLabel, gbcpwdLbl);
		ref.add(passwordField, gbcpwdField);
	}

	public String getUserName(){
		return userNameField.getText();
	}
	
	public String getPassword(){
		return passwordField.getText();
	}
	
}
