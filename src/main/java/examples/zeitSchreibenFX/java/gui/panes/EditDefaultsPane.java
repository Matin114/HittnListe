package examples.zeitSchreibenFX.java.gui.panes;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import examples.zeitSchreibenFX.java.general.SettingsData;
import examples.zeitSchreibenFX.java.gui.interfaces.DefaultPaneImpl;
import examples.zeitSchreibenFX.java.start.StartStundenClient;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EditDefaultsPane extends DefaultPaneImpl {

	@Override
	public void init(Stage stage) {
		System.out.println("reached EditDefaults.init");


		BorderPane mainPane = (BorderPane) stage.getScene().getRoot();

		GridPane defaultsGridPane = new GridPane();
		defaultsGridPane.setPadding(new Insets(10, 20, 10, 20));

		Label defaultsHeader = new Label("Defaults");
		defaultsHeader.setFont(new Font("Yu Gothic Bold", 20));
		defaultsHeader.setPadding(new Insets(0, 0, 12, 0));


		Line headerLine = new Line();
		headerLine.setStartX(10);
		headerLine.endXProperty().bind(stage.widthProperty().subtract(50));;


		GridPane inputGridPane = new GridPane();
		inputGridPane.setPadding(new Insets(15, 0, 0, 0));


		GridPane salesPane = new DefaultsGridPane(SettingsData.SALES_DOC,
				"Insert a default Sales Document", new Insets(0, 18, 15, 0));

		GridPane ltpPane = new DefaultsGridPane(SettingsData.LTP_CODE,
				"Insert a default LTP Code", new Insets(0, 18, 15, 0));

		GridPane descPane = new DefaultsGridPane(SettingsData.DESCRIPTION,
				"Insert a default Description", new Insets(0, 18, 12, 0));

		Line defaultsLine = new Line();
		defaultsLine.setStartX(10);
		defaultsLine.endXProperty().bind(stage.widthProperty().subtract(50));;

		GridPane waitPane = new DefaultsGridPane(SettingsData.WAIT_TIME,
				"Insert the time in sec you want to wait for the writer to begin (2-60)", new Insets(12, 18, 15, 0));


		GridPane createMorePane = new GridPane();

		Label createMoreLabel = new Label(SettingsData.CREATE_MORE);
		createMoreLabel.setPadding(new Insets(0, 101, 0, 0));

		CheckBox createMoreBox = new CheckBox();
		createMoreBox.setTooltip(new Tooltip("Tick if you mostly write several days at once"));


		createMorePane.getChildren().addAll(createMoreLabel, createMoreBox);
		GridPane.setConstraints(createMoreLabel, 0, 0);
		GridPane.setConstraints(createMoreBox, 1, 0);


		GridPane buttonPane = new GridPane();
		buttonPane.setAlignment(Pos.BOTTOM_CENTER);
		buttonPane.setHgap(8);

		Button saveButton = new Button("Save");
		saveButton.setMinSize(55, 25);
		saveButton.setPrefWidth(80);
		saveButton.setOnAction(e -> {
			SettingsData inputSettings = writeData(inputGridPane);

			if (!inputSettings.getErrorList().isEmpty()) {
				Alert errorsAlert = new Alert(AlertType.INFORMATION);
				errorsAlert.setTitle("Information");
				errorsAlert.setHeaderText("There are existing errors! Please fix those.");
				for (String error : inputSettings.getErrorList()) {
					errorsAlert.setContentText(errorsAlert.getContentText() + error + "\n");
				}
				errorsAlert.showAndWait();
			} else {
				StartStundenClient.xmlTransfer.writeSettings(inputSettings);
				Alert savedInfoAlert = new Alert(AlertType.INFORMATION);
				savedInfoAlert.getButtonTypes().clear();
				savedInfoAlert.getButtonTypes().addAll(ButtonType.OK);
				savedInfoAlert.setTitle("Information");
				savedInfoAlert.setHeaderText("Settings successfully safed!");
				savedInfoAlert.showAndWait();
			}
		});

			Button resetButton = new Button("Reset");
			resetButton.setMinSize(55, 25);
			resetButton.setPrefWidth(80);
			resetButton.setTooltip(new Tooltip("Restores the current saved settings from file"));
			resetButton.setOnAction(e -> {
				Alert confirmAlert = new Alert(AlertType.WARNING);
				confirmAlert.getButtonTypes().clear();
				confirmAlert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
				confirmAlert.setTitle("Warning");
				confirmAlert.setHeaderText("Do you really want to load data from file?");
				confirmAlert.setContentText("All changes will be lost unrecoverably!");

				Optional<ButtonType> result = confirmAlert.showAndWait();
				if (result.get() == ButtonType.YES) {
					loadData(inputGridPane);
				}
			});

			buttonPane.getChildren().addAll(saveButton, resetButton);
			GridPane.setConstraints(saveButton, 0, 0);
			GridPane.setConstraints(resetButton, 1, 0);

			inputGridPane.getChildren().addAll(salesPane, ltpPane, descPane, defaultsLine, waitPane, createMorePane);
			GridPane.setConstraints(salesPane, 0, 0);
			GridPane.setConstraints(ltpPane, 0, 1);
			GridPane.setConstraints(descPane, 0, 2);
			GridPane.setConstraints(defaultsLine, 0, 3, 5, 1);
			GridPane.setConstraints(waitPane, 0, 4);
			GridPane.setConstraints(createMorePane, 0, 5);


			StackPane fillerPane = new StackPane();
			fillerPane.prefWidthProperty().bind(defaultsGridPane.widthProperty());
			fillerPane.prefHeightProperty().bind(defaultsGridPane.heightProperty());
			ListView<String> fillerList = new ListView<>();
			fillerList.setFocusTraversable(false);
			fillerList.setEditable(false);
			fillerList.setVisible(false);
			fillerList.setDisable(true);
			fillerPane.getChildren().add(fillerList);
			
			defaultsGridPane.getChildren().addAll(defaultsHeader, headerLine, inputGridPane, fillerPane, buttonPane);
			GridPane.setConstraints(defaultsHeader, 0, 0);
			GridPane.setConstraints(headerLine, 0, 1);
			GridPane.setConstraints(inputGridPane, 0, 2);
			GridPane.setConstraints(fillerPane, 0, 3);
			GridPane.setConstraints(buttonPane, 0, 4);

			loadData(inputGridPane);

			mainPane.requestFocus();

			mainPane.setCenter(defaultsGridPane);
	}

	private void loadData(GridPane pane) {
		SettingsData settingsFromFile = StartStundenClient.xmlTransfer.getSettings();
		for (Node child : pane.getChildren()) {
			if (!(child instanceof GridPane)) {
				continue;
			}
			GridPane inputPane = (GridPane) child;
			Node valueNode = inputPane.getChildren().get(1);
			if (valueNode instanceof TextField) {
				String id = valueNode.getId();
				String newValue = "";
				switch (id) {
				case SettingsData.SALES_DOC:
					newValue = settingsFromFile.getSalesDoc();
					break;
				case SettingsData.LTP_CODE:
					newValue = settingsFromFile.getLtpCode();
					break;
				case SettingsData.DESCRIPTION:
					newValue = settingsFromFile.getDescription();
					break;
				case SettingsData.WAIT_TIME:
					newValue = settingsFromFile.getWaitTime();
					break;
				}
				((TextField) valueNode).setText(newValue);
			} else {
				((CheckBox) valueNode).setSelected(settingsFromFile.isCreateMore());
			}
		}
	}

	private SettingsData writeData(GridPane pane) {
		List<String> errorList = new LinkedList<>();
		SettingsData inputSettings = new SettingsData();
		
		// get the input data
		for (Node child : pane.getChildren()) {
			if (!(child instanceof GridPane)) {
				continue;
			}
			GridPane inputPane = (GridPane) child;
			Node valueNode = inputPane.getChildren().get(1);
			if (valueNode instanceof TextField) {
				String id = valueNode.getId();
				String value = ((TextField) valueNode).getText().trim();
				switch (id) {
				case SettingsData.SALES_DOC:
					inputSettings.setSalesDoc(value);
					break;
				case SettingsData.LTP_CODE:
					inputSettings.setLtpCode(value);
					break;
				case SettingsData.DESCRIPTION:
					inputSettings.setDescription(value);
					break;
				case SettingsData.WAIT_TIME:
					inputSettings.setWaitTime(value);
					break;
				}
			} else {
				inputSettings.setCreateMore(((CheckBox) valueNode).isSelected());
			}
		}
		
		try {
			Integer.valueOf(inputSettings.getSalesDoc());
		} catch (NumberFormatException e) {
			errorList.add("Sales Document can only contain numbers!");
		}
		try {
			Integer.valueOf(inputSettings.getLtpCode());
		} catch (NumberFormatException e) {
			errorList.add("Ltp Code can only contain numbers!");
		}
		try {
			int time = Integer.valueOf(inputSettings.getWaitTime());
			if (time > 60 || time < 2) {
				errorList.add("Wait time must be between 2 and 60!");
			}
		} catch (NumberFormatException e) {
			errorList.add("Wait time can only contain numbers!");
		}

		inputSettings.setErrorList(errorList);
		return inputSettings;
	}

}


class DefaultsGridPane extends GridPane {

	private Label label;
	private TextField textField;

	public DefaultsGridPane(String labelText, String toolTip, Insets insets) {
		super();
		label = new Label(labelText);
		label.autosize();
		label.setPadding(new Insets(0,100-new Text(labelText).getLayoutBounds().getWidth(),0,0));
		textField = new TextField();
		textField.setTooltip(new Tooltip(toolTip));
		textField.setId(labelText);

		setPadding(insets);
		getChildren().addAll(label, textField);
		GridPane.setConstraints(label, 0, 0);
		GridPane.setConstraints(textField, 1, 0);
	}

	public Label getLabel() {
		return label;
	}
	public TextField getTextField() {
		return textField;
	}

}
