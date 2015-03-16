package userver;
import java.io.*;
import java.net.*;
import java.util.*;
/**
 *
 * @author StarryDawn
 */
public class Client implements Runnable {
	private Socket sock;
	private UServer mainserver;
	private DataInputStream in;
	private DataOutputStream out;
	private String msg, username;
	private int clientID;
        public int userColor;

	public Client( UServer us, Socket s, int id ) {
		try {
			sock = s;
			mainserver = us;
			in = new DataInputStream( s.getInputStream() );
			out = new DataOutputStream( s.getOutputStream() );
			clientID = id;
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}

                userColor = java.awt.Color.BLACK.getRGB();
	}
	public String getname() { return username; }
	public void setname() throws IOException {
		String name;
		out.writeUTF("/u");
		while( true ) {
			name = in.readUTF();
			//System.out.println(name);
			if( mainserver.userlist.contains(name) ) {
				out.writeUTF("Name already taken! Please use another name.");
				continue;
			}
			else {
				username = name;
				send( "/ua" ); // send username ACK
				send( "/s Server Welcome to the chatroom");
				for( Client c: (mainserver.cli) ) { // send the current userlist to the new user
					if (c!=this) send("/q+ "+ c.username+" " + c.userColor);
				}
				mainserver.sendAll( "/q+ " + username+" " + userColor); // send the new user information to all other users
				mainserver.adduser(username, clientID);
				break;
			}
		}
	}
	public void send( String s ) {
		try {
			out.writeUTF(s);
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			setname();
			while(true) {
				msg = in.readUTF();
				System.out.println(msg);
				mainserver.printMsg(msg);
				parseMsg(msg);
			}
		} catch (IOException e) {
			if( e instanceof SocketException ) {
				mainserver.remove(this, clientID);
			}
			else {
				System.out.println(e.toString());
				e.printStackTrace();
			}
		}
	}

	public void parseMsg( String msg ) {
		if( msg.startsWith("/s")) { // public chat
			// /s [srcname] message
			mainserver.sendPublic(msg);
		}
		else if( msg.startsWith("/w")) { // whisper
			// /w [srcname] [destname] message
			String src = msg.split(" ", 4)[1];
			String dest = msg.split(" ", 4)[2];
			String temp = msg.split(" ", 4)[3];
			if( mainserver.sendPrivate( dest, msg ) != true ) {
				send("/e 沒有這個使用者");
			}
			else {
				send(msg);
			}
		}
                else if( msg.startsWith("/c")) { // color change
			// /c [srcname] color
                        String newColor = msg.split(" ", 3)[2];
                        userColor = java.lang.Integer.parseInt(newColor);
			mainserver.sendPublic(msg);
		}
		else if( msg.startsWith("/n") ) { // new room
			// /n
			int roomid = mainserver.makeRoom();
			mainserver.roomadd(roomid, this);
		}
		else if( msg.startsWith("/rs") ) { // send to room
			// /rs [roomid] [srcname] message
			int room = Integer.parseInt(msg.split(" ", 4)[1]);
			//String temp = msg.split(" ", 3)[2];
			if( mainserver.sendRoom(room, msg) != true ) {
				send("/e You're not in this room!");
			}
		}
		else if( msg.startsWith("/a") ) { // add user to room
			// /a [roomid] [username]
			int room = Integer.parseInt(msg.split(" ", 3)[1]);
			String dest = msg.split(" ", 3)[2];
			mainserver.roomadd(room, dest);
		}
		else if( msg.startsWith("/l") ) { // leave room
			// /l [roomid]
			int room = Integer.parseInt(msg.split(" ", 2)[1]);
			mainserver.roomrm(room, this);
		}
		else if( msg.startsWith("/f") ) { // send file
			// /f [src name] [dest name]
			String src = msg.split(" ", 3)[1];
			String dest = msg.split(" ", 3)[2];
			mainserver.sendPrivate(dest, msg+" "+sock.getInetAddress().getHostName());
		}
	}

	public void closeConnection() throws IOException {
		sock.close();
	}

}
