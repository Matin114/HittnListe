package examples.zeitSchreibenFX.java.gui.threads;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;

import com.sun.glass.events.KeyEvent;

import examples.zeitSchreibenFX.java.general.Day;
import examples.zeitSchreibenFX.java.general.DayPos;
import examples.zeitSchreibenFX.java.general.Variable;
import examples.zeitSchreibenFX.java.gui.panes.StartUpPane;
import examples.zeitSchreibenFX.java.start.StartStundenClient;
import examples.zeitSchreibenFX.java.xmlConverter.XMLTransfer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class WritingThread extends Thread implements Runnable {

	private Day day;
	private Text infoText;
	private Button stopButton;
	private Node[] nodes;
	private Robot robo;

	public  WritingThread(Day day, Text infoText, Button stopButton, Node...nodes ) {
		this.day = day;
		this.infoText = infoText;
		this.stopButton = stopButton;
		this.nodes = nodes; 

		try {
			robo = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		boolean oldNumState = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK);
		EventHandler<ActionEvent> oldStopButtonEventHandler =  stopButton.getOnAction();

		StartStundenClient.menuBarAllowed = false;
		for (Node node : nodes) {
			node.setDisable(true);
		}

		EventHandler<ActionEvent> stopEventHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("kill now");
				interrupt();
			}
		};
		//		stopButton.setText("Stop");
		stopButton.setOnAction(stopEventHandler);


		try {

			int waitTime = Integer.valueOf(StartStundenClient.xmlTransfer.getDefaultValue(XMLTransfer.WAITINGTIME_TAG));
			for (int i = waitTime; i > 0; i--) {
				infoText.setText("Starting in " + i + "s.");
				Thread.sleep(1000);
			}
			infoText.setText("Running");

			for (int i = 0; i < day.getDayPosList().size(); i++) {
				DayPos dp = day.getDayPosList().get(i);
				writeString(dp.getSalesDoc(), XMLTransfer.SALESDOC_TAG, i+1);
				writeTabs(1, false, false);
				writeString(dp.getLtpCode(), XMLTransfer.LTPCODE_TAG, i+1);
				writeTabs(6, false, false);
				writeString(dp.getStartTime(), XMLTransfer.STARTTIME_TAG, i+1);
				writeTabs(1, false, false);
				writeString(dp.getEndTime(), XMLTransfer.ENDTIME_TAG, i+1);
				writeTabs(1, false, false);
				writeString(dp.getDescription(), XMLTransfer.DESCRIPTION_TAG, i+1);
				writeTabs(9, true, true);
			}



			Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, oldNumState);

			if (!StartUpPane.createMore) {
				Platform.exit();
			}
		} catch (InterruptedException | NullPointerException e) {
			e.printStackTrace();
		}
		StartStundenClient.menuBarAllowed = true;
		for (Node node : nodes) {
			node.setDisable(false);
		}

		//			stopButton.setText(oldStopButtonText);
		stopButton.setOnAction(oldStopButtonEventHandler);

		infoText.setText("Waiting...");
	}

	private void writeString (String string, String column, int row) throws InterruptedException {
		boolean ignoreNext = false;

		for (int i = 0; i < string.length(); i++) {
			String s = string.substring(i, i+1);
			if (!ignoreNext) {
				if (s.equals("\\")) {
					ignoreNext = true;
					continue;
				} else if (s.equals("!")) {
					if (string.equals("!")) {
						// only press F8 if the string contains nothing but the !
						pressKey(KeyEvent.VK_F8);
					} else {
						writeString(day.findCell(column, row-1), column, row-1);
					}
					continue;
				} else if (s.equals("?")) {
					writeString(StartStundenClient.xmlTransfer.getDefaultValue(column), column, row);
					continue;
				} else if (s.equals("$")) {
					String varName = "";
					String curChar = "";
					while (!curChar.equals("$")) {
						varName = varName + curChar;
						i++;
						curChar = string.substring(i, i+1);
					}
					final String varNEU = varName;
					System.out.println(varNEU);
					Variable var = day.getVariablesList().stream().filter(
							e -> varNEU.equals(e.getKey())).findFirst().get();
					writeString(var.getValue(), column, row);
					continue;
				}
			}
			ignoreNext = false;
			writeChar(s.charAt(0));
		}
	}

	private void writeChar (char c) throws InterruptedException {
		if (!Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK)) {
			Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_NUM_LOCK, true);
		}
		int charcode_int = (int)c;
		String charcode = Integer.toString(charcode_int);
		robo.keyPress(KeyEvent.VK_ALT);
		Thread.sleep(5);
		for (char ascii_c : charcode.toCharArray()){
			int ascii_n = Integer.parseInt(String.valueOf(ascii_c)) + 96;
			pressKey(ascii_n);
		}
		Thread.sleep(5);
		robo.keyRelease(KeyEvent.VK_ALT);
		Thread.sleep(5);
	}

	private void writeTabs (int tabAmount, boolean shiftPressed, boolean goDown) throws InterruptedException {
		if (shiftPressed) {
			robo.keyPress(KeyEvent.VK_SHIFT);
			Thread.sleep(10);
		}
		for (int i = 0; i < tabAmount; i++) {
			pressKey(KeyEvent.VK_TAB);
		}
		robo.keyRelease(KeyEvent.VK_SHIFT);
		System.out.println("shiftReleased");
		Thread.sleep(20);
		if (goDown) {
			pressKey(KeyEvent.VK_DOWN);
		}
	}

	private void pressKey (int keyCode) throws InterruptedException {
		robo.keyPress(keyCode);
		Thread.sleep(5);
		robo.keyRelease(keyCode);
		Thread.sleep(10);
	}

}
