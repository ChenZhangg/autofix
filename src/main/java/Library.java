import java.util.ArrayList;
import java.util.List;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */

public class Library {
  @Deprecated
    public static boolean someLibraryMethod() {
        return true;
    }
    public static void main(String[] args){
      List<String> list = new ArrayList<>();
      List l = list;
      list = (List<String>)l;
      someLibraryMethod();
    }
}
