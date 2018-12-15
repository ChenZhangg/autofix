package fdse.zc.fix.abs;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;

import fdse.zc.git.GitRepo;

public class AddMethod {
  public static void main(String[] args) throws Exception {
    String gitRepoPath = "/Users/zhangchen/projects/projectanalysis/pinpoint/.git";
    String commitSHA = "03eb0891c485e49d538dd547195ff4c89d930615";
    String filePath = "bootstrap/src/test/java/com/navercorp/pinpoint/bootstrap/interceptor/MockTrace.java";
    String methodName = "getId";
    String paraName = "";
    String parentClassName = "com.navercorp.pinpoint.bootstrap.context.Trace";
    AddMethod.add(gitRepoPath, commitSHA, filePath, methodName, paraName, parentClassName);
  }

  public static void add(String gitRepoPath, String commitSHA, String filePath, String methodName, String paraName, String parentClassName) throws Exception {
    GitRepo gitRepo = new GitRepo(gitRepoPath);
    List<String> javaFileList = gitRepo.getFileList(commitSHA);
    String parentFilePath = null;
    for(String s : javaFileList){
      if(s.replaceAll("\\.java", "").replaceAll("/", ".").endsWith(parentClassName)){
        parentFilePath = s;
      }
    }
    String fileContent = gitRepo.getFileContent(commitSHA, parentFilePath);
    Document document = new Document(fileContent);
    ASTParser parser = ASTParser.newParser(AST.JLS10);
    parser.setSource(document.get().toCharArray());
    CompilationUnit compilationUnit = (CompilationUnit)parser.createAST(null);
  }
  

}