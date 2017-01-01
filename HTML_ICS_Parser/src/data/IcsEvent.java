package data;

import java.util.UUID;

public class IcsEvent {
	String dtstart, dtstamp, dtend, summary, location, categories, uid;
	
	public IcsEvent() {
		dtstart = "TZID=Europe/Berlin:";
		dtend = "TZID=Europe/Berlin:";
		uid = UUID.randomUUID().toString();
	}
	
	public String getDtstart() {
		return dtstart;
	}
	
	public String getDtstamp() {
		return dtstamp;
	}
	
	public String getUid() {
		return uid;
	}
	
	public void addDtstart(String dtstart) {
		this.dtstart += dtstart;
		this.dtstamp = dtstart + "Z";
	}
	
	public String getDtend() {
		return dtend;
	}
	
	public void addDtend(String dtend) {
		this.dtend += dtend;
	}
	
	public String getSummary() {
		return summary;
	}
	
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getCategories() {
		return categories;
	}
	
	public void setCategories(String categories) {
		this.categories = categories;
	}
	
	public void displayEvent() {
		System.out.println("I: Details for event");
		System.out.println("-- DTSTART: " + getDtstart());
		System.out.println("-- DTEND: " + getDtend());
		System.out.println("-- SUMMARY: " + getSummary());
		System.out.println("-- UID: " + getUid());
		System.out.println("-- LOCATION: " + getLocation());
		System.out.println("-- CATEGORIES: " + getCategories());
		System.out.println("");
	}

}
