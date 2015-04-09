package client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class UserData
{
	public String username;
	public int color;
	public BufferedImage face = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
	
	public UserData(String newUser, int c) {
		username = newUser;
		color = c;
		
		try {
			BufferedImage tempImg
			= ImageIO.read(CurveClient.class.getResource("/res/profileimg.png"));
			Graphics g = face.createGraphics();
			if(tempImg.getHeight()>tempImg.getWidth()) {
				g.drawImage(tempImg, 0, 0, 50*tempImg.getWidth()/tempImg.getHeight(), 50, null);
			}
			else {
				g.drawImage(tempImg, 0, 0, 50, 50*tempImg.getHeight()/tempImg.getWidth(), null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return username;
	}
}
