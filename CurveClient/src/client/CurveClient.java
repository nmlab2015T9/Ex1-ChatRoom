/**
 * 
 */
package client;

public class CurveClient {
	public static ClientMgr cMgr;
	public static DisplayMgr dMgr;
	
	private static void createAndShowGUI() {
		cMgr = new ClientMgr();
		dMgr = new DisplayMgr();
	}	
	
	public static void main(String[] args){
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
}
