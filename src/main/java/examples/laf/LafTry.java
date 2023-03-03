package examples.laf;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

public class LafTry {

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(new FlatMacDarkLaf());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		JOptionPane.showInputDialog("Sup");
		
	}

}
