package Server;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import Peer.Constants ;
import Trie.KMP ;
/**
 *
 * @author aayushshivam7
 */
public class DatabaseHandler {

    private Connection conn;

    // make connection with databases
    void makeConnection() {

        try {

            Class.forName("com.mysql.jdbc.Driver");

            conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/" + Constants.DATABASE, Constants.USERNAME, Constants.PASSWORD);

        } catch (SQLException | ClassNotFoundException ex) {

            ex.printStackTrace();
        }

    }

    /// create new channel and make its entry in DB  
    boolean createChannel(String channelName, String channelID) {

        try {;
            makeConnection();
            String query = "INSERT INTO `channels` (`cid`, `cname`) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, channelID);
            stmt.setString(2, channelName);

            stmt.executeUpdate();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    // delete channel from DB
    boolean deleteChannel(String channelID) {

        try {
            makeConnection();

            //delete channel 
            String query = "DELETE FROM `channels` WHERE cid=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, channelID);

            stmt.executeUpdate();

            // deleting all associated videos 
            query = "DELETE FROM `videos` where cid=?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, channelID);

            stmt.executeUpdate();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    // add video to a channel
    boolean addVideo(String channelID, String videoName, String videoPath) {

        try {
            makeConnection();
            String query = "INSERT INTO `videos` (`name`, `path`, `cid`) VALUES (?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, videoName);
            stmt.setString(2, videoPath);
            stmt.setString(3, channelID);

            stmt.executeUpdate();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    // remove video from a channel 
    boolean removeVideo(String channelID, String videoName) {

        try {

            makeConnection();
            String query = "DELETE FROM `videos` WHERE cid=? AND name=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, channelID);
            stmt.setString(2, videoName);
            stmt.executeUpdate();
            System.out.println("remove ho gya h");

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // increase like count for a video
    boolean incrementLikeCount(String channelID, String videoName) {

        try {
            makeConnection();
            String query = "UPDATE `videos` SET likesCount = likesCount + 1 WHERE cid=? AND name=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, channelID);
            stmt.setString(2, videoName);

            stmt.executeUpdate();
        } catch (Exception e) {
            return false;
        }

        return true;
    }
    
    // decrement likes count 
    boolean decrementLikeCount(String channelID, String videoName) {

        try {
            makeConnection();
            String query = "UPDATE `videos` SET likesCount = likesCount - 1 WHERE cid=? AND name=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, channelID);
            stmt.setString(2, videoName);

            stmt.executeUpdate();
        } catch (Exception e) {
            return false;
        }

        return true;
    }
   
    
    // increase subscriber count 
    boolean incrementSubCount(String channelID){
        try{
            makeConnection() ;
            String query = "UPDATE `channels` SET subscriberCount = subscriberCount + 1 WHERE cid=?" ;
            PreparedStatement stmt = conn.prepareStatement(query) ;
            stmt.setString(1, channelID);
            stmt.executeUpdate();
            
        }catch(Exception e){
            return false ;
        }
        return true ;
    }
    
    // decrement subscriber count 
    boolean decrementSubCount(String channelID){
        try{
            makeConnection() ;
            String query = "UPDATE `channels` SET subscriberCount = subscriberCount - 1 WHERE cid=?" ;
            PreparedStatement stmt = conn.prepareStatement(query) ;
            stmt.setString(1, channelID);
            stmt.executeUpdate();
            
        }catch(Exception e){
            return false ;
        }
        return true ;
    }

    HashMap<String, String> search(String query) {
        makeConnection() ;
        HashMap<String,String> toReturn = null;
        String queryToDB = "SELECT name , path FROM videos" ;
        try {
            PreparedStatement stmt = conn.prepareStatement(queryToDB) ;
            ResultSet res = stmt.executeQuery() ;
            HashMap<String,String > allVideos = new HashMap<>();
            HashSet<String> names = new HashSet<>() ;
            while(res.next()){
                String name = res.getString("name");
                String path = res.getString("path") ; 
                names.add(name) ;
                allVideos.put(name,path) ;
            }
            names = new KMP().search(names, query) ; 
           
            toReturn = new HashMap<>();
            for(String s:names){
                toReturn.put(s, allVideos.get(s));
            }
//            return toReturn ; 
        } catch (SQLException ex) {
            ex.printStackTrace() ;
        }
        return toReturn ;
    }
}
