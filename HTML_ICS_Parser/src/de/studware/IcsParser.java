package de.studware;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import data.HtmlEvent;
import display.Screen;

public class IcsParser {

	ArrayList<HtmlEvent> eventInfos = new ArrayList<>();
	Screen screen;
	
	public void startProcess(Screen screen, String baseurl) {
		this.screen = screen;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String docName = "Calendar-Export.ics";
		for (int i = 0; i < 6; i++) {
			screen.addInfo("Getting events for week " + cal.get(Calendar.WEEK_OF_YEAR));
			parseRaplaUrl(baseurl + addUrlTimeParameters(dateFormat.format(cal.getTime())));
			cal.add(Calendar.DATE, 7);
			screen.addInfo("");
		}
		if (eventInfos.size() > 0) {
			screen.addInfo("Starting to parse information into iCal format");
			IcsGenerator generator = new IcsGenerator(eventInfos);
			generator.generateIcsEvents();
			if (generator.createIcsDoc(docName)) {
				screen.addInfo("- iCal file was created successfully");
			} else {
				screen.addInfo("- iCal file creation failed - Sorry!");
			}
		}
		if (eventInfos.size() != 1) {
			screen.addInfo("Program finished with getting " + eventInfos.size() + " events.");
		} else {
			screen.addInfo("Program finished with getting " + eventInfos.size() + " event.");
		}
	}

	private String addUrlTimeParameters(String format) {
		String parts[] = format.split("-");
		return "&day=" + parts[2] + "&month=" + parts[1] + "&year=" + parts[0] + "&goto=Datum+anzeigen";
	}
	
	private void parseRaplaUrl(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			Elements allEvents = doc.getElementsByClass("week_block");
			HtmlEvent newEvent = null;
			for (Element currentEvent : allEvents) {
				Elements span = currentEvent.getElementsByTag("span");
				for (Element element : span) {
					Elements divs = element.getElementsByTag("div");
					for (Element div : divs) {
						if (!div.text().contains("zuletzt")) {
							newEvent = createEvent(div.text().substring(3));
							Elements allStrong = element.getElementsByTag("strong");
							for (Element strong : allStrong) {
								newEvent.setCategorie(strong.text());
							}
							Elements value = element.getElementsByClass("value");
							if (value.size() > 0) {
								newEvent.setEventTitle(value.get(0).text());
							}
						}
					}
					System.out.println(element.hasClass("resource"));
					if (element.hasClass("resource") && checkIfRoom(element.text())) {
						System.out.println("HI" + element.text());
						newEvent.addEventRoom(element.text());
					}
				}
				if (eventInfos.add(newEvent)) {
					screen.addInfo("- Event: " + newEvent.getTitle());
				}
			}
		} catch (IOException e) {
			screen.addInfo("- error while getting the online calender");
			e.printStackTrace();
		}
	}
	
	private boolean checkIfRoom(String text) {
		if (text.startsWith("A") || text.startsWith("B") || text.startsWith("C") || text.startsWith("D") || text.startsWith("E") || text.startsWith("F")) {
			return true;
		}
		return false;
	}

	private HtmlEvent createEvent(String input) {
		String parts[] = input.split(" ");
		String times[] = parts[1].split("-");
		HtmlEvent event = new HtmlEvent(parts[0], parts[0], times[0], times[1]);
		return event;
	}
	
}
