package server;

import java.util.Vector;

public class Room {
	public int roomId;
	public Vector<Client> memberList;
	public String roomName;

	public Room(int i) {
		roomId = i;
		memberList = new Vector<Client>();
		roomName = "Room #" + roomId;
	}
	public Room(int i, String roomname) {
		roomId = i;
		memberList = new Vector<Client>();
		roomName = roomname;
	}

	public boolean hasClient(Client client) {
		return memberList.contains(client);
	}

	public int getID() {
		return roomId;
	}

	public void sendRoom(String msg) {
		for(Client c: memberList)
			c.send(msg);
	}

	public void roomAddUser(Client client) {
		memberList.add(client);
	}

	public void roomRmUser(Client client) {
		memberList.remove(client);
	}

}
