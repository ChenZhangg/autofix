package fdse.zc.util.changes;

import java.util.Objects;

public abstract class Change {
  public static final int UPDATE_METHOD_NAME = 1;
  public static final int UPDATE_CLASS_NAME = 2;

  protected int type;
  protected String oldName;
  protected String newName;
  protected String oldFilePath;
  protected String newFilePath;
  public Change(int type, String oldName, String oldFilePath, String newName, String newFilePath) {
    this.type = type;
    this.oldName = oldName;
    this.oldFilePath = oldFilePath;
    this.newName = newName;
    this.newFilePath = newFilePath;
  }

  public boolean equals(Object otherObject) {
    if (this == otherObject) return true;
    if (otherObject == null) return false;
    if (getClass() != otherObject.getClass())
      return false;
    Change other = (Change)otherObject;
    return type == other.type 
    && Objects.equals(oldName, other.oldName)
    && Objects.equals(oldFilePath, other.oldFilePath) 
    && Objects.equals(newName, other.newName) 
    && Objects.equals(newFilePath, other.newFilePath);  
  }
}