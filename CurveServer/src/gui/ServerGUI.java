package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.ServerSocket;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/*
 * the GUI of Curve Server
 */

public class ServerGUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static final String VERSION = "1.0";
	private ServerSocket ss;
	private JTextArea textarea = new JTextArea();
	private JList<String> userlist;
	
	public ServerGUI() {
		super("Curve Server" + VERSION);
		initComponent();
		askPort();
	}
	public ServerGUI(ServerSocket s) {
		super("Curve Server" + VERSION);
		initComponent();
		ss = s;
	}
	
	public void setSocket(ServerSocket s) {
		ss = s;
	}
	
	private void initComponent() {
		setVisible(true);
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// user list
		userlist = new JList<String>(new DefaultListModel<String>());
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		JScrollPane userscroll = new JScrollPane(userlist);
		add(userscroll, c);
		
		// text area
		textarea.setEditable(false);
		Font font = new Font("Verdana", Font.BOLD, 12);
		textarea.setFont(font);
		textarea.setForeground(Color.GREEN);
		textarea.setBackground(Color.BLACK);
		textarea.setText("asdfasdfasdfasdfasdf");
		textarea.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		add(textarea, c);
		
	}
	
	/* 
	 * a pop up dialog
	 */
	private void askPort() {
		JDialog dialog = new JDialog(this, "What port do you want the server to listen?");
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	}
	public void addText(String s) {
		// TODO Auto-generated method stub
		
	}

}

