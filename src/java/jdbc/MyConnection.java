package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {

  public static Connection getConnection(String args[]) {
    Connection con = null;
    String urlStr = null;

    if (args.length < 2) {
      System.out.println("You need to enter: arg[0] = Userid, arg[1] = password");
      return con;
    }

    try {
      //Load the Driver Class Now       
      Class.forName("com.mysql.jdbc.Driver");
      urlStr = "jdbc:mysql://russet.wccnet.edu/" + args[0];
      System.out.println("Connecting to : " + urlStr);
      con = DriverManager.getConnection(urlStr, args[0], args[1]);
    } catch (SQLException ex) {
        System.err.println("SQLException(" + urlStr + "): " + ex);
    } catch (Exception ex) {
        System.err.println("Exception(" + urlStr + "): " + ex);
    }

    return con;
  }
}