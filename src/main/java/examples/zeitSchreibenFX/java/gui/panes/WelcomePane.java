package examples.zeitSchreibenFX.java.gui.panes;

import examples.zeitSchreibenFX.java.gui.interfaces.PaneInterface;
import javafx.stage.Stage;

public class WelcomePane implements PaneInterface {
	
	@Override
	public void init(Stage stage) {
		System.out.println("reached WelcomePane.init");
	}

}
