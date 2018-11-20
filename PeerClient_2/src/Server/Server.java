package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import Peer.Constants ;
import Peer.Peer ;
/**
 *
 * @author aayushshivam7
 */
public class Server implements Runnable {

    private Socket socket;
    private BufferedReader read;
    private PrintWriter output;
    private Peer peer;
    private Constants constant;
//    private int PORT ;
    private ServerSocket server;

    public Server(Peer peer) {
        this.peer = peer;
//        this.PORT = 5050 ;
       
        try {
            server = new ServerSocket(Constants.MY_SERVER_PORT) ;
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        System.out.println("server started ") ;
        while (true) { 
            try {
                
                socket = server.accept() ;
                System.out.println("koi connect hua!!!");
                InetAddress address = server.getInetAddress() ;
                System.out.println(address) ;
                System.out.println(address.getHostName());
//                new PlayVideoThread(this,server,address,peer,socket) ;         //streaming karane wali class
                new ClientHandler(peer,server,socket,this,address) ;
            } catch (IOException ex) {
                ex.printStackTrace() ;
            }
        }
    }

    private boolean makeConnection() {
        try {
            System.out.println("connection banana hai");

            this.socket = new Socket(Constants.CENTRAL_SERVER, Constants.CENTRAL_SERVER_PORT);
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            read = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException ex) {
            System.out.println("connection nahi bana");
            return false;
        }
        return true;
    }

    public boolean authenticate(String username, String password, String type) throws IOException {
        boolean conn = makeConnection();

        if (conn == false) {
            return false;
        }

        output.println(username);
        output.println(password);
        output.println(type);
        output.flush();
        String check = read.readLine();
        socket.close();
        if (check.equals("0")) {
            
            return false;
        } else {
            return true;
        }
    }
}
