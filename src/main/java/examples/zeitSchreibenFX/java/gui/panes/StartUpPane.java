package examples.zeitSchreibenFX.java.gui.panes;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import examples.zeitSchreibenFX.java.general.Constants;
import examples.zeitSchreibenFX.java.general.CustomCellEditor;
import examples.zeitSchreibenFX.java.general.Day;
import examples.zeitSchreibenFX.java.general.DayPos;
import examples.zeitSchreibenFX.java.general.DayPosError;
import examples.zeitSchreibenFX.java.general.TimeComparator;
import examples.zeitSchreibenFX.java.general.Variable;
import examples.zeitSchreibenFX.java.gui.interfaces.DefaultPaneImpl;
import examples.zeitSchreibenFX.java.gui.threads.WritingThread;
import examples.zeitSchreibenFX.java.start.StartStundenClient;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class StartUpPane extends DefaultPaneImpl {

	// this boolean is used in every setOnEditCommit method, since the code in it get called twice for some reason
	private boolean timeAlertShowed = false;
	
	public static boolean createMore = false;
			
	@Override
	public void init(Stage stage) {
		System.out.println("reached StartUpPane.init");

		BorderPane mainPane = (BorderPane) stage.getScene().getRoot();

		GridPane editPane = new GridPane();
		editPane.setPadding(new Insets(10, 30, 10, 30));

		StackPane tablePane = new StackPane();
		tablePane.prefWidthProperty().bind(editPane.widthProperty());
		tablePane.prefHeightProperty().bind(editPane.heightProperty());
		tablePane.setPadding(new Insets(0, 0, 5, 0));

		// Used so that the fucking tablePane doesn't overlap with the variable Textfields
		ChangeListener<Number> resizeListener = ((observable, oldValue, newValue) -> {
			resize(tablePane, 10);
		});

		stage.widthProperty().addListener(resizeListener);

		ComboBox<Day> dayNameBox = new ComboBox<>();
		dayNameBox.setPromptText("Select a Day");
		dayNameBox.getItems().addAll(StartStundenClient.xmlTransfer.getAllDays());
		dayNameBox.getSelectionModel().selectFirst();
		dayNameBox.prefWidthProperty().bind(stage.widthProperty());

		dayNameBox.setConverter(new StringConverter<Day>() {

			@Override
			public String toString(Day day) {
				return day == null? null : day.getId();
			}

			@Override
			public Day fromString(String string) {
				return null;
			}
		});


		TableView<DayPos> dayPosTable = new TableView<>();
		dayPosTable.setEditable(true);
		dayPosTable.getSelectionModel().setCellSelectionEnabled(true);
		dayPosTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		dayPosTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getButton().equals(MouseButton.PRIMARY)){
					// only add new empty line if no one exists yet and a day is selected
					if(!dayPosTable.getItems().contains(new DayPos()) 
							&& !dayNameBox.getSelectionModel().isEmpty()) {
						dayPosTable.getItems().add(new DayPos());
					}
				}
			}
		});

		Callback<TableColumn<DayPos, String>, TableCell<DayPos, String>> cellFactoryString = 
				(TableColumn<DayPos, String> p) -> new CustomCellEditor(dayPosTable); 


				TableColumn<DayPos, String> salesDocCol = new TableColumn<>("Sales Document");
				salesDocCol.setEditable(true);
				salesDocCol.setSortable(false);
				salesDocCol.setCellValueFactory(
						new PropertyValueFactory<DayPos, String>("salesDoc"));
				salesDocCol.setCellFactory(cellFactoryString);
				salesDocCol.setOnEditCommit(e -> {
					e.getRowValue().setSalesDoc(e.getNewValue().trim());
					dayPosTable.requestFocus();
				});

				TableColumn<DayPos, String> ltpCodeCol = new TableColumn<>("Ltp Code");
				ltpCodeCol.setSortable(false);
				ltpCodeCol.setCellValueFactory(
						new PropertyValueFactory<DayPos, String>("ltpCode"));
				ltpCodeCol.setCellFactory(cellFactoryString);
				ltpCodeCol.setOnEditCommit(e -> {
					e.getRowValue().setLtpCode(e.getNewValue().trim());
					dayPosTable.requestFocus();
				});

				TableColumn<DayPos, String> startTimeCol = new TableColumn<>("Start Time");
				startTimeCol.setSortType(SortType.ASCENDING);
				startTimeCol.setComparator(new TimeComparator());
				startTimeCol.setCellValueFactory(
						new PropertyValueFactory<DayPos, String>("startTime"));
				startTimeCol.setCellFactory(cellFactoryString);
				startTimeCol.setOnEditCommit(e -> {
					String newValue = e.getNewValue().trim();
					newValue = isViableTime(newValue);
					boolean valueChanged;
					if(newValue == null) {
						valueChanged = false;
						if (!timeAlertShowed && !e.getNewValue().equals("")) {
							timeAlertShowed = true;
							Alert valueNotChangedAlert = new Alert(AlertType.WARNING);
							valueNotChangedAlert.setTitle("Warning");
							valueNotChangedAlert.setHeaderText(e.getNewValue() + " is no viable Input!");
							valueNotChangedAlert.setContentText("Please enter a viable time format!");
							System.out.println("CONFIGURED");
							valueNotChangedAlert.showAndWait();
							System.out.println("SHOW");
						}
					} else {
						valueChanged = true;
						e.getRowValue().setStartTime(newValue);
					}
					EditDaysPane.reloadTable(dayPosTable, valueChanged);
					timeAlertShowed = false;
				});

				TableColumn<DayPos, String> endTimeCol = new TableColumn<>("End Time");
				endTimeCol.setComparator(new TimeComparator());
				endTimeCol.setCellValueFactory(
						new PropertyValueFactory<DayPos, String>("endTime"));
				endTimeCol.setCellFactory(cellFactoryString);
				endTimeCol.setOnEditCommit(e -> {
					String newValue = e.getNewValue().trim();
					newValue = isViableTime(newValue);
					boolean valueChanged;
					if(newValue == null) {
						valueChanged = false;
						if(!timeAlertShowed && !e.getNewValue().equals("")) {
							timeAlertShowed = true;
							Alert valueNotChangedAlert = new Alert(AlertType.WARNING);
							valueNotChangedAlert.setTitle("Warning");
							valueNotChangedAlert.setHeaderText(e.getNewValue() + " is no viable Input!");
							valueNotChangedAlert.setContentText("Please enter a viable time format!");
							valueNotChangedAlert.showAndWait();
						}
					} else {
						valueChanged = true;
						e.getRowValue().setEndTime(newValue);
					}
					EditDaysPane.reloadTable(dayPosTable, valueChanged);	
					timeAlertShowed = false;
				});

				TableColumn<DayPos, String> descriptionCol = new TableColumn<>("Description");
				descriptionCol.setSortable(false);
				descriptionCol.setCellValueFactory(
						new PropertyValueFactory<DayPos, String>("description"));
				descriptionCol.setCellFactory(cellFactoryString);
				descriptionCol.setOnEditCommit(e -> {
					e.getRowValue().setDescription(e.getNewValue().trim());
					dayPosTable.requestFocus();
				});


				dayPosTable.getColumns().add(salesDocCol);
				dayPosTable.getColumns().add(ltpCodeCol);
				dayPosTable.getColumns().add(startTimeCol);
				dayPosTable.getColumns().add(endTimeCol);
				dayPosTable.getColumns().add(descriptionCol);

				updateTableContent(dayPosTable, dayNameBox.getSelectionModel().getSelectedItem());

				dayPosTable.getSortOrder().add(startTimeCol);

				ContextMenu tableContextMenu = new ContextMenu();

				MenuItem deleteItem = new MenuItem("Delete");
				deleteItem.setOnAction((ActionEvent event) -> {
					dayPosTable.getItems().remove(dayPosTable.getSelectionModel().getSelectedItem());
				});

				tableContextMenu.getItems().addAll(deleteItem);

				dayPosTable.setContextMenu(tableContextMenu);

				tablePane.getChildren().add(dayPosTable);
				StackPane.setAlignment(dayPosTable, Pos.CENTER);

				GridPane randomGrid = new GridPane();
				randomGrid.setPadding(new Insets(1, 0, 5, 0));
				FlowPane variablePane = new FlowPane();
				variablePane.prefWidthProperty().bind(randomGrid.widthProperty());
				variablePane.setHgap(20);
				variablePane.setVgap(10);

				updateVariablesPane(variablePane, dayNameBox.getSelectionModel().getSelectedItem());

				randomGrid.getChildren().add(variablePane);
				GridPane.setConstraints(variablePane, 0, 0);


				dayNameBox.valueProperty().addListener(new ChangeListener<Day>() {

					@Override
					public void changed(ObservableValue<? extends Day> observable, Day oldValue, Day newValue) {
						if (oldValue.equals(newValue)) {
							return;
						}
						updateTableContent(dayPosTable, newValue);
						updateVariablesPane(variablePane, newValue);
					}

				});


				GridPane controlButtonsPane = new GridPane();
				controlButtonsPane.setHgap(8);
				controlButtonsPane.setAlignment(Pos.CENTER);

				CheckBox createMoreBox = new CheckBox("Create More");
				createMoreBox.setPadding(new Insets(0, 20, 0, 0));
				createMoreBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
						StartUpPane.createMore = newValue;
					}
				});
				
				Button startButton = new Button("Start / Stop");
				startButton.setMinSize(55, 25);
				startButton.setPrefWidth(80);

				Text infoText = new Text("Waiting...");

				startButton.setOnAction(e -> {

					// get all variables and their values
					List<Variable> newVariablesList = new LinkedList<>();
					for (Node node : variablePane.getChildren()) {
						GridPane gp = (GridPane) node;
						for (Node gpNode : gp.getChildren()) {
							if (gpNode instanceof TextField) {
								TextField field = (TextField) gpNode;
								newVariablesList.add(new Variable(field.getId(), field.getText().trim()));
							}
						}
					}
					Day givenDay = dayNameBox.getSelectionModel().getSelectedItem();
					givenDay.setVariablesList(newVariablesList);
					givenDay.setDayPosList(DayPos.orderByStartTime(
							DayPos.removeEmpty(dayPosTable.getItems())));
					startWriting(givenDay, infoText, startButton, dayNameBox, tablePane, randomGrid);
					// add the empty line
					dayPosTable.getItems().add(new DayPos());

				});


				controlButtonsPane.getChildren().addAll(createMoreBox, startButton, infoText);
				GridPane.setConstraints(createMoreBox, 0, 0);
				GridPane.setConstraints(startButton, 1, 0);
				GridPane.setConstraints(infoText, 0, 1, 2, 1);
				GridPane.setHalignment(infoText, HPos.CENTER);


				editPane.getChildren().addAll(dayNameBox, tablePane, randomGrid, controlButtonsPane);
				GridPane.setConstraints(dayNameBox, 0, 0);
				GridPane.setConstraints(tablePane, 0, 1);
				GridPane.setConstraints(randomGrid, 0, 2);
				GridPane.setConstraints(controlButtonsPane, 0, 3);

				boolean centerEmpty = mainPane.getCenter() == null;

				mainPane.setCenter(editPane);


				// here to autosize the tablePane
				int waitTime = 70;
				if (centerEmpty) {
					waitTime = 400;
				}
				resize(tablePane, waitTime);


	}
	
	private void updateTableContent (TableView<DayPos> table, Day day) {
		if (day == null) {
			return;
		}
		table.setItems(FXCollections.observableList(day.getDayPosList()));
		if (!table.getItems().contains(new DayPos())) {
			// adding one empty line by default
			table.getItems().add(new DayPos());
		}

	}

	private void updateVariablesPane(FlowPane flowPane, Day day) {
		ObservableList<Node> nodeList = flowPane.getChildren();
		while (!nodeList.isEmpty()) {
			flowPane.getChildren().remove(0);
		}

		List<Variable> variablesList = new LinkedList<>();
		if (day != null) {
			variablesList = day.getVariablesList();
		}
		boolean first = true;
		for(Variable vb : variablesList) {
			GridPane vbPane = new GridPane();

			Label vbLabel = new Label(vb.getKey());
			vbLabel.setFont(Font.font(11));

			TextField vbField = new TextField();
			vbField.setId(vb.getKey());
			vbField.setText(vb.getValue());
			vbField.setTooltip(new Tooltip("Insert the default value of the Variable."));

			vbPane.getChildren().addAll(vbLabel, vbField);

			flowPane.getChildren().add(vbPane);

			if (first) {
				vbField.requestFocus();
				first = false;
			}

			GridPane.setConstraints(vbField, 0, 0);
			GridPane.setConstraints(vbLabel, 0, 1);

		}
	}

	/**
	 * 
	 * @param day
	 * @return is error has been shown
	 */
	private boolean startWriting(Day day, Text infoText, Button stopButton, Node...nodes) {
		verifyDay(day);
		if (day.getErrors().isEmpty()) {
			WritingThread wt = new WritingThread(day, infoText, stopButton, nodes);
			wt.setName(Constants.WRITING_THREAD_NAME);
			wt.start();
		} else {
			Alert errorsAlert = new Alert(AlertType.INFORMATION);
			errorsAlert.setTitle("Information");
			errorsAlert.setHeaderText("There are existing errors! Please fix those.");
			for (DayPosError dpe : DayPosError.sortByPriority(day.getErrors())) {
				errorsAlert.setContentText(errorsAlert.getContentText() + dpe.toString());
			}
			errorsAlert.showAndWait();
			return true;
		}

		return false;
	}

	@Override
	public Day verifyDay(Day day) {
		List<DayPosError> errorsList = new LinkedList<>();
		List<String> variableNames = new LinkedList<>();
		List<DayPos> dpList = DayPos.orderByStartTime(day.getDayPosList());

		for (Variable var : day.getVariablesList()) {
			variableNames.add(var.getKey());
		}

		if (dpList.isEmpty()) {
			errorsList.add(new DayPosError("Day must have at least one Position!", 8));
		}

		// Check every field for errors and add start and end Times to list for later check
		List<LocalTime> startTimes = new LinkedList<>();
		List<LocalTime> endTimes = new LinkedList<>();
		for (int i = 0; i < dpList.size(); i++) {
			DayPos dp = dpList.get(i);
			if (dp.getStartTime().equals("")) {
				errorsList.add(new DayPosError(i+1, "Start Time", "Cannot be left empty!", 3));
			} else {
				startTimes.add(LocalTime.parse(dp.getStartTime()));
			}
			if (dp.getEndTime().equals("")) {
				errorsList.add(new DayPosError(i+1, "End Time", "Cannot be left empty!", 3));
			} else {
				endTimes.add(LocalTime.parse(dp.getEndTime()));
			}

			// Handle Strings of fields
			errorsList.addAll(verifyString(variableNames, dp.getSalesDoc(), "Sales Document", i+1));

			errorsList.addAll(verifyString(variableNames, dp.getLtpCode(), "Ltp Code", i+1));

			errorsList.addAll(verifyString(variableNames, dp.getDescription(), "Description", i+1));
		}

		// Check that startTime is always before endTime
		for (int i = 0; i < startTimes.size(); i++) {
			if (endTimes.get(i) == null) {
				break;
			}
			if(!startTimes.get(i).isBefore(endTimes.get(i))) {
				errorsList.add(new DayPosError(i+1, "End Time", "Start Time must be before End Time!", 4));
			}
		}

		// Check that no times are overlapping 
		for (int i = 0; i < startTimes.size()-1; i++) {
			if (startTimes.get(i) == null) {
				break;
			}
			if(!startTimes.get(i+1).plusSeconds(1).isAfter(endTimes.get(i))) {
				errorsList.add(new DayPosError(i+1, i+2, "Times are not allowed to overlap!", 8));
			}
		}

		day.setErrors(errorsList);

		return day;
	}

	@Override
	public List<DayPosError> verifyString(List<String> variableNames, String stringToVerify, String column, int row ) {
		char[] charArray = stringToVerify.toCharArray();
		List<DayPosError> errorsList = new LinkedList<>();
		String curVariable = "";
		boolean variableOpened = false;
		// this will be set if one ore more special characters in a variablename have been spotted
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] == '\\') {
				// if a '\' is written the next character will be ignored unless it's in a variable
				i++;
				continue;
			} else if (charArray[i] == '$') {
				// handling variables
				if (variableOpened) {
					curVariable = curVariable.trim();

					if (!variableNames.contains(curVariable)) {
						errorsList.add(new DayPosError(row, column, "Cannot declare new Variables! (" 
								+ curVariable + ")", 6));
					}

					// reset parameters
					curVariable = "";
					variableOpened = false;
				} else {
					variableOpened = true;
				}
				continue;
			} else if (charArray[i] == '?') {
				// we don't care about '?'
				continue;
			} else if (charArray[i] == '!') {
				if (row == 1) {
					errorsList.add(new DayPosError(row, column, "Cannot use a \"!\" in the first row!", 2));
				}
				continue;
			}
			if(variableOpened) {
				curVariable += charArray[i];
			}

		}
		if(variableOpened) {
			errorsList.add(new DayPosError(row, column, "Variable was never closed! (" 
					+ curVariable + ")" , 7));
		}
		if(stringToVerify.equals("") && !column.equals("Description")) {
			errorsList.add(new DayPosError(row, column, "Cannot be left empty!", 3));
		}
		return errorsList;
	}

}