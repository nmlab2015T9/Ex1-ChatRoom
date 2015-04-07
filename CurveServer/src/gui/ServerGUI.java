package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import server.Client;
import server.CurveServer;

/*
 * the GUI of Curve Server
 */

public class ServerGUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static final String VERSION = "1.0";
	//private ServerSocket ss;
	private JTextArea textarea = new JTextArea(30, 50);
	private JList<Client> userList;
	private DefaultListModel<Client> userListModel = new DefaultListModel<>();
	private JTextField broadcastT = new JTextField();
	
	public ServerGUI() {
		super("Curve Server" + VERSION);
		initComponent();
		askPort();
	}
	public ServerGUI(ServerSocket s) {
		super("Curve Server" + VERSION);
		initComponent();
		//ss = s;
	}
	
	public void setSocket(ServerSocket s) {
		//ss = s;
	}
	
	private void initComponent() {
		setVisible(true);
		//setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		// user list
		userList = new JList<>(userListModel);
		userList.setCellRenderer(new ClientListRenderer());
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 10;
		c.fill = GridBagConstraints.BOTH;
		JScrollPane userscroll = new JScrollPane(userList);
		//add(userscroll, c);
		
		// text area
		textarea.setEditable(false);
		Font font = new Font("Verdana", Font.BOLD, 12);
		textarea.setFont(font);
		textarea.setForeground(Color.GREEN);
		textarea.setBackground(Color.BLACK);
		textarea.setText("port : " + CurveServer.PORT + "\n");
		textarea.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 3;
		c.weighty = 10;
		c.fill = GridBagConstraints.BOTH;
		JSplitPane northSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                userscroll, textarea);
		//add(splitPane, c);
		
		// broadcast label
		JLabel broadcastL = new JLabel("Broadcast : ");
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		//add(broadcastL, c);
		
		// broadcast text field
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.LINE_START;
		//add(broadcastT, c);
		
		// South panel
		JPanel southPane = new JPanel(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.LINE_END;
		southPane.add(broadcastL, c);
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		southPane.add(broadcastT, c);
		
		JSplitPane southSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                northSplitPane, southPane);
		
		add(southSplitPane, BorderLayout.CENTER);
		
		broadcastT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String txt = broadcastT.getText();
				broadcastT.setText("");
				CurveServer.sendBroadcast("/b " + txt);
			}
		});
	}
	
	/* 
	 * a pop up dialog
	 */
	private void askPort() {
		JDialog dialog = new JDialog(this, "What port do you want the server to listen?");
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	}
	// append the message to a new line.
	public void addText(String s) {
		textarea.append(s + "\n");
	}
	
	// add user to the end of the list
	public void addUser(Client c) {
		userListModel.addElement(c);
	}
	// remove the specified user
	public void removeUser(int id) {
		userListModel.remove(id);
	}
	public void removeUser(String username) {
		userListModel.removeElement(username);
	}

}

