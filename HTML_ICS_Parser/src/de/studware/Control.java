package de.studware;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JMenuItem;

import display.InitScreen;
import display.PrefScreen;

public class Control implements ActionListener {
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == screen.getBtStart()) {
			if (!screen.getTfUrl().getText().isEmpty()) {
				screen.getTaOutput().setText("");
				new IcsParser().startProcess(screen, screen.getTfUrl().getText(), prefs);
			}
		} else if (e.getSource() instanceof JMenuItem) {
			if (e.getSource() == screen.getMiClearInput()) {
				screen.getTfUrl().setText("");
				screen.getTaOutput().setText("");
			} else if (e.getSource() == screen.getMiReportBug()) {
				openWebpage(
						"mailto:development@studware.de?subject=iCal%20File%20Parser%20bug%20report&body=Please%20specify%20as%20many%20details%20as%20possible%20about%20the%20bug%20that%20occurred%20as%20well%20as%20the%20url%20you%20were%20trying%20to%20access.%0A%0AURL%3A%20"
								+ screen.getTfUrl().getText() + "%0ADetails%20about%20the%20bug%3A%0A",
						false);
			} else if (e.getSource() == screen.getMiExit()) {
				System.exit(0);
			} else if (e.getSource() == screen.getMiSettings()) {
				new PrefScreen(screen, prefs);
			} else if (e.getSource() == screen.getMiWebsite()) {
				openWebpage("http://studware.de/java/", true);
			} else if (e.getSource() == screen.getMiFacebook()) {
				openWebpage("https://www.facebook.com/studware/", true);
			} else if (e.getSource() == screen.getMiTwitter()) {
				openWebpage("https://twitter.com/studwarede", true);
			}
		}
	}

	private void openWebpage(String url, boolean openWebsite) {
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
