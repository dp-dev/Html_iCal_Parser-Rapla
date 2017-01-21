package display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.studware.Control;
import de.studware.IcsParserPreferences;

@SuppressWarnings("serial")
public class InitScreen extends JFrame implements ActionListener {
	private JTextField tfUrl;
	private JButton btStart;
	private JTextArea taOutput;
	private JMenuItem miClearInput, miReportBug, miExit, miSettings, miFacebook, miTwitter, miWebsite;
	private Control control;
	
	public InitScreen(Control control, IcsParserPreferences prefs) {
		System.out.println("I: InitScreen initialized");
		this.control = control;
		
		// Native Look and Feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Image image = ImageIO.read(this.getClass().getResource("/img/calendar.png"));
			setIconImage(image);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | IOException e) {
			e.printStackTrace();
		}

		this.setTitle("iCal File Parser for DHBW Rapla");
		this.setSize(350, 400);
		this.setMinimumSize(new Dimension(320, 200));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		// URL textfield and button
		JPanel pTop = new JPanel();
		pTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
		pTop.setLayout(new BorderLayout(5, 5));
		this.add(pTop, BorderLayout.NORTH);
		
		tfUrl = new JTextField(prefs.getUrlPath());
		tfUrl.setPreferredSize(new Dimension(110, 25));
		tfUrl.setToolTipText("Insert rapla url here");
		pTop.add(tfUrl, BorderLayout.CENTER);
		
		btStart = new JButton("Create iCal file");
		btStart.addActionListener(this);
		pTop.add(btStart, BorderLayout.EAST);
		
		// Textarea for outputs
		JPanel pCenter = new JPanel();
		pCenter.setLayout(new BorderLayout());
		pCenter.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
		this.add(pCenter, BorderLayout.CENTER);
		
		taOutput = new JTextArea();
		taOutput.setEditable(false);
		JScrollPane sp = new JScrollPane(taOutput);
		pCenter.add(sp, BorderLayout.CENTER);
		
		this.setJMenuBar(createMenuBar());
		this.setVisible(true);
	}
	
	private JMenuBar createMenuBar() {
		System.out.println("I: Menubar created");
		miClearInput = new JMenuItem("Clear input");
		miClearInput.addActionListener(this);
		miReportBug = new JMenuItem("Report bug");
		miReportBug.addActionListener(this);
		miExit = new JMenuItem("Exit");
		miExit.addActionListener(this);
		miSettings = new JMenuItem("Settings");
		miSettings.addActionListener(this);

		miWebsite = new JMenuItem("JAVA Corner at STUDWARE");
		miWebsite.addActionListener(this);
		miFacebook = new JMenuItem("Our Facebook fan page");
		miFacebook.addActionListener(this);
		miTwitter = new JMenuItem("Our Twitter profile");
		miTwitter.addActionListener(this);
		
		JMenu mFile = new JMenu("File");
		mFile.add(miClearInput);
		mFile.add(miReportBug);
		mFile.addSeparator();
		mFile.add(miExit);

		JMenu mWindow = new JMenu("Window");
		mWindow.add(miSettings);
		
		JMenu mAbout = new JMenu("About");
		mAbout.add(miWebsite);
		mAbout.addSeparator();
		mAbout.add(miFacebook);
		mAbout.add(miTwitter);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(mFile);
		menuBar.add(mWindow);
		menuBar.add(mAbout);
		return menuBar;
	}

	public void addInfo(String message) {
		taOutput.append(message + System.lineSeparator());
	}

	public void setURLandClear(String url) {
		tfUrl.setText(url);
		taOutput.setText("");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btStart) {
			if (!tfUrl.getText().isEmpty()) {
				taOutput.setText("");
				control.startProcess(tfUrl.getText());
			}
		} else if (e.getSource() instanceof JMenuItem) {
			if (e.getSource() == miClearInput) {
				tfUrl.setText("");
				taOutput.setText("");
			} else if (e.getSource() == miReportBug) {
				control.openWebpage(
						"mailto:development@studware.de?subject=iCal%20File%20Parser%20bug%20report&body=Please%20specify%20as%20many%20details%20as%20possible%20about%20the%20bug%20that%20occurred%20as%20well%20as%20the%20url%20you%20were%20trying%20to%20access.%0A%0AURL%3A%20"
								+ tfUrl.getText() + "%0ADetails%20about%20the%20bug%3A%0A",
						false);
			} else if (e.getSource() == miExit) {
				System.exit(0);
			} else if (e.getSource() == miSettings) {
				control.openPrefScreen();
			} else if (e.getSource() == miWebsite) {
				control.openWebpage("http://studware.de/java/", true);
			} else if (e.getSource() == miFacebook) {
				control.openWebpage("https://www.facebook.com/studware/", true);
			} else if (e.getSource() == miTwitter) {
				control.openWebpage("https://twitter.com/studwarede", true);
			}
		}
	}
}
