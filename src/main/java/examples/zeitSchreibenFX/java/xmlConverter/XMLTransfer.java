package examples.zeitSchreibenFX.java.xmlConverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import examples.zeitSchreibenFX.java.general.Day;
import examples.zeitSchreibenFX.java.general.DayPos;
import examples.zeitSchreibenFX.java.general.SettingsData;
import examples.zeitSchreibenFX.java.general.Variable;

public class XMLTransfer {

	private final String DIRECTORY = "C:\\ProgramData\\Stunden";
	private final String DATAFILEDIR = DIRECTORY + "\\Data.xml";
	private final String SETTINGSFILEDIR = DIRECTORY + "\\Settings.xml";

	private File dataFile;
	private File settingsFile;
	private DocumentBuilderFactory dbf;
	private DocumentBuilder db;
	private Document dataDoc;
	private Document settingsDoc;


	//	private final String SETTINGS_TAG = "settings";
	public static final String WAITINGTIME_TAG = "waitingTime";
	public static final String DEFCREATEMORE_TAG = "defCreateMore";

	//	private final String DAYS_TAG = "days";
	private final String DAY_TAG = "day";
	private final String DAYPOS_TAG = "dayPos";

	public static final String SALESDOC_TAG = "salesDoc";
	public static final String LTPCODE_TAG = "ltpCode";
	public static final String STARTTIME_TAG = "startTime";
	public static final String ENDTIME_TAG = "endTime";
	public static final String DESCRIPTION_TAG = "description";

	private final String VARIABLES_TAG = "variables";
	private final String VARIABLE_TAG = "variable";
	private final String VALUE_TAG = "value";

	private final String ID_ATTR= "id";
	private final String KEY_ATTR= "key";




	public XMLTransfer() {
		dbf = DocumentBuilderFactory.newInstance();
		try {
			File directory = new File(DIRECTORY);
			if (!directory.exists()){
				directory.mkdirs();
			}
			dataFile = new File(DATAFILEDIR);
			if(dataFile.createNewFile()) {
				createNewDataFile();
			}
			settingsFile = new File(SETTINGSFILEDIR);
			if(settingsFile.createNewFile()) {
				createNewSettingsFile();
			}

			db = dbf.newDocumentBuilder();
			dataDoc = db.parse(dataFile);
			db = dbf.newDocumentBuilder();
			settingsDoc = db.parse(settingsFile);
			dataDoc.getDocumentElement().normalize();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	private void createNewDataFile() throws IOException {
		InputStreamReader streamReader =
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream(
						"DefaultData.xml"), StandardCharsets.UTF_8);
		BufferedReader br = new BufferedReader(streamReader);
		BufferedWriter bw = new BufferedWriter(new FileWriter(dataFile));

		String line = br.readLine();
		do {
			bw.write(line + "\n");
			line = br.readLine();
		} while (line != null);

		br.close();
		bw.close();
	}

	private void createNewSettingsFile() throws IOException {
		InputStreamReader streamReader =
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream(
						"DefaultSettings.xml"), StandardCharsets.UTF_8);
		BufferedReader br = new BufferedReader(streamReader);
		BufferedWriter bw = new BufferedWriter(new FileWriter(settingsFile));

		String line = br.readLine();
		do {
			bw.write(line + "\n");
			line = br.readLine();
		} while (line != null);


		br.close();
		bw.close();
	}

