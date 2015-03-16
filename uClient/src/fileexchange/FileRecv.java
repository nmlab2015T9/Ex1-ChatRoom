package fileexchange;
import java.net.*;
import java.io.*;
/**
 *
 * @author StarryDawn
 */
public class FileRecv implements Runnable {
	private RecvGUI gui;
	private String srcaddr;
	private String srcname;
	private Socket s;
	public FileRecv( String addr, String name ) {
		srcaddr = addr;
		srcname = name;
	}
	@Override
	public void run() {
		gui = new RecvGUI();
		try {
			s = new Socket( srcaddr, 9988 );
			DataInputStream in = new DataInputStream( s.getInputStream() );
			DataOutputStream out = new DataOutputStream( s.getOutputStream() );
			String filename = in.readUTF();
			int filesize = in.readInt();
			System.out.println(filesize);
			gui.setSrcname(srcname);
			gui.setFileInformation(filename, filesize);
			if( gui.confirm() ) {
				File fout = gui.getFile(filename);
				//System.out.println(fout.getAbsoluteFile());
				if( fout != null ) {
					//System.out.println("Begin receiving");
					out.writeUTF("/a"); // accept transfer
					FileOutputStream fos = new FileOutputStream(fout);
					BufferedOutputStream bos = new BufferedOutputStream( fos );
					int bytesRead;
					int current = 0;
					byte [] bufferArray = new byte [filesize];

					bytesRead = in.read( bufferArray, 0, filesize );
					current = bytesRead;
					gui.setVisible(true);
					gui.showProgress(current);
					//System.out.print( current + " read, " );
					do {
						//System.out.println( current*100/filesize + "% completed" );
						bytesRead = in.read(bufferArray, current, (bufferArray.length-current));
						//System.out.print( bytesRead+" read, " );
						if(bytesRead >= 0) {
							current += bytesRead;
							//System.out.print( "current total=" + current + " bytes, ");
						}
						gui.showProgress(current);
					} while( current < filesize );
					//out.writeUTF("ok");
					//System.out.println("transfer ok");
					bos.write(bufferArray, 0, current);
					bos.flush();
					bos.close();
					//System.out.println("file output ok");
					gui.done();
					s.close();
				}
				else {
					out.writeUTF("/d"); // deny transfer
					s.close();
				}
			}
			else {
				out.writeUTF("/d"); // deny transfer
				s.close();
			}
			//gui.setVisible(true);
		}
		catch( IOException e ) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
}
