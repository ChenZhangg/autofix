package fdse.zc.util.changes;

public class UpdateMethodName extends Change {
  public UpdateMethodName(String oldName, String oldFilePath, String newName, String newFilePath) {
    super(Change.UPDATE_METHOD_NAME, oldName, oldFilePath, newName, newFilePath);
  }
   public String toString() {
     return "Update Method Name From " + oldName + " to " + newName;
   }
}