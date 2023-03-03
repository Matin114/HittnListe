package desktop.client;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import desktop.client.gui.LoginPanel;
import desktop.client.gui.MainFrame;
import desktop.client.serverCommunication.HttpManager;

public class ClientStart {

	public static void main(String[] args) {
		
		GUIManager guiManager = GUIManager.initGUIManager();
		guiManager.startGui();
		
		HttpManager httpManager = HttpManager.initHttpManager();
		httpManager.setUpHttpCon();
		
	}

}
