package examples.zeitSchreibenFX.java.gui.panes;

import examples.zeitSchreibenFX.java.gui.interfaces.DefaultPaneImpl;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelpPane extends DefaultPaneImpl {	

//	private static HelpStage hs;

	public HelpPane () { 
	}

	@Override
	public void init(Stage stage) {
//				System.out.println("reached HelpPane.init");
//				if (hs == null) {
//					hs = new HelpStage();
//				}
//				if (!hs.isShowing()) {
//					hs.show();
//				} else {
//					hs.requestFocus();
//				}


		Alert helpAlert = new Alert(AlertType.INFORMATION);
		helpAlert.setTitle("Help");
		helpAlert.setHeaderText("This is a short description of the command characters");
		helpAlert.setContentText("? is used to call the default value from the settings\n" + 
				"! is used to get the same text as in the row above\n" + 
				"$variableName$ is used to get the value of the variable; You can define variables as you like while max amount being 12 with max lenght of 15 characters.\n" + 
				"\\ is used to disable the following characters; \\\\ to write one \\\n\n"
				+ "All of these can be used on their own or combined with text or even each other!");

		helpAlert.showAndWait();
	}

}

class HelpStage extends Stage {

	public HelpStage(){

		BorderPane bp = new BorderPane();

		VBox box = new VBox();
		Button generalButton = new Button("General");
		Button editDaysButton = new Button("Edit Days");
		Button defaultsButton = new Button("Defaults");

		box.getChildren().addAll(generalButton, editDaysButton, defaultsButton);

		bp.setLeft(box);

		this.setScene(new Scene(bp, 300, 300));

	}
}
