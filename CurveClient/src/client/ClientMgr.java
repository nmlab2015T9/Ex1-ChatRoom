package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import file.FileRX;
import file.FileTX;


public class ClientMgr implements Runnable {
	public String IP, name;
	private int port;
	private Socket socket;
	private DataOutputStream output;
	private DataInputStream input;
	private Thread thread;
	private gui.MainHead mainhead;
	private gui.OpeningDialog openingdialog;
	
	public ClientMgr () {
		//connectionBegin();
	}
	
	public void connectionBegin() {
		try {
            socket = new Socket(InetAddress.getByName(IP), port);
            System.out.println("IP = " + IP + "    InetAddress.getByName(IP) = " + InetAddress.getByName(IP));
            mainhead.addSysLine("Connected: " + IP + ":" + port);
            
            //check if name available
            checkName();

            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
            this.output = new DataOutputStream(output); //?
            this.input = new DataInputStream(input); //?

            thread = new Thread(this);
            thread.start();  //call run()
        }
        catch (Exception e) {
        	mainhead.addWarnLine("Failed: " + IP + ":" + port);
            interrupt();
            e.printStackTrace();
        }
	}
	
	private synchronized void checkName() throws IOException{
		DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		DataInputStream input = new DataInputStream(socket.getInputStream());
		String massage = input.readUTF();
		System.out.println(massage);
	    	if( massage.startsWith("/u") ) {
	    		System.out.println("Send username "+name);
	    		output.writeUTF(name);
	    	}
	    	while( true ) {
	    		massage = input.readUTF();
	    		if( massage.equals("/ua") ) break;
	    		else {
	    			mainhead.addSysLine("Name already in use! please change it!");
	                CurveClient.dMgr.openDialog.rename();
	                output.writeUTF(name);
	    		}	
	        }
	     //GUIObject.setUsername(username);
	     //GUIObject.setTitle("uSocket chatroom: " + username);
	}
	
	private void interrupt() {
		mainhead.addWarnLine("Interrupt!");
		mainhead.clear();
        reconnect();
	}
	
	public synchronized void reconnect() {
		openingdialog.reconnect();

        //create socket
        try {
        	socket = new Socket(InetAddress.getByName(IP), port);
        	mainhead.addSysLine("Connected: " + IP + ":" + port);
             
        	//check if name available
        	checkName();

        	output = new DataOutputStream(socket.getOutputStream());
        	input = new DataInputStream(socket.getInputStream());
        	this.output = new DataOutputStream(output); //?
        	this.input = new DataInputStream(input); //?

            thread = new Thread(this);
            thread.start();  //call run()
        }
        catch (Exception e) {
            mainhead.addWarnLine("Failed: " + IP + ":" + port);
            e.printStackTrace();
        }
    }
	
	public void setClientInfo(String ip, int port, String name){
		IP = ip;
		this.port = port;
		this.name = name;
		System.out.println(IP + " " + port + " " + name);
	}
	
	public void setOpeningDialogAndMainhead(gui.OpeningDialog openingDialog, gui.MainHead mainHead){
		openingdialog = openingDialog;
		mainhead = mainHead;
	}

	@Override
	public void run() {
		 try {
	            while (true) {
			String TransferLine = input.readUTF();
	                System.out.println("Recv: " + TransferLine);

	                readInput(TransferLine);
	            }
		}
	        catch (Exception e) {
	            interrupt();
		}		
	}

	 public void send ( String msg ) {
	        try{
	        	output.writeUTF(msg);
	            System.out.println("Send: " + msg);
		}
		catch (Exception e) {
	            interrupt();
		}
	    }

	 public void sendDelUser(){
		 send("/q- " + name);
	 }
	 
	 //open new room
	 public void sendNewRoom () {
		 //new room: "/n"
		 send("/n");
	 }

	 //leave room
	 public void sendLeaveRoom ( int roomID ) {
		 //leave room: "/l <roomID>"
		 send("/l " + roomID);
	 }

	 //add user into room
	 public void sendAddRoomUser ( String user, int roomID ) {
		 //leave room: "/a <roomID> <user>"
		 send("/a " + roomID + " " + user);
	 }

	 //send public msg
	 public void sendSMsg ( String msg ) {
		 //send msg: "/s <user> <msg>"
		 send("/s " + name + " " + msg);
	 }

	 //send room msg
	 public void sendRMsg ( String msg , int roomID ) {
		 //send room msg: "/rs <roomID> [src name] <msg>"
		 send("/rs " + roomID + " " + name + " " + msg);
	 }

