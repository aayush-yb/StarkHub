/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import Peer.Peer ;


/**
 *
 * @author aayushshivam7
 */
public class ClientHandler extends Thread{
    private Server server ;
    private ServerSocket serversocket ;
    private Socket socket ;
    private InetAddress user ;
    private BufferedReader reader ;
    private PrintWriter writer ;
    
    private String filePath ;
    private String hostAddress ;
    private int hostPort ;
    private int ttl = 12 ;
    private StartStreaming streamer ;
    Peer peer ;
    public ClientHandler(Peer peer,ServerSocket serversocket,Socket  socket, Server aThis, InetAddress address) {
        this.server = aThis; 
        this.serversocket = serversocket ;
        this.socket = socket ;
        this.peer = peer ;
        this.user = address ;
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream())) ;
            this.writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream())) ;
        } catch (IOException ex) {
            ex.printStackTrace() ;
        }
        
        this.start(); 
    }
    
    @Override
    public void run(){
        while(socket.isConnected()){
            try {
                System.out.println(socket) ;
                String request = reader.readLine() ;
                System.out.println(request) ;
                switch(request){
                    case "SEARCH" :
                        searchHandler();
                        break ;
                    case "SETUP" :
                        setupHandler() ;
                        break;
                    case "PLAY" :
                        playHandler() ;
                        break ;
//                    case "PAUSE":
//                        pauseHandler();
//                        break;
                    case "TEARDOWN":
                        tearDownHandler() ;
                        
                }
            } catch (IOException ex) {
                ex.printStackTrace() ;
            }
        }
    }
    
    private void searchHandler() {
        try{
            String query = reader.readLine() ;
            System.out.println(query) ;
            DatabaseHandler dbh = new DatabaseHandler() ;
            
            // hashset ke videos ka name and path bhej do client ko using writer ;
            HashMap<String,String > res = dbh.search(query) ;              //   <name of file , path of file> now write this to stream
            writer.println("RESULT");
            writer.println(res.size());
            System.out.println(res.size());
            for(Map.Entry<String,String > entry : res.entrySet()){
                writer.println(entry.getKey());
                writer.println(entry.getValue()) ;
            }
            writer.flush(); 
        } catch (IOException ex) {
            ex.printStackTrace(); 
        }
    }

    private void setupHandler() {
        try { 
            filePath = reader.readLine() ;
            /// setup connection with vlc 
            hostAddress = user.getHostAddress() ;
                  
            String port = reader.readLine() ;
            hostPort = Integer.parseInt(port) ;
            System.out.println(filePath + '\n' + hostAddress +'\n' + hostPort) ;
                   
        } catch (IOException ex) {
            ex.printStackTrace(); 
        }
        
    }

    private void playHandler() {
        // play set stream
        streamer = new StartStreaming(filePath , hostAddress , hostPort , ttl) ;
    }

    private void tearDownHandler() {
        //stop current stream
        streamer.stopVideo(); 
    }

//    private void pauseHandler() {
//        // pause current stream 
//    }
    
}
