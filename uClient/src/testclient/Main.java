/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testclient;

import gui.*;
import javax.swing.UIManager;

/**
 *
 * @author Eddie
 */
public class Main {
        
        private static Client _ClientObject;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        }
        catch (Exception e) {
            
        }


                _ClientObject = new Client();

                _ClientObject.connectToServer();
    }

}
