package gui;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class MainFrame extends JFrame implements ActionListener{
	private static String frameName = "ChatRoom";
	public static int frameSX = 600, frameSY = 480;
	private Container c;
	private BufferedImage profileBufferedImg = OpeningDialog.profileBufferedImg;
	private String clientName;
	private JButton profileImage;
	private JLabel profileNameLabel;
	private JList userList;
	private JTextPane chatArea;
	private JTextPane textInputArea;
	private JPanel userListPanel, textInputAreaPanel, chatAreaPanel;
    private static Point point = new Point();
    
    private Vector <String> userListVector;
    private StyledDocument doc;
    private Vector <String> ChatLines;
    private Vector <String> ChatLineTexture;
	
	public MainFrame(){
		super(frameName);
		initFrame();
	}
	
	public void initFrame(){
		//frame
		setSize(frameSX, frameSY);
		setLocation(100, 100);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c = getContentPane();
		
		//client's profile panel
		JPanel profilePanel = new JPanel();
		
		
		//client's profile image
		profileImage = new JButton(new ImageIcon(profileBufferedImg));
		profileImage.addActionListener(this);
		
		//client's profile name
		profileNameLabel = new JLabel();
		profileNameLabel.setText("Name: "+ clientName);
		
		//client's profile on the left hand side
		profilePanel.setLayout( null );
		profileImage.setBounds(new Rectangle(0, 0, 120, 100));
		profileNameLabel.setBounds(new Rectangle(0, 100, 120, 20));
		profilePanel.add(profileImage);
		profilePanel.add(profileNameLabel);
		profilePanel.setBounds(new Rectangle(0, 0, 120, 125));
		
		//user list on the left side
		userList = new JList();
		JScrollPane userListScrollPane = new JScrollPane(userList);
		userListScrollPane.setBounds(new Rectangle(0, 125, 120, 335));
		
		//chat content area on the right hand side
		chatArea = new JTextPane();
		chatArea.setEditable(false);
		JScrollPane chatAreaScrollPane = new JScrollPane(chatArea);
		chatAreaScrollPane.setBounds(new Rectangle(0, 0, 450, 400));
		
		//text input area on the right hand side
		textInputArea = new JTextPane();
		JScrollPane textInputAreaScrollPane = new JScrollPane(textInputArea);
		textInputAreaScrollPane.setBounds(new Rectangle(0, 410, 450, 50));
		
		//left side panel
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(null);
		leftPanel.add(profilePanel);
		leftPanel.add(userListScrollPane);
		leftPanel.setBounds(10, 10, 120, 460);
		
		//ride hand side panel
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout( null );
		rightPanel.add(chatAreaScrollPane);
		rightPanel.add(textInputAreaScrollPane);
		rightPanel.setBounds(140, 10, 450, 460);
		
		c.setLayout(null);
		c.add(leftPanel);
		c.add(rightPanel);

		userListVector = new Vector<String>();
		ChatLines = new Vector<String>();
        ChatLineTexture = new Vector<String>();
		
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				Point p = getLocation();
				setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
			}
		});
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				point.x = e.getX();
				point.y = e.getY();
			}
		});
	}
	
	public void setClientName(String name) {
		clientName = name;
		profileNameLabel.setText("Name: " + clientName);
	}

	public void addNewUser(String newUser, int color){
		userListVector.add(newUser);
		userList.setListData(userListVector);
		
		Style base = doc.getStyle("regular");
        Style s = doc.addStyle(newUser, base);
        StyleConstants.setForeground(s, new Color(color));

        addSysLine(newUser + " joined.");
	}
	
	public void delUser ( String newUser ) {
		userListVector.remove(newUser);
		userList.setListData(userListVector);

        doc.removeStyle(newUser);

        addSysLine(newUser + " left.");
    }
	
    public void addSysLine ( String text ) {
        ChatLines.add(text + "\n");
        ChatLineTexture.add("system");
        refreshChat(text + "\n", "system");
    }

    public void addWarnLine ( String text ) {
        ChatLines.add(text + "\n");
        ChatLineTexture.add("warn");
        refreshChat(text + "\n", "warn");
    }

    //refresh chat room text
    private void refreshChat ( String text, String texture) {
        try {
            doc.insertString( doc.getLength(),
                              text,
                              doc.getStyle(texture) );
        }
        catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text into text pane.");
        }

        chatArea.setCaretPosition(doc.getLength());
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
