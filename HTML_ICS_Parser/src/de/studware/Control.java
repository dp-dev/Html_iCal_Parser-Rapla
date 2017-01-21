package de.studware;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import display.InitScreen;
import display.PrefScreen;

public class Control {
	IcsParserPreferences prefs;
	InitScreen screen;
	
	public static void main(String[] args) {
		new Control();
	}

	public Control() {
		// "https://rapla.dhbw-karlsruhe.de/rapla?key=ah9tAVphicaj4FqCtMVJcjd_lZ1iixBS1zuq9hhCg18VjF-uALtZZ-zv5CsgF0ap"
		prefs = new IcsParserPreferences();
		screen = new InitScreen(this, prefs);
	}

	public void displayInfo(String text) {
		screen.addInfo(text);
	}
	
	public void openPrefScreen() {
		new PrefScreen(screen, prefs);
	}
	
	public void startProcess(String url) {
		IcsParser parser = new IcsParser();
		url = parser.removeUnusedParamsInUrl(url);
		parser.startCalenderPulling(this, url, prefs);
	}

	public void openWebpage(String url, boolean openWebsite) {
		Desktop desktop;
		try {
			URI uri = new URI(url);
			if (openWebsite) {
				System.out.println("I: Website will be opened: " + url);
				if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.BROWSE)) {
					desktop.browse(uri);
				}
			} else {
				System.out.println("I: E-Mail for bug report will be opened: " + url);
				if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
					desktop.mail(uri);
				}
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
	}
	
}
