package gui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import server.Client;

public class ClientListRenderer extends JLabel implements ListCellRenderer<Client>
{
	private static final long serialVersionUID = 1L;
	
	public ClientListRenderer() {
		setOpaque(true);
	}
	@Override
	public Component getListCellRendererComponent(JList<? extends Client> list,
			Client client, int index, boolean isSelected, boolean cellHasFocus) {
		String name = client.username;
		ImageIcon img = new ImageIcon(client.face);
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
