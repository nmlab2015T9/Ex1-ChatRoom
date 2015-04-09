package gui;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import client.CurveClient;
import client.UserData;

public class RoomHead extends JFrame{
	private static final long serialVersionUID = 1L;
	private static int sx = 60, sy = 60, sx2 = 80, sy2 = 80;
    private static Ellipse2D ellipse = new Ellipse2D.Double(0, 0, sx, sy), ellipse2 = new Ellipse2D.Double(0, 0, sx2, sy2); 
    private static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private static int width = gd.getDisplayMode().getWidth();
    private static int height = gd.getDisplayMode().getHeight();
    private static final JFrame closeArea = new JFrame();
	private static int i = 0;
    private static Point point = new Point() , pp = new Point();
    private RoomFrame roomframe;
    private int ID;
    
	public RoomHead(int RoomID){
		
		ID = RoomID;
		initFrame();
		initComponents();
	}

	private void initFrame(){
		roomframe = new RoomFrame(ID);
	}
	
	private void initComponents() {

		setUndecorated(true);
        setShape(ellipse);
        setSize(sx, sy);
        setVisible(true);
        setLayout(new BorderLayout());
    	URL url = client.CurveClient.class.getResource("/res/otherhead.png");
		ImageIcon otherheadImage = new ImageIcon(url);
        JLabel otherhead = new JLabel(otherheadImage);
        getContentPane().add(otherhead,BorderLayout.CENTER);
        
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
        
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            	pp = getLocation();
                point.x = e.getX();
                point.y = e.getY();
            }
            
            public void mouseClicked(MouseEvent e){
            	if(!roomframe.isVisible()){
            		roomframe.setVisible(true);
            	}
            	else
            		roomframe.setVisible(false);
            }
            
            public void mouseReleased(MouseEvent e){
            		closeProcess();
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
            	
                Point p = getLocation();
                setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
               
                	closeArea.setVisible(true);
                	closeArea.setLocation((width/2 - sx2/2) + ((p.x + e.getX() - point.x) - (width/2 - sx2/2)) / 50, 
                						  (height - sy2) + ((p.y + e.getY() - point.y) - (height - sy2)) / 50);
            }
        });
        
    }
    
	
	public void setDraggedPosition(int x, int y){
		
		setVisible(false);
		setLocation(x, y);
	}
	
    private void closeProcess(){
    	final Point p = getLocation();
    	final Timer timer = new Timer();
    	timer.scheduleAtFixedRate(new TimerTask(){  
    		
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
	    					roomframe.dispose();
	    					dispose();
	    					cancel();
	    					client.CurveClient.cMgr.sendLeaveRoom(ID);
	    				}
	    				else {
	    					closeArea.setVisible(false);
	    					closeArea.setLocation(width/2 - sx2/2, height - sy2);
	    					timer.cancel();
	    					i = 0;
	    					
	    					final Timer timer = new Timer();
	    			    	timer.scheduleAtFixedRate(new TimerTask(){  
	    			    		
	    			    		@Override
	    			    		public void run() {
	    			    			if(i<=10){
	    			    				
	    			    				setLocation(p.x + (pp.x - p.x) * i/10, p.y + (pp.y - p.y) * i/10);
	    			    				i++;
	    			    			}
	    			    			else{
	    			    				timer.cancel();
	    			    				i = 0; 				
	    			    			}
	    			    		}
	    			    	},10 ,10);
	    				}
	    			}
				}
    		},10 ,10);
	}
	
    public void addNewLine ( String text , String color) {
    	roomframe.addNewLine(text, color);
    }

	public void setTarget(String name) {
		roomframe.setTarget(name);
	}
	
	public void userChangeColor ( String name, int c ) {
		roomframe.userChangeColor(name, c);
	}
	
	public void addUser(UserData user){
		roomframe.addNewUser(user);
	}
	public void addUser(String username) {
		int idx = CurveClient.dMgr.mainFrame.searchUserByName(username);
		addUser(CurveClient.dMgr.mainFrame.mainUserListModel.get(idx));
	}
	
	public void inviteUser(String username){
		int idx = CurveClient.dMgr.mainFrame.searchUserByName(username);
		roomframe.inviteUser(CurveClient.dMgr.mainFrame.mainUserListModel.get(idx));
	}
	
	public void delUser(UserData user){
		roomframe.delUser(user);
	}
	public void delUser(String username) {
		int idx = CurveClient.dMgr.mainFrame.searchUserByName(username);
		delUser(CurveClient.dMgr.mainFrame.mainUserListModel.get(idx));
	}
	/*public void setUserListVectorOfRoom(DefaultListModel<UserData> lobbyList){
		roomframe.setUserListVectorOfRoom(lobbyList);
	}*/
}
