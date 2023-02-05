package examples.zeitSchreibenFX.java.general;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DayPos {

	private String salesDoc;
	private String ltpCode;
	private String startTime;
	private String endTime;
	private String description;

	public DayPos() {
		description = "";
		salesDoc = "";
		ltpCode = "";
		startTime = "";
		endTime = "";
	}

	public DayPos(String salesDoc, String ltpCode, String startTime, String endTime, String description) {
		this.salesDoc = salesDoc;
		this.ltpCode = ltpCode;
		this.startTime = startTime;
		this.endTime = endTime;
		this.description = description;
	}


	public String getSalesDoc() {
		return salesDoc;
	}
	public void setSalesDoc(String salesDoc) {
		this.salesDoc = salesDoc;
	}

	public String getLtpCode() {
		return ltpCode;
	}
	public void setLtpCode(String ltpCode) {
		this.ltpCode = ltpCode;
	}

	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEmpty() {
		if (description.equals("") && salesDoc.equals("") && ltpCode.equals("") && 
				startTime.equals("") && endTime.equals("")) {
			return true;
		}
		return false;
	}

	public static boolean containsEmpty(List<DayPos> dpList) {
		for (DayPos dp : dpList) {
			if(dp.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public static List<DayPos> removeEmpty(List<DayPos> dpList) {
		List<DayPos> dpToRemove = new ArrayList<>();
		for (DayPos dp : dpList) {
			if(dp.isEmpty()) {
				dpToRemove.add(dp);
			}
		}
		dpList.removeAll(dpToRemove);
		return dpList;
	}

	public static List<DayPos> orderByStartTime(List<DayPos> dpList) {
		TimeComparator tc = new TimeComparator();
		Collections.sort(dpList, new Comparator<DayPos>() {
			@Override
			public int compare(DayPos o1, DayPos o2) {
				return tc.compare(o1.getStartTime(), o2.getStartTime());
			}
		});

		return dpList;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DayPos)) {
			return false;
		}
		DayPos dp = (DayPos) obj;
		if (salesDoc.equals(dp.getSalesDoc()) && ltpCode.equals(dp.getLtpCode()) 
				&& startTime.equals(dp.getStartTime()) && endTime.equals(dp.getEndTime())
				&& description.equals(dp.getDescription())) {
			return true;
		}
		return false;
	}


}
