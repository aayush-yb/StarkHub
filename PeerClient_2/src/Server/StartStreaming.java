
package Server ;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aayushshivam7
 */
public class StartStreaming {

    Process process;

    StartStreaming(String filePath, String hostAddress, int  hostPort, int ttlValue) {

        try {

            //  start streaming    --qt-start-minimized  for background streaming
            process = Runtime.getRuntime().exec("vlc " + "-vvv " + filePath + " --sout" + " udp:" + hostAddress + ":" + hostPort + " --ttl " + ttlValue + " --qt-start-minimized");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

//    public void pauseVideo() {
//
//    }

    public void stopVideo() {
        process.destroy();
    }

}
