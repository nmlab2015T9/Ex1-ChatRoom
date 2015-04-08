package gui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import client.UserData;;

public class UserDataListRenderer extends JLabel implements ListCellRenderer<UserData>
{
	private static final long serialVersionUID = 1L;
	
	public UserDataListRenderer() {
		setOpaque(true);
	}
	@Override
	public Component getListCellRendererComponent(JList<? extends UserData> list,
			UserData user, int index, boolean isSelected, boolean cellHasFocus) {
		String name = user.username;
		ImageIcon img = new ImageIcon(user.face);
		setIcon(img);
		setText(name);
		if (isSelected) {
		    setBackground(list.getSelectionBackground());
		    setForeground(list.getSelectionForeground());
		} else {
		    setBackground(list.getBackground());
		    setForeground(list.getForeground());
		}
		return this;
	}

}
