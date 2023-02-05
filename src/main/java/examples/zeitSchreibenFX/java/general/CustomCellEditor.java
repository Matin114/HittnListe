package examples.zeitSchreibenFX.java.general;


import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

// Credits: Tomasz Mularczyk
public class CustomCellEditor extends TableCell<DayPos, String>{ 
	private TextField textField;
	private TableView<DayPos> parentTableView;
	public static int numberOfColumns;
	public static boolean needsReEdit = false;
	private boolean editKilled = false;

	public CustomCellEditor(TableView<DayPos> parent) {
		this.parentTableView = parent;
		numberOfColumns = parent.getColumns().size();
	}

	@Override
	public void startEdit(){
		if (!isEmpty()) {
			super.startEdit();
			createTextField();
			setText(null);
			setGraphic(textField);
			textField.requestFocus();
			textField.deselect();
			editKilled = false;
		}
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setText(getItem());
		setGraphic(null);
	}
	
	@Override
	public void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			if (isEditing()) {
				if (textField != null) {
					textField.setText(getString());

				}
				setText(null);
				setGraphic(textField);
			} else {
				setText(getString());
				setGraphic(null);
			}
		}
	}

	private void createTextField() {
		textField = new TextField(getString());
		textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
		textField.focusedProperty().addListener(
				(ObservableValue<? extends Boolean> arg0, 
						Boolean arg1, Boolean arg2) -> {
							if (!arg2 && !editKilled) {
								commitEdit(textField.getText());
							}
						});
		//        textField.setOnKeyReleased(new EventHandler<Event>() {
		//            @Override
		//            public void handle(Event event) {
		//                try{
		//                    String s = textField.getText();
		//                    //digit given...
		//                    if( (s.length()>=0) && (s.length()<10) ){//making sure cell is filled with just one digit
		//                       commitEdit(textField.getText());
		//                       int selectedColumn = parentTableView.getSelectionModel().getSelectedCells().get(0).getColumn(); // gets the number of selected column
		//                       int selectedRow = parentTableView.getSelectionModel().getSelectedCells().get(0).getRow();
		//                       if(selectedColumn < numberOfColumns-1){
		//                           parentTableView.getSelectionModel().selectNext();
		//                           parentTableView.edit(selectedRow, parentTableView.getColumns().get(selectedColumn+1));
		//                       }else{
		//                           parentTableView.getSelectionModel().select(selectedRow+1, parentTableView.getColumns().get(0));
		//                           parentTableView.edit(selectedRow+1, parentTableView.getColumns().get(0));
		//
		//                       }
		//
		//                    }else
		//                       textField.clear();
		//                }catch(NumberFormatException e){
		//                    textField.clear();
		//                }
		//            }
		//        });

		textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				int selectedColumn = parentTableView.getSelectionModel().getSelectedCells().get(0).getColumn(); // gets the number of selected column
				int selectedRow = parentTableView.getSelectionModel().getSelectedCells().get(0).getRow();
				if (event.getCode().equals(KeyCode.ENTER)) {
					// this here is used to commit the changes
					parentTableView.requestFocus();
					if (needsReEdit) {
						needsReEdit = false;
						parentTableView.edit(selectedRow, parentTableView.getColumns().get(selectedColumn));
						return;
					}
				} else if (event.getCode().equals(KeyCode.ESCAPE)) {
					editKilled = true;
				} else if (event.getCode().equals(KeyCode.F8)) {
					// copy the value of the cell above and add it in the textfield
					
					// cannot copy, if no line is above 
					if (selectedRow == 0) {
						return;
					}
					TablePosition<DayPos, String> pos = parentTableView.getSelectionModel().getSelectedCells().get(0);
		            // Item here is the table view type:
		            DayPos itemAbove = parentTableView.getItems().get(selectedRow-1);
		            TableColumn<DayPos, String> colAbove = pos.getTableColumn();
		            // this gives the value in the selected cell:
		            String data = colAbove.getCellObservableValue(itemAbove).getValue();
					textField.setText(data);
					
					
				} else if (event.getCode().equals(KeyCode.TAB)) {
					parentTableView.requestFocus();
					if (needsReEdit) {
						needsReEdit = false;
						parentTableView.edit(selectedRow, parentTableView.getColumns().get(selectedColumn));
						return;
					}
					if (event.isShiftDown()) {
						if(selectedColumn != 0){
							parentTableView.getSelectionModel().selectPrevious();
							selectedRow = parentTableView.getSelectionModel().getSelectedCells().get(0).getRow();
							parentTableView.edit(selectedRow, parentTableView.getColumns().get(selectedColumn-1));
						}else{
							parentTableView.getSelectionModel().select(selectedRow-1, parentTableView.getColumns().get(parentTableView.getColumns().size()-1));
							parentTableView.edit(selectedRow-1, parentTableView.getColumns().get(parentTableView.getColumns().size()-1));

						}
					} else {
						if(selectedColumn < numberOfColumns-1){
							// handling if the selected cell is in the last row
							if (selectedRow == parentTableView.getItems().size()-1) {
								if(!DayPos.containsEmpty(parentTableView.getItems())) {
									parentTableView.getItems().add(new DayPos());
								}
							}
							parentTableView.getSelectionModel().selectNext();
							selectedRow = parentTableView.getSelectionModel().getSelectedCells().get(0).getRow();
							parentTableView.edit(selectedRow, parentTableView.getColumns().get(selectedColumn+1));
						}else{
							parentTableView.getSelectionModel().select(selectedRow+1, parentTableView.getColumns().get(0));
							parentTableView.edit(selectedRow+1, parentTableView.getColumns().get(0));
						}
					}
				} else if (event.getCode().equals(KeyCode.UP)) {
					if (selectedRow > 0) {
						parentTableView.requestFocus();
						if (needsReEdit) {
							needsReEdit = false;
							parentTableView.edit(selectedRow, parentTableView.getColumns().get(selectedColumn));
							return;
						}
						parentTableView.getSelectionModel().select(selectedRow-1, parentTableView.getColumns().get(selectedColumn));
						parentTableView.edit(selectedRow-1, parentTableView.getColumns().get(selectedColumn));
					}
				} else if (event.getCode().equals(KeyCode.DOWN)) {
					if (selectedRow < parentTableView.getItems().size()-1) {
						parentTableView.requestFocus();
						if (needsReEdit) {
							needsReEdit = false;
							parentTableView.edit(selectedRow, parentTableView.getColumns().get(selectedColumn));
							return;
						}
						parentTableView.getSelectionModel().select(selectedRow+1, parentTableView.getColumns().get(selectedColumn));
						parentTableView.edit(selectedRow+1, parentTableView.getColumns().get(selectedColumn));
					}
				}
			}
		});
	}

	private String getString() {
		return getItem() == null ? "" : getItem().toString();
	}
}