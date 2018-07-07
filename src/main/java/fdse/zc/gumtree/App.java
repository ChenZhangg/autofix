package fdse.zc.gumtree;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class App {
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) throws Exception{
        System.out.println(System.getProperty("user.dir"));
        new App().generate();
    }
    public void generate() throws Exception {
        ASTParser parser = ASTParser.newParser(AST.JLS10);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        Map<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_10);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_10);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
        options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
        parser.setCompilerOptions(options);
        TreeNode oldRoot = getRoot(parser, "TestOld.java");
        postOrder(oldRoot);
        TreeNode newRoot = getRoot(parser, "TestNew.java");
        postOrder(newRoot);
        System.out.println("====================");
        MappingStore mappingStore = new MappingStore();
        new GreedySubtreeMatcher(oldRoot, newRoot, mappingStore).match();
        new GreedyBottomUpMatcher(oldRoot, newRoot, mappingStore).match();
        for(Mapping m : mappingStore.asSet()){
            System.out.println(m);
        }
        ActionGenerator g = new ActionGenerator(oldRoot, newRoot, mappingStore);
        g.generate();
        List<Action> actions = g.getActions(); // return the actions
        for(Action a : actions){
            System.out.println(a);
        }
        //postOrder(root);
/*
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_9);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_9);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_9);
        options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
        parser.setCompilerOptions(options);
        parser.setSource(readerToCharArray(r));
        AbstractJdtVisitor v = createVisitor();
        parser.createAST(null).accept(v);
        return v.getTreeContext();
        */
    }
    public TreeNode getRoot(ASTParser parser, String filename) throws Exception{
        List<String> lines = Files.readAllLines(Paths.get(System.getProperty("user.dir"),"src", "main", "java", filename));
        StringBuilder sb = new StringBuilder();
        for(String s : lines){
            sb.append(s + "\n");
        }
        char[] ca = sb.toString().toCharArray();
        System.out.println(ca);
        //System.out.println(ca.length);
        parser.setSource(ca);
        JdtVisitor visitor = new JdtVisitor();
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

    public void postOrder(TreeNode root){
        for(TreeNode node:root.getPreOrderTreeNodeList()){
            System.out.println(node);
            TreeNode parent = node.getParent();
            if(parent == null)
                System.out.println("parent: null");
            else
                System.out.println("parent: " + parent.getId());
        }
        /*
        System.out.println(root);
        if(root.isLeaf())
            return;
        for (TreeNode node : root.getChildren()){
            postOrder(node);
        }
        */
    }
}
