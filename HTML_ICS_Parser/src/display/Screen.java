package display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.studware.IcsParser;

public class Screen extends JFrame implements ActionListener {
	private static final long serialVersionUID = -5936139625553605954L;
	JTextField tfUrl;
	JButton btStart;
	JTextArea tfoutput;
	IcsParser parser;
	
	public static void main(String[] args) {
		new Screen();
	}

	public Screen() {
		parser = new IcsParser();
		this.setTitle("Rapla to iCal file parser");
		this.setSize(300, 400);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		
		tfUrl = new JTextField("https://rapla.dhbw-karlsruhe.de/rapla?key=ah9tAVphicaj4FqCtMVJcjd_lZ1iixBS1zuq9hhCg18VjF-uALtZZ-zv5CsgF0ap");
		tfUrl.setBounds(5, 5, 180, 25);
		this.add(tfUrl);
		
		btStart = new JButton("Get iCal file");
		btStart.setBounds(190, 5, 100, 24);
		btStart.addActionListener(this);
		this.add(btStart);
		
		tfoutput = new JTextArea();
		tfoutput.setEditable(false);
		JScrollPane sp = new JScrollPane(tfoutput);
		sp.setBounds(5, 35, 285, 330);
		this.add(sp);
		this.setVisible(true);
	}

	public void addInfo(String message) {
		tfoutput.append(message + System.lineSeparator());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btStart) {
			tfoutput.setText("");
			parser.startProcess(this, tfUrl.getText());
		}
	}
}
