package gui;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class OtherHead extends JFrame{
	private MainFrame mainframe;
	private String username;
    private int RoomID, positionNum = 0;
    private static int sx = 60, sy = 60;
    private static Ellipse2D ellipse = new Ellipse2D.Double(0, 0, sx, sy); 
    private static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private static int width = gd.getDisplayMode().getWidth();
    private static int height = gd.getDisplayMode().getHeight();
    private static int ii;

    public Vector <RoomHead> Rooms;
    public Map<Integer, RoomHead> Map;
    
    public OtherHead(MainFrame mainFrame){
    	mainframe = mainFrame;
		initComponents();
    }
   
	private void initComponents(){
		Rooms = new Vector <RoomHead>();
		Map = new HashMap<Integer, RoomHead>();
		setUndecorated(true);
        setShape(ellipse);
        setSize(sx, sy);
        setLocation(width-sx, 100);
        setLayout(new BorderLayout());
    	URL url = client.CurveClient.class.getResource("/res/plus.png");
		ImageIcon otherheadImage = new ImageIcon(url);
        JLabel otherhead = new JLabel(otherheadImage);
        getContentPane().add(otherhead,BorderLayout.CENTER);
        
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
            	client.CurveClient.cMgr.sendNewRoom();
            }
        });
	}
		
	public void setDraggedPosition(int x, int y){
		
		setVisible(false);
		//setLocation(x, y);
	}
	
	public void setClickedPosition(Point position){
		Point p = position;
		
		if(mainframe.isVisible()){
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask(){  
				@Override
				public void run() {
					setVisible(true);
					if(ii<=15){
						setLocation(p.x, p.y + ((sy + 5) * ii/15));
						ii++;
					}
					else{
						ii = 0;
						cancel();
					}
				}},150 ,3);
		}
		
		else{
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask(){  
				@Override
				public void run() {
					setVisible(false);
					if(ii<=15){
						setLocation(p.x, p.y + (sy + 5) - ((sy + 5) * ii/15));
						ii++;
					}
					else{
						ii = 0;
						cancel();
					}
				}},150 ,3);
			
			for(int i = 0; i != Rooms.size() ; i++){
        		RoomHead rm = Rooms.get(i);
        		int j = i;
        		Timer timer2 = new Timer();
            	timer2.scheduleAtFixedRate(new TimerTask(){  
            		@Override
            		public void run() {
            			if(ii<=15){
            				rm.setLocation(p.x, p.y + ((sy + 5)*j)  - (((sy + 5)*j) * ii/15));
            				ii++;
            			}
            			else{
            				ii = 0;
            				cancel();
            			}
            		}},10 ,3);
			}
		}
    	
		//setLocation(p.x, p.y + sy + 5);
	}

	
	public void addRoom(int roomID){
		Point p = getLocation();
		RoomHead room = new RoomHead(roomID);
		Rooms.add(room);
        Map.put(roomID, room);
    	for(int i = 0; i != Rooms.size(); i++){
    		RoomHead rm = Rooms.get(i);
    		int j = i;
    		Timer timer = new Timer();
        	timer.scheduleAtFixedRate(new TimerTask(){  
        		@Override
        		public void run() {
        			if(ii<=15){
        				rm.setLocation(p.x, p.y + ((sy + 5)*j) * ii/15);
        				setLocation(p.x, p.y + (sy + 5) * ii/15);
        				ii++;
        			}
        			else{
        				ii = 0;
        				cancel();
        			}
        		}},10 ,3);
    		//Rooms.get(i).setLocation(p.x, p.y + (sy + 5)*i);
    		//setLocation(p.x, p.y + (sy + 5)*(i+1));
    	}
    	/*
        newTab.setFrame(this);
        newTab.setClientObject(ClientObject);
        */
	}
	
	public void delRoom(int roomID){
		/*ChatTab tab = map.get(roomID);
        tabs.remove(tab);
        map.remove(roomID);
        TabPane.remove(tab);*/
	}

	public Vector <RoomHead> getRooms(){
		return Rooms;
	}

    public Map<Integer, RoomHead> getMaps(){
    	return Map;
    }
}
