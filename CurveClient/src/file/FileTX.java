package file;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileTX implements Runnable {
	private TXFrame gui;
	@Override
	public void run() {
		gui = new TXFrame();
		
		try {
			ServerSocket ss = new ServerSocket( 9988 );
			System.out.println("Waiting for connection.........");
			Socket s = ss.accept();
			System.out.println("connection accepted!!!!!!");

			File sendfile = gui.getFile();
			gui.setVisible(true);
			//System.out.println(sendfile.getAbsoluteFile());

			if( sendfile == null ) {
				gui.setVisible(false);
				s.close();
				ss.close();
				return;
			}

			DataInputStream in = new DataInputStream( s.getInputStream() );
			DataOutputStream out = new DataOutputStream( s.getOutputStream() );
			FileInputStream fs = new FileInputStream( sendfile );
			BufferedInputStream bis = new BufferedInputStream(fs);
			int filesize = (int)sendfile.length();

			out.writeUTF(sendfile.getName());
			//System.out.println("size = "+filesize);
			out.writeInt(filesize);

			if( in.readUTF().equals("/a") ) {
				byte [] bufferArray = new byte [ filesize ];
				bis.read( bufferArray, 0, bufferArray.length );
				gui.send();
				out.write(bufferArray, 0, bufferArray.length);
				out.flush();
				out.close();
				bis.close();
				//System.out.println( in.readUTF() );
				gui.done();
				s.close();
				ss.close();
			}
			else {
				gui.denyTransfer();
				gui.setVisible(false);
				s.close();
				ss.close();
			}
		}
		catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
}
