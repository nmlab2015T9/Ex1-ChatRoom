package gui;


import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class MainHead extends JFrame{
	private static final long serialVersionUID = 1L;
	private static int sx = 60, sy = 60, sx2 = 80, sy2 = 80; //sx2, sy2 = cross size
	private static int gap = 200;
	private static int i = 0;
    private static Point point = new Point();
    private static Ellipse2D ellipse = new Ellipse2D.Double(0, 0, sx, sy), ellipse2 = new Ellipse2D.Double(0, 0, sx2, sy2);   
    private static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private static int width = gd.getDisplayMode().getWidth();
    private static int height = gd.getDisplayMode().getHeight();
    private static final JFrame closeArea = new JFrame();
    //private static Timer timer;
    //private static boolean inArea;
    private MainFrame mainframe;
    private OtherHead otherhead;
    
    private Vector <RoomHead> Rooms;
    private Map<Integer, RoomHead> Map;

    public MainHead(MainFrame mainFrame, OtherHead otherHead){
    	mainframe = mainFrame;
    	otherhead = otherHead;
    	Rooms = otherhead.getRooms();
    	Map = otherhead.getMaps();
    	initComponents();
}
    
    private void initComponents(){
    	setUndecorated(true);
        setAlwaysOnTop(true);
        setShape(ellipse);
        setSize(sx, sy);
        setLocation(width-sx, 100);
        setLayout(new BorderLayout());
    	URL url = client.CurveClient.class.getResource("/res/lobby.png");
		ImageIcon lobbyImage = new ImageIcon(url);
        JLabel mainhead = new JLabel(lobbyImage);
        getContentPane().add(mainhead,BorderLayout.CENTER);
        
        //setting of the close area
    	URL url2 = client.CurveClient.class.getResource("/res/cross.png");
		ImageIcon crossImage = new ImageIcon(url2);
        JLabel cross = new JLabel(crossImage);
        closeArea.getContentPane().setLayout(null);
        cross.setSize(50, 50);
        cross.setLocation(15, 15);
        closeArea.getContentPane().add(cross);
        closeArea.setUndecorated(true);
        closeArea.setShape(ellipse2);
        closeArea.setSize(sx2, sy2);
        closeArea.setLocation(width/2 - sx2/2, height - sy2);
        closeArea.setOpacity(0.5f);
        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher(){
        	 public boolean dispatchKeyEvent(KeyEvent e) {
                 if(e.getID() == KeyEvent.KEY_PRESSED && (e.getKeyCode() == KeyEvent.VK_Q) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)){
                	 System.exit(0);
                 }
                 return false;
             }
        });
        
        //
        // The mouse listener and mouse motion listener we add here is to simply
        // make our frame dragable.
        //
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                point.x = e.getX();
                point.y = e.getY();
            }
            
            public void mouseClicked(MouseEvent e){
            	Point p = getLocation();
            	if(!mainframe.isVisible()){
            		mainframe.setVisible(true);
            	}
            	else
            		mainframe.setVisible(false);
            	otherhead.setClickedPosition(p);
            	/*timer = new Timer();
            	timer.scheduleAtFixedRate(new TimerTask(){  
            		@Override
            		public void run() {
            			if(i<=20){
            				if(p.y > 20)
            					setLocation(p.x + ((width - sx) - p.x)*i/20, p.y - (p.y - 20)*i/20);
            				else if(p.y < 20)
            					setLocation(p.x + ((width - sx) - p.x)*i/20, p.y + (20 - p.y)*i/20);
            				i++;
            				mainframe.setSize(gui.MainFrame.frameSX*i/20, gui.MainFrame.frameSY*i/20);
            			}
            			else{
            					timer.cancel();
            					i = 0;
            			}
            		}},10 ,1);*/
            }
            
            public void mouseReleased(MouseEvent e){
            	final Point p = getLocation();
            	if(p.x < gap){
            		Timer timer = new Timer();
                	timer.scheduleAtFixedRate(new TimerTask(){  
                		@Override
                		public void run() {
                			if(i<=15){
                				if(i<=10)
                					setLocation(p.x - (p.x + 10) * i/10, p.y);
                				else if(i>10)
                					setLocation(-10 + 2 * (i - 10), p.y);
                				i++;
                			}
                			else{
                				closeProcess();
                				cancel();
                				i = 0;
                			}
                		}},10 ,3);
            	}
            	else if(p.y < gap){
            		Timer timer = new Timer();
                	timer.scheduleAtFixedRate(new TimerTask(){  
                		@Override
                		public void run() {
                			if(i<=15){
                				if(i<=10)
                					setLocation(p.x, p.y - (p.y - 10) * i/10);
                				else if(i>10)
                					setLocation(p.x, 10 + 2 * (i - 10));
                				i++;
                			}
                			else{
                				closeProcess();
                				cancel();
                				i = 0;
                			}
                		}},10 ,3);
            	}
            	else if(Math.abs(p.x - width) < (gap + sx)){
            		Timer timer = new Timer();
                	timer.scheduleAtFixedRate(new TimerTask(){  
                		@Override
                		public void run() {
                			if(i<=15){
                				if(i<=10)
                					setLocation(p.x + ((width - sx) - p.x+ 10) * i/10, p.y);
                				else if(i>10)
                					setLocation(width - sx + 10 - 2 * (i - 10), p.y);
                				i++;
                			}
                			else{
                				closeProcess();
                				cancel();
                				i = 0;
                			}
                		}},10 ,3);
            	}
            	else if(Math.abs(p.y - height) < (gap + sy)){
            		Timer timer = new Timer();
                	timer.scheduleAtFixedRate(new TimerTask(){  
                		@Override
                		public void run() {
                			if(i<=15){
                				if(i<=10)
                					setLocation(p.x , p.y + ((height - sy) - p.y+ 10) * i/10);
                				else if(i>10)
                					setLocation(p.x, height - sy + 10 - 2 * (i - 10));
                				i++;
                			}
                			else{
                				closeProcess();
                				cancel();
                				i = 0;
                			}
                		}},10 ,3);
            	}
            	else
            		closeProcess();
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
            	
                Point p = getLocation();
                setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
                otherhead.setDraggedPosition(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
                //if(p.x > (width/2 - sx2/2 - 200) && p.x < (width/2 + sx2/2 + 200) && p.y > (height - sy2 - 200)){
                	closeArea.setVisible(true);
                	closeArea.setLocation((width/2 - sx2/2) + ((p.x + e.getX() - point.x) - (width/2 - sx2/2)) / 50, 
                						  (height - sy2) + ((p.y + e.getY() - point.y) - (height - sy2)) / 50);
                //	inArea = true;
            	//}
                //else if(inArea && (p.x < (width/2 - sx2/2 - 200) || p.x > (width/2 + sx2/2 + 200) || p.y < (height - sy2 - 200))){
                //	closeProcess();
                //	inArea = false;
                //}
            }
        });
        
    }
    
    private void closeProcess(){
    	final Point p = getLocation();
    	Timer timer = new Timer();
    	timer.scheduleAtFixedRate(new TimerTask(){  
    		public void run(){  
    			SwingUtilities.invokeLater(new Runnable(){
    		
    			
				@Override
				public void run() {
					if(i<=50){
	    				closeArea.setLocation(width/2 - sx2/2, (height - sy2)+300*i/50);
	    				//for the main head to set in the center of closeArea
	    				if(p.x > (width/2 - sx2/2) && p.x < (width/2 + sx2/2) && p.y > (height - sy2))
	    					setLocation(width/2 - sx2/2 + 10, (height - sy2 + 10)+300*i/50);
	    				i++;
	    			}
	    			else{
	    				if(p.x > (width/2 - sx2/2) && p.x < (width/2 + sx2/2) && p.y > (height - sy2)){
	    					client.CurveClient.cMgr.sendDelUser();
	    					System.exit(0);
	    				}
	    				else {
	    					closeArea.setVisible(false);
	    					closeArea.setLocation(width/2 - sx2/2, height - sy2);
	    					cancel();
	    					i = 0;
	    				}
	    			}
				}
    		});
    			}},10 ,10);
    }
    
    // "/q+" command
    public void addNewUser ( String user , int color ) {
        mainframe.addNewUser(user, color);
    }

    // "/q-" command
    public void delUser ( String user ) {
    	mainframe.delUser(user);
    }

    // "/r+" command
    public void addUser ( String user , int color , int roomID ) {
    	RoomHead room = Map.get(roomID);
        room.addUser(user);
    }

    // "/r-" command
    public void delUser ( String user , int roomID ) {
    	RoomHead room = Map.get(roomID);
    	room.delUser(user);
    }

    //add new lines
    public void addNewLine ( String text , String color , int roomID ) {
        if (roomID == 0) {
        	mainframe.addNewLine(text, color);
        }
        else {
        	RoomHead room = Map.get(roomID);
        	room.addNewLine(text, color);
        }
    }

    public void addSysLine ( String text ) {
    	mainframe.addSysLine(text);
    }

    public void addWarnLine ( String text ) {
    	mainframe.addWarnLine(text);
    }

    public void setTarget ( String name , int roomID ) {
        if (roomID == 0) {
        	mainframe.setTarget(name);
        }
        else {
        	RoomHead room = Map.get(roomID);
        	room.setTarget(name);
        }
    }

    // "/a" command
    public void addRoom ( int roomID ) {
        otherhead.addRoom(roomID);
    }

    // "/l" command
    public void delRoom ( int roomID ) {
    	otherhead.delRoom(roomID);
    }

    // "/c" command
    public void userChangeColor ( String name, int c ) {
    	mainframe.userChangeColor(name, c);
    	for(int i = 0; i != Rooms.size(); i++){
    		Rooms.get(i).userChangeColor(name, c);
    	}
    }

    // "/f" command
    /*public void sendFile( String dest ) {
    	ClientObject.sendFile(username, dest);
    }*/


    public void clear () {
    	mainframe.clear();
    }
    
}
