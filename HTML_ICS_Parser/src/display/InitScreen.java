package display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
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
public class InitScreen extends JFrame {
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
		btStart.addActionListener(control);
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
		miClearInput.addActionListener(control);
		miReportBug = new JMenuItem("Report bug");
		miReportBug.addActionListener(control);
		miExit = new JMenuItem("Exit");
		miExit.addActionListener(control);
		miSettings = new JMenuItem("Settings");
		miSettings.addActionListener(control);
		
		miWebsite = new JMenuItem("JAVA Corner at STUDWARE");
		miWebsite.addActionListener(control);
		miFacebook = new JMenuItem("Our Facebook fan page");
		miFacebook.addActionListener(control);
		miTwitter = new JMenuItem("Our Twitter profile");
		miTwitter.addActionListener(control);

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
	
	public JTextField getTfUrl() {
		return tfUrl;
	}
	
	public JButton getBtStart() {
		return btStart;
	}
	
	public JTextArea getTaOutput() {
		return taOutput;
	}
	
	public JMenuItem getMiClearInput() {
		return miClearInput;
	}
	
	public JMenuItem getMiReportBug() {
		return miReportBug;
	}
	
	public JMenuItem getMiExit() {
		return miExit;
	}
	
	public JMenuItem getMiSettings() {
		return miSettings;
	}

	public JMenuItem getMiFacebook() {
		return miFacebook;
	}

	public JMenuItem getMiTwitter() {
		return miTwitter;
	}

	public JMenuItem getMiWebsite() {
		return miWebsite;
	}
}
