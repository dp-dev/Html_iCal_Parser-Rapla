package de.studware;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class IcsParserPreferences {
	private final static File STORAGE_FOLDER = new File(System.getProperty("user.home") + "\\iCal-Parser");
	private final static File STORAGE_DOCUMENT = new File(System.getProperty("user.home") + "\\iCal-Parser\\parser_user_prefs.properties");
	private final String KEYWORDS[] = { "PREF_URL_PATH", "PREF_WEEKS", "PREF_STORAGE" };
	
	private String urlPath, storageLocation;
	private int weeksToGet;
	private boolean customSettings;
	
	public IcsParserPreferences() {
		customSettings = checkIfCustomSettingsAvailable();
		if (customSettings) {
			readSettingsFromFile();
		} else {
			setDefaultSettings();
		}
	}
	
	private void setDefaultSettings() {
		customSettings = false;
		urlPath = "";
		storageLocation = System.getProperty("user.home") + "\\Desktop";
		weeksToGet = 4;
	}

	public void setCustomSettings(String url, String location, int weeks) {
		customSettings = true;
		urlPath = url;
		storageLocation = location;
		weeksToGet = weeks;
	}
	
	private boolean checkIfCustomSettingsAvailable() {
		if (STORAGE_FOLDER.exists()) {
			if (STORAGE_DOCUMENT.exists()) {
				return true;
			}
		}
		return false;
	}
	
	public void writeSettingsToFile() {
		if (!STORAGE_FOLDER.exists()) {
			STORAGE_FOLDER.mkdir();
		}
		System.out.println("Write file: " + STORAGE_DOCUMENT);
		try {
			FileWriter fw = new FileWriter(STORAGE_DOCUMENT.getAbsoluteFile(), false);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(KEYWORDS[0] + "=:=" + urlPath + System.lineSeparator());
			bw.write(KEYWORDS[1] + "=:=" + weeksToGet + System.lineSeparator());
			bw.write(KEYWORDS[2] + "=:=" + storageLocation + System.lineSeparator());
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteSettingsFile() {
		if (STORAGE_DOCUMENT.exists()) {
			STORAGE_DOCUMENT.delete();
		}
		if (STORAGE_FOLDER.exists()) {
			STORAGE_FOLDER.delete();
		}
		setDefaultSettings();
	}

	private void readSettingsFromFile() {
		System.out.println("Read file: " + STORAGE_DOCUMENT);
		BufferedReader reader = null;
		ArrayList<String> prefList = new ArrayList<>();
		try {
			if (STORAGE_DOCUMENT.exists()) {
				prefList.clear();
				reader = new BufferedReader(new FileReader(STORAGE_DOCUMENT));
				String line;
				while ((line = reader.readLine()) != null) {
					prefList.add(line);
				}
			} else {
				System.err.println("File does not exists!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		getValuesFromList(prefList);
	}
	
	private void getValuesFromList(ArrayList<String> prefList) {
		for (String pref : prefList) {
			String parts[] = pref.split("=:=");
			if (parts[0].equals(KEYWORDS[0])) {
				urlPath = parts[1];
			} else if (parts[0].equals(KEYWORDS[1])) {
				weeksToGet = Integer.parseInt(parts[1]);
			} else if (parts[0].equals(KEYWORDS[2])) {
				storageLocation = parts[1];
			}
		}
	}
	
	public String getUrlPath() {
		return urlPath;
	}
	
	public String getStorageLocation() {
		return storageLocation;
	}
	
	public int getWeeksToGet() {
		return weeksToGet;
	}
	
	public boolean isCustomSettings() {
		return customSettings;
	}
	
}
