package examples.zeitSchreibenFX.java.gui;


import examples.zeitSchreibenFX.java.general.Constants;
import examples.zeitSchreibenFX.java.general.Day;
import examples.zeitSchreibenFX.java.gui.panes.EditDaysPane;
import examples.zeitSchreibenFX.java.gui.panes.EditDefaultsPane;
import examples.zeitSchreibenFX.java.gui.panes.EnterVariableValuesPane;
import examples.zeitSchreibenFX.java.gui.panes.HelpPane;
import examples.zeitSchreibenFX.java.gui.panes.SelectDayPane;
import examples.zeitSchreibenFX.java.gui.panes.StartUpPane;
import examples.zeitSchreibenFX.java.gui.panes.WelcomePane;
import javafx.stage.Stage;

public class SceneChanger {
	
	public static boolean unsavedChanges = false;
	
	public static void openStartUp(Stage stage) {
		stage.getScene().getRoot().setId(Constants.START_UP_PANE_ID);
		StartUpPane startUpScene = new StartUpPane();
		startUpScene.init(stage);
		unsavedChanges = false; 
	}
	
	public static void openSelectDay(Stage stage) {
		stage.getScene().getRoot().setId(Constants.SELECT_PANE_ID);
		SelectDayPane selectDay = new SelectDayPane();
		selectDay.init(stage);
		unsavedChanges = false;
	}

	public static void openEditDays(Stage stage) {
		openEditDays(stage, null);
	}
	
	public static void openEditDays(Stage stage, Day day) {
		stage.getScene().getRoot().setId(Constants.EDIT_DAYS_PANE_ID);
		EditDaysPane editDays = new EditDaysPane();
		editDays.init(stage, day);
		unsavedChanges = true;
	}
	
	public static void openEnterVariableValues(Stage stage, Day day) {
		stage.getScene().getRoot().setId(Constants.ENTER_VARIABLE_VALUES_ID);
		EnterVariableValuesPane variableValues = new EnterVariableValuesPane();
		variableValues.init(stage, day);
		unsavedChanges = true;
	}
	
	public static void openEditDefaults(Stage stage) {
		stage.getScene().getRoot().setId(Constants.EDIT_DEFAULTS_PANE_ID);
		EditDefaultsPane editDefaults = new EditDefaultsPane();
		editDefaults.init(stage);
		unsavedChanges = true;
	}
	
	public static void openWelcome(Stage stage) {
		stage.getScene().getRoot().setId(Constants.WELCOME_PANE_ID);
		WelcomePane welcome = new WelcomePane();
		welcome.init(stage);
		unsavedChanges = false;
	}
	
	public static void openHelp(Stage stage) {
//		stage.getScene().getRoot().setId(Constants.HELP_PANE_ID);
		HelpPane help = new HelpPane();
		help.init(stage);
	}
	
}
