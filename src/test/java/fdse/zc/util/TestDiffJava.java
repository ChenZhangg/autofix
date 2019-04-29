package fdse.zc.util;

import static org.junit.Assert.assertEquals;

import java.nio.file.Paths;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.junit.Test;

import fdse.zc.gumtree.Action;
import fdse.zc.gumtree.Update;

public class TestDiffJava {
  private void traverseParents(Action action) {
    traverseParents(action.getNode().getASTNode());
  }

  private void traverseParents(ASTNode node) {
    while(node != null) {
      System.out.println(node.getClass());
      node = node.getParent();
    }
  }

  @Test
  public void UpdateClassNameTest() throws Exception {
    String aPath = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "UpdateClassNameA.java").toString();
    String bPath = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "UpdateClassNameB.java").toString();
    DiffJava diffJava = DiffJava.getInstance();
    List<Action> actions = diffJava.diffFile(diffJava.readCharArrayFromFile(aPath), diffJava.readCharArrayFromFile(bPath));
    Action action = actions.get(0);
    assertEquals(action.getClass(), Update.class);
    assertEquals(action.getNode().getASTNode().getClass(), SimpleName.class);
    //assertEquals(action.getNode().getParent().getASTNode().getClass(), TypeDeclaration.class);
    //assertEquals(action.getNode().getParent().getASTNode().getClass(), SimpleName.class);
  }

  /*
  @Test
  public void UpdatePackageNameTest() throws Exception {
    int a = 10 / 0;
    String aPath = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "UpdatePackageNameA.java").toString();
    String bPath = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "UpdatePackageNameB.java").toString();
    DiffJava diffJava = DiffJava.getInstance();
    List<Action> actions = diffJava.diffFile(diffJava.readCharArrayFromFile(aPath), diffJava.readCharArrayFromFile(bPath));
    Action action = actions.get(0);
    assertEquals(action.getClass(), Update.class);
    assertEquals(action.getNode().getASTNode().getClass(), QualifiedName.class);
    assertEquals(action.getNode().getParent().getASTNode().getClass(), PackageDeclaration.class);
    //traverseParents(actions);
  }
*/
  @Test
  public void UpdateMethodParametersTest() throws Exception {
    String aPath = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "UpdateMethodParametersA.java").toString();
    String bPath = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "UpdateMethodParametersB.java").toString();
    DiffJava diffJava = DiffJava.getInstance();
    List<Action> actions = diffJava.diffFile(diffJava.readCharArrayFromFile(aPath), diffJava.readCharArrayFromFile(bPath));
    Action action = actions.get(0);
    //assertEquals(action.getClass(), Update.class);
    //assertEquals(action.getNode().getASTNode().getClass(), QualifiedName.class);
    //assertEquals(action.getNode().getParent().getASTNode().getClass(), PackageDeclaration.class);
    traverseParents(actions.get(5));
  }
}