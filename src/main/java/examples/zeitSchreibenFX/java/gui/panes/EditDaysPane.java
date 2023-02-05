package examples.zeitSchreibenFX.java.gui.panes;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.sun.javafx.collections.ObservableListWrapper;

import examples.zeitSchreibenFX.java.general.Constants;
import examples.zeitSchreibenFX.java.general.CustomCellEditor;
import examples.zeitSchreibenFX.java.general.Day;
import examples.zeitSchreibenFX.java.general.DayPos;
import examples.zeitSchreibenFX.java.general.DayPosError;
import examples.zeitSchreibenFX.java.general.TimeComparator;
import examples.zeitSchreibenFX.java.gui.SceneChanger;
import examples.zeitSchreibenFX.java.gui.interfaces.DefaultPaneImpl;
import examples.zeitSchreibenFX.java.start.StartStundenClient;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class EditDaysPane extends DefaultPaneImpl {

	// this boolean is used in every setOnEditCommit method, since the code in it get called twice for some reason
	private boolean timeAlertShowed = false;
	private Day localDay;

	public void init(Stage stage, Day day) {
		// needed because of a scope problem
		localDay = day;

		System.out.println("reached EditDaysPane.init");
		BorderPane mainPane = (BorderPane) stage.getScene().getRoot();


		GridPane editPane = new GridPane();
		editPane.setPadding(new Insets(10, 30, 10, 30));

		StackPane tablePane = new StackPane();
		tablePane.prefWidthProperty().bind(editPane.widthProperty());
		tablePane.prefHeightProperty().bind(editPane.heightProperty());
		tablePane.setPadding(new Insets(0, 0, 5, 0));

		TextField dayNameField = new TextField();
		dayNameField.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
	        	int maxLength = Constants.NAME_MAX_CHARS;
	            if (dayNameField.getText().length() > maxLength) {
	                String s = dayNameField.getText().substring(0, maxLength);
	                dayNameField.setText(s);
	            }
	        }
	    });
		dayNameField.setPromptText("Enter Name");
		if (localDay != null) {
			dayNameField.setText(localDay.getId());
		}


		TableView<DayPos> dayPosTable = new TableView<>();
		dayPosTable.setEditable(true);
		//		dayPosTable.setSortPolicy(e -> {return false;});
		dayPosTable.getSelectionModel().setCellSelectionEnabled(true);
		dayPosTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		dayPosTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getButton().equals(MouseButton.PRIMARY)){
					if(!DayPos.containsEmpty(dayPosTable.getItems())) {
						dayPosTable.getItems().add(new DayPos());
					}
				}
			}
		});

		Callback<TableColumn<DayPos, String>, TableCell<DayPos, String>> cellFactoryString = (TableColumn<DayPos, String> p) -> new CustomCellEditor(dayPosTable);


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
					valueNotChangedAlert.showAndWait();
				}
			} else {
				valueChanged = true;
				e.getRowValue().setStartTime(newValue);
			}
			reloadTable(dayPosTable, valueChanged);
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
			reloadTable(dayPosTable, valueChanged);	
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


		if (localDay != null) {
			ObservableList<DayPos> dayData =
					FXCollections.observableArrayList(localDay.getDayPosList());
			dayPosTable.setItems(dayData);
		}
		// adding one empty line by default
		dayPosTable.getItems().add(new DayPos());

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


		GridPane controlButtonsPane = new GridPane();
		controlButtonsPane.setHgap(8);
		controlButtonsPane.setAlignment(Pos.CENTER_RIGHT);

		GridPane backNextButtonPane = new GridPane();
		backNextButtonPane.setHgap(1);

		Button backButton = new Button("< Back");
		backButton.setMinSize(55, 25);
		backButton.setPrefWidth(80);
		backButton.setDisable(true);

		Button nextButton = new Button("Next >");
		nextButton.setMinSize(55, 25);
		nextButton.setPrefWidth(80);

		backNextButtonPane.getChildren().addAll(backButton, nextButton);
		GridPane.setConstraints(backButton, 0, 0);
		GridPane.setConstraints(nextButton, 1, 0);


		Button cancelButton = new Button("Cancel");
		cancelButton.setMinSize(55, 25);
		cancelButton.setPrefWidth(80);

		nextButton.addEventHandler(MouseEvent.MOUSE_CLICKED, 
				new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Day completeDay = new Day(dayNameField.getText().trim(), 
						DayPos.removeEmpty(dayPosTable.getItems()));
				if (localDay != null) {
					completeDay.setOldId(localDay.getOldId());
					completeDay.setVariablesList(localDay.getVariablesList());
				}
				completeDay = verifyDay(completeDay);
				if(completeDay.getErrors().isEmpty()) { 
					if (completeDay.getVariablesList().isEmpty()) {
						Alert saveAlert = new Alert(AlertType.CONFIRMATION);
						saveAlert.getButtonTypes().clear();
						saveAlert.getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);
						saveAlert.setTitle("Confirmation");
						saveAlert.setHeaderText("Save \"" + completeDay.getId() + "\"?");
						saveAlert.setContentText("Do you want to save the day \"" + completeDay.getId() + "\"?");
						Optional<ButtonType> saveResult = saveAlert.showAndWait();
						if(saveResult.get() != ButtonType.YES) {
							return;
						}


						StartStundenClient.xmlTransfer.addNewDay(completeDay);

						// setting oldId since it got created just now 
						if (localDay == null) {
							localDay = new Day(completeDay);
						}
						localDay.setOldId(completeDay.getId());

						Alert informationAlert = new Alert(AlertType.INFORMATION);
						informationAlert.getButtonTypes().clear();
						informationAlert.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
						informationAlert.setTitle("Information");
						informationAlert.setHeaderText("Day \"" + completeDay.getId() + "\" was successfully ");
						if (completeDay.getId().equals(completeDay.getOldId())) {
							informationAlert.setHeaderText(informationAlert.getHeaderText() + "edited!");
						} else {
							informationAlert.setHeaderText(informationAlert.getHeaderText() + "created!");
						}
						informationAlert.setContentText("You will be redirected to the select day screen.");

						Optional<ButtonType> result = informationAlert.showAndWait();
						if(result.get() != ButtonType.CANCEL) {
							SceneChanger.openSelectDay(stage);
						}
					} else {
						SceneChanger.openEnterVariableValues(stage, completeDay);
					}
				} else {
					// adding one empty line since it got removed
					dayPosTable.getItems().add(new DayPos());
					Alert errorsAlert = new Alert(AlertType.INFORMATION);
					errorsAlert.setTitle("Information");
					errorsAlert.setHeaderText("There are existing errors! Please fix those.");
					for (DayPosError dpe : DayPosError.sortByPriority(completeDay.getErrors())) {
						errorsAlert.setContentText(errorsAlert.getContentText() + dpe.toString());
					}
					errorsAlert.showAndWait();
				}
			}

		});

		cancelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, 
				new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Alert confirmAlert = new Alert(AlertType.WARNING);
				confirmAlert.getButtonTypes().clear();
				confirmAlert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
				confirmAlert.setTitle("Warning");
				confirmAlert.setHeaderText("Do you really want to go back?");
				confirmAlert.setContentText("All changes will be lost unrecoverably!");

				Optional<ButtonType> result = confirmAlert.showAndWait();
				if(result.get() == ButtonType.YES) {
					SceneChanger.openSelectDay(stage);
				}
			}
		});

		controlButtonsPane.getChildren().addAll(backNextButtonPane, cancelButton);
		GridPane.setConstraints(backNextButtonPane, 0, 0);
		GridPane.setConstraints(cancelButton, 1, 0);


		editPane.getChildren().addAll(dayNameField, tablePane, controlButtonsPane);
		GridPane.setConstraints(dayNameField, 0, 0);
		GridPane.setConstraints(tablePane, 0, 1);
		GridPane.setConstraints(controlButtonsPane, 0, 2);

		// Here so the default focus gets lost
		mainPane.requestFocus();

		mainPane.setCenter(editPane);
	}

	public static void reloadTable(TableView<DayPos> dayPosTable, boolean valueChanged) {
		// Reselect the edited Cell for the CustomCellEditor to work
		int selectedColumn = dayPosTable.getSelectionModel().getSelectedCells().get(0).getColumn();
		int selectedRow = dayPosTable.getSelectionModel().getSelectedCells().get(0).getRow();
		DayPos selectedDayPos = dayPosTable.getSelectionModel().getSelectedItem();

		List<DayPos> dpList = new LinkedList<>();
		List<TableColumn<DayPos, ?>> sortOrderList = new LinkedList<>();
		// Here all DayPoses get saved in a List
		for (DayPos dp : dayPosTable.getItems()) {
			dpList.add(dp);
		}
		// Here all Sorts get saved in a List
		for (TableColumn<DayPos, ?> tc : dayPosTable.getSortOrder()) {
			sortOrderList.add(tc);
		}


		dayPosTable.getItems().clear();
		dayPosTable.setItems(new ObservableListWrapper<DayPos>(dpList));
		for(TableColumn<DayPos, ?> tc : sortOrderList) {
			dayPosTable.getSortOrder().add(tc);
		}

		dayPosTable.getSelectionModel().select(selectedDayPos);
		if (valueChanged) {
			selectedRow = dayPosTable.getSelectionModel().getSelectedCells().get(0).getRow();
			dayPosTable.getSelectionModel().select(selectedRow, dayPosTable.getColumns().get(selectedColumn));
		} else {
			CustomCellEditor.needsReEdit = true;
			dayPosTable.getSelectionModel().select(selectedRow, dayPosTable.getColumns().get(selectedColumn));
		}
	}

}
