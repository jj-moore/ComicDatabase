package comics;

public class Collection {

  private int collectionID;
  private String name;

  Collection() {
    this (0, "");
  }

  Collection(int id, String name) {
    collectionID = id;
    this.name = name;
  }

  public int getCollectionID() {
    return collectionID;
  }

  public void setCollectionID(int id) {
    collectionID = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String toString() {
    return collectionID + " " + name;
  }

  public boolean equals(Object obj) {
    if (obj != null && obj.getClass() == this.getClass()) {
      Collection other = (Collection) obj;
      return other.collectionID == this.collectionID
              && other.name.equals(this.name);
    }
    return false;
  }
}
