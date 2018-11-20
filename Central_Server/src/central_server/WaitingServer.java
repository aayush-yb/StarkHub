/* 
    Author : Aayush Chauhan
    MNNIT , Allahabad
 */
package central_server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author aayushshivam7
 */
public class WaitingServer {

    HashSet<InetAddress> activeCreators;                               // list of active content providers
    private ServerSocket socket;

    // separate hash maps to store ip and port numbers of creators and viewers
    HashMap<String, Integer> serversInfo;
    HashMap<String, Integer> clientInfo;
    HashSet<ClientThreads>   cThreads ;
    
    WaitingServer() throws IOException {
        socket = new ServerSocket(Constants.PORT);
        serversInfo = new HashMap<>();
        clientInfo = new HashMap<>();
        activeCreators = new HashSet<>();
        cThreads = new HashSet<>() ;
        
        
    }

    // start server....... keep on waiting for more connections 
    void start() throws IOException {

        while (true) {
            Socket s = socket.accept();

            InetAddress address = s.getInetAddress();
            if (serversInfo.containsKey(address.getHostAddress())) {
                clientInfo.put(address.getHostAddress() , s.getPort()) ;
                cThreads.add(new ClientThreads(s,this,address)) ;
            }
//            System.out.println("address = " + address );  
            else 
                new ServerThreads(s, this, address);

        }

    }

    // add user to set of active users
    void addToSet(InetAddress address) {

        activeCreators.add(address);
        System.out.println(activeCreators.size());
    }

    //remove users from set of active users 
    void removeFromSet(InetAddress address) {
        activeCreators.remove(address);
    }
}
