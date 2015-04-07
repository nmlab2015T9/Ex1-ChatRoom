package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;


public class Client implements Runnable
{
	private Socket sock;
	private DataInputStream in;
	private DataOutputStream out;
	private String msg;
	public String username;
	public int clientID;
    public int userColor;
	
    public Client(Socket s, int id) {
		try {
			sock = s;
			in = new DataInputStream( sock.getInputStream() );
			out = new DataOutputStream( sock.getOutputStream() );
			clientID = id;
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		
		userColor = java.awt.Color.BLACK.getRGB();
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
				CurveServer.printMsg(msg);
				parseMsg(msg);
			}
		} catch (IOException e) {
			if( e instanceof SocketException ) {
				CurveServer.removeUser(this, clientID);
			}
			else {
				System.out.println(e.toString());
				e.printStackTrace();
			}
		}
	}
	@SuppressWarnings("unused")
	private void parseMsg(String msg) {
		if( msg.startsWith("/s")) { // public chat
			// /s [srcname] message
			CurveServer.sendPublic(msg);
		}
		else if( msg.startsWith("/w")) { // whisper
			// /w [srcname] [destname] message
			String src = msg.split(" ", 4)[1];
			String dest = msg.split(" ", 4)[2];
			String temp = msg.split(" ", 4)[3];
			if( CurveServer.sendPrivate( dest, msg ) != true ) {
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
                        CurveServer.sendPublic(msg);
		}
		else if( msg.startsWith("/n") ) { // new room
			// /n
			int roomid = CurveServer.roomNew();
			CurveServer.roomAddUser(roomid, this);
		}
		else if( msg.startsWith("/rs") ) { // send to room
			// /rs [roomid] [srcname] message
			int room = Integer.parseInt(msg.split(" ", 4)[1]);
			//String temp = msg.split(" ", 3)[2];
			if( CurveServer.sendRoom(room, msg) != true ) {
				send("/e You're not in this room!");
			}
		}
		else if( msg.startsWith("/a") ) { // add user to room
			// /a [roomid] [username]
			int room = Integer.parseInt(msg.split(" ", 3)[1]);
			String dest = msg.split(" ", 3)[2];
			CurveServer.roomAddUser(room, dest);
		}
		else if( msg.startsWith("/l") ) { // leave room
			// /l [roomid]
			int room = Integer.parseInt(msg.split(" ", 2)[1]);
			CurveServer.roomRmUser(room, this);
		}
		else if( msg.startsWith("/f") ) { // send file
			// /f [src name] [dest name]
			String src = msg.split(" ", 3)[1];
			String dest = msg.split(" ", 3)[2];
			CurveServer.sendPrivate(dest, msg+" "+sock.getInetAddress().getHostName());
		}
		else if( msg.startsWith("/q-")) { // remove user
			// /q- [username]
			CurveServer.removeUser(this, clientID);
		}
		else if(msg.startsWith("/v")) {
			// /v [srcname] [destname] 
			
		}
	}
	private void setname() throws IOException {
		String name;
		out.writeUTF("/u");
		while( true ) {
			name = in.readUTF();
			//System.out.println(name);
			if( CurveServer.findUserByName(name) != -1) {
				out.writeUTF("Name already taken! Please use another name.");
				continue;
			}
			else {
				username = name;
				send( "/ua" ); // send username ACK
				send( "/s Server Welcome to the chatroom");
				for( Client c: (CurveServer.clientList) ) { // send the current userlist to the new user
					if (c!=this) send("/q+ "+ c.username+" " + c.userColor);
				}
				CurveServer.sendAll( "/q+ " + username+" " + userColor); // send the new user information to all other users
				CurveServer.adduser(username, clientID);
				break;
			}
		}
	}
	
}
