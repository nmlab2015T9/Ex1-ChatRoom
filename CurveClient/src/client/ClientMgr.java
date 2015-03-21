package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;


public class ClientMgr implements Runnable {
	private String IP = "140.112.18.198", name;
	private int port = 9987;
	private Socket socket;
	private DataOutputStream output;
	private DataInputStream input;
	private Thread thread;
	
	public ClientMgr () {
		connectionBegin();
	}
	
	private void connectionBegin() {
		try {
            socket = new Socket(InetAddress.getByName(IP), port);
            //CurveClient.dMgr.mainFrame.addSysLine("Connected: " + IP + ":" + port);
            //check if name available
            //sendName();

            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
            this.output = new DataOutputStream(output);
            this.input = new DataInputStream(input);

            thread = new Thread(this);
            thread.start();  //call run()
        }
        catch (Exception e) {
            //GUIObject.addWarnLine("Failed: " + serverIP + ":" + port);
            //interrupt();
            e.printStackTrace();
        }
	}
	
	public void setClientInfo(String ip, int port, String name){
		IP = ip;
		this.port = port;
		this.name = name;
		System.out.println(IP + " " + port + " " + name);
	}

	@Override
	public void run() {
		 try {
	            while (true) {
			String TransferLine = input.readUTF();
	                System.out.println("Recv: " + TransferLine);

	               // parseMsg(TransferLine);
	            }
		}
	        catch (Exception e) {
	           // interrupt();
		}		
	}
}
