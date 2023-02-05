package examples.zeitSchreibenFX.java.general;

import java.util.List;

public class SettingsData {

	public static final String SALES_DOC = "Sales Document:";
	public static final String LTP_CODE = "LTP Code:";
	public static final String DESCRIPTION = "Description:";
	public static final String WAIT_TIME = "Waiting Time:";
	public static final String CREATE_MORE = "Create More:";
	
	private String salesDoc;
	private String ltpCode;
	private String description;
	private String waitTime;
	private boolean createMore;
	private List<String> errorList;
	
	public SettingsData() {}
	
	public SettingsData(String salesDoc, String ltpCode, String description, String waitTime, boolean createMore) {
		super();
		this.salesDoc = salesDoc;
		this.ltpCode = ltpCode;
		this.description = description;
		this.waitTime = waitTime;
		this.createMore = createMore;
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

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getWaitTime() {
		return waitTime;
	}
	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}

	public boolean isCreateMore() {
		return createMore;
	}
	public void setCreateMore(boolean createMore) {
		this.createMore = createMore;
	}

	public List<String> getErrorList() {
		return errorList;
	}
	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}
	
}
