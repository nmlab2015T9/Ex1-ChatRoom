/**
 * 
 */
package server;

import gui.ServerGUI;

import java.awt.Dimension;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**
 * @author dd
 *
 */
public class CurveServer {
	private static ServerSocket ss;
	public static int PORT = 9987;
	private static boolean hasGUI;
	private static ServerGUI gui;
	static Vector<Client> clientList = new Vector<Client>();
	static Vector<Room> roomList = new Vector<Room>();
	private static int maxId; // accumulated client id
	private int maxRoomId; // accumulated room id
	public static void main(String[] args) {		
		// run in command line mode, no GUI
		if(args.length == 2 && args[0] == "-c") { 
			hasGUI = false;
			try {
				PORT = Integer.valueOf(args[1]);
				ss = new ServerSocket(PORT);
			} catch (NumberFormatException e) {
				System.err.println("argument \"" + args[1] + "\" is not a valid port number.");
				e.printStackTrace();
				System.exit(-1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// run in GUI mode...
		// first, there will be a dialog asking the port number you want to use
		// then, the main frame will pop up
		else {
			hasGUI = true;
			try {
				UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
				ss = new ServerSocket(PORT);
				//SwingUtilities.invokeLater(new Runnable() {
					//public void run() {
						gui = new ServerGUI(ss);
						gui.setSize(new Dimension(1024, 768));
						gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						// place the frame in the center of the screen
						gui.setLocationRelativeTo(null);
						//frame.pack();
						gui.setVisible(true);
					//}
				//});
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (UnsupportedLookAndFeelException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		listenForever();
	}
	
	/***********************/
	/*  LISTENING FOREVER  */ 
	/***********************/
	// synchronized static method ensures only one thread can operate at one time
	synchronized static void listenForever() {
		try {
			if(gui != null)
				printMsg("start listen forever");
			while(true) {
				Socket s = ss.accept();
				printMsg(s.getInetAddress().getHostAddress()+" connected. ID: "+maxId);
				clientList.add( new Client(s, maxId++ ) );
				Thread thd = new Thread( clientList.lastElement() );
				thd.start();
			}
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
	
	/********************/
	/*   SEND MESSAGE   */
	/********************/
	// prints msg on screen
	public static void printMsg(String s) {
		if(hasGUI)
			gui.addText(s);
		System.out.println(s);
	}
	// iterate thu every client
	public static void sendAll(String str) {
		for( Client c : clientList ) {
			c.send(str);
		}		
	}
	public static void sendPublic(String msg) {
		sendAll(msg);
	}
	public static void sendBroadcast(String string) {
		sendAll(string);
		printMsg("[SERVERBROADCAST] " + string);
	}
	public static boolean sendPrivate(String username, String msg) {
		int findId = findUserByName(username);
		if(findId == -1) return false;
		else {
			clientList.get(findId).send(msg);
			return true;
		}
	}
	public static boolean sendRoom(int room, String msg) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/***********************/
	/*   USER ADD REMOVE   */
	/***********************/
	public static void adduser(String username, int id) {
		printMsg( "Client "+id+" is "+username );
		//userlist.add(username);
		if(hasGUI)
			gui.addUser(username);
	}
	public static void removeUser(Client client, int id) {
		printMsg("Removing user: " + client.username + ", id: "+id);
		String name = client.username;
		//int i = cli.indexOf(c);
		//sock.remove(i);
		clientList.remove(client);
		//thd.remove(i);
		if( name!=null ) {
			gui.removeUser(name);
			//userlist.remove(name);
			for( Room cr: roomList ) {
				if( cr.hasClient(client) ) {
					sendRoom(cr.getID(), "/r- "+cr.getID()+" "+name);
				}
			}
			sendAll("/q- "+ name );
		}
		printMsg("Client "+name+" (id:"+id+") disconnected.");		
	}
	
	/***************************/
	/*   ROOM NEW ADD REMOVE   */
	/***************************/
	public static int roomNew() {
		// TODO Auto-generated method stub
		return 0;
	}
	public static void roomAddUser(int roomid, Client client) {
		// TODO Auto-generated method stub
		
	}
	public static void roomAddUser(int room, String dest) {
		// TODO Auto-generated method stub
		
	}
	public static void roomRmUser(int room, Client client) {
		// TODO Auto-generated method stub
		
	}
	
	/**********************/
	/*   HELPER METHODS   */
	/**********************/
	public static int findUserByName(String username) {
		int size = clientList.size();
		for(int i = 0; i != size; ++i)
			if(clientList.get(i).username == username)
				return i;
		return -1;
	}
}
