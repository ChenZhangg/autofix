package fdse.zc;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import fdse.zc.gumtree.java.Action;
import fdse.zc.gumtree.java.ActionGenerator;
import fdse.zc.gumtree.java.GreedyBottomUpMatcher;
import fdse.zc.gumtree.java.GreedySubtreeMatcher;
import fdse.zc.gumtree.java.HashGenerator;
import fdse.zc.gumtree.java.JdtVisitor;
import fdse.zc.gumtree.java.Mapping;
import fdse.zc.gumtree.java.MappingStore;
import fdse.zc.gumtree.java.TreeContext;
import fdse.zc.gumtree.java.TreeNode;
import fdse.zc.gumtree.java.TreeUtils;



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

  public void initParser(){
    parser = ASTParser.newParser(AST.JLS10);
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    Map<String, String> options = JavaCore.getOptions();
    options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_10);
    options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_10);
    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
    options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
    parser.setCompilerOptions(options);
  }

  public void diffFile(String repoPath, String filePath, String oldCommit, String newCommit) throws Exception{
    initParser();
    GitRepo repo = new GitRepo(repoPath);
    char[] oldCharArray = repo.getChars(oldCommit, filePath);
    char[] newCharArray = repo.getChars(newCommit, filePath);
    TreeNode oldRoot = getRoot(oldCharArray);
    TreeNode newRoot = getRoot(newCharArray);
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

  public TreeNode getRoot(char[] charArray){
    JdtVisitor visitor = new JdtVisitor();
    parser.setSource(charArray);
    parser.createAST(null).accept(visitor);
    TreeContext treeContext = visitor.getTreeContext();
    TreeNode root = treeContext.getRoot();
    TreeUtils treeUtils = new TreeUtils();
    treeUtils.computeHeight(root);
    treeUtils.computeSize(root);
    treeUtils.computeDepth(root, -1);
    treeUtils.computeId(root);
    new HashGenerator().hash(root);
    return root;
  }
  public static void postOrder(TreeNode root){
    for(TreeNode node:root.getPreOrderTreeNodeList()){
        System.out.println(node);
        TreeNode parent = node.getParent();
        if(parent == null)
            System.out.println("parent: null");
        else
            System.out.println("parent: " + parent.getId());
    }
  }
}