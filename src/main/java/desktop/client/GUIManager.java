package desktop.client;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import desktop.client.gui.LoginPanel;
import desktop.client.gui.MainFrame;
import examples.entity.User;

public class GUIManager {

	private static GUIManager ref;
	
	private GUIManager() {
	}
	
	public static GUIManager initGUIManager() {
		
		if(ref == null) {
			ref = new GUIManager();
		}
		return ref;
	}

	public void startGui() {


//		try {
//			UIManager.setLookAndFeel(new FlatDarkLaf());
//		} catch (UnsupportedLookAndFeelException e) {
//			e.printStackTrace();
//		}
		
		MainFrame mainFrame = MainFrame.initMainFrame();

		CardLayout card = new CardLayout();
		JPanel cardPane = new JPanel();

		LoginPanel loginPane = LoginPanel.initLoginPanel();

//		JOptionPane.showConfirmDialog(null, loginPane, "Login: ", JOptionPane.OK_CANCEL_OPTION,
//				JOptionPane.PLAIN_MESSAGE);
		
		String userName = loginPane.getUserName();
		String password = loginPane.getPassword();

		
		MainPanel mainPanel = MainPanel.initMainPanel();
		
		HittnTable hittnTable = mainPanel.getHittnTable();
		
		hittnTable.addMouseListener(new MouseAdapter() {
			  public void mouseClicked(MouseEvent e) {
			      JTable target = (JTable)e.getSource();
			      int row = target.getSelectedRow();
			     
			      card.next(cardPane);
			      
			  }
			});
		
		UserStrokePane userStrokePane = UserStrokePane.initUserStrokePane(new User(0, "Moritz", "Rieder"));
//		
//		JPanel panel3 = new JPanel();
//		mainPane.setBackground(Color.blue);
//		
//        JButton button1 = new JButton("Click me to change panel 1");
//        button1.addActionListener(e -> card.next(cardPane));
//        mainPane.add(button1);
//         
//        JButton button2 = new JButton("Click me to change panel 2");
//        button2.addActionListener(e -> card.next(cardPane));
//        panel2.add(button2);
//         
//        JButton button3 = new JButton("Click me to change panel 3");
//        button3.addActionListener(e -> card.next(cardPane));
//        panel3.add(button3);

		cardPane.setLayout(card);
//        cardPane.add(panel2, "Second Pane");
//        cardPane.add(panel3, "Third Pane");

		cardPane.add(mainPanel);
		cardPane.add(userStrokePane);
		
		mainFrame.add(cardPane);
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		mainFrame.setSize(new Dimension(1000, 563));
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		mainFrame.setVisible(true);

		
		
	}

	
	
}
