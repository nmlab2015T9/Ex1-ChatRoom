package userver;
import javax.swing.UIManager;
/**
 * uSocket chatroom server Main
 * @author NMlab group 6
 */
public class Main {

	private static UServer s;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Throwable {
        UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		s = new UServer();
    }

}
