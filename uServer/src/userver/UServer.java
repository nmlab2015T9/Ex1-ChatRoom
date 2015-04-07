package userver;
import java.io.*;
import java.util.*;
import java.net.*;
import gui.*;
/**
 * uSocket chatroom server class
 * @author StarryDawn
 */
public class UServer {
	private ServerSocket ss;
	//private Vector<Socket> sock;
	private int port;
	//private Vector<Thread> thd;
	Vector<Client> cli;
        Vector<String> userlist;
	Vector<Chatroom> roomlist;
	private int id; // cumulate client ids
	private int roomid; // cumulate room ids
	private Display dis;

	public UServer() {
		// initialize data
		//sock = new Vector<Socket>();
		//thd = new Vector<Thread>();
		cli = new Vector<Client>();
                userlist=new Vector<String>();
		roomlist = new Vector<Chatroom>();
		id = roomid = 0;
		dis = new Display( this );
		dis.setVisible(true);
		port = 9987;
		System.out.println("port: " + port);
		// create socket: listening forever
		try {
			ss = new ServerSocket(port);
			dis.addText( "Server created." );
			dis.addText( "Waiting for client." );
			while(true) {
				synchronized(this) {
					Socket s = ss.accept();
					//sock.add( ss.accept() );
					//dis.addText(sock.lastElement().getInetAddress().getHostAddress()+" connected. ID: "+id);
					dis.addText(s.getInetAddress().getHostAddress()+" connected. ID: "+id);
					//cli.add( new Client( this, sock.lastElement(), id++) );
					cli.add( new Client( this, s, id++ ) );
					//thd.add( new Thread(cli.lastElement()) );
				}
				Thread thd = new Thread( cli.lastElement() );
				//thd.lastElement().start();
				thd.start();
			}
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	public void printMsg( String msg ) {
		dis.addText(msg);
	}

	public void adduser( String username, int id ) {
		System.out.println( "Client "+id+" is "+username );
		userlist.add(username);
		dis.addUser(username);
		dis.addText( "Client "+id+" is "+username );
	}
	public void remove( Client c, int id ) {
		//System.out.println("Removing user "+id);
		String name = c.getname();
		//int i = cli.indexOf(c);
		//sock.remove(i);
		cli.remove(c);
		//thd.remove(i);
		if( name!=null ) {
			dis.removeUser(name);
			userlist.remove(name);
			for( Chatroom cr: roomlist ) {
				if( cr.hasUser(c) ) {
					sendRoom(cr.getID(), "/r- "+cr.getID()+" "+name);
				}
			}
			sendAll("/q- "+ name );
		}
		dis.addText("Client "+name+" (id:"+id+") disconnected.");
	}
	public void kick( int index ) {
		try {
			//sock.get(index).close();
			cli.get(index).closeConnection();
		} catch(IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	// chat related functions
	public void sendAll( String str ) {
		//System.out.println("sendAll: " + str);
		for( Client c : cli ) {
			c.send(str);
		}
	}
	public void sendPublic( String msg ) {
		sendAll( msg );
		//dis.addText( msg );
	}
	public boolean sendPrivate( String dest, String msg ) {
		int destID = userlist.indexOf(dest);
		if( destID == -1 ) return false;
		else {
			cli.get(destID).send(msg);
			//dis.addText( msg );
			return true;
		}
	}
	public boolean sendRoom( int roomid, String msg ) {
		if( roomid>roomlist.size() ) return false;
		Chatroom r = roomlist.get(roomid-1);
		/*if( r.hasUser(src) ) {
			r.sendRoom( "/r " + roomid + " " + src + " says: " + msg);
			return true;
		}
		else return false;*/
		r.sendRoom( msg );
		return true;
	}

	public int makeRoom() {
		roomid++;
		roomlist.add( new Chatroom(roomid) );
		//dis.addText("New room: #"+roomid+" made.");
		return roomid;
	}
	/*public int makeRoom( String name ) {
		roomid++;
		roomlist.add( new Chatroom(roomid, name) );
		return roomid;
	}*/
	public void roomadd( int room, Client c ) {
		//dis.addText("Room #"+room+" add "+c.getname());
		roomlist.get(room-1).adduser(c);
		c.send("/a "+room);
        for( Client cli: (roomlist.get(room-1).roomCli) ) { // send the current userlist to the new user
                if (cli!=c) c.send("/r+ "+ room+" " + cli.getname()+" " + cli.userColor);
        }
        sendRoom( room, "/r+ "+room+" "+c.getname() + " "+c.userColor );
	}
	public void roomadd( int room, String cname ) {
		Client c = cli.get( userlist.indexOf(cname) );
		roomlist.get(room-1).adduser(c);
		c.send("/a "+room);
                for( Client cli: (roomlist.get(room-1).roomCli) ) { // send the current userlist to the new user
                        if (cli!=c) c.send("/r+ "+ room+" " + cli.getname()+" " + cli.userColor);
                }
		sendRoom( room, "/r+ "+room+" "+c.getname() + " "+c.userColor );
	}
	public void roomrm( int room, Client c ) {
		roomlist.get(room-1).rmuser(c);
		sendRoom( room, "/r- "+room+" "+ c.getname() );
	}

}
