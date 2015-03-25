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
import java.net.URL;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;


public class MainFrame extends JFrame implements ActionListener{
	private static String frameName = "ChatRoom";
	public static int frameSX = 600, frameSY = 480;
	private Container c;
	private BufferedImage profileBufferedImg = OpeningDialog.profileBufferedImg;
	private String clientName, target = "All members";
	private JButton profileImage, editColor;
	private JLabel profileNameLabel, sendingTarget;
	private JList userList;
	private JTextPane chatArea;
	private JTextPane textInputArea;
    private static Point point = new Point();
    private	JPanel choiceInside[] = new JPanel[9];
   	private	JPanel choiceFrame[] = new JPanel[9];
   	private int colorPanelLength = 20;
    private	int colorColumn[][]={
       		{255,255,255,  0,  0,  0, 43, 87,  0},
       		{  0,165,255,255,255,  0,  0,  0,  0},
       		{  0,  0,  0,  0,255,255,255,255,  0}
       		};
    
    private Vector <String> userListVector;
    private StyledDocument doc;
    private Vector <String> ChatLines;
    private Vector <String> ChatLineTexture;
    private int color = Color.black.getRGB();
   
	
	public MainFrame(){
		super(frameName);
		initFrame();
		initComponents();
	}
	
	public void initFrame(){
		//frame
		setSize(frameSX, frameSY);
		setLocation(100, 100);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c = getContentPane();
		
		//client's profile image
		profileImage = new JButton(new ImageIcon(profileBufferedImg));
		profileImage.addActionListener(this);
		
		//client's profile name
		profileNameLabel = new JLabel();
		profileNameLabel.setText("Name: "+ clientName);
		
		//client's profile on the left hand side
		JPanel profilePanel = new JPanel();
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
		chatAreaScrollPane.setBounds(new Rectangle(0, 0, 450, 365));
		
		//sending target panel on the right hand side
		sendingTarget = new JLabel();
		sendingTarget.setText("Send to: "+ target);
		sendingTarget.setBounds(new Rectangle(0, 0, 140, 30));
		
		//target and color panel on the right hand side
		JPanel sendingTarAndColorPanel = new JPanel();
		sendingTarAndColorPanel.setLayout(null);
		
		for(int i = 0; i < choiceInside.length; i++){
			choiceInside[i]=new JPanel();
			choiceInside[i].setLayout(new GridLayout(1,1));
			choiceInside[i].setBounds(new Rectangle(1, 1, colorPanelLength+1, colorPanelLength+1));
			choiceInside[i].setBackground(new Color(colorColumn[0][i],colorColumn[1][i],colorColumn[2][i]));
		}
		
		for(int i = 0; i < choiceFrame.length; i++){
			choiceFrame[i]=new JPanel();
			choiceFrame[i].setLayout(null);
			choiceFrame[i].add(choiceInside[i]);
			sendingTarAndColorPanel.add(choiceFrame[i]);
			choiceFrame[i].setBounds(new Rectangle(140 + i * (colorPanelLength+4), 3, colorPanelLength+4, colorPanelLength+4));
			choiceFrame[i].setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));
		}
		
		URL url = client.DisplayMgr.class.getResource("/res/editcolor.png");
		ImageIcon editColorImage = new ImageIcon(url);
		editColor = new JButton("Edit Color",editColorImage);
		editColor.setBounds(140 + 5 + choiceFrame.length * (colorPanelLength+4), 0, 90, 30);
		editColor.setVerticalTextPosition(AbstractButton.CENTER);
		editColor.setHorizontalTextPosition(AbstractButton.LEFT);
		editColor.addActionListener(this);
		
		sendingTarAndColorPanel.add(editColor);
		sendingTarAndColorPanel.add(sendingTarget);
		sendingTarAndColorPanel.setBounds(new Rectangle(0, 370, 450, 30));
		
		//text input area on the right hand side
		textInputArea = new JTextPane();
		JScrollPane textInputAreaScrollPane = new JScrollPane(textInputArea);
		textInputAreaScrollPane.setBounds(new Rectangle(0, 405, 450, 55));
		
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
		rightPanel.add(sendingTarAndColorPanel);
		rightPanel.add(textInputAreaScrollPane);
		rightPanel.setBounds(140, 10, 450, 460);
		
		c.setLayout(null);
		c.add(leftPanel);
		c.add(rightPanel);

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
	
	public void initComponents(){

		userListVector = new Vector<String>();
		ChatLines = new Vector<String>();
        ChatLineTexture = new Vector<String>();
        doc = chatArea.getStyledDocument();
        addStylesToDocument(doc);
        addSmileysToDocument(doc);
		
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
    private void refreshChat ( String text, String color) {
        try {
            doc.insertString( doc.getLength(),
                              text,
                              doc.getStyle(color) );
        }
        catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text into text pane.");
        }

        chatArea.setCaretPosition(doc.getLength());
    }
    
    public void clear () {
        for (String u : userListVector) {
            doc.removeStyle(u);
        }
        userListVector.clear();
        userList.setListData(userListVector);

        color = Color.black.getRGB();
        //NameLabel.setForeground(Color.black);
        //lastWhisper = "All Members";
    }
	   //Settings of JTextArea
    protected void addStylesToDocument(StyledDocument doc) {
        //Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().
                        getStyle(StyleContext.DEFAULT_STYLE);

        //base style
        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");

        Style s = doc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);

        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);

        s = doc.addStyle("small", regular);
        StyleConstants.setFontSize(s, 10);

        s = doc.addStyle("large", regular);
        StyleConstants.setFontSize(s, 16);

        //system style
        s = doc.addStyle("Server", regular);
        StyleConstants.setForeground(s, Color.BLUE);

        s = doc.addStyle("system", regular);
        StyleConstants.setBold(s, true);
        StyleConstants.setForeground(s, Color.BLUE);

        s = doc.addStyle("warn", regular);
        StyleConstants.setItalic(s, true);
        StyleConstants.setForeground(s, Color.RED);

        s = doc.addStyle("whisper", regular);
        StyleConstants.setForeground(s, Color.MAGENTA);

        //default style
        s = doc.addStyle("black", regular);
        StyleConstants.setForeground(s, Color.BLACK);

        s = doc.addStyle("red", regular);
        StyleConstants.setForeground(s, Color.RED);

        s = doc.addStyle("blue", regular);
        StyleConstants.setForeground(s, Color.BLUE);

        s = doc.addStyle("green", regular);
        StyleConstants.setForeground(s, Color.GREEN);

        s = doc.addStyle("orange", regular);
        StyleConstants.setForeground(s, Color.ORANGE);

        s = doc.addStyle("yellow", regular);
        StyleConstants.setForeground(s, Color.YELLOW);

        s = doc.addStyle("cyan", regular);
        StyleConstants.setForeground(s, Color.CYAN);

        s = doc.addStyle("gray", regular);
        StyleConstants.setForeground(s, Color.GRAY);
    }

    //smileys, regex's length should be 2
    protected void addSmileysToDocument(StyledDocument doc) {
        /*Style regular = doc.getStyle("regular");

        //icons
        Style s = doc.addStyle("smile.png", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        ImageIcon smileIcon = new javax.swing.ImageIcon("smile.png");
        if (smileIcon != null) {
            StyleConstants.setIcon(s, smileIcon);
        }
        smileys.add("smile.png");

        s = doc.addStyle("saddy.png", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        ImageIcon saddyIcon = new javax.swing.ImageIcon("saddy.png");
        if (saddyIcon != null) {
            StyleConstants.setIcon(s, saddyIcon);
        }
        smileys.add("saddy.png");

        s = doc.addStyle("laugh.png", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        ImageIcon laughIcon = new javax.swing.ImageIcon("laugh.png");
        if (laughIcon != null) {
            StyleConstants.setIcon(s, laughIcon);
        }
        smileys.add("laugh.png");

        s = doc.addStyle("angry.png", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        ImageIcon angryIcon = new javax.swing.ImageIcon("angry.png");
        if (angryIcon != null) {
            StyleConstants.setIcon(s, angryIcon);
        }
        smileys.add("angry.png");*/
    }
}