	public void addNewDay(Day day) {
		boolean isDayNew = day.getOldId().equals("");

		Element days = dataDoc.getDocumentElement();

		Element newDay = dataDoc.createElement(DAY_TAG);
		if (!isDayNew) {
			NodeList allDaysNodeList = days.getElementsByTagName(DAY_TAG);
			for (int i = 0; i < allDaysNodeList.getLength(); i++) {
				Element oneDay = (Element) allDaysNodeList.item(i);
				if (oneDay.getAttribute(ID_ATTR).equals(day.getOldId())) {
					while (oneDay.hasChildNodes()) {
						oneDay.removeChild(oneDay.getFirstChild());
					}
					newDay = oneDay;
					break;
				}
			}
		} else {
			// only here to keep the structure human readable
			days.appendChild(dataDoc.createTextNode("\t"));
		}
		newDay.setAttribute(ID_ATTR, day.getId());
		newDay.appendChild(dataDoc.createTextNode("\n\t\t"));

		for (DayPos dp : day.getDayPosList()) {
			Element newDayPos = dataDoc.createElement(DAYPOS_TAG);
			newDay.appendChild(newDayPos);
			newDayPos.appendChild(dataDoc.createTextNode("\n\t\t\t"));

			Element newSalesDoc = dataDoc.createElement(SALESDOC_TAG);
			newSalesDoc.appendChild(dataDoc.createTextNode(dp.getSalesDoc()));
			newDayPos.appendChild(newSalesDoc);
			newDayPos.appendChild(dataDoc.createTextNode("\n\t\t\t"));

			Element newLtpCode = dataDoc.createElement(LTPCODE_TAG);
			newLtpCode.appendChild(dataDoc.createTextNode(dp.getLtpCode()));
			newDayPos.appendChild(newLtpCode);
			newDayPos.appendChild(dataDoc.createTextNode("\n\t\t\t"));

			Element newStartTime = dataDoc.createElement(STARTTIME_TAG);
			newStartTime.appendChild(dataDoc.createTextNode(dp.getStartTime()));
			newDayPos.appendChild(newStartTime);
			newDayPos.appendChild(dataDoc.createTextNode("\n\t\t\t"));

			Element newEndTime = dataDoc.createElement(ENDTIME_TAG);
			newEndTime.appendChild(dataDoc.createTextNode(dp.getEndTime()));
			newDayPos.appendChild(newEndTime);
			newDayPos.appendChild(dataDoc.createTextNode("\n\t\t\t"));

			Element newDescription = dataDoc.createElement(DESCRIPTION_TAG);
			newDescription.appendChild(dataDoc.createTextNode(dp.getDescription()));
			newDayPos.appendChild(newDescription);
			newDayPos.appendChild(dataDoc.createTextNode("\n\t\t"));

			newDay.appendChild(dataDoc.createTextNode("\n\t\t"));

		}

		Element newVariables = dataDoc.createElement(VARIABLES_TAG);
		newDay.appendChild(newVariables);
		newVariables.appendChild(dataDoc.createTextNode("\n\t\t"));
		for (Variable var : day.getVariablesList()) {
			newVariables.appendChild(dataDoc.createTextNode("\t"));

			Element newVariable = dataDoc.createElement(VARIABLE_TAG);
			newVariable.setAttribute(KEY_ATTR, var.getKey());
			newVariables.appendChild(newVariable);
			newVariable.appendChild(dataDoc.createTextNode("\n\t\t\t\t"));

			Element newValue = dataDoc.createElement(VALUE_TAG);
			newValue.appendChild(dataDoc.createTextNode(var.getValue()));
			newVariable.appendChild(newValue);
			newVariable.appendChild(dataDoc.createTextNode("\n\t\t\t"));

			newVariables.appendChild(dataDoc.createTextNode("\n\t\t"));
		}

		newDay.appendChild(dataDoc.createTextNode("\n\t"));

		if (isDayNew) {
			days.appendChild(newDay);
			days.appendChild(dataDoc.createTextNode("\n"));
		}

		saveDataFile();
	}

