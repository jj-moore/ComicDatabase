package comics;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import jdbc.MyConnection;

public class CreateDatabase {

  static void createDatabase(Statement statement) throws SQLException {

    String dropComics = "DROP TABLE IF EXISTS comics";
    String dropUsers = "DROP TABLE IF EXISTS users";
    String dropCollections = "DROP TABLE IF EXISTS collections";
    String dropProtagonists = "DROP TABLE IF EXISTS protagonists";

    String comics = "CREATE TABLE comics (comicID int NOT NULL AUTO_INCREMENT, "
            + "userID int, title varchar(40) NOT NULL, issueNum int, releaseDate date, cost float, "
            + "protagonistID int, collectionID int, "
            + "PRIMARY KEY(comicID), "
            + "FOREIGN KEY(userID) REFERENCES users(userID), "
            + "FOREIGN KEY(protagonistID) REFERENCES protagonists(protagonistID), "
            + "FOREIGN KEY(collectionID) REFERENCES collections(collectionID));";

    String comicIndex = "CREATE INDEX comics_index_title_issueNum ON comics (title, issueNum);";

    String test1 = "INSERT INTO comics VALUES (null, 1, 'Batman', 216, '2016-12-15', 2.99, 1, 1);";
    String test2 = "INSERT INTO comics VALUES (null, 2, 'Flash', 142, '2016-12-15', 0.99, 1, 1);";

    String users = "CREATE TABLE users (userID int NOT NULL AUTO_INCREMENT, "
            + "userName varchar(20), password varchar(10), PRIMARY KEY(userID));";

    String userDavid = "INSERT INTO users VALUES (null, 'david', 'd');";
    String userJeremy = "INSERT INTO users VALUES (null, 'jeremy', 'j');";

    String protagonists = "CREATE TABLE protagonists (protagonistID int NOT NULL AUTO_INCREMENT, "
            + "name varchar(40), PRIMARY KEY(protagonistID));";

    String defaultProtagonist = "INSERT INTO protagonists VALUES(null, 'Undefined');";

    String collections = "CREATE TABLE collections (collectionID int NOT NULL AUTO_INCREMENT, "
            + "name varchar(40), PRIMARY KEY(collectionID));";

    String defaultCollection = "INSERT INTO collections VALUES(null, 'None');";

    statement.executeUpdate(dropComics);
    statement.executeUpdate(dropUsers);
    statement.executeUpdate(dropProtagonists);
    statement.executeUpdate(dropCollections);
    statement.executeUpdate(protagonists);
    statement.executeUpdate(defaultProtagonist);
    statement.executeUpdate(collections);
    statement.executeUpdate(defaultCollection);
    statement.executeUpdate(users);
    statement.executeUpdate(userDavid);
    statement.executeUpdate(userJeremy);
    statement.executeUpdate(comics);
    statement.executeUpdate(comicIndex);
    statement.executeUpdate(test1);
    statement.executeUpdate(test2);
  }

  public static void main(String[] args) {
    String[] credentials = {"jjmoore", "63jb2x4aWqGQ"};
    Connection connection = null;
    Statement statement = null;

    try {
      connection = MyConnection.getConnection(credentials);
      statement = connection.createStatement();
      createDatabase(statement);
    } catch (SQLException e) {
      System.out.println("CreateDatabase EXCEPTION:\n" + e);
    } finally {
      try {
        if (statement != null) {
          statement.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        System.out.println("CreateDatabase EXCEPTION:\n" + e);
      }
    }
  }

}
