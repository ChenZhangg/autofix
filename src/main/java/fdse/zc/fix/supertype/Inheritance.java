package fdse.zc.fix.supertype;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.BadLocationException;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.UndoEdit;

import fdse.zc.git.GitRepo;

public class Inheritance {
  public static void main(String[] args) throws Exception {
    String gitRepoPath = "/Users/zhangchen/projects/projectanalysis/Cobweb/.git";
    String commitSHA = "5755ea7ad982e3548cd61333daa155aef10d5a94";
    String filePath = "src/main/java/com/xiongbeer/cobweb/zk/resources/INodeDirectory.java";
    int lineNum = 32;
    Map<String, String> superMap = Inheritance.getSuperMap(gitRepoPath, commitSHA, filePath, lineNum);
    Inheritance.getMethodSignature(gitRepoPath, commitSHA, superMap);
  }

  public static Map<String, String> getSuperMap(String gitRepoPath, String commitSHA, String filePath, int wrongLineNum) throws Exception {
    GitRepo gitRepo = new GitRepo(gitRepoPath);
    String fileContent = gitRepo.getFileContent(commitSHA, filePath);
    Document document = new Document(fileContent);
    ASTParser parser = ASTParser.newParser(AST.JLS10);
    parser.setSource(document.get().toCharArray());
    CompilationUnit compilationUnit = (CompilationUnit)parser.createAST(null);
    TypeDeclarationVisitor tdVisitor = new TypeDeclarationVisitor(wrongLineNum);
    compilationUnit.accept(tdVisitor);
    TypeDeclaration td = tdVisitor.getTypeDeclaration();
    Type superClassType = td.getSuperclassType();
    List interfaceList = td.superInterfaceTypes();
    List<String> superList = new ArrayList<>();

    if(superClassType != null){
      if(superClassType.isSimpleType()){
        superList.add(((SimpleType)superClassType).getName().getFullyQualifiedName());
      }
    }

    for(int i = 0; i < interfaceList.size(); i++){
      superList.add(((SimpleType)interfaceList.get(i)).getName().getFullyQualifiedName());
    }

    superList.forEach(System.out::println);
    List<ImportDeclaration> importList = compilationUnit.imports();
    Map<String, String> superMap = new LinkedHashMap<>();
    for(int i = 0; i < superList.size(); i++){
      superMap.put(superList.get(i), null);
    }

    for(int i = 0; i < importList.size(); i++){
      for(int j = 0; j < superList.size(); j++){
        String importString = importList.get(i).getName().getFullyQualifiedName();
        if(importString.endsWith(superList.get(j))){
          superMap.put(superList.get(j), importString);
        }
      }
    }

    String packageName = compilationUnit.getPackage().getName().getFullyQualifiedName();
    for(String s : superMap.keySet()){
      if(superMap.get(s) == null){
        superMap.put(s, packageName + "." + s);
      }
    }
    return superMap;
  }

  public static List<MethodDeclaration> getMethodSignature(String gitRepoPath, String commitSHA, Map<String, String> superMap)  throws Exception {
    GitRepo gitRepo = new GitRepo(gitRepoPath);
    List<String> fileList = gitRepo.getFileList(commitSHA);
    Map<String, String> superFileMap = new LinkedHashMap<>();
    for(String s : superMap.keySet()){
      String fileName = superMap.get(s).replaceAll("\\.", "/") + ".java";
      for(int j = 0; j < fileList.size(); j++){
        if(fileList.get(j).endsWith(fileName)){
          superFileMap.put(s, fileList.get(j));
          break;
        }
      }
    }
    List<MethodDeclaration> result = new ArrayList<>();
    for(String s : superFileMap.keySet()){
      String superFilePath = superFileMap.get(s);
      String fileContent = gitRepo.getFileContent(commitSHA, superFilePath);
      Document document = new Document(fileContent);
      ASTParser parser = ASTParser.newParser(AST.JLS10);
      parser.setSource(document.get().toCharArray());
      CompilationUnit compilationUnit = (CompilationUnit)parser.createAST(null);
      MethodDeclarationVisitor mdVisitor = new MethodDeclarationVisitor();
      compilationUnit.accept(mdVisitor);
      List<MethodDeclaration> mdList = mdVisitor.getMethodDeclarationList();
      result.addAll(mdList);
    }
    return result;
  }

  

  public static class TypeDeclarationVisitor extends ASTVisitor{
    private int wrongLineNum;
    private CompilationUnit cu;
    private TypeDeclaration td;
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
  
}