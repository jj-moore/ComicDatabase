package comics;

public class Protagonist {

  private int protagonistID;
  private String name;

  Protagonist() {
    this(0, "");
  }

  Protagonist(int id, String name) {
    protagonistID = id;
    this.name = name;
  }

  public int getProtagonistID() {
    return protagonistID;
  }
  
  public void setProtagonistID(int id) {
    protagonistID = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String toString() {
    return protagonistID + " " + name;
  }

  public boolean equals(Object obj) {
    if (obj != null && obj.getClass() == this.getClass()) {
      Protagonist other = (Protagonist) obj;
      return (other.name.equals(this.name));
    }
    return false;
  }
}
