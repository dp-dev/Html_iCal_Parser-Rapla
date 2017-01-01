package display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.studware.IcsParserPreferences;

public class PrefScreen implements ActionListener, ChangeListener {
	private JButton btStorage, btApply, btCancel;
	private JRadioButton rbDefault, rbCustom;
	private JTextField tfStorage, tfUrl;
	private JFrame prefFrame;
	private InitScreen screen;
	private JLabel lbTime;
	private JSlider slWeeks;
	private IcsParserPreferences prefs;

	public PrefScreen(InitScreen screen, IcsParserPreferences prefs) {
		System.out.println("I: PrefScreen initialized");
		this.screen = screen;
		this.prefs = prefs;
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		prefFrame = new JFrame("Settings");
		prefFrame.setSize(300, 350);
		prefFrame.setResizable(false);
		prefFrame.setLocationRelativeTo(screen);
		prefFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		prefFrame.setLayout(null);

		// Parser preferences profiles
		rbDefault = new JRadioButton("Default parser preferences");
		rbDefault.setBounds(20, 10, 200, 25);
		rbDefault.addActionListener(this);
		prefFrame.add(rbDefault);

		rbCustom = new JRadioButton("Custom parser preferences");
		rbCustom.setBounds(20, 35, 200, 25);
		rbCustom.addActionListener(this);
		prefFrame.add(rbCustom);

		ButtonGroup prefGroup = new ButtonGroup();
		prefGroup.add(rbDefault);
		prefGroup.add(rbCustom);

		// Custom parser preferences
		JPanel pPreferences = new JPanel();
		pPreferences.setLayout(null);
		pPreferences.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "User parser preferences"));
		pPreferences.setBounds(10, 70, 270, 200);
		prefFrame.add(pPreferences);
		
		JLabel lb_url = new JLabel("URL");
		lb_url.setBounds(10, 20, 30, 21);
		pPreferences.add(lb_url);

		tfUrl = new JTextField();
		tfUrl.setBounds(40, 20, 220, 21);
		pPreferences.add(tfUrl);

		lbTime = new JLabel();
		lbTime.setBounds(10, 50, 250, 21);
		pPreferences.add(lbTime);

		slWeeks = new JSlider(JSlider.HORIZONTAL, 1, 10, 4);
		slWeeks.setBounds(10, 80, 250, 50);
		pPreferences.add(slWeeks);

		slWeeks.setMajorTickSpacing(3);
		slWeeks.setMinorTickSpacing(1);
		slWeeks.setPaintTicks(true);
		slWeeks.setPaintLabels(true);
		slWeeks.addChangeListener(this);

		JLabel lb_storage = new JLabel("Storage location for .ics-file");
		lb_storage.setBounds(10, 140, 200, 30);
		pPreferences.add(lb_storage);

		tfStorage = new JTextField();
		tfStorage.setBounds(10, 165, 210, 21);
		pPreferences.add(tfStorage);

		btStorage = new JButton("...");
		btStorage.setBounds(225, 165, 30, 20);
		btStorage.addActionListener(this);
		pPreferences.add(btStorage);

		btApply = new JButton("Apply");
		btApply.setBounds(110, 280, 80, 23);
		btApply.addActionListener(this);
		prefFrame.add(btApply);

		btCancel = new JButton("Cancel");
		btCancel.setBounds(198, 280, 80, 23);
		btCancel.addActionListener(this);
		prefFrame.add(btCancel);

		setPrefValues();
		prefFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btStorage) {
			JFileChooser fc = new JFileChooser(tfStorage.getText());
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (fc.showOpenDialog(prefFrame) == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				tfStorage.setText(file.getAbsolutePath());
			}
		} else if (e.getSource() == btCancel) {
			prefFrame.dispose();
		} else if (e.getSource() == btApply) {
			if (rbDefault.isSelected()) {
				prefs.deleteSettingsFile();
				screen.getTfUrl().setText(prefs.getUrlPath());
				screen.getTaOutput().setText("");
				prefFrame.dispose();
			} else if (!tfUrl.getText().isEmpty()) {
				if (!tfStorage.getText().isEmpty()) {
					prefs.setCustomSettings(tfUrl.getText(), tfStorage.getText(), slWeeks.getValue());
					prefs.writeSettingsToFile();
					screen.getTfUrl().setText(prefs.getUrlPath());
					screen.getTaOutput().setText("");
					prefFrame.dispose();
				} else {
					JOptionPane.showMessageDialog(prefFrame, "Please provide a storage location", "Urlpath empty", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(prefFrame, "Please provide an url path that starts with: https://rapla.dhbw-karlsruhe.de", "Storage empty", JOptionPane.ERROR_MESSAGE);
			}
		}
		if ((e.getSource() == rbDefault) || (e.getSource() == rbCustom)) {
			if (rbDefault.isSelected()) {
				setAllCustomSettings(false);
			} else {
				setAllCustomSettings(true);
			}
		}
	}

	private void setPrefValues() {
		System.out.println("I: Values from preferences will be inserted");
		rbDefault.setSelected(!prefs.isCustomSettings());
		rbCustom.setSelected(prefs.isCustomSettings());
		tfUrl.setText(prefs.getUrlPath());
		slWeeks.setValue(prefs.getWeeksToGet());
		lbTime.setText("Download all events within " + prefs.getWeeksToGet() + " weeks");
		tfStorage.setText(prefs.getStorageLocation());
		setAllCustomSettings(prefs.isCustomSettings());
	}

	private void setAllCustomSettings(boolean condition) {
		tfUrl.setEditable(condition);
		slWeeks.setEnabled(condition);
		tfStorage.setEditable(condition);
		btStorage.setEnabled(condition);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (slWeeks.getValue() < 2) {
			lbTime.setText("Download all events within 1 week");
		} else {
			lbTime.setText("Download all events within " + slWeeks.getValue() + " weeks");
		}
	}

}
