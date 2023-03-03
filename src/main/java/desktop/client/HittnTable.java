package desktop.client;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

public class HittnTable extends JTable {

	private static final long serialVersionUID = -8913802804700897554L;
	private static HittnTable ref;
	
	private HittnTable(Object[][] rowData, Object[] columnNames) {
		super(rowData, columnNames);
	}
	
	public static HittnTable initHittnTable(Object[][] rowData, Object[] columnNames) {
		
		if(ref == null) {
			ref = new HittnTable(rowData, columnNames);
			createHittnTable();
		}
		return ref;
	}

	private static void createHittnTable() {

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		
		ref.setFont( UIManager.getFont( "h1.font" ) );
		ref.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
		ref.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
		ref.setRowHeight(50);
		ref.setFillsViewportHeight(true);
		ref.setTableHeader(null);
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
        return false;
    }
	
}
