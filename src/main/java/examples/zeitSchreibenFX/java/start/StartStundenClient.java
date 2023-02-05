package examples.zeitSchreibenFX.java.start;

import java.util.Locale;
import java.util.Optional;

import examples.zeitSchreibenFX.java.general.Constants;
import examples.zeitSchreibenFX.java.gui.SceneChanger;
import examples.zeitSchreibenFX.java.xmlConverter.XMLTransfer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class StartStundenClient extends Application {

	public static XMLTransfer xmlTransfer;
	public static boolean menuBarAllowed = true;

	private Stage stage;

	@Override
	public void start(Stage primaryStage) throws Exception {
		Locale.setDefault(Locale.ENGLISH);
		xmlTransfer = new XMLTransfer();

		stage = primaryStage;
		stage.setMinHeight(420);
		stage.setMinWidth(563);
		stage.setHeight(450);
		stage.setWidth(750);
		stage.setTitle("StundenV2");
		stage.fullScreenProperty().addListener((observable, oldValue, newValue) -> {

			if (newValue) {
				System.out.println("Fullscreen now!");
			} else {
				System.out.println("Partscreen!");
			}
		});


		BorderPane mainPane = new BorderPane();
		mainPane.prefHeightProperty().bind(primaryStage.heightProperty());
		mainPane.prefWidthProperty().bind(primaryStage.widthProperty());

		//----------------------------------------------------------------------

		MenuBar menuBar = new MenuBar();

		Menu fileMenu = new Menu("File");

		MenuItem homeItem = new MenuItem("Home");
		homeItem.setOnAction(e -> {
			if (!menuBarAllowed) {return;}
			if (SceneChanger.unsavedChanges) {
				if (!unsavedChanges()) {
					return;
				}
			}
			if (!stage.getScene().getRoot().getId().equals(Constants.START_UP_PANE_ID)) {
				SceneChanger.openStartUp(stage);
			}
		});

		MenuItem editDaysItem = new MenuItem("Edit Days");
		editDaysItem.setOnAction(e -> {
			if (!menuBarAllowed) {return;}
			if (SceneChanger.unsavedChanges) {
				if (!unsavedChanges()) {
					return;
				}
			}
			if (!stage.getScene().getRoot().getId().equals(Constants.SELECT_PANE_ID)) {
				SceneChanger.openSelectDay(stage);
			}
		});

		MenuItem editDefaultsItem = new MenuItem("Edit Defaults");
		editDefaultsItem.setOnAction(e -> {
			if (!menuBarAllowed) {return;}
			if (stage.getScene().getRoot().getId().equals(Constants.EDIT_DEFAULTS_PANE_ID)) {return;}
			if (SceneChanger.unsavedChanges) {
				if (!unsavedChanges()) {
					return;
				}
			}
			SceneChanger.openEditDefaults(stage);
		});

		MenuItem exitItem = new MenuItem("Exit");
		exitItem.setOnAction(e -> {
			stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
		});

		fileMenu.getItems().addAll(homeItem, editDaysItem, editDefaultsItem, new SeparatorMenuItem(), exitItem);

		//----------------------------------------------------------------------

		Menu helpMenu = new Menu("Help");

		MenuItem welcomeItem = new MenuItem("Welcome");
		welcomeItem.setOnAction(e -> {
			if (!menuBarAllowed) {return;}
			if (SceneChanger.unsavedChanges) {
				if (!unsavedChanges()) {
					return;
				}
			}
			if (!stage.getScene().getRoot().getId().equals(Constants.WELCOME_PANE_ID)) {
				SceneChanger.openWelcome(stage);
			}
		});

		MenuItem helpItem = new MenuItem("Help");
		helpItem.setOnAction(e -> {
			if (!menuBarAllowed) {return;}
			if (!stage.getScene().getRoot().getId().equals(Constants.HELP_PANE_ID)) {
				SceneChanger.openHelp(stage);
			}
		});

		helpMenu.getItems().addAll(welcomeItem, helpItem);

		//----------------------------------------------------------------------

		menuBar.getMenus().addAll(fileMenu, helpMenu);

		mainPane.setTop(menuBar);


		Scene scene = new Scene(mainPane);

		stage.setScene(scene);

		stage.show();

		scene.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);

		SceneChanger.openStartUp(stage);
	}

	private void closeWindowEvent(WindowEvent event) {
		if (SceneChanger.unsavedChanges) {
			if (!unsavedChanges()) {
				event.consume();
			}
		}
		Optional<Thread> writingThread = Thread.getAllStackTraces().keySet().stream().filter(
				e -> e.getName().equals(Constants.WRITING_THREAD_NAME)).findFirst();
		if (writingThread.isPresent()) {
			writingThread.get().interrupt();
		}
	}


	public static MouseEvent createEvent(MouseButton button) {
		return new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
				0, 0, 0, button, 0, true, true, true, true,
				true, true, true, true, true, true, null);
	}


	/**
	 * 
	 * @return true if the user wants to leave the panel
	 */
	private boolean unsavedChanges() {
		Alert confirmAlert = new Alert(AlertType.WARNING);
		confirmAlert.getButtonTypes().clear();
		confirmAlert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
		confirmAlert.setTitle("Warning");
		confirmAlert.setHeaderText("Do you really want to leave?");
		confirmAlert.setContentText("All unsaved changes will be lost unrecoverably!");

		Optional<ButtonType> result = confirmAlert.showAndWait();
		return (result.get() == ButtonType.YES);
	}
}
