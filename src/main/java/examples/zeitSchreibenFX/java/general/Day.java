package examples.zeitSchreibenFX.java.general;

import java.util.Collections;
import java.util.List;

import examples.zeitSchreibenFX.java.xmlConverter.XMLTransfer;

public class Day {
	
	private String id;
	private String oldId = "";
	private List<DayPos> dayPosList;
	private List<Variable> variablesList;
	private List<DayPosError> errors;
	
	public Day() {}
	
	public Day(String id, List<DayPos> dayPosList) {
		this.id = id;
		this.dayPosList = dayPosList;
		variablesList = Collections.emptyList();
		errors = Collections.emptyList();
	}
	
	public Day(String id, List<DayPos> dayPosList, List<Variable> variablesList) {
		this(id, dayPosList);
		this.variablesList = variablesList;
	}
	
	public Day(String id, List<DayPos> dayPosList, List<Variable> variablesList, List<DayPosError> errors) {
		this(id, dayPosList, variablesList);
		this.errors = errors;
	}
	
	public Day(Day d) {
		if (d == null) {return;}
		this.id = d.getId();
		this.oldId = d.getOldId();
		this.dayPosList = d.getDayPosList();
		this.variablesList = d.getVariablesList();
		this.errors = d.getErrors();
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;	
	}

	public String getOldId() {
		return oldId;
	}
	public void setOldId(String oldId) {
		this.oldId = oldId;
	}

	public List<DayPos> getDayPosList() {
		return dayPosList;
	}
	public void setDayPosList(List<DayPos> dayPosList) {
		this.dayPosList = dayPosList;
	}

	public List<Variable> getVariablesList() {
		return variablesList;
	}
	public void setVariablesList(List<Variable> variablesList) {
		this.variablesList = variablesList;
	}

	public List<DayPosError> getErrors() {
		return errors;
	}
	public void setErrors(List<DayPosError> errors) {
		this.errors = errors;
	}
	
	public String findCell (String column, int row) {
		String cellString = "";
		DayPos dp = dayPosList.get(row-1);
		switch(column) {
		case XMLTransfer.SALESDOC_TAG:
			cellString = dp.getSalesDoc();
			break;
		case XMLTransfer.LTPCODE_TAG:
			cellString = dp.getLtpCode();
			break;
		case XMLTransfer.STARTTIME_TAG:
			cellString = dp.getStartTime();
			break;
		case XMLTransfer.ENDTIME_TAG:
			cellString = dp.getEndTime();
			break;
		case XMLTransfer.DESCRIPTION_TAG:
			cellString = dp.getDescription();
			break;
		}
		
		return cellString;
	}
	
}
