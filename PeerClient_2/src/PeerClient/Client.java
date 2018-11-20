package PeerClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import Peer.Peer;
import Peer.Constants ;
/**
 *
 * @author aayushshivam7
 */
public class Client extends Thread {

    private Peer peer;
    private BufferedReader read;
    private PrintWriter output;
    private Socket socket;
    private HashMap<String, Integer> activeCreators;

    public Client(Peer peer) {

        this.peer = peer;
        activeCreators = new HashMap<>();

    }

    @Override
    public void run() {

        // always wait for new creators 
        while (socket.isConnected()) {
            try {
                String what = read.readLine();
                if (what.equals("ADD")) {
                    int count = Integer.parseInt(read.readLine());
                    for (int i = 1; i <= count; i++) {
                        String IP = read.readLine();
                        Integer port = Integer.parseInt(read.readLine());
//                        if(IP.equals(socket.getLocalAddress().getHostAddress()) == false){
                        this.activeCreators.put(IP, port);
//                        }
                    }
                } else if (what.equals("REMOVE")) {
                    int count = Integer.parseInt(read.readLine());
                    for (int i = 1; i <= count; i++) {
                        String IP = read.readLine();
                        Integer port = Integer.parseInt(read.readLine());
                        this.activeCreators.remove(IP, port);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    // trying to authenticate peer 
    public boolean authenticate(String username, String password, String type) throws IOException {
        boolean conn = makeConnection();

        if (conn == false) {
            return false;
        }
        output.println(username);
        output.println(password);
        output.println(type);
        output.flush();
        System.out.println("read krna h");
        String check = read.readLine();
        System.out.println("read ho gya" + check);

        if (check.equals(Constants.LOGIN)) {
            return false;
        } else {
            System.out.println("login done");
            return true;
        }
    }

    // make connection with server 
    private boolean makeConnection() {

        try {
            this.socket = new Socket(Constants.CENTRAL_SERVER, Constants.CENTRAL_SERVER_PORT);
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            read = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException ex) {
            return false;
        }
        return true;
    }

}
