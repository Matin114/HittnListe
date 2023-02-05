package examples.zeitSchreibenFX.java.gui.panes;

import java.util.Optional;

import examples.zeitSchreibenFX.java.general.Day;
import examples.zeitSchreibenFX.java.gui.SceneChanger;
import examples.zeitSchreibenFX.java.gui.interfaces.DefaultPaneImpl;
import examples.zeitSchreibenFX.java.start.StartStundenClient;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class SelectDayPane extends DefaultPaneImpl {

	@Override
	public void init(Stage stage) {
		System.out.println("reached SelectPane.init");
		BorderPane mainPane = (BorderPane) stage.getScene().getRoot();

		GridPane selectPane = new GridPane();
		selectPane.setPadding(new Insets(10, 30, 10, 30));
		selectPane.setVgap(5);

		StackPane tablePane = new StackPane();
		tablePane.prefWidthProperty().bind(selectPane.widthProperty());
		tablePane.prefHeightProperty().bind(selectPane.heightProperty());

		TableView<Day> daysTable = new TableView<>();
		daysTable.getSelectionModel().setCellSelectionEnabled(true);
		daysTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<Day, String> daysCol = new TableColumn<>("Day");
		daysCol.setCellValueFactory(
				new PropertyValueFactory<Day, String>("id"));

		daysTable.getColumns().add(daysCol);

		ObservableList<Day> data = FXCollections.observableArrayList(
				StartStundenClient.xmlTransfer.getAllDays());
		daysTable.setItems(data);


		ContextMenu tableContextMenu = new ContextMenu();

		MenuItem editItem = new MenuItem("Edit");
		editItem.setOnAction((ActionEvent event) -> {
			Day day = daysTable.getSelectionModel().getSelectedItem();
			if (day == null) {
				return;
			}
			day.setOldId(day.getId());
			SceneChanger.openEditDays(stage, day);
		});

		MenuItem deleteItem = new MenuItem("Delete");
		deleteItem.setOnAction((ActionEvent event) -> {
			Day selectedDay =  daysTable.getSelectionModel().getSelectedItem();
			if (selectedDay == null) {
				return;
			}
			Alert confirmAlert = new Alert(AlertType.WARNING);
			confirmAlert.getButtonTypes().clear();
			confirmAlert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
			confirmAlert.setTitle("Warning");
			confirmAlert.setHeaderText("Do you really want to delete \"" 
					+ selectedDay.getId() + "\"?");
			// TODO (Optional) gelöschte in anderes XML schreiben lassen 
			confirmAlert.setContentText("It will be lost unrecoverably!");

			Optional<ButtonType> result = confirmAlert.showAndWait();
			if(result.get() == ButtonType.YES) {
				StartStundenClient.xmlTransfer.deleteDay(daysTable.getSelectionModel().getSelectedItem());
				daysTable.getItems().remove(daysTable.getSelectionModel().getSelectedItem());
			}
		});

		tableContextMenu.getItems().addAll(editItem, deleteItem);
		
		daysTable.setContextMenu(tableContextMenu);


		Button addButton = new Button("ADD NEW");
		addButton.setFont(Font.font("System", FontWeight.BOLD, 15));
		addButton.prefWidthProperty().bind(selectPane.widthProperty());

		addButton.addEventHandler(MouseEvent.MOUSE_CLICKED, 
				new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				SceneChanger.openEditDays(stage);
			}
		});

		tablePane.getChildren().add(daysTable);
		StackPane.setAlignment(daysTable, Pos.CENTER);

		selectPane.getChildren().addAll(tablePane, addButton);
		GridPane.setConstraints(tablePane, 0, 0);
		GridPane.setConstraints(addButton, 0, 1);

		// Here so the default focus gets lost
		mainPane.requestFocus();

		mainPane.setCenter(selectPane);
	}

}
