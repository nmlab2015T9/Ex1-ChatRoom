package client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class UserData
{
	public String username;
	public BufferedImage face = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
	
	public UserData(String newUser) {
		username = newUser;
		
		
		try {
			BufferedImage tempImg
			= ImageIO.read(CurveClient.class.getResource("/res/profileimg.png"));
			Graphics g = face.createGraphics();
			g.drawImage(tempImg, 0, 0, 50, 50, null);
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return username;
	}
}
