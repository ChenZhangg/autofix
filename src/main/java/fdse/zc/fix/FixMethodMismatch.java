package fdse.zc.fix;

import java.io.IOException;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jface.text.Document;

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
    List list = methodInvocation.arguments();
    list.forEach(System.out::println);
    //list.clear();
    System.out.println(root);
  }
}