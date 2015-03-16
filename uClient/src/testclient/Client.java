/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testclient;

import java.io.*;
import java.net.*;
import java.lang.*;

import gui.*;
import fileexchange.*;

/**
 *
 * @author Eddie
 */
public class Client implements Runnable {

    //
    ChatFrame GUIObject;
    ConnectWindow connectWin;

    //Connection Objects
    private Socket socket;
    private int port;
    private String serverIP;
    private DataOutputStream os;
    private DataInputStream is;
    private Thread thread;
    private String username;

    Client () {
        GUIObject = new ChatFrame(this);
        GUIObject.setVisible(true);
        
    }

    //called by thread.start(), receiving msg
    @Override
    public void run() {
        try {
            while (true) {
		String TransferLine = is.readUTF();
                System.out.println("Recv: " + TransferLine);

                parseMsg(TransferLine);
            }
	}
        catch (Exception e) {
            interrupt();
	}
    }

    //build socket with server
    public void connectToServer () {

        //get ip, port, name by connectWindow
        connectWin = new ConnectWindow(GUIObject);
        connectWin.setLocationRelativeTo(GUIObject);
        connectWin.setVisible(true);
        
        serverIP = connectWin.ip;
        port = connectWin.port;
        username = connectWin.name;

        //create socket
        try {
            socket = new Socket(InetAddress.getByName(serverIP), port);
            GUIObject.addSysLine("Connected: " + serverIP + ":" + port);
            //check if name available
            sendName();

            os = new DataOutputStream(socket.getOutputStream());
            is = new DataInputStream(socket.getInputStream());
            this.os = new DataOutputStream(os);
            this.is = new DataInputStream(is);

            thread = new Thread(this);
            thread.start();  //call run()
        }
        catch (Exception e) {
            GUIObject.addWarnLine("Failed: " + serverIP + ":" + port);
            interrupt();
            e.printStackTrace();
        }
    }

    //check if name available
    public synchronized void sendName() throws IOException {
	DataOutputStream o = new DataOutputStream(socket.getOutputStream());
	DataInputStream i = new DataInputStream(socket.getInputStream());
	String msg = i.readUTF();
	System.out.println(msg);
            if( msg.startsWith("/u") ) {
		System.out.println("Send username "+username);
		o.writeUTF(username);
            }
            while( true ) {
		msg = i.readUTF();
		if( msg.equals("/ua") ) break;
		else {
                        GUIObject.addSysLine("名稱已被使用，請換一個名稱");
                	connectWin.rename();
			username = connectWin.name;
			o.writeUTF(username);
		}
            }
        GUIObject.setUsername(username);
        GUIObject.setTitle("uSocket chatroom: " + username);
    }

    //
    private void interrupt() {
            GUIObject.addWarnLine("Interrupt!");
            GUIObject.clear();
            reconnect();
    }

    //reconnect when interrupted
    public synchronized void reconnect() {
        connectWin.reconnect();

        serverIP = connectWin.ip;
        port = connectWin.port;
        username = connectWin.name;

        //create socket
        try {
            socket = new Socket(InetAddress.getByName(serverIP), port);
            GUIObject.addSysLine("Connected: " + serverIP + ":" + port);
            //check if name available
            sendName();

            os = new DataOutputStream(socket.getOutputStream());
            is = new DataInputStream(socket.getInputStream());
            this.os = new DataOutputStream(os);
            this.is = new DataInputStream(is);

            thread = new Thread(this);
            thread.start();  //call run()
        }
        catch (Exception e) {
            GUIObject.addWarnLine("Failed: " + serverIP + ":" + port);
            e.printStackTrace();
        }
    }

    //***
    //send
    public void send ( String msg ) {
        try{
            os.writeUTF(msg);
            System.out.println("Send: " + msg);
	}
	catch (Exception e) {
            interrupt();
	}
    }

    //open new room
    public void sendNewRoom () {
        //new room: "/n"
        send("/n");
    }

    //leave room
    public void sendLeaveRoom ( int roomID ) {
        //leave room: "/l <roomID>"
        send("/l "+roomID);
    }

    //add user into room
    public void sendAddRoomUser ( String user, int roomID ) {
        //leave room: "/a <roomID> <user>"
        send("/a " +roomID+" " +user);
    }

    //send public msg
    public void sendSMsg ( String msg ) {
        //send msg: "/s <user> <msg>"
        send("/s " + username+" " + msg);
    }

