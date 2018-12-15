package fdse.zc.fix.pack;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

import fdse.zc.git.GitRepo;

public class ChangePackage {
  /*
  private String fileName;
  private int wrongLineNumber;
  private GitRepo gitRepo;
  private String commitSHA;
  public ChangePackage(String fileName, int wrongLineNumber, GitRepo gitRepo, String commitSHA){
    this.fileName = fileName;
    this.wrongLineNumber = wrongLineNumber;
    this.gitRepo = gitRepo;
    this.commitSHA = commitSHA;
  }

  public void run() throws Exception  {
    String fileContent = gitRepo.getFileContent(commitSHA, fileName);
    //System.out.println(fileContent);
    List<String> javaFileList = gitRepo.getFileList(commitSHA);
    javaFileList.forEach(System.out::println);
  }
  
  public static class ImportDeclarationVisitor extends ASTVisitor{
    private int wrongLineNum;
    private CompilationUnit cu;
    private 
    private int maxStartLineNum;
    public TypeDeclarationVisitor(int wrongLineNum){
      this.wrongLineNum = wrongLineNum;
    }
    @Override
    public boolean visit(CompilationUnit node) {
      this.cu = node;
      return super.visit(node);
    }

    @Override
    public boolean visit(TypeDeclaration node) {
      int startLineNumber = cu.getLineNumber(node.getStartPosition());
      int endLineNumber = cu.getLineNumber(node.getStartPosition() + node.getLength() - 1);
      if(wrongLineNum >= startLineNumber && wrongLineNum <= endLineNumber && startLineNumber > maxStartLineNum){
        td = node;
        maxStartLineNum = startLineNumber;
      }
      return super.visit(node);
    }

    public TypeDeclaration getTypeDeclaration(){
      return td;
    }
  }
  
  public static class MethodDeclarationVisitor extends ASTVisitor{
    private List<MethodDeclaration> mdList = new ArrayList<>();
    @Override
    public boolean visit(MethodDeclaration node) {
      mdList.add(node);
      return super.visit(node);
    }

    public List<MethodDeclaration> getMethodDeclarationList(){
      return mdList;
    }
  }
  */
}