package examples.zeitSchreibenFX.java.general;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DayPosError {

	private int row;
	private int row2;
	private int priority;
	private String column;
	private String message;

	public DayPosError(String message, int priority) {
		this.message = message;
		this.priority = priority;
	}
	public DayPosError(int row, String column, String message, int priority) {
		this.row = row;
		this.column = column;
		this.message = message;
		this.priority = priority;
	}
	public DayPosError(int row, int row2, String message, int priority) {
		this.row = row;
		this.row2 = row2;
		this.message = message;
		this.priority = priority;
	}

	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}

	public int getRow2() {
		return row2;
	}
	public void setRow2(int row2) {
		this.row2 = row2;
	}

	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}

	public static List<DayPosError> sortByPriority(List<DayPosError> dpErrors) {

		Collections.sort(dpErrors, new Comparator<DayPosError>() {
			@Override
			public int compare(DayPosError o1, DayPosError o2) {
				if (o1.getPriority() > o2.getPriority()) {
					return -1;
				} else if (o1.getPriority() < o2.getPriority()) {
					return 1;
				}
				return 0;
			}
		});

		return dpErrors;
	}

	@Override
	public String toString() {
		String stringMessage = "";
		if (column == null && row == 0) {
			stringMessage = message + "\n";
		} else if (column == null) {
			stringMessage = message + " [Row1: " + row + "; Row2: " + row2 + "]\n";
		} else {
			stringMessage = message + " [Row: " + row + "; Column: " + column + "]\n";
		}
		return stringMessage;
	}
}