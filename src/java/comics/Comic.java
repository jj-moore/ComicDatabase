package comics;

import java.sql.Date;
import java.text.NumberFormat;

public class Comic {

  private int comicID;
  private int userID;
  private String title;
  private int issueNum;
  private Date releaseDate;
  private float cost;  
  private int protagonistID;
  private int collectionID;

  Comic() {
    this(0, 0, "", 0, new Date(0), 0F, 1, 1);
  }

  Comic(int comicID, int userID, String title, int issueNum, Date releaseDate,
          float cost, int collectionID, int antagonistID) {
    this.comicID = comicID;
    this.userID = userID;
    this.title = title;
    this.issueNum = issueNum;
    this.releaseDate = releaseDate;
    this.cost = cost;
    this.collectionID = collectionID;
    this.protagonistID = antagonistID;
  }

  public int getComicID() {
    return comicID;
  }

  public void setComicID(int comicID) {
    this.comicID = comicID;
  }
  
  public int getUserID() {
    return userID;
  }
  
  public void setUserID(int userID) {
    this.userID = userID;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getIssueNum() {
    return issueNum;
  }

  public void setIssueNum(int issueNum) {
    this.issueNum = issueNum;
  }

  public String getReleaseDate() {
    String date = releaseDate.toString();
    return date.substring(5) + "-" + date.substring(0, 4);
  }

  public Date getSQLReleaseDate() {
    return releaseDate;
  }
  
  public void setReleaseDate(Date releaseDate) {
    this.releaseDate = releaseDate;
  }

  public String getCost() {
    NumberFormat format = NumberFormat.getNumberInstance();
    format.setMinimumFractionDigits(2);
    return format.format(cost);
  }

  public void setCost(float cost) {
    this.cost = cost;
  }

  public int getProtagonistID() {
    return protagonistID;
  }

  public void setProtagonistID(int antagonistID) {
    this.protagonistID = antagonistID;
  }
  
  public int getCollectionID() {
    return collectionID;
  }

  public void setCollectionID(int collectionID) {
    this.collectionID = collectionID;
  }

  public String toString() {
    return "ID " + comicID + " " + title + " issue: " + issueNum;
  }

  public boolean equals(Object obj) {
    if (obj != null && obj.getClass() == this.getClass()) {
      Comic other = (Comic) obj;
      return other.getTitle().equals(this.getTitle()) && other.issueNum == this.issueNum;
    }
    return false;
  }

}
