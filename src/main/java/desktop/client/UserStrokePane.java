package desktop.client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;

import examples.entity.User;

public class UserStrokePane extends Panel {

	Long serialVersionUID = 3529556279974932686L;

	private static UserStrokePane ref;

	private UserStrokePane(GridBagLayout gbl) {
		super(gbl);
	}

	public static UserStrokePane initUserStrokePane(User user) {

		if (ref == null) {
			ref = new UserStrokePane(new GridBagLayout());
			createUserStrokePane();
		}
		return ref;
	}

	private static void createUserStrokePane() {

		// TODO: hier die getränke art menge und und menge an einzelnen strichen pro
		// user / getränk

		int drinkTypes = 5;
		int strokeQty = 10;

		int yCount = 0;

		for (int i = 0; i < drinkTypes; i++) {
			createDrinkStrokeAssembly(yCount, strokeQty);
			yCount += 2;
		}

	}

	private static void createDrinkStrokeAssembly(int yCount, int strokeQty) {

		JLabel label = new JLabel("Bier");
		JLabel qtyLabel = new JLabel("8");
		JButton minusButton = new JButton("-");
		JTextField textfield = new JTextField("0");
		JButton plusButton = new JButton("+");

		textfield.setEnabled(false);
		textfield.setHorizontalAlignment(JTextField.CENTER);
		
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(UIManager.getFont( "h1.font" ));
		
		qtyLabel.setHorizontalAlignment(JLabel.CENTER);
		qtyLabel.setFont(UIManager.getFont( "h1.font" ));
		
		minusButton.addActionListener(e -> qtyLabel.setText(String.valueOf(Integer.parseInt(qtyLabel.getText()) - 1)));
		plusButton.addActionListener(e -> qtyLabel.setText(String.valueOf(Integer.parseInt(qtyLabel.getText()) + 1)));
		
		minusButton.setPreferredSize(new Dimension(50, 50));
		plusButton.setPreferredSize(new Dimension(50, 50));
		qtyLabel.setPreferredSize(new Dimension(100, 50));
		
		GridBagConstraints constr = new GridBagConstraints();
		constr.gridx = 1;
		constr.gridy = yCount;
		constr.fill = GridBagConstraints.BOTH;
		constr.insets = new Insets(25, 0, 0, 0);
		
		yCount++;

		GridBagConstraints minusConst = new GridBagConstraints();
		minusConst.gridx = 0;
		minusConst.gridy = yCount;
		minusConst.fill = GridBagConstraints.BOTH;
		minusConst.insets = new Insets(0, 0, 0, 0);

		GridBagConstraints textConst = new GridBagConstraints();
		textConst.gridx = 1;
		textConst.gridy = yCount;
		textConst.fill = GridBagConstraints.BOTH;
		textConst.insets = new Insets(0, 0, 0, 0);

		GridBagConstraints plusConst = new GridBagConstraints();
		plusConst.gridx = 2;
		plusConst.gridy = yCount;
		plusConst.fill = GridBagConstraints.BOTH;
		plusConst.insets = new Insets(0, 0, 0, 0);

		yCount++;

		ref.add(label, constr);
		ref.add(minusButton, minusConst);
		ref.add(qtyLabel, textConst);
		ref.add(plusButton, plusConst);

	}

}
