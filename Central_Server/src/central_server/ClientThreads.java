/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package central_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;

/**
 *
 * @author aayushshivam7
 */
public class ClientThreads extends Thread {

    Socket socket;
    WaitingServer server;
    InetAddress address;
    BufferedReader input;
    PrintWriter output;

    ClientThreads(Socket socket, WaitingServer server, InetAddress address) throws IOException {
        this.socket = socket;
        this.server = server;
        this.address = address;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        System.out.println(address.getHostAddress() + " is connected as a client at port = " + socket.getPort()) ;
        this.start();
    }

    @Override
    public void run() {
        output.println("ADD"); 
        output.println(server.serversInfo.size());
        for(Map.Entry<String , Integer> entry : server.serversInfo.entrySet()){
            output.println(entry.getKey()) ;
            output.println(entry.getValue()) ;
        }
        output.flush() ;
        while(socket.isConnected()){
            
        }
        
        server.clientInfo.remove(address.getHostAddress() , socket.getPort()) ;
        server.cThreads.remove(this) ;
    }

}
