package desktop.client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MainPanel extends JPanel{

	private static final long serialVersionUID = -8913802804700897554L;
	private static MainPanel ref;
	private static HittnTable table;
	
	private MainPanel(GridBagLayout gridBagLayout) {
		super(gridBagLayout);
	}
	
	public static MainPanel initMainPanel() {
		
		if(ref == null) {
			ref = new MainPanel(new GridBagLayout());
			createMainPanel();
		}
		return ref;
	}

	private static void createMainPanel() {

		
		String[][] rowData = new String[2][2];
		String[] columnNames = {"" ,""};
		
		rowData[0][0] = "Moritz";
		rowData[0][1] = "20.0€";
		
		rowData[1][0] = "Kim";
		rowData[1][1] = "40.0€";
		
		table = HittnTable.initHittnTable(rowData, columnNames);
		
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(900, 900));
		
		GridBagConstraints tableGbc = new GridBagConstraints();
		tableGbc.gridx = 0;
		tableGbc.gridy = 0;
		tableGbc.weightx = 0.5;
		tableGbc.weighty = 0.1;
		tableGbc.anchor = GridBagConstraints.CENTER;
		tableGbc.insets = new Insets(0, 0, 0, 0);
				
		ref.add(scrollpane, tableGbc);
	}
	
	public HittnTable getHittnTable() {
		return table;
	}
	
}
