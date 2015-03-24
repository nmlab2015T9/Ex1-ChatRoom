package gui;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import client.CurveClient;


public class OpeningDialog extends JDialog implements ActionListener{

	private MainHead mainhead;
	public static int profileImageSX = 120, profileImageSY = 100;
	private int OpeningDialogSX = 120, OpeningDialogSY = 280;
	public static BufferedImage profileBufferedImg = new BufferedImage(profileImageSX ,profileImageSY,BufferedImage.TYPE_INT_ARGB);
	private Container c;
	private JButton profileImage, logIn;
	private String clientName;
	private JTextField profileName, IP, port;
	private JLabel profileNameLabel;
	
	public OpeningDialog(MainHead mainHead){
		//super(mainFrame);
		mainhead = mainHead;
		setLocation(500, 300);
		setSize(OpeningDialogSX, OpeningDialogSY);
	    setVisible(true);
		c = getContentPane();
		c.setLayout(null);
		
		initDialog();
	}

	private void initDialog() {
		//set profile null image
		URL url = client.CurveClient.class.getResource("/res/profileimg.png");
		ImageIcon profileNullImage = new ImageIcon(url);
		Graphics profileImg = (Graphics) profileBufferedImg.getGraphics();
		profileImg.drawImage(profileNullImage.getImage(),0,0,this);
		
		//profile image button
		profileImage = new JButton(new ImageIcon(profileBufferedImg));
		profileImage.setSize(profileImageSX ,profileImageSY);
		profileImage.addActionListener(this);
		c.add(profileImage);
		
		//profile name label: if you want to change and edit this, remember to also change the label down at the rename() method
		profileNameLabel = new JLabel();
		profileNameLabel.setText("Profile Name");
		profileNameLabel.setSize(profileImageSX, 30);
		profileNameLabel.setLocation(5, profileImageSY-5);
		c.add(profileNameLabel);
		
		//profile name text field
		profileName = new JTextField();
		profileName.setSize(profileImageSX, 30);
		profileName.setLocation(0, profileImageSY+15);
		c.add(profileName);

		
		//IP label
		JLabel IPLabel = new JLabel();
		IPLabel.setText("IP");
		IPLabel.setSize(profileImageSX, 30);
		IPLabel.setLocation(5, profileImageSY+35);
		c.add(IPLabel);
				
		//IP text field
		IP = new JTextField();
		IP.setText("140.112.18.199");
		IP.setSize(profileImageSX, 30);
		IP.setLocation(0, profileImageSY+55);
		c.add(IP);
		
		//port name label
		JLabel portLabel = new JLabel();
		portLabel.setText("Port");
		portLabel.setSize(profileImageSX, 30);
		portLabel.setLocation(5, profileImageSY+75);
		c.add(portLabel);
				
        //port text field
		port = new JTextField();
		port.setText("9987");
		port.setSize(profileImageSX, 30);
		port.setLocation(0, profileImageSY+95);
		c.add(port);
		
		//log in button
		logIn = new JButton("Log In");
		logIn.setSize(profileImageSX ,30);
		logIn.setLocation(0, profileImageSY+125);
		logIn.addActionListener(this);
		c.add(logIn);
		
		//press enter = log in button pressed
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher(){
        	 public boolean dispatchKeyEvent(KeyEvent e) {
                 if(e.getID() == KeyEvent.KEY_PRESSED && (e.getKeyCode() == KeyEvent.VK_ENTER)){
                	 logIn.doClick();
                 }
                 return false;
             }
        });
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(profileImage)){
			FileDialog fileDialog = new FileDialog( new Frame() , "please select a photo for your profile image.", FileDialog.LOAD );
			fileDialog.setVisible(true);
			if(fileDialog.getFile()==null) return;
			ImageIcon icon = new ImageIcon(fileDialog.getDirectory()+fileDialog.getFile());	
		
			//resize the profile image
			BufferedImage bi = new BufferedImage(icon.getIconHeight(), icon.getIconWidth(), BufferedImage.TYPE_INT_ARGB);
			Graphics g = bi.createGraphics();
			g.drawImage(icon.getImage(), 0, 0, profileImageSX, profileImageSY, null);
			ImageIcon iconResized = new ImageIcon(bi);
			
			Graphics2D profileImgT = (Graphics2D) profileBufferedImg.getGraphics();
			AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f);
			profileImgT.setComposite(composite);
			profileImgT.setColor(new Color(0, 0, 0, 0));
			profileImgT.fillRect(0, 0, profileImageSX, profileImageSY);
			
			Graphics2D profileImg = (Graphics2D) profileBufferedImg.getGraphics();
			profileImg.drawImage(iconResized.getImage(),0,0,this);
		}
		
		else if(e.getSource().equals(logIn)){
			
			//check if the name is valid
			if( profileName.getText().equals("") ) {
	            JOptionPane.showMessageDialog(this, "Please don't leave the Profile Name blank!", "", JOptionPane.ERROR_MESSAGE);
	        } 
			else if( profileName.getText().contains(" ") ) {
	            JOptionPane.showMessageDialog(this, "Please don't contain blank in your Profile Name!", "", JOptionPane.ERROR_MESSAGE);
	            profileName.setText("");
	        } 
			//continue to proceed only if all requirements is achieved
			else {
	        	this.setVisible(false);
				mainhead.setVisible(true);
				client.CurveClient.dMgr.mainFrame.setClientName(profileName.getText());
				client.CurveClient.cMgr.setClientInfo(IP.getText(), new java.lang.Integer(port.getText()).intValue(), profileName.getText());
				CurveClient.cMgr.connectionBegin();
	        }	
		}
	}
	
	
	public String GetclientName(){
		return clientName;
	}

	public void rename() {
		IP.setBackground(new Color(200, 200, 200));
		IP.setEditable(false);
		port.setBackground(new Color(200, 200, 200));
		port.setEditable(false);
		
		profileNameLabel = new JLabel("<html>Profile Name <font color='red'>Already used!</font></html>");
		profileNameLabel.setText("Profile Name");
		profileNameLabel.setSize(profileImageSX, 30);
		profileNameLabel.setLocation(5, profileImageSY-5);
		c.add(profileNameLabel);
		//profileNameLabel.setText("Profile Name: Already used!");
		
		profileName.setText("");
		
		this.setVisible(true);
	}
}
