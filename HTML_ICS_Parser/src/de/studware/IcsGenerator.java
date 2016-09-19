package de.studware;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import data.HtmlEvent;
import data.IcsEvent;

public class IcsGenerator {
	ArrayList<HtmlEvent> eventInfos;
	ArrayList<IcsEvent> icsEvents;

	public IcsGenerator(ArrayList<HtmlEvent> eventInfos) {
		this.eventInfos = eventInfos;
		icsEvents = new ArrayList<>();
	}

	public void generateIcsEvents() {
		for (HtmlEvent htmlEvent : eventInfos) {
			IcsEvent event = new IcsEvent();
			event.addDtstart(formatDate(htmlEvent.getBegindate()) + formatTime(htmlEvent.getBegintime()));
			event.addDtend(formatDate(htmlEvent.getEnddate()) + formatTime(htmlEvent.getEndtime()));
			event.setSummary(htmlEvent.getTitle().replace(",", "\\,"));
			event.setLocation(addAllLocations(htmlEvent.getRooms()));
			event.setCategories(htmlEvent.getCategorie());
			icsEvents.add(event);
		}
	}

	private String addAllLocations(ArrayList<String> rooms) {
		if(rooms.size() > 0) {
			String result = "";
			for (String room : rooms) {
				result = result + room + "\\, ";
			}
			return result.substring(0, result.length() - 3);
		} else {
			return "";
		}
	}

	private String formatTime(String time) {
		return "T" + time.replace(":", "") + "00";
	}

	private String formatDate(String date) {
		StringBuilder result = new StringBuilder();
		String parts[] = date.split("\\.");
		for (int i = parts.length - 1; i >= 0; i--) {
			if (i == 2) {
				if (parts[i].length() < 3) {
					result.append("20" + parts[i]);
				} else {
					result.append("2" + parts[i]);
				}
			} else {
				result.append(parts[i]);
			}
		}
		return result.toString();
	}

	public boolean createIcsDoc(String docname) {
		System.out.println("I: Ics Doc will be created on the desktop");
		File doc = new File(System.getProperty("user.home") + "\\Desktop\\" + docname);
		StringBuilder builder = new StringBuilder();
		try {
			InputStream input = getClass().getResourceAsStream("/data/CalendarStart.txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				builder.append(inputLine + System.lineSeparator());
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (IcsEvent event : icsEvents) {
			builder.append("BEGIN:VEVENT" + System.lineSeparator());
			builder.append("DTSTART;" + event.getDtstart() + System.lineSeparator());
			builder.append("DTSTAMP:" + event.getDtstamp() + System.lineSeparator());
			builder.append("DTEND;" + event.getDtend() + System.lineSeparator());
			builder.append("SUMMARY:" + event.getSummary() + System.lineSeparator());
			builder.append("UID:" + event.getUid() + System.lineSeparator());
			builder.append("LOCATION:" + event.getLocation() + System.lineSeparator());
			builder.append("CATEGORIES:" + event.getCategories() + System.lineSeparator());
			builder.append("END:VEVENT" + System.lineSeparator());
		}
		builder.append("END:VCALENDAR" + System.lineSeparator());
		return writeToFile(doc, builder.toString(), false);
	}
	
	private boolean writeToFile(File doc, String message, boolean append) {
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(doc.getAbsoluteFile()), "UTF-8"));
			out.write(message);
			out.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
