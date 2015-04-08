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

import client.UserData;

public class OtherHead extends JFrame{
	private static final long serialVersionUID = 1L;
	private MainFrame mainframe;
    private static int sx = 60, sy = 60;
    private static Ellipse2D ellipse = new Ellipse2D.Double(0, 0, sx, sy); 
    private static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private static int width = gd.getDisplayMode().getWidth();
    //private static int height = gd.getDisplayMode().getHeight();
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
			for(int i = 0; i != Rooms.size() ; i++){	
        		Rooms.get(i).setVisible(true);
			}
			setVisible(true);
			timer.scheduleAtFixedRate(new TimerTask(){  
				@Override
				public void run() {
					if(ii<=15){
						setLocation(p.x, p.y + ((sy + 5) * (Rooms.size() + 1) * ii/15));
						ii++;
						for(int i = 0; i != Rooms.size() ; i++){	
			        		Rooms.get(i).setLocation(p.x, p.y + ((sy + 5)*(i+1)) * ii/15);
						}
					}
					else{
						ii = 0;
						timer.cancel();
					}
				}},200 ,10);
			
			/*for(int i = 0; i != Rooms.size() ; i++){
        		final RoomHead rm = Rooms.get(i);
        		final int j = i;
        		Timer timer2 = new Timer();
            	timer2.scheduleAtFixedRate(new TimerTask(){  
            		@Override
            		public void run() {
            			if(ii<=15){
            				rm.setVisible(true);
            				rm.setLocation(p.x, p.y + ((sy + 5)*(j+1)) * ii/15);
            				ii++;
            			}
            			else{
            				ii = 0;
            				timer2.cancel();
            			}
            		}},100 ,3);
			}*/
		}
		
		else{
			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask(){  
				@Override
				public void run() {
					if(ii<=15){
						setLocation(p.x, p.y + (sy + 5) * (Rooms.size() + 1) - ((sy + 5) * (Rooms.size() + 1) * ii/15));
						ii++;
						for(int i = 0; i != Rooms.size() ; i++){
							Rooms.get(i).setLocation(p.x, p.y + ((sy + 5)*(i+1))  - (((sy + 5)*(i+1)) * ii/15));
						}
					}
					else{
						ii = 0;
						for(int i = 0; i != Rooms.size() ; i++){
							Rooms.get(i).setVisible(false);
						}
						setVisible(false);
						timer.cancel();
					}
				}},200 ,10);
			
			/*for(int i = 0; i != Rooms.size() ; i++){
        		RoomHead rm = Rooms.get(i);
        		int j = i;
        		Timer timer2 = new Timer();
            	timer2.scheduleAtFixedRate(new TimerTask(){  
            		@Override
            		public void run() {
            			if(ii<=15){
            				rm.setLocation(p.x, p.y + ((sy + 5)*(j+1))  - (((sy + 5)*(j+1)) * ii/15));
            				ii++;
            			}
            			else{
            				ii = 0;
            				timer2.cancel();
            				rm.setVisible(false);
            			}
            		}},10 ,3);
			}*/
		}
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
        				timer.cancel();
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
    
    public void addNewUserToEveryRoom(UserData user){
    	 for(int i = 0; i != Rooms.size(); i++){
    			Rooms.get(i).addUser(user);
    		}
	}
    public void delUserToEveryRoom(UserData user){
   	 	for(int i = 0; i != Rooms.size(); i++){
   			Rooms.get(i).delUser(user);
   		}
	}

}
