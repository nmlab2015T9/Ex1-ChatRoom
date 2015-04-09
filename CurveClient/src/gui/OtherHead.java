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
	private MainHead mainhead;
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
		setLocation(x, y);
		for(int i = 0; i != Rooms.size(); i++){
			Rooms.get(i).setDraggedPosition(x,y);
		}
	}
	
	public void setClickedPosition(Point position){
		final Point p = position;
		
		if(mainframe.isVisible()){
			for(int i = 0; i != Rooms.size() ; i++){	
        		Rooms.get(i).setVisible(true);
			}
			setVisible(true);
			final Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask(){  
				@Override
				public void run() {
					if(ii<=15){
						setLocation(p.x, p.y + ((sy + 5) * (Rooms.size() + 1) * ii/15));
						++ii;
						if (ii == 16){
							timer.cancel();
							ii = 0;
						}
					}
				}},500 ,10);
			
			final Timer t = new Timer();
			t.scheduleAtFixedRate(new TimerTask(){  
				@Override
				public void run() {
					if(ii<=15){
						for(int i = 0; i != Rooms.size() ; i++){	
			        		Rooms.get(i).setLocation(p.x, p.y + ((sy + 5)*(i+1)) * ii/15);						
						}
						ii++;
						if (ii == 16){
							t.cancel();
							ii = 0;
						}
					}
				}},200 ,10);
		}
		
		else{		
			final Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask(){  
				@Override
				public void run() {
					if(ii<=15){
						setLocation(p.x, p.y + ((sy + 5) * (Rooms.size() + 1) - ((sy + 5) * (Rooms.size() + 1) * ii/15)));
						++ii;
						if (ii == 16){
							timer.cancel();
							ii = 0;
							setVisible(false);
						}						
					}
				}},500 ,10);
			

			final Timer t = new Timer();
			t.scheduleAtFixedRate(new TimerTask(){  
				@Override
				public void run() {
					if(ii<=15){
						for(int i = 0; i != Rooms.size() ; i++){	
			        		Rooms.get(i).setLocation(p.x, p.y + ((sy + 5)*(i+1)) - (((sy + 5)*(i+1)) * ii/15));
						}
						ii++;
						if (ii == 16){
							t.cancel();
							ii = 0;
						}
					}
				}},200 ,10);
		}
	}

	
	public void addRoom(int roomID){
		final Point p = getLocation();
		RoomHead room = new RoomHead(roomID);
		Rooms.add(room);
        Map.put(roomID, room);

        mainframe.setVisible(true);
        setClickedPosition(mainhead.getLocation());
    	/*for(int i = 0; i != Rooms.size(); i++){
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
		RoomHead room = Map.get(roomID);		
        Rooms.remove(room);
        Map.remove(roomID);
        setClickedPosition(mainhead.getLocation());
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
    
    public void setMainHead(MainHead mh){
    	mainhead = mh;
    }

}
