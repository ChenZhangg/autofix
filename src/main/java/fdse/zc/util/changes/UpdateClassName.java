package fdse.zc.util.changes;

public class UpdateClassName extends Change {
  public UpdateClassName(String oldName, String oldFilePath, String newName, String newFilePath) {
    super(Change.UPDATE_CLASS_NAME, oldName, oldFilePath, newName, newFilePath);
  }
}