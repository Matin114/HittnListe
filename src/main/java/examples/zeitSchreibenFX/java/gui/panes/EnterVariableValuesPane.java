package examples.zeitSchreibenFX.java.gui.panes;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import examples.zeitSchreibenFX.java.general.Day;
import examples.zeitSchreibenFX.java.general.DayPos;
import examples.zeitSchreibenFX.java.general.TimeComparator;
import examples.zeitSchreibenFX.java.general.Variable;
import examples.zeitSchreibenFX.java.gui.SceneChanger;
import examples.zeitSchreibenFX.java.gui.interfaces.DefaultPaneImpl;
import examples.zeitSchreibenFX.java.start.StartStundenClient;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class EnterVariableValuesPane extends DefaultPaneImpl {

	public void init(Stage stage, Day day) {
		System.out.println("reached EnterVariableValuesPane.init");
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

		TextField dayNameField = new TextField();
		dayNameField.setEditable(false);
		dayNameField.setText(day.getId());


		TableView<DayPos> dayPosTable = new TableView<>();
		dayPosTable.setEditable(false);
		dayPosTable.getSelectionModel().setCellSelectionEnabled(true);
		dayPosTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<DayPos, String> salesDocCol = new TableColumn<>("Sales Document");
		salesDocCol.setEditable(true);
		salesDocCol.setSortable(false);
		salesDocCol.setCellValueFactory(
				new PropertyValueFactory<DayPos, String>("salesDoc"));

		TableColumn<DayPos, String> ltpCodeCol = new TableColumn<>("Ltp Code");
		ltpCodeCol.setSortable(false);
		ltpCodeCol.setCellValueFactory(
				new PropertyValueFactory<DayPos, String>("ltpCode"));

		TableColumn<DayPos, String> startTimeCol = new TableColumn<>("Start Time");
		startTimeCol.setSortType(SortType.ASCENDING);
		startTimeCol.setComparator(new TimeComparator());
		startTimeCol.setCellValueFactory(
				new PropertyValueFactory<DayPos, String>("startTime"));

		TableColumn<DayPos, String> endTimeCol = new TableColumn<>("End Time");
		endTimeCol.setComparator(new TimeComparator());
		endTimeCol.setCellValueFactory(
				new PropertyValueFactory<DayPos, String>("endTime"));

		TableColumn<DayPos, String> descriptionCol = new TableColumn<>("Description");
		descriptionCol.setSortable(false);
		descriptionCol.setCellValueFactory(
				new PropertyValueFactory<DayPos, String>("description"));


		dayPosTable.getColumns().add(salesDocCol);
		dayPosTable.getColumns().add(ltpCodeCol);
		dayPosTable.getColumns().add(startTimeCol);
		dayPosTable.getColumns().add(endTimeCol);
		dayPosTable.getColumns().add(descriptionCol);


		ObservableList<DayPos> dayData =
				FXCollections.observableArrayList(day.getDayPosList());
		dayPosTable.setItems(dayData);
		dayPosTable.getSortOrder().add(startTimeCol);

		tablePane.getChildren().add(dayPosTable);
		StackPane.setAlignment(dayPosTable, Pos.TOP_CENTER);

		GridPane randomGrid = new GridPane();
		randomGrid.setPadding(new Insets(1, 0, 5, 0));
		FlowPane variablePane = new FlowPane();
		variablePane.prefWidthProperty().bind(randomGrid.widthProperty());
		variablePane.setHgap(20);
		variablePane.setVgap(10);

		List<Variable> variablesList = day.getVariablesList();
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

			variablePane.getChildren().add(vbPane);

			if (first) {
				vbField.requestFocus();
				first = false;
			}

			GridPane.setConstraints(vbField, 0, 0);
			GridPane.setConstraints(vbLabel, 0, 1);

		}

		randomGrid.getChildren().add(variablePane);
		GridPane.setConstraints(variablePane, 0, 0);


		GridPane controlButtonsPane = new GridPane();
		controlButtonsPane.setHgap(8);
		controlButtonsPane.setAlignment(Pos.CENTER_RIGHT);

		GridPane backNextButtonPane = new GridPane();
		backNextButtonPane.setHgap(1);

		Button backButton = new Button("< Back");
		backButton.setMinSize(55, 25);
		backButton.setPrefWidth(80);

		Button nextButton = new Button("Finish");
		nextButton.setMinSize(55, 25);
		nextButton.setPrefWidth(80);

		backNextButtonPane.getChildren().addAll(backButton, nextButton);
		GridPane.setConstraints(backButton, 0, 0);
		GridPane.setConstraints(nextButton, 1, 0);


		Button cancelButton = new Button("Cancel");
		cancelButton.setMinSize(55, 25);
		cancelButton.setPrefWidth(80);

		backButton.addEventHandler(MouseEvent.MOUSE_CLICKED, 
				new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				List<Variable> newVariablesList = new LinkedList<>();
				for (Node node : variablePane.getChildren()) {
					GridPane gp = (GridPane) node;
					for (Node gpNode : gp.getChildren()) {
						if (gpNode instanceof TextField) {
							TextField field = (TextField) gpNode;
							System.out.println(field.getId());
							newVariablesList.add(new Variable(field.getId(), field.getText().trim()));
						}
					}

				}
				day.setVariablesList(newVariablesList);
				stage.widthProperty().removeListener(resizeListener);
				SceneChanger.openEditDays(stage, day);
			}

		});


		nextButton.addEventHandler(MouseEvent.MOUSE_CLICKED, 
				new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				Alert saveAlert = new Alert(AlertType.CONFIRMATION);
				saveAlert.getButtonTypes().clear();
				saveAlert.getButtonTypes().addAll(ButtonType.YES, ButtonType.CANCEL);
				saveAlert.setTitle("Confirmation");
				saveAlert.setHeaderText("Save \"" + day.getId() + "\"?");
				saveAlert.setContentText("Do you want to save the day \"" + day.getId() + "\"?");
				Optional<ButtonType> saveResult = saveAlert.showAndWait();
				if(saveResult.get() != ButtonType.YES) {
					return;
				}


				List<Variable> newVariablesList = new LinkedList<>();
				for (Node node : variablePane.getChildren()) {
					GridPane gp = (GridPane) node;
					for (Node gpNode : gp.getChildren()) {
						if (gpNode instanceof TextField) {
							TextField field = (TextField) gpNode;
							System.out.println(field.getId());
							newVariablesList.add(new Variable(field.getId(), field.getText().trim()));
						}
					}
				}
				day.setVariablesList(newVariablesList);

				StartStundenClient.xmlTransfer.addNewDay(day);

				// setting oldId since it got created just now 
				day.setOldId(day.getId());

				Alert informationAlert = new Alert(AlertType.INFORMATION);
				informationAlert.getButtonTypes().clear();
				informationAlert.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
				informationAlert.setTitle("Information");
				informationAlert.setHeaderText("Day \"" + day.getId() + "\" was successfully ");
				if (day.getId().equals(day.getOldId())) {
					informationAlert.setHeaderText(informationAlert.getHeaderText() + "edited!");
				} else {
					informationAlert.setHeaderText(informationAlert.getHeaderText() + "created!");
				}
				informationAlert.setContentText("You will be redirected to the select day screen.");


				Optional<ButtonType> result = informationAlert.showAndWait();
				if(result.get() != ButtonType.CANCEL) {
					stage.widthProperty().removeListener(resizeListener);
					SceneChanger.openSelectDay(stage);
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
					stage.widthProperty().removeListener(resizeListener);
					SceneChanger.openSelectDay(stage);
				}
			}
		});

		controlButtonsPane.getChildren().addAll(backNextButtonPane, cancelButton);
		GridPane.setConstraints(backNextButtonPane, 0, 0);
		GridPane.setConstraints(cancelButton, 1, 0);


		editPane.getChildren().addAll(dayNameField, tablePane, randomGrid, controlButtonsPane);
		GridPane.setConstraints(dayNameField, 0, 0);
		GridPane.setConstraints(tablePane, 0, 1);
		GridPane.setConstraints(randomGrid, 0, 2);
		GridPane.setConstraints(controlButtonsPane, 0, 3);

		mainPane.setCenter(editPane);

		// Used to gain focus in the first textfield
		GridPane gp = (GridPane) variablePane.getChildren().get(0);
		for (Node gpNode : gp.getChildren()) {
			if (gpNode instanceof TextField) {
				gpNode.requestFocus();
				break;
			}
		}

		// here to autosize the tablePane
		resize(tablePane, 70);



		// shit i tried to autosize the tablePane
		//----------------------------------------------------------------------
		//		stage.widthProperty().addListener((observable, oldValue, newValue) -> {
		//			if(oldValue.doubleValue()-newValue.doubleValue() >= 10 || oldValue.doubleValue()-newValue.doubleValue() <= -10) {
		//				stage.setWidth(stage.getWidth()+1);
		//				System.out.println("resized");
		//			}
		//		});
		//		
		//		stage.setWidth(stage.getWidth()+10);


		//----------------------------------------------------------------------

		//		GridPane gp = (GridPane) variablePane.getChildren().get(0);
		//		for (Node gpNode : gp.getChildren()) {
		//			if (gpNode instanceof TextField) {
		//				gpNode.requestFocus();
		////				MyThread th = new MyThread(gpNode);
		////				th.start();
		//				break;
		//			}
		//		}

		//		stage.widthProperty().addListener((observable, oldValue, newValue) -> {
		//			Node focusedNode = stage.getScene().getFocusOwner();
		//			System.out.println(focusedNode);
		//			mainPane.requestFocus();
		//			try {
		//				Thread.sleep(1);
		//			} catch (InterruptedException e) {
		//				e.printStackTrace();
		//			}
		//			
		//			Event.fireEvent(focusedNode, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0,
		//	                0, 0, 0, MouseButton.PRIMARY, 1, false, false, false, false,
		//	                true, false, false, false, false, true, null));
		//			
		//		});


	}

}
