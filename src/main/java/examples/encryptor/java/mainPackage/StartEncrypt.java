package examples.encryptor.java.mainPackage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StartEncrypt extends Application {

	private Stage mainStage;
	private Scene scene;
	private BorderPane mainPane;
	private GridPane encryptGridPane;
	private GridPane encryptPlusGridPane;
	private GridPane decryptGridPane;
	private ScrollPane infoPane;

	private Button encryptButton;
	private TextField passField;
	private TextField encryptedResultField;

	private Button encryptPlusButton;
	private TextArea passPlusArea;
	private TextArea encryptedPlusResultArea;

	private Button decryptButton;
	private TextArea decryptedResultArea;
	private TextArea encryptedPassArea;

	private Text infoLabel;

	private MenuBar menuBar;
	private Menu modeMenu;
	private MenuItem encryptMenuItem;
	private MenuItem encryptPlusMenuItem;
	private MenuItem decryptMenuItem;
	private MenuItem infoMenuItem;

	private final String ENCRYPTION = "Encryption";
	private final String ENCRYPTIONPLUS = "Encryption++";
	private final String DECRYPTION = "Decryption";
	private final String INFO = "Info";

	private EncryptLogic encryptLogic;
	private DecryptLogic decryptLogic;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		mainStage = stage;
		mainStage.setMinHeight(210);
		mainStage.setMinWidth(400);
		encryptLogic = new EncryptLogic();
		decryptLogic = new DecryptLogic();
		mainStage.setTitle(ENCRYPTION);

		encryptMenuItem = new MenuItem("Encrypt");
		encryptPlusMenuItem = new MenuItem("Encrypt++");
		decryptMenuItem = new MenuItem("Decrypt");
		infoMenuItem = new MenuItem("Info");

		modeMenu = new Menu("Mode");
		modeMenu.getItems().addAll(encryptMenuItem, encryptPlusMenuItem, decryptMenuItem, new SeparatorMenuItem(), infoMenuItem);

		menuBar = new MenuBar();
		menuBar.getMenus().addAll(modeMenu);


		setUpEncryptPane();
		setUpEncryptPlusPane();
		setUpDecryptPane();
		setUpInfoPane();


		mainPane = new BorderPane();
		mainPane.setCenter(encryptGridPane);
		mainPane.setTop(menuBar);

		setListeners();


		scene = new Scene(mainPane);

		mainStage.setScene(scene);
		mainStage.show();

	}

	private void setUpEncryptPane() {
		passField = new TextField(); 
		passField.setPromptText("Enter password");
		passField.setTooltip(new Tooltip("Enter up to 30 characters"));
		addTextLimiter(passField, 30);
		GridPane.setConstraints(passField, 0, 0);

		encryptedResultField = new TextField();
		encryptedResultField.setMinWidth(330);
		encryptedResultField.setPromptText("(go for it)");
		encryptedResultField.setEditable(false);
		GridPane.setConstraints(encryptedResultField, 0, 1);

		encryptButton = new Button("Encrypt");
		encryptButton.setMinWidth(75);
		GridPane.setConstraints(encryptButton, 0, 2);

		encryptGridPane = new GridPane();
		encryptGridPane.setPadding(new Insets(15, 30, 10, 30));
		encryptGridPane.setVgap(10);
		encryptGridPane.setHgap(5);
		encryptGridPane.getChildren().addAll(passField, encryptButton, encryptedResultField);
	}

	private void setUpEncryptPlusPane() {
		passPlusArea = new TextArea(); 
		passPlusArea.setPromptText("Enter Text");
		passPlusArea.setWrapText(true);
		passPlusArea.setTooltip(new Tooltip("Enter up to 999 characters"));
		addTextLimiter(passPlusArea, 999);
		GridPane.setConstraints(passPlusArea, 0, 0);

		encryptedPlusResultArea = new TextArea();
		encryptedPlusResultArea.setMinWidth(330);
		encryptedPlusResultArea.setWrapText(true);
		encryptedPlusResultArea.setEditable(false);
		GridPane.setConstraints(encryptedPlusResultArea, 0, 1);

		encryptPlusButton = new Button("Encrypt++");
		encryptPlusButton.setMinWidth(75);
		GridPane.setConstraints(encryptPlusButton, 0, 2);

		encryptPlusGridPane = new GridPane();
		encryptPlusGridPane.setPadding(new Insets(15, 30, 10, 30));
		encryptPlusGridPane.setVgap(10);
		encryptPlusGridPane.setHgap(5);
		encryptPlusGridPane.getChildren().addAll(passPlusArea, 
				encryptedPlusResultArea, encryptPlusButton);
	}

	private void setUpDecryptPane() {
		encryptedPassArea = new TextArea(); 
		encryptedPassArea.setPromptText("Enter encrypted password");
		encryptedPassArea.setWrapText(true);;
		encryptedPassArea.setMinWidth(330);
		encryptedPassArea.setTooltip(new Tooltip("Enter encrypted Text"));
		GridPane.setConstraints(encryptedPassArea, 0, 0);

		decryptedResultArea = new TextArea();
		decryptedResultArea.setPromptText("(decrypted password)");
		decryptedResultArea.setWrapText(true);;
		decryptedResultArea.setEditable(false);
		GridPane.setConstraints(decryptedResultArea, 0, 1);

		decryptButton = new Button("Decrypt");
		decryptButton.setMinWidth(75);
		GridPane.setConstraints(decryptButton, 0, 2);

		decryptGridPane = new GridPane();
		decryptGridPane.setPadding(new Insets(15, 30, 10, 30));
		decryptGridPane.setVgap(10);
		decryptGridPane.setHgap(5);
		decryptGridPane.getChildren().addAll(encryptedPassArea, decryptedResultArea, decryptButton);

	}

	private void setUpInfoPane() {
		StringBuffer sb = new StringBuffer();

		// get all lines from the Patchnotes file
		InputStreamReader isr = new InputStreamReader(getClass().getClassLoader().getResourceAsStream("examples/encryptor/PatchNotes"));
		BufferedReader br = new BufferedReader(isr);
		List<String> notesList =  br.lines().collect(Collectors.toList());
		for(String line : notesList) {
			sb.append(line + "\n");
		}

		infoLabel = new Text(sb.toString());
		infoLabel.wrappingWidthProperty().bind(mainStage.widthProperty());

		infoPane = new ScrollPane(infoLabel);
		infoPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		infoPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
	}

	private void setListeners() {
		// ########################ENCRYPT#################################
		passField.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().equals(KeyCode.ENTER)) {
					encrypt();
				} else {
					encryptedResultField.setText("");
				}
			}
		}); 

		encryptButton.addEventHandler(MouseEvent.MOUSE_CLICKED, 
				new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton().equals(MouseButton.PRIMARY)) {
					encrypt();
				}
			}
		});

		// ########################ENCRYPT++###############################

		encryptPlusButton.addEventHandler(MouseEvent.MOUSE_CLICKED, 
				new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton().equals(MouseButton.PRIMARY)) {
					encryptPlus();
				}
			}
		});

		// ########################DECRYPT#################################

		//				encryptedPassArea.setOnKeyPressed(new EventHandler<KeyEvent>() {
		//					@Override
		//					public void handle(KeyEvent event) {
		//						if(event.getCode().equals(KeyCode.ENTER)) {
		//							decrypt();
		//						} else {
		//							decryptedResultArea.setText("");
		//						}
		//					}
		//				}); 

		decryptButton.addEventHandler(MouseEvent.MOUSE_CLICKED, 
				new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.getButton().equals(MouseButton.PRIMARY)) {
					decrypt();
				}
			}
		});

		// ##########################MENUS#################################

		encryptMenuItem.setOnAction(e -> {
			if (mainPane.getCenter() != encryptGridPane) {
				changeCenterPanel(ENCRYPTION);
			}
		});

		encryptPlusMenuItem.setOnAction(e -> {
			if (mainPane.getCenter() != encryptPlusGridPane) {
				changeCenterPanel(ENCRYPTIONPLUS);
			}
		});

		decryptMenuItem.setOnAction(e -> {
			if (mainPane.getCenter() != decryptGridPane) {
				changeCenterPanel(DECRYPTION);
			}
		});

		infoMenuItem.setOnAction(e -> {
			if (mainPane.getCenter() != infoPane) {
				changeCenterPanel(INFO);
			}
		});
	}

	private void changeCenterPanel(String panel) {
		mainStage.setTitle(panel);
		if(mainStage.getWidth() < 400) {
			mainStage.setWidth(400);
		}
		switch (panel) {
		case ENCRYPTION:
			mainPane.setCenter(encryptGridPane);
			if(mainStage.getHeight() < 210 || mainStage.getHeight() == 350) {
				mainStage.setHeight(210);
			}
			break;
		case ENCRYPTIONPLUS:
			mainPane.setCenter(encryptPlusGridPane);
			if(mainStage.getHeight() < 350) {
				mainStage.setHeight(350);
			}
			break;
		case DECRYPTION:
			mainPane.setCenter(decryptGridPane);
			if(mainStage.getHeight() < 350) {
				mainStage.setHeight(350);
			}
			break;
		case INFO: 
			mainPane.setCenter(infoPane);
			if(mainStage.getHeight() < 210 || mainStage.getHeight() == 350) {
				mainStage.setHeight(210);
			}
			break;
		}
	}

	public static void addTextLimiter(final TextInputControl tic, final int maxLength) {
		//Credits: ceklock (StackOverflow)
		tic.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
				if (tic.getText().length() > maxLength) {
					String s = tic.getText().substring(0, maxLength);
					tic.setText(s);
				}
			}
		});
	}

	private void encrypt() {
		try {
			if(!passField.getText().equals("")) {
				encryptedResultField.setText(encryptLogic.encrypt(passField.getText().trim()));
			}
		} catch (EncryptException e) {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Input not valid");
			errorAlert.setContentText(e.getMessage() + e.getProblem());
			errorAlert.showAndWait();
		}
	}

	private void encryptPlus() {
		try {
			// TODO encryptPlus method in encryptLogic
			encryptedPlusResultArea.setText(encryptLogic.encryptPlus(passPlusArea.getText().trim()));
		} catch (EncryptException e) {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Input not valid");
			errorAlert.setContentText(e.getMessage() + e.getProblem());
			errorAlert.showAndWait();
		}
	}

	private void decrypt() {
		try {
			if(!encryptedPassArea.getText().equals("")) {
				decryptedResultArea.setText(decryptLogic.decrypt(encryptedPassArea.getText().trim()));
			}
		} catch (EncryptException e) {
			Alert errorAlert = new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Input not valid");
			errorAlert.setContentText(e.getMessage());
			errorAlert.showAndWait();
		}
	}
}
