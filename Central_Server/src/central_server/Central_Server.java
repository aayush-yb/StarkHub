/* 
    Author : Aayush Chauhan
    MNNIT , Allahabad
 */
package central_server;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aayushshivam7
 */
public class Central_Server {

    /**
     * @param args the command line arguments
     *
     */
    public static void main(String[] args) {
        
        try {
            WaitingServer server = new WaitingServer();
            server.start();                    // start concurrent server
        } catch (IOException ex) {
            Logger.getLogger(Central_Server.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace() ;
        }
        
            
    }

}
