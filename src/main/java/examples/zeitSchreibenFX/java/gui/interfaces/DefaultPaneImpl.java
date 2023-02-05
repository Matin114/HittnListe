package examples.zeitSchreibenFX.java.gui.interfaces;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import examples.zeitSchreibenFX.java.general.Day;
import examples.zeitSchreibenFX.java.general.DayPos;
import examples.zeitSchreibenFX.java.general.DayPosError;
import examples.zeitSchreibenFX.java.general.Variable;
import examples.zeitSchreibenFX.java.gui.threads.ResizeThread;
import examples.zeitSchreibenFX.java.start.StartStundenClient;
import javafx.scene.Node;
import javafx.stage.Stage;

public class DefaultPaneImpl implements PaneInterface {

	@Override
	public void init(Stage stage) {
		System.out.println("Not Implemented");
	};

	public void resize(Node node, int waitTime) {
		ResizeThread rt = new ResizeThread(node, waitTime);
		rt.start();
	}

	public String isViableTime(String time) {
		String hour = "";
		String minute = "";

		time = time.replaceAll("[^0-9]", ".");

		int collectedNumbers = 0;
		for (int i = 0; i < time.length(); i++) {
			String s = time.substring(i, i+1);
			// if a number, just added to convertedString
			if (s.matches("[0-9]")) {
				if (collectedNumbers < 2) {
					hour += s;
				} else {
					minute += s;
				}
				collectedNumbers++;
			} else {
				if (collectedNumbers == 0) {
					continue;
				} else if (collectedNumbers <= 2) {
					collectedNumbers = 2;
				}
			}
			if (collectedNumbers >= 4) {
				break;
			}
		}
		//		if(hour.equals("")) {
		//			hour = "0";
		//		}
		if(minute.equals("")) {
			minute = "0";
		}
		System.out.println("Calculated Time: " + hour + ":" + minute);
		try {
			return LocalTime.of(Integer.valueOf(hour), Integer.valueOf(minute)).toString();
		} catch (DateTimeException | NumberFormatException e) {
			return null;
		}
	}

	public Day verifyDay(Day day) {
		List<DayPosError> errorsList = new LinkedList<>();
		List<String> variableNames = new LinkedList<>();
		List<DayPos> dpList = DayPos.orderByStartTime(day.getDayPosList());

		// Check the Name
		if (day.getId().equals("")) {
			errorsList.add(new DayPosError("No name entered!", 9));
		} else if (day.getId().length() > 30) {
			errorsList.add(new DayPosError("Not more than 30 characters allowed for the name!", 9));
		}

		// check if day name is already taken (packed in two ifs for performance)
		if(!day.getOldId().equals(day.getId())) {
			if(StartStundenClient.xmlTransfer.existsDay(day.getId())) {
				errorsList.add(new DayPosError("The name \"" + day.getId() + "\" is already taken!", 9));
			}
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


		// Check that there are no more than 12 variables
		if (variableNames.size() > 12) {
			errorsList.add(new DayPosError("No more than 12 variables allowed!", 1));
		}


		if (errorsList.isEmpty()) {
			List<Variable> oldVariablesList = day.getVariablesList();
			if (oldVariablesList.isEmpty()) {
				oldVariablesList = new LinkedList<>();
			}
			List<Variable> newVariablesList = new LinkedList<>();

			for (String s : variableNames) {
				Variable newVar = Variable.getVariableByKey(oldVariablesList, s);
				if(newVar == null) {
					newVar = new Variable(s, "");
				}
				newVariablesList.add(newVar);
			}
			day.setVariablesList(newVariablesList);
		} else {
			day.setErrors(errorsList);
		}
		return day;
	}

	public List<DayPosError> verifyString(List<String> variableNames, String stringToVerify, String column, int row ) {
		char[] charArray = stringToVerify.toCharArray();
		List<DayPosError> errorsList = new LinkedList<>();
		String curVariable = "";
		boolean variableOpened = false;
		// this will be set if one ore more special characters in a variablename have been spotted
		boolean specialCharVariableError = false;
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] == '\\') {
				// if a '\' is written the next character will be ignored unless it's in a variable
				if (variableOpened) {
					specialCharVariableError = true;
				} else {
					i++;
					continue;
				}
			} else if (charArray[i] == '$') {
				// handling variables
				if (variableOpened) {
					curVariable = curVariable.trim();
					if (specialCharVariableError) {
						errorsList.add(new DayPosError(row, column, "Special characters are not allowed inside a variablename! Remember to use \\", 6));
					} else if (curVariable.equals("")) {
						errorsList.add(new DayPosError(row, column, "Variables need to have viable names!", 5));
					} else if (curVariable.length() > 15) {
						errorsList.add(new DayPosError(row, column, "Variables cannot be any longer than 15 characters!", 5));
					} else if (!variableNames.contains(curVariable)) {
						variableNames.add(curVariable);
					}

					// reset parameters
					curVariable = "";
					variableOpened = false;
					specialCharVariableError = false;
				} else {
					variableOpened = true;
				}
				continue;
			} else if (charArray[i] == '?') {
				// we don't care about '?' unless it's in a variable name
				if(variableOpened) {
					specialCharVariableError = true;
				}
				continue;
			} else if (charArray[i] == '!') {
				if (row == 1) {
					errorsList.add(new DayPosError(row, column, "Cannot use a \"!\" in the first row!", 2));
				}
				if(variableOpened) {
					specialCharVariableError = true;
				}
				continue;
			}
			if(variableOpened) {
				curVariable += charArray[i];
			}

		}
		if(variableOpened) {
			errorsList.add(new DayPosError(row, column, "Variable was never closed!" 
					+ curVariable + ")" , 7));
		}
		if(stringToVerify.equals("") && !column.equals("Description")) {
			errorsList.add(new DayPosError(row, column, "Cannot be left empty!", 3));
		}
		return errorsList;
	}

}
