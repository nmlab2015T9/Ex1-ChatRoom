package userver;
import java.util.*;
/**
 *
 * @author StarryDawn
 */
public class Chatroom {
	private int roomID;
	Vector<Client> roomCli;
	private String roomname;

	public Chatroom( int id ) {
		roomID = id;
		roomCli = new Vector<Client>();
		roomname = "Room #"+id;
	}
	public Chatroom( int id, String name ) {
		roomID = id;
		roomCli = new Vector<Client>();
		roomname = name;
	}

	public int getID() { return roomID; }
	public void adduser( Client c ) {
		roomCli.add(c);
	}
	public void rmuser( Client c ) {
		roomCli.remove(c);
	}
	public boolean isEmpty() {
		return roomCli.isEmpty();
	}
	public boolean hasUser( Client c ) {
		return roomCli.contains(c);
	}
	public boolean hasUser( String username ) {
		for( Client c: roomCli ) {
			if( username.equals(c.getname()) ) return true;
		}
		return false;
	}
	public void sendRoom( String msg ) {
		for( Client c: roomCli ) {
			c.send(msg);
		}
	}

}