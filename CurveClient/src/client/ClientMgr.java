package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


public class ClientMgr implements Runnable {
	private String IP, name;
	private int port;
	private Socket socket;
	private DataOutputStream output;
	private DataInputStream input;
	private Thread thread;
	private gui.MainFrame mainframe;
	
	public ClientMgr () {
		//connectionBegin();
	}
	
	public void connectionBegin() {
		try {
            socket = new Socket(InetAddress.getByName(IP), port);
            //CurveClient.dMgr.mainFrame.addSysLine("Connected: " + IP + ":" + port);
            
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
            //GUIObject.addWarnLine("Failed: " + serverIP + ":" + port);
            //interrupt();
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
	    			//GUIObject.addSysLine("名稱已被使用，請換一個名稱");
	                CurveClient.dMgr.openDialog.rename();
	                output.writeUTF(name);
	    		}	
	        }
	     //GUIObject.setUsername(username);
	     //GUIObject.setTitle("uSocket chatroom: " + username);
	}
	
	public void setClientInfo(String ip, int port, String name){
		IP = ip;
		this.port = port;
		this.name = name;
		System.out.println(IP + " " + port + " " + name);
	}
	
	public void setMainFrame(gui.MainFrame mainFrame){
		mainframe = mainFrame;
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
	           // interrupt();
		}		
	}

	private void readInput(String msg) {
		//add user:  /q+ <user> <texture>
        if (msg.startsWith("/q+")) {
            String[] splitedLine = msg.split(" ", 3);
                System.out.println("User joined:" + splitedLine[1]);
                mainframe.addNewUser(splitedLine[1], Integer.parseInt(splitedLine[2]));
        }
        //delete user:  /q- <user>
        else if (msg.startsWith("/q-")) {
            String[] splitedLine = msg.split(" ", 2);
                System.out.println("User left:" + splitedLine[1]);
                mainframe.delUser(splitedLine[1]);
        }
        
        else if (msg.startsWith("/s")) {
            String[] splitedLine = msg.split(" ", 3);
            //mainframe.addNewLine(splitedLine[1] + " says: " + splitedLine[2], splitedLine[1], 0);
        }
	}
}
