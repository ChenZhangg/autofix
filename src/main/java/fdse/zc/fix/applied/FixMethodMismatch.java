package fdse.zc.fix.applied;

import java.util.List;


import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.UndoEdit;

import fdse.zc.git.GitRepo;

public class FixMethodMismatch {
  public static void main(String[] args) throws Exception {
    int lineNumber = 1387;
    String repoPath = "/Users/zhangchen/projects/projectanalysis/flyway/.git";
    GitRepo gitRepo = new GitRepo(repoPath);
    String fileContent = gitRepo.getFileContent("ed3dbc0779f680a72237cf81e6e13f642d162ad4", "flyway-core/src/main/java/org/flywaydb/core/Flyway.java");
    //String fileContent = gitRepo.getFileContent("242abf15b5d1ceaa97d1213443aabd177ccc4e77", "src/main/java/org/stellar/sdk/requests/OrderBookRequestBuilder.java");
    //System.out.println(fileContent);
    Document document = new Document(fileContent);
    ASTParser parser = ASTParser.newParser(AST.JLS10);
    parser.setSource(document.get().toCharArray());
    CompilationUnit root = (CompilationUnit)parser.createAST(null);
    MethodMismatchVisitor visitor = new MethodMismatchVisitor();
    root.accept(visitor);
    MethodInvocation methodInvocation = visitor.getMethodInvocation();
    AST ast = methodInvocation.getAST();
    ASTRewrite astRewrite = ASTRewrite.create(ast);
    ListRewrite listRewrite = astRewrite.getListRewrite(methodInvocation, MethodInvocation.ARGUMENTS_PROPERTY);
    List list = methodInvocation.arguments();
    for(Object o : list){
      //listRewrite.remove((ASTNode)o, null);
    }
    TextEdit edits = astRewrite.rewriteAST(document, null);
    UndoEdit undo = null;
    try {
      undo = edits.apply(document);
    } catch(MalformedTreeException e) {
        e.printStackTrace();
    }
    System.out.println(document.get());
    //List list = methodInvocation.arguments();
    //list.forEach(System.out::println);
    //list.clear();
    //System.out.println(root);
  }
}