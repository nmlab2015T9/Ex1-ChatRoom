package fileexchange;
import java.net.*;
import java.io.*;
/**
 *
 * @author StarryDawn
 */
public class FileSend implements Runnable {
	private SendGUI gui;
	@Override
	public void run() {
		gui = new SendGUI();
		
		try {
			ServerSocket ss = new ServerSocket( 9988 );
			//System.out.println("Waiting for connection");
			Socket s = ss.accept();

			File sendfile = gui.getFile();
			//System.out.println(sendfile.getAbsoluteFile());
			gui.setVisible(true);

			if( sendfile == null ) {
				gui.setVisible(false);
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
