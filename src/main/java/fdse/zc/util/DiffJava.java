package fdse.zc.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import org.eclipse.jdt.core.dom.ASTNode;

import fdse.zc.gumtree.Action;
import fdse.zc.gumtree.ActionGenerator;
import fdse.zc.gumtree.GreedyBottomUpMatcher;
import fdse.zc.gumtree.GreedySubtreeMatcher;
import fdse.zc.gumtree.ITree;
import fdse.zc.gumtree.Mapping;
import fdse.zc.gumtree.MappingStore;
import fdse.zc.gumtree.TreeContext;
import fdse.zc.gumtree.java.JdtTreeGenerator;

public class DiffJava{

  private static DiffJava diffJava;

  private DiffJava() { }

  public static DiffJava getInstance() {
    if(diffJava == null) {
      diffJava = new DiffJava();
    }
    return diffJava;
  }

  public List<Action> diffFile(char[] oldChars,char[] newChars) throws Exception{
    ITree oldRoot = getRoot(oldChars);
    ITree newRoot = getRoot(newChars);
    MappingStore mappingStore = new MappingStore();
    new GreedySubtreeMatcher(oldRoot, newRoot, mappingStore).match();
    new GreedyBottomUpMatcher(oldRoot, newRoot, mappingStore).match();
    //for(Mapping m : mappingStore.asSet()){
        //System.out.println(m);
    //}
    ActionGenerator g = new ActionGenerator(oldRoot, newRoot, mappingStore);
    g.generate();
    List<Action> actions = g.getActions();
    for(Action action : actions) {
      System.out.println(action);
    }
    return actions;
  }

  public char[] readCharArrayFromFile(String filePath) {
    String theString = "";

    File file = new File(filePath);
    Scanner scanner = null;
    try {
      scanner = new Scanner(file);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
	}
    
    theString = scanner.nextLine();
    while (scanner.hasNextLine()) {
           theString = theString + "\n" + scanner.nextLine();
    }
    
    char[] charArray = theString.toCharArray();
    return charArray;
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