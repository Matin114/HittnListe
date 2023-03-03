package desktop.client.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

public class MainFrame extends JFrame{

	private static final long serialVersionUID = -3118943814883337097L;
	private static MainFrame ref;
	
	private MainFrame() {
	}
	
	public static MainFrame initMainFrame() {
		
		if(ref == null) {
			return new MainFrame();
		}
		return ref;
	}
	
}
