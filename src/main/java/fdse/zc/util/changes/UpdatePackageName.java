package fdse.zc.util.changes;

public class UpdatePackageName extends Change {
  public UpdatePackageName(String oldName, String oldFilePath, String newName, String newFilePath) {
    super(Change.UPDATE_PACKAGE_NAME, oldName, oldFilePath, newName, newFilePath);
  }
}