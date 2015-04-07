package gui;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class OtherHead extends JFrame{
	private String username;
    private int RoomID;
    private static int sx = 60, sy = 60;
    private static Ellipse2D ellipse = new Ellipse2D.Double(0, 0, sx, sy); 
    private static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private static int width = gd.getDisplayMode().getWidth();
    private static int height = gd.getDisplayMode().getHeight();
    
    
    public OtherHead(){
		initComponents();
    }
    
	public OtherHead(int ID , String name){
        RoomID = ID;
        username = new String(name);
        
        initComponents(); // need to fix!!
	}

	private void initComponents(){
		setUndecorated(true);
        setShape(ellipse);
        setSize(sx, sy);
        setLocation(width-sx, 100);
        setLayout(new BorderLayout());
    	URL url = client.CurveClient.class.getResource("/res/plus.png");
		ImageIcon otherheadImage = new ImageIcon(url);
        JLabel otherhead = new JLabel(otherheadImage);
        getContentPane().add(otherhead,BorderLayout.CENTER);
	}
	
	
	public void setDraggedPosition(int x, int y){
		
		setLocation(x, y);
	}
	
	public void setPressedPosition(Point position){
		Point p = position;
		setLocation(p);
	}
}