	public void deleteDay(Day day) {
		Element days = dataDoc.getDocumentElement();
		NodeList daysList = days.getChildNodes();

		for (int i = 0; i < daysList.getLength(); i++) {
			Node dayNode = daysList.item(i);
			if (dayNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			Element dayElement = (Element) dayNode;
			if (dayElement.getAttribute(ID_ATTR).equals(day.getId())) {
				days.removeChild(dayNode);
				// remove an empty line
				//				if (daysList.item(i).getNodeType() == Node.TEXT_NODE) {
				//					days.removeChild(daysList.item(i));
				//				}
				break;
			}
		}

		saveDataFile();
	}

	public List<Day> getAllDays() {
		List<Day> days = new LinkedList<Day>();

		NodeList daysNodeList = dataDoc.getElementsByTagName(DAY_TAG);

		for (int i = 0; i < daysNodeList.getLength(); i++) {
			Day day = new Day();
			Node dayNode = daysNodeList.item(i);
			if (dayNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			Element dayElement = (Element) dayNode;
			day.setId(dayElement.getAttribute(ID_ATTR));

			// get all dayPoses
			List<DayPos> dpList = new LinkedList<>();
			NodeList dayPosNodeList = dayElement.getElementsByTagName(DAYPOS_TAG);
			for (int j = 0; j < dayPosNodeList.getLength(); j++) {
				Node dayPosNode = dayPosNodeList.item(j);
				if (dayPosNode.getNodeType() != Node.ELEMENT_NODE) {
					continue;
				}
				Element dayPosElement = (Element) dayPosNode;
				dpList.add(new DayPos(dayPosElement.getElementsByTagName(SALESDOC_TAG).item(0).getTextContent().trim(),
						dayPosElement.getElementsByTagName(LTPCODE_TAG).item(0).getTextContent().trim(),
						dayPosElement.getElementsByTagName(STARTTIME_TAG).item(0).getTextContent().trim(),
						dayPosElement.getElementsByTagName(ENDTIME_TAG).item(0).getTextContent().trim(),
						dayPosElement.getElementsByTagName(DESCRIPTION_TAG).item(0).getTextContent().trim()));

			}
			day.setDayPosList(dpList);

			// get all variables
			List<Variable> varList = new LinkedList<>();
			Node variablesNode = dayElement.getElementsByTagName(VARIABLES_TAG).item(0);
			if (variablesNode.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}
			Element variablesElement = (Element) variablesNode;

			NodeList variablesNodeList = variablesElement.getElementsByTagName(VARIABLE_TAG);
			for (int j = 0; j < variablesNodeList.getLength(); j++) {
				Node variableNode = variablesNodeList.item(j);
				if (variableNode.getNodeType() != Node.ELEMENT_NODE) {
					continue;
				}
				Element variableElement = (Element) variableNode;
				varList.add(new Variable(variableElement.getAttribute(KEY_ATTR).trim(),
						variableElement.getElementsByTagName(VALUE_TAG).item(0).getTextContent().trim()));

			}
			day.setVariablesList(varList);

			days.add(day);
		}

		return days;
	}

	public boolean existsDay(String dayId) {
		if (getDayById(dayId) != null) {
			return true;
		}
		return false;
	}

	public Day getDayById(String dayId) {
		List<Day> allDaysList = getAllDays();

		Optional<Day> day = allDaysList.stream().filter(((d) -> d.getId().equals(dayId))).findFirst();
		if(!day.isPresent()) {
			return null;
		}
		return day.get();

	}

	public String getDefaultValue(String key) {
		Element defaultElement= (Element) settingsDoc.getElementsByTagName(key).item(0);
		return defaultElement.getTextContent().trim();

	}

	public SettingsData getSettings() {
		SettingsData settings = new SettingsData();
		settings.setSalesDoc(settingsDoc.getElementsByTagName(SALESDOC_TAG).item(0).getTextContent());
		settings.setLtpCode(settingsDoc.getElementsByTagName(LTPCODE_TAG).item(0).getTextContent());
		settings.setDescription(settingsDoc.getElementsByTagName(DESCRIPTION_TAG).item(0).getTextContent());
		settings.setWaitTime(settingsDoc.getElementsByTagName(WAITINGTIME_TAG).item(0).getTextContent());
		settings.setCreateMore(Boolean.valueOf(settingsDoc.getElementsByTagName(DEFCREATEMORE_TAG).item(0).getTextContent()));
		return settings;
	}

	public void writeSettings(SettingsData settingsData) {
		settingsDoc.getElementsByTagName(SALESDOC_TAG).item(0).setTextContent(settingsData.getSalesDoc());
		settingsDoc.getElementsByTagName(LTPCODE_TAG).item(0).setTextContent(settingsData.getLtpCode());
		settingsDoc.getElementsByTagName(DESCRIPTION_TAG).item(0).setTextContent(settingsData.getDescription());
		settingsDoc.getElementsByTagName(WAITINGTIME_TAG).item(0).setTextContent(settingsData.getWaitTime());
		settingsDoc.getElementsByTagName(DEFCREATEMORE_TAG).item(0).setTextContent(String.valueOf(settingsData.isCreateMore()));
		saveSettingsFile();
	}

	private void saveDataFile() {
		try {
			DOMSource source = new DOMSource(dataDoc);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();

			Transformer transformer = transformerFactory.newTransformer();
			StreamResult result = new StreamResult(DATAFILEDIR);
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	private void saveSettingsFile() {
		try {
			DOMSource source = new DOMSource(settingsDoc);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();

			Transformer transformer = transformerFactory.newTransformer();
			StreamResult result = new StreamResult(SETTINGSFILEDIR);
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}
