package fdse.zc;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import fdse.zc.gumtree.Action;
import fdse.zc.gumtree.ActionGenerator;
import fdse.zc.gumtree.GreedyBottomUpMatcher;
import fdse.zc.gumtree.GreedySubtreeMatcher;
import fdse.zc.gumtree.HashGenerator;
import fdse.zc.gumtree.ITree;
import fdse.zc.gumtree.java.JdtVisitor;
import fdse.zc.gumtree.Mapping;
import fdse.zc.gumtree.MappingStore;
import fdse.zc.gumtree.TreeContext;
import fdse.zc.gumtree.java.JavaTree;
import fdse.zc.gumtree.java.JdtTreeGenerator;
import fdse.zc.gumtree.TreeUtils;



public class DiffJava{
  private ASTParser parser = null;
  public static void main(String[] args) throws Exception {
    DiffJava diff = new DiffJava();
    String repoPath = "/Users/zhangchen/projects/projectanalysis/dynjs/.git";
    String filePath = "src/main/java/org/dynjs/runtime/GlobalObject.java";
    String preCommit = "29dcbb74a5ef857b88116e6b30eaaeddc70703a3";
    String nextCommit = "4462b9831f3b003c224c20d5c5efa9304a2815fc";
    diff.diffFile(repoPath, filePath, preCommit, nextCommit);
  }

  public void diffFile(String repoPath, String filePath, String oldCommit, String newCommit) throws Exception{
    GitRepo repo = new GitRepo(repoPath);
    char[] oldCharArray = repo.getChars(oldCommit, filePath);
    char[] newCharArray = repo.getChars(newCommit, filePath);
    ITree oldRoot = getRoot(oldCharArray);
    ITree newRoot = getRoot(newCharArray);
    MappingStore mappingStore = new MappingStore();
    new GreedySubtreeMatcher(oldRoot, newRoot, mappingStore).match();
    new GreedyBottomUpMatcher(oldRoot, newRoot, mappingStore).match();
    for(Mapping m : mappingStore.asSet()){
        //System.out.println(m);
    }
    ActionGenerator g = new ActionGenerator(oldRoot, newRoot, mappingStore);
    g.generate();
    List<Action> actions = g.getActions();
    for(Action a : actions){
      System.out.println(a.toString());
    }
  }

  public ITree getRoot(char[] charArray){
    JdtTreeGenerator generator = new JdtTreeGenerator();
    TreeContext treeContext = generator.getTreeContext(charArray);
    ITree root = treeContext.getRoot();
    return root;
  }
  public static void postOrder(ITree root){
    for(ITree node:root.getPreOrderTreeNodeList()){
        System.out.println(node);
        ITree parent = node.getParent();
        if(parent == null)
            System.out.println("parent: null");
        else
            System.out.println("parent: " + parent.getId());
    }
  }
}