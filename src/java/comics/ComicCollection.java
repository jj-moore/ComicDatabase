package comics;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ComicCollection {

  private ArrayList<Comic> comicCollection = new ArrayList<>();
  private ArrayList<Protagonist> protagonistCollection = new ArrayList<>();
  private ArrayList<Collection> collectionCollection = new ArrayList<>();
  private String errorMessage = "";

  public ArrayList<Comic> getComicCollection() {
    return comicCollection;
  }

  public void setComicCollection(ArrayList<Comic> comicCollection) {
    this.comicCollection = comicCollection;
  }

  public ArrayList<Protagonist> getProtagonistCollection() {
    return protagonistCollection;
  }

  public void setProtagonistCollection(ArrayList<Protagonist> protagonistCollection) {
    this.protagonistCollection = protagonistCollection;
  }

  public ArrayList<Collection> getCollectionCollection() {
    return collectionCollection;
  }

  public void setCollectionCollection(ArrayList<Collection> collectionCollection) {
    this.collectionCollection = collectionCollection;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public void buildArray(Statement statement, HttpServletRequest request) {
    comicCollection.clear();
    protagonistCollection.clear();
    collectionCollection.clear();
    HttpSession session = request.getSession();
    int sessionUserID = Integer.parseInt(session.getAttribute("userID").toString());
    
    try {
      ResultSet results = statement.executeQuery("SELECT * FROM comics WHERE userID="
              + sessionUserID + " ORDER BY title, issueNum;");
      while (results.next()) {
        int comicID = results.getInt("comicID");
        int userID = results.getInt("userID");
        String title = results.getString("title");
        int issueNum = results.getInt("issueNum");
        Date releaseDate = results.getDate("releaseDate");
        float cost = results.getFloat("cost");
        int collectionID = results.getInt("collectionID");
        int protagonistID = results.getInt("protagonistID");
        Comic comic = new Comic(comicID, userID, title, issueNum, releaseDate, cost, collectionID, protagonistID);
        comicCollection.add(comic);
      }

      results = statement.executeQuery("SELECT * FROM protagonists ORDER BY name;");
      while (results.next()) {
        int protagonistID = results.getInt("protagonistID");
        String protagonistName = results.getString("name");
        Protagonist protagonist = new Protagonist(protagonistID, protagonistName);
        protagonistCollection.add(protagonist);
      }

      results = statement.executeQuery("SELECT * FROM collections ORDER BY name;");
      while (results.next()) {
        int collectionID = results.getInt("collectionID");
        String collectionName = results.getString("name");
        Collection collection = new Collection(collectionID, collectionName);
        collectionCollection.add(collection);
      }
    } catch (SQLException e) {
      errorMessage = e.toString() + "<br>";
    }
  }

  public Comic createComic(Statement statement, HttpServletRequest request) {
    int comicID = 0;
    int userID = 0;
    String title;
    int issueNum = 0;
    Date releaseDate = null;
    float cost = 0.0F;
    int protagonistID = 1;
    int collectionID = 1;

    try {
      if (request.getParameter("comicID") != null) {
        comicID = Integer.parseInt(request.getParameter("comicID").trim());
      }
    } catch (NumberFormatException e) {
      errorMessage += "NumberFormatException in hidden field 'comicID'<br>"; //should not happen
    }

    try {
      HttpSession session = request.getSession();
      userID = Integer.parseInt(session.getAttribute("userID").toString());
    } catch (NumberFormatException e) {
    }
    title = request.getParameter("title");

    try {
      issueNum = Integer.parseInt(request.getParameter("issueNum").trim());
    } catch (NumberFormatException e) {
      errorMessage += "Issue number must be an integer. Defaulting to zero.<br>";
    }

    try {
      if (request.getParameter("releaseDate") != null) {
        releaseDate = formatDate(request.getParameter("releaseDate"));
      }
    } catch (IllegalArgumentException e) {
      errorMessage += "Invalid date value in 'Release Date'. Defaulting to today's date.<br>";
    }

    try {
      if (request.getParameter("cost") != null) {
        cost = Float.parseFloat(request.getParameter("cost").trim());
      }
    } catch (NumberFormatException e) {
      errorMessage += "Invalid number in 'cost'. Defaulting to 0.00.<br>";
    }

    try {
      if (request.getParameter("protagonistID").equals("addNew")) {
        protagonistID = addProtagonist(statement, request.getParameter("addProtagonist"), request);
      } else {
        protagonistID = Integer.parseInt(request.getParameter("protagonistID").trim());
      }
    } catch (NumberFormatException e) {
      //should not happen
    }

    try {
      if (request.getParameter("collectionID").equals("addNew")) {
        collectionID = addCollection(statement, request.getParameter("addCollection"), request);
      } else {
        collectionID = Integer.parseInt(request.getParameter("collectionID").trim());
      }
    } catch (NumberFormatException e) {
       //should not happen
    }

    return new Comic(comicID, userID, title, issueNum, releaseDate, cost, collectionID, protagonistID);
  }

  public int addProtagonist(Statement statement, String newProtagonist, HttpServletRequest request) {
    String sql = "INSERT INTO protagonists VALUES(null,'" + newProtagonist + "');";
    int newProtagonistID = executeUpdate(statement, sql);
    buildArray(statement, request);
    return newProtagonistID;
  }

  public int addCollection(Statement statement, String newCollection, HttpServletRequest request) {
    String sql = "INSERT INTO collections VALUES(null,'" + newCollection + "');";
    int newCollectionID = executeUpdate(statement, sql);
    buildArray(statement, request);
    return newCollectionID;
  }

  public boolean hasRecord(Statement statement, Comic comic) {
    String sql = "SELECT title FROM comics WHERE "
            + "title='" + comic.getTitle()
            + "' AND issueNum=" + comic.getIssueNum();
    System.out.println(sql);
    try {
      ResultSet rs = statement.executeQuery(sql);
      return rs.first();
    } catch (SQLException e) {
      errorMessage = e.getMessage() + "<br>";
    }
    return true;
  }

  public void updateCollection(Statement statement, HttpServletRequest request) {
    errorMessage = "";
    String sql = "";
    int arrayIndex = 0;
    Comic comic;

    if (request.getParameter("action") == null) {
      return;
    }
    if (request.getParameter("arrayIndex") != null) {
      arrayIndex = Integer.parseInt(request.getParameter("arrayIndex"));
    }

    switch (request.getParameter("action")) {
      
      case "add":
        comic = createComic(statement, request);
        if (!hasRecord(statement, comic)) {
          sql = "INSERT INTO comics VALUES(null, "
                  + comic.getUserID() + ", '"
                  + comic.getTitle() + "', "
                  + comic.getIssueNum() + ", '"
                  + comic.getSQLReleaseDate() + "', "
                  + comic.getCost() + ", "
                  + comic.getCollectionID() + ", "
                  + comic.getProtagonistID() + ");";
        } else {
          errorMessage = "Record exists. Addition aborted.";
        }
        int newId = executeUpdate(statement, sql);
        if (newId > 0) {
          comic.setComicID(newId);
          comicCollection.add(comic);
        }
        break;
      case "remove":
        comic = createComic(statement, request);
        sql = "DELETE FROM comics WHERE comicID=" + comic.getComicID();
        if (executeUpdate(statement, sql) == 0) {
          comicCollection.remove(arrayIndex);
        }
        break;
      case "update":
        comic = createComic(statement, request);

        sql = "UPDATE comics SET "
                + "title='" + comic.getTitle() + "', "
                + "issueNum=" + comic.getIssueNum() + ", "
                + "releaseDate='" + comic.getSQLReleaseDate() + "', "
                + "cost=" + comic.getCost() + ", "
                + "collectionID=" + comic.getCollectionID() + ", "
                + "protagonistID=" + comic.getProtagonistID()
                + " WHERE comicID=" + comic.getComicID();
        if (executeUpdate(statement, sql) == 0) {
          comicCollection.set(arrayIndex, comic);
        }
        break;
      case "rebuild":
        buildArray(statement, request);
        break;
    }
  }

  public int executeUpdate(Statement statement, String sql) {
    int newID = -1;
    if (sql.length() > 0) {
      System.out.println(sql);
      try {
        if (statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS) > 0) {
          newID = 0;
        }
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.first()) {
          newID = rs.getInt(1);
          rs.close();
        }
      } catch (SQLException e) {
        errorMessage += e.toString() + "<br>";
      }
    }
    return newID;
  }

  public void updateProtagonists(Statement statement, HttpServletRequest request) {
    String sql = "";
    int protagonistID = 0;
    int protagonistIndex = 0;

    try {
      protagonistID = Integer.parseInt(request.getParameter("protagonistID"));
      protagonistIndex = Integer.parseInt(request.getParameter("protagonistIndex"));
    } catch (NumberFormatException e) {
    }

    switch (request.getParameter("action")) {
      case "add":
        sql = "INSERT INTO protagonists VALUES(null, '" + request.getParameter("newProtagonist") + "');";
        int newId = executeUpdate(statement, sql);
        if (newId > 0) {
          Protagonist protagonist = new Protagonist(newId, request.getParameter("newProtagonist"));
          protagonistCollection.add(protagonist);
        }
        break;
      case "remove":
        sql = "DELETE FROM protagonists WHERE protagonistID=" + protagonistID + ";";
        if (executeUpdate(statement, sql) == 0) {
          protagonistCollection.remove(protagonistIndex);
        }
        sql = "UPDATE comics SET protagonistID=1 WHERE protagonistID=" + protagonistID + ";";
        executeUpdate(statement, sql);
        break;
      case "update":
        sql = "UPDATE protagonists SET name='" + request.getParameter("protagonist")
                + "' WHERE protagonistID=" + protagonistID + ";";
        if (executeUpdate(statement, sql) == 0) {
          Protagonist protagonist = new Protagonist(protagonistID, request.getParameter("protagonist"));
          protagonistCollection.set(protagonistIndex, protagonist);
        }
        break;
    }
  }

  public void updateCollections(Statement statement, HttpServletRequest request) {
    String sql = "";
    int collectionID = 0;
    int collectionIndex = 0;

    try {
      collectionID = Integer.parseInt(request.getParameter("collectionID"));
      collectionIndex = Integer.parseInt(request.getParameter("collectionIndex"));
    } catch (NumberFormatException e) {
    }

    switch (request.getParameter("action")) {
      case "add":
        sql = "INSERT INTO collections VALUES(null, '" + request.getParameter("newCollection") + "');";
        int newId = executeUpdate(statement, sql);
        if (newId > 0) {
          Collection collection = new Collection(newId, request.getParameter("newCollection"));
          collectionCollection.add(collection);
        }
        break;
      case "remove":
        sql = "DELETE FROM collections WHERE collectionID=" + collectionID + ";";
        if (executeUpdate(statement, sql) == 0) {
          collectionCollection.remove(collectionIndex);
        }
        sql = "UPDATE comics SET collectionID=1 WHERE collectionID=" + collectionID + ";";
        executeUpdate(statement, sql);
        break;
      case "update":
        sql = "UPDATE collections SET name='" + request.getParameter("collection")
                + "' WHERE collectionID=" + collectionID + ";";
        if (executeUpdate(statement, sql) == 0) {
          Collection collection = new Collection(collectionID, request.getParameter("collection"));
          collectionCollection.set(collectionIndex, collection);
        }
        break;
    }
  }

  private Date formatDate(String inputDate) {
    Pattern pattern = Pattern.compile("^(\\d+)[-\\/]?+(\\d+)?+[-\\/]?+(\\d+)?+$");
    Matcher match = pattern.matcher(inputDate);
    Calendar now = Calendar.getInstance();
    now.setLenient(false);
    int year = 0, month = 0, day = 0;

    try {
      if (match.matches() && match.group(1) != null) {
        if (match.group(2) == null) {
          if (match.group(1).length() == 8) {
            year = Integer.parseInt(match.group(1).substring(4, 8));
            month = Integer.parseInt(match.group(1).substring(0, 2)) - 1;
            day = Integer.parseInt(match.group(1).substring(2, 4));
          } else {
            year = now.get(Calendar.YEAR);
            month = now.get(Calendar.MONTH);
            day = Integer.parseInt(match.group(1));
          }
        } else if (match.group(3) == null) {
          year = now.get(Calendar.YEAR);
          month = Integer.parseInt(match.group(1).trim()) - 1;
          day = Integer.parseInt(match.group(2).trim());
        } else if (match.group(1).trim().length() == 4) {
          year = Integer.parseInt(match.group(1).trim());
          month = Integer.parseInt(match.group(2).trim()) - 1;
          day = Integer.parseInt(match.group(3).trim());
        } else if (match.group(3).trim().length() == 4) {
          year = Integer.parseInt(match.group(3).trim());
          month = Integer.parseInt(match.group(1).trim()) - 1;
          day = Integer.parseInt(match.group(2).trim());
        }
      }
      now.set(year, month, day);
      now.getTime();

    } catch (RuntimeException e) {
      errorMessage += "Unrecognized date. Defaulting to today's date.<br>";
      now = Calendar.getInstance();
    }

    return new Date(now.getTimeInMillis());
  }

}
