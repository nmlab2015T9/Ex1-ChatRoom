package gui;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
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
	private JPopupMenu userListPopup;
	private JMenuItem whisper, video, sendfile;
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
    private Vector <String> ChatLineColor;
    private Vector <String> smileys;
    private int color = Color.black.getRGB();
    
    private client.ClientMgr user = client.CurveClient.cMgr;
   
	
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
		setShape(new RoundRectangle2D.Double(0, 0, frameSX, frameSY, 30, 30));
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
	
		//items that pop up when right key pressed
		whisper = new JMenuItem("Whisper");
		whisper.addActionListener(this);
		video = new JMenuItem("Camera Conference");
		video.addActionListener(this);
		sendfile = new JMenuItem("Send File");
		sendfile.addActionListener(this);
		
		userListPopup = new JPopupMenu();
		userListPopup.add(whisper);
		userListPopup.add(video);
		userListPopup.add(sendfile);
		
		//user list on the left side
		userList = new JList(userListVector);
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            	userList.clearSelection();
            	Rectangle r = userList.getCellBounds( 0, userListVector.size()-1 );
            	if( r!=null && r.contains(e.getPoint()) ) {
            		int index = userList.locationToIndex(e.getPoint());
            		userList.setSelectedIndex(index);
            	}
            }
            public void mouseReleased(MouseEvent e) {
            	if( userList.getSelectedIndex() != -1) {
            		userListPopup.show(e.getComponent(), e.getX(), e.getY());
            	}
            }
        });
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
		
		URL url = client.CurveClient.class.getResource("/res/editcolor.png");
		ImageIcon editColorImage = new ImageIcon(url);
		editColor = new JButton("Edit Color",editColorImage);
		editColor.setBounds(140 + 5 + choiceFrame.length * (colorPanelLength+4), 0, 90, 30);
		editColor.setVerticalTextPosition(AbstractButton.CENTER);
		editColor.setHorizontalTextPosition(AbstractButton.LEFT);
		editColor.addActionListener(this);
		
		sendingTarAndColorPanel.add(editColor);
		sendingTarAndColorPanel.add(sendingTarget);
		sendingTarAndColorPanel.setBounds(new Rectangle(0, 370, 450, 30));
		sendingTarAndColorPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(e.getX() > 140 && e.getX() < 140 + choiceFrame.length * (colorPanelLength+4)){
					int columnChoosed = (e.getX()-140)/(colorPanelLength+4);
					Color temp = new Color(colorColumn[0][columnChoosed],colorColumn[1][columnChoosed],colorColumn[2][columnChoosed]);
					color = temp.getRGB();
					user.sendColorChange(color);
					textInputArea.setForeground(temp);
				}
			}
		});
		
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
		
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher(){
        	 public boolean dispatchKeyEvent(KeyEvent e) {
                 if(e.getID() == KeyEvent.KEY_PRESSED && (e.getKeyCode() == KeyEvent.VK_ENTER) && isVisible()){
                	 prepareMsg(getInputText(textInputArea));
                	 textInputArea.setText(null);
                 }
                 if(e.getID() == KeyEvent.KEY_RELEASED && (e.getKeyCode() == KeyEvent.VK_ENTER) && isVisible()){
                	 textInputArea.setText(null);
                 }
                 return false;
             }
        });
	}
	
	public void initComponents(){
		userListVector = new Vector<String>();
		ChatLines = new Vector<String>();
        ChatLineColor = new Vector<String>();
        smileys = new Vector<String>();
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
		else if(e.getSource().equals(editColor)){
			Color temp = JColorChooser.showDialog(this, "Choose color.", new Color(color));
			if( temp != null ) {
				color = temp.getRGB();
			}
	        user.sendColorChange(color);
	        textInputArea.setForeground(temp);
		}
		
		else if(e.getSource().equals(whisper)){
	        String tar = userList.getSelectedValue().toString();
	        target = tar;
	        sendingTarget.setText("Whisper to: "+ target);
		}
		
		else if(e.getSource().equals(video)){
			
		}
		
		else if(e.getSource().equals(sendfile)){
	
		}
	}
	 
	public List<Element> getAllElements(JTextPane x) {
			Element[] roots = x.getStyledDocument().getRootElements();
			return getAllElements(roots);
		    }
		    private List<Element> getAllElements(Element[] roots) {
		            List<Element> icons = new LinkedList<Element>();
		            for (int a = 0; a < roots.length; a++) {
		                    if(roots[a] == null)
		                            continue ;
		                    icons.add(roots[a]);
		                    for (int c = 0; c < roots[a].getElementCount(); c++) {
		                        Element element = roots[a].getElement(c);
		                        icons.addAll(getAllElements(new Element[] { element }));
		                    }
		            }
		            return icons;
		    }
	
    public String getInputText(JTextPane x) {
        Map<Integer,String> mp = new HashMap<Integer,String>();
        String t =x.getText();
        List<Element> els = getAllElements(x);
        for(Element el : els) {
                Icon icon = StyleConstants.getIcon(el.getAttributes());
                if(icon != null) {
                        String tmp = ((ImageIcon)icon).getDescription();
                        // 假设 icon中的desc存放它的 filePath
                        mp.put(el.getStartOffset(), tmp);
                }
        }
        StringBuffer tt = new StringBuffer("");
        char[] chr = t.toCharArray();
        for(int c=0; c<chr.length; c++) {
                String v = mp.get(new Integer(c));
                if(v == null)
                        tt.append(chr[c]);
                else
                        tt.append(v);
        }
        return tt.toString();
}
	
	 //send msg as public or whisper
    private void prepareMsg ( String msg ) {

        String tar = target;
        //public msg
        if ( tar == "All members" ) {
        	user.sendSMsg(msg);
        }
        //whisper msg
        else {
            user.sendWMsg(msg, tar, 0);
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
	
	public void delUser ( String user ) {
		userListVector.remove(user);
		userList.setListData(userListVector);

        doc.removeStyle(user);

        addSysLine(user + " left.");
    }
	
	public void addNewLine ( String text , String color ) {
		ChatLines.add(text + "\n");
		ChatLineColor.add(color);
		String oriText = text + "\n";
		String oriTexture = color;
		
		Vector<String> texts = new Vector<String>();
		Vector<String> textures = new Vector<String>();
		Vector<Integer> indexs  = new Vector<Integer>();

        for (String regex : smileys) {
            int index = 0;
            while (index >= 0) {
                index = oriText.indexOf(regex, index);
                if (index>=0) {
                    indexs.add(index);
                    index++;
                }
            }
        }

        if ( indexs.size()!=0 ) {  //some smileys exist
            int[] indexs_array = new int[indexs.size()];
            for (int k=0; k<indexs.size(); k++) {
                indexs_array[k] = indexs.elementAt(k);
            }

            Arrays.sort(indexs_array);

            Vector<String> splited = new Vector<String>();
            splited.add(oriText.substring(0, indexs_array[0]));
            for (int j=0; j<indexs_array.length; j++) {
                splited.add(oriText.substring(indexs_array[j], indexs_array[j]+9));
                if (j==indexs_array.length-1)
                    splited.add(oriText.substring(indexs_array[j]+9));
                else
                    splited.add(oriText.substring(indexs_array[j]+9, indexs_array[j+1]));
            }
            for (int i=0; i<splited.size(); i++) {
                String s = splited.elementAt(i);
                String t = oriTexture;
                for (String smi: smileys) {
                    if (s.equals(smi)) {
                        t = smi;
                    }
                    else if (s.equals("")) {
                        s = new String(" ");
                    }
                }
                texts.add(i, s);
                textures.add(i, t);
            }            
        }
        else {
            texts.add(oriText);
            textures.add(oriTexture);
        }
        try {
            for (int i =0; i<texts.size(); i++) {
                doc.insertString( doc.getLength(),
                                  texts.elementAt(i),
                                  doc.getStyle(textures.elementAt(i)) );
            }
        }
        catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text into text pane.");
        }
        chatArea.setCaretPosition(doc.getLength());
	}
	
    public void addSysLine ( String text ) {
        ChatLines.add(text + "\n");
        ChatLineColor.add("system");
        refreshChat(text + "\n", "system");
    }

    public void addWarnLine ( String text ) {
        ChatLines.add(text + "\n");
        ChatLineColor.add("warn");
        refreshChat(text + "\n", "warn");
    }
    
    public void userChangeColor ( String name, int c ) {
        Style s = doc.getStyle(name);
        StyleConstants.setForeground(s, new Color(c));
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
    
    public void setTarget(String t){
    	target = t;
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