    //send room msg
    public void sendRMsg ( String msg , int roomID ) {
        //send room msg: "/rs <roomID> [src name] <msg>"
        send("/rs " + roomID+" "+ username+" " + msg);
    }

    //send whisper msg
    public void sendWMsg ( String msg , String target , int roomID) {
       //whisper msg: "/w <user> <target> <roomID> <msg>"
       send("/w " + username+" " + target+" " + roomID+" " + msg);
	
    }

    //color change
    public void sendColorChange ( int c ) {
        //color change: "/c <user> <color>"
        send("/c " + username+" " + c);
    }

    //send file
    public void sendFile( String src, String dest ) {
        // send file request: /f [src name] [dest name]
        send( "/f "+src+" "+dest );
        Thread fsthd = new Thread( new FileSend() );
        fsthd.start();
    }
    //***

    //***
    //parse received msg
    public void parseMsg ( String msg ) {
        //add user:  /q+ <user> <texture>
        if (msg.startsWith("/q+")) {
            String[] splitedLine = msg.split(" ", 3);
                System.out.println("User joined:" + splitedLine[1]);
                GUIObject.addUser(splitedLine[1], Integer.parseInt(splitedLine[2]));
        }
        //delete user:  /q- <user>
        else if (msg.startsWith("/q-")) {
            String[] splitedLine = msg.split(" ", 2);
                System.out.println("User left:" + splitedLine[1]);
                GUIObject.delUser(splitedLine[1]);
        }
        //normal msg:  /s <user> <msg>
        else if (msg.startsWith("/s")) {
            String[] splitedLine = msg.split(" ", 3);
            GUIObject.addNewLine(splitedLine[1] + " says: " + splitedLine[2], splitedLine[1], 0);
        }
        //error msg:  /e error_msg
        else if (msg.startsWith("/e")) {
            String[] splitedLine = msg.split(" ", 2);
            GUIObject.addWarnLine(splitedLine[1]);
        }
        //whisper msg:  /w <user> <target> <roomID> <msg>
        else if (msg.startsWith("/w")) {
            String[] splitedLine = msg.split(" ", 5);
            if ( splitedLine[1].equals(username) ) {
                GUIObject.addNewLine("Whisper to " + splitedLine[2] + " : " + splitedLine[4], "whisper", Integer.parseInt(splitedLine[3]));
            }
            else {
                GUIObject.addNewLine(splitedLine[1] + " whispers: " + splitedLine[4], "whisper", Integer.parseInt(splitedLine[3]));
                GUIObject.setLastWhisper(splitedLine[1], Integer.parseInt(splitedLine[3]));
            }
        }
        //color change:  /c <user> <texture>
        else if (msg.startsWith("/c")) {
            String[] splitedLine = msg.split(" ", 3);
            GUIObject.userChangeColor(splitedLine[1], Integer.parseInt(splitedLine[2]));
        }
        //add client to room:  /a <roomID>
        else if (msg.startsWith("/a")) {
            String[] splitedLine = msg.split(" ", 2);
            GUIObject.addTab(Integer.parseInt(splitedLine[1]));
        }
        //add user into room:  /r+ <roomID> [username] [texture]
        else if (msg.startsWith("/r+")) {
            String[] splitedLine = msg.split(" ", 4);
                GUIObject.addUser(splitedLine[2], Integer.parseInt(splitedLine[3]), Integer.parseInt(splitedLine[1]));
        }
        //remove user from room:  /r- <roomID> <user>
        else if (msg.startsWith("/r-")) {
            String[] splitedLine = msg.split(" ", 3);
                GUIObject.delUser(splitedLine[2], Integer.parseInt(splitedLine[1]));
        }
        //room msg:  /rs <roomID> <user> msg
        else if (msg.startsWith("/rs")) {
            String[] splitedLine = msg.split(" ", 4);
            GUIObject.addNewLine(splitedLine[2] + " says: " + splitedLine[3], splitedLine[2],
                                 Integer.parseInt(splitedLine[1]));
        }
        // file transfer request: /f [src name] [dest name] [src IP]
        else if( msg.startsWith("/f") ) {
                String srcName = msg.split(" ", 4)[1];
                String srcAddr = msg.split(" ", 4)[3];
                Thread recvThd = new Thread( new FileRecv( srcAddr, srcName ) );
                recvThd.start();
        }
        else {
            //
        }
    }

}
