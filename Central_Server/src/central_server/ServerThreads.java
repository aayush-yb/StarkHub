/* 
    Author : Aayush Chauhan
    MNNIT , Allahabad
 */
package central_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;

/**
 *
 * @author aayushshivam7
 */
public class ServerThreads extends Thread {

    Socket socket;
    WaitingServer server;
    InetAddress address;
    BufferedReader input;
    PrintWriter output;
    String username;
    String type;

    ServerThreads(Socket socket, WaitingServer server, InetAddress address) throws IOException {
        this.socket = socket;
        this.server = server;
        this.address = address;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        System.out.println(address.getHostAddress() + " is connected as a server at port = " + socket.getPort());

        server.serversInfo.put(address.getHostAddress(), socket.getPort());

        this.start();
    }

    // retrieve username and password , return respose
    private boolean loginfo() throws IOException, SQLException, ClassNotFoundException {

//        System.out.println("i m trying to read username");

        String username = input.readLine();
//        System.out.println("i m trying to read password");

        String password = input.readLine();
        this.username = username;
//        System.out.println("user trying to login");

        String type = input.readLine();
        this.type = type;
        if (type.equals("1")) {

            return addUser(username, password);
        } else {
//            System.out.println("trying to authenticate");
            return authenticate(username, password);
        }

    }

    @Override
    public void run() {
        boolean flag = false;
        try {
            // wait for either signup or sign in request
            // (username,password,1 or 0 ) ;     1 for signup , 0 for sign in
            flag = loginfo();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        // no chance for login  then return 
        if (flag == false) {
            if (type.equals("1")) {
                output.println("username already exist");           // assuming we are having no trouble from Database side 
            } else {
                output.println(Constants.FAILURE);
            }

            output.flush();
            return;
        }
        System.out.println("logged in as : " + username);
        output.println(Constants.SUCCESS);
        output.flush();

        System.out.println(address);
        server.addToSet(address);

        String serverIP = address.getHostAddress();
        int serverPort = socket.getPort();

        // send to all 
//        output.println("ADD") ;
//        output.println(server.serversInfo.size()) ;
        for (ClientThreads entry : server.cThreads) {
            entry.output.println("ADD");
            entry.output.println("1");
            entry.output.println(address.getHostAddress());
            entry.output.println(socket.getPort());
            entry.output.flush();
        }

        while (socket.isConnected()) {

        }

        for (ClientThreads entry : server.cThreads) {
            entry.output.println("REMOVE");
            entry.output.println("1");
            entry.output.println(address.getHostAddress());
            entry.output.println(socket.getPort());
            entry.output.flush();
        }

        server.removeFromSet(address);
        server.serversInfo.remove(address.getHostAddress(), socket.getPort());
    }

    // adding  new user to database 
    private boolean addUser(String username, String password) throws SQLException {

        DatabaseHandler dbh = new DatabaseHandler();
        boolean possible = dbh.insertIntoDB(username, password);

        return possible;
    }

    // match password 
    private boolean authenticate(String username, String password) throws ClassNotFoundException, SQLException {
        System.out.println("enter in arena");

        DatabaseHandler dbh = new DatabaseHandler();

        String hashedPassword = dbh.getHashedPassword(username);

        System.out.println(hashedPassword);

        if (hashedPassword.equals("-1") == true) {
            return false;
        } else {
            return hashedPassword.equals(password);
        }

    }
}
