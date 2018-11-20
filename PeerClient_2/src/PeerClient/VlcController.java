
package PeerClient ;

import Peer.Constants;
import java.io.IOException;


/**
 *
 * @author aayushshivam7
 */

public class VlcController {
    
    private Process process ;
     
    
    VlcController(){
        
        Runtime runtime = Runtime.getRuntime() ;
        try {
            process = runtime.exec("vlc "+"-vvv "+"udp://:"+Constants.VLC_UDP_PORT);
        } catch (IOException ex) {
            //// alert can't start vlc now ...
        }
        System.out.println("start vlc");
               
    }
    
    public void closeWindow(){
        process.destroy();
    }
    
}