	 //send whisper msg
	 public void sendWMsg ( String msg , String target , int roomID) {
		 //whisper msg: "/w <user> <target> <roomID> <msg>"
		 send("/w " + name + " " + target + " " + roomID + " " + msg);	 
	 }

	 //color change
	 public void sendColorChange ( int c ) {
		 //color change: "/c <user> <color>"
		 send("/c " + name + " " + c);
	 }
	 
	 public void sendVideo(String dest){
		 // send file request: /v [src name] [dest name]
		 send( "/v " + name + " " + dest);
	 }

	 //send file
	 public void sendFile(String dest ) {
	        // send file request: /f [src name] [dest name]
	        send( "/f "+name+" "+dest );
	        Thread fsthd = new Thread( new FileTX() );
	        fsthd.start();
	    }
	
	private void readInput(String msg) {
		//add user:  /q+ <user> <texture>
        if (msg.startsWith("/q+")) {
            String[] splitedLine = msg.split(" ", 3);
            System.out.println("User joined:" + splitedLine[1]);
            mainhead.addNewUser(splitedLine[1], Integer.parseInt(splitedLine[2]));
        }
        //delete user:  /q- <user>
        else if (msg.startsWith("/q-")) {
            String[] splitedLine = msg.split(" ", 2);
            System.out.println("User left:" + splitedLine[1]);
            mainhead.delUser(splitedLine[1]);
        }
        
        else if (msg.startsWith("/s")) {
            String[] splitedLine = msg.split(" ", 3);
            mainhead.addNewLine(splitedLine[1] + " says: " + splitedLine[2], splitedLine[1], 0);
        }
        
        else if (msg.startsWith("/b")){
        	String[] splitedLine = msg.split(" ", 2);
        	mainhead.addSysLine("SERVER BROADCAST: " + splitedLine[1] +" !!!");
        }
        
        //error msg:  /e error_msg
        else if (msg.startsWith("/e")) {
            String[] splitedLine = msg.split(" ", 2);
            mainhead.addWarnLine(splitedLine[1]);
        }
        
        
        //whisper msg:  /w <user> <target> <roomID> <msg>
        else if (msg.startsWith("/w")) {
            String[] splitedLine = msg.split(" ", 5);
            if ( splitedLine[1].equals(name) ) {
            	mainhead.addNewLine("Whisper to " + splitedLine[2] + " : " + splitedLine[4], "whisper", Integer.parseInt(splitedLine[3]));
            }
            else {
            	mainhead.addNewLine(splitedLine[1] + " whispers: " + splitedLine[4], "whisper", Integer.parseInt(splitedLine[3]));
            	mainhead.setTarget(splitedLine[1], Integer.parseInt(splitedLine[3]));
            }
        }
        
        //color change:  /c <user> <texture>
        else if (msg.startsWith("/c")) {
            String[] splitedLine = msg.split(" ", 3);
            mainhead.userChangeColor(splitedLine[1], Integer.parseInt(splitedLine[2]));
        }
        //add client to room:  /a <roomID>
       else if (msg.startsWith("/a")) {
            String[] splitedLine = msg.split(" ", 2);
            mainhead.addRoom(Integer.parseInt(splitedLine[1]));
        }
        //add user into room:  /r+ <roomID> [username] [texture]
       else if (msg.startsWith("/r+")) {
            String[] splitedLine = msg.split(" ", 4);
            mainhead.addUser(splitedLine[2], Integer.parseInt(splitedLine[3]), Integer.parseInt(splitedLine[1]));
        }
        //remove user from room:  /r- <roomID> <user>
       else if (msg.startsWith("/r-")) {
            String[] splitedLine = msg.split(" ", 3);
            mainhead.delUser(splitedLine[2], Integer.parseInt(splitedLine[1]));
        }
        //room msg:  /rs <roomID> <user> msg
        else if (msg.startsWith("/rs")) {
            String[] splitedLine = msg.split(" ", 4);
            mainhead.addNewLine(splitedLine[2] + " says: " + splitedLine[3], splitedLine[2],
                                 Integer.parseInt(splitedLine[1]));
        }
        
        // video transfer request: /v [src name] [dest name] 
        else if( msg.startsWith("/v") ) {
                String srcName = msg.split(" ", 4)[1];
        }
        // file transfer request: /f [src name] [dest name] [src IP]
        else if( msg.startsWith("/f") ) {
                String srcName = msg.split(" ", 4)[1];
                String srcAddr = msg.split(" ", 4)[3];
                Thread recvThd = new Thread( new FileRX( srcAddr, srcName ) );
                recvThd.start();
        }
	}
}
