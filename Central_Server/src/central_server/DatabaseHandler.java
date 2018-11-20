/* 
    Author : Aayush Chauhan
    MNNIT , Allahabad
*/
package central_server;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author aayushshivam7
 */
// handle all queries to database 
public class DatabaseHandler {

    Connection conn;

    // make connection with databases
    void makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        conn = (Connection) DriverManager.getConnection("jdbc:mysql://"+Constants.DB_SERVER+":"+
                Constants.DB_PORT+"/"+Constants.DATABASE,Constants.USERNAME, Constants.PASSWORD);
    }

    
     // retrive and return password from database
    String getHashedPassword(String username) throws SQLException, ClassNotFoundException {

        makeConnection();
        String askPassword = "Select password from Login where username=?";           // prepared statement for parameterized query
        
        // prepared statement 
        
        PreparedStatement stmt = null;

        stmt = conn.prepareStatement(askPassword);

        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            conn.close() ;
            return "-1";
        }
        // if signed up then return hashed password
        return rs.getString(1);
    }

    public boolean insertIntoDB(String username, String password) throws SQLException {
        try {
            makeConnection();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("not able to make connection right now") ;
        }

        PreparedStatement stmt = null ;
        String insertUser = "INSERT INTO Login Values(?,?,?)" ;

        // salt = 0  .. .for now 
        String salt = "0" ;
     
        stmt = conn.prepareStatement(insertUser) ;
        stmt.setString(1, username) ;
        stmt.setString(2, password) ;
        stmt.setString(3, salt) ;

        try {
            stmt.executeUpdate() ;
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
