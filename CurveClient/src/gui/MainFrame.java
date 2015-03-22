package gui;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class MainFrame extends JFrame implements ActionListener{
	private static String frameName = "ChatRoom";
	private int frameSX = 800, frameSY = 600;
	private Container c;
	private BufferedImage profileBufferedImg = OpeningDialog.profileBufferedImg;
	private String clientName;
	private JButton profileImage;
	private JLabel profileNameLabel;
	
	public MainFrame(){
		super(frameName);
		initFrame();
	}
	
	public void initFrame(){
		//frame
		setSize(frameSX, frameSY);
		setLocation(100, 100);
		//setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c = getContentPane();
		
		//client's profile panel
		JPanel profilePanel = new JPanel();
		c.add(profilePanel,BorderLayout.NORTH);
		
		//client's profile image
		profileImage = new JButton(new ImageIcon(profileBufferedImg));
		profileImage.addActionListener(this);
		profilePanel.add(profileImage,BorderLayout.NORTH);
		
		//client's profile name
		profileNameLabel = new JLabel();
		profileNameLabel.setText("Name: "+ clientName);
		profileNameLabel.setSize(120,30);
		profilePanel.add(profileNameLabel,BorderLayout.SOUTH);			
	}
	
	public void setClientName(String name) {
		clientName = name;
		profileNameLabel.setText("Name: " + clientName);
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
			g.drawImage(icon.getImage(), 0, 0, OpeningDialog.profileImageSX, OpeningDialog.profileImageSY, null);
			ImageIcon iconResized = new ImageIcon(bi);
			
			Graphics2D profileImgT = (Graphics2D) profileBufferedImg.getGraphics();
			AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f);
			profileImgT.setComposite(composite);
			profileImgT.setColor(new Color(0, 0, 0, 0));
			profileImgT.fillRect(0, 0, OpeningDialog.profileImageSX, OpeningDialog.profileImageSY);
			
			Graphics2D profileImg = (Graphics2D) profileBufferedImg.getGraphics();
			profileImg.drawImage(iconResized.getImage(),0,0,this);
		}
		
	}
}
