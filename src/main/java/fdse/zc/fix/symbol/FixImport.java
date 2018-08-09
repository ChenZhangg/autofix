package fdse.zc.fix.symbol;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.UndoEdit;

import fdse.zc.fix.Similarity;
import fdse.zc.git.GitRepo;

public class FixImport{
  public static void main(String[] args) throws Exception {
    FixImport.changeImport();
  }

  public static void deleteImport() throws Exception {
    //错误的location为package一般应该是值import语句
    String repoPath = "/Users/zhangchen/projects/projectanalysis/pinpoint/.git";
    GitRepo gitRepo = new GitRepo(repoPath);
    String fileContent = gitRepo.getFileContent("a8b42c542185b90974b38033883b61850cb4f0f3", "profiler/src/main/java/com/navercorp/pinpoint/profiler/context/DefaultTrace.java");

    //System.out.println(fileContent);
    Document document = new Document(fileContent);
    ASTParser parser = ASTParser.newParser(AST.JLS10);
    parser.setSource(document.get().toCharArray());
    CompilationUnit compilationUnit = (CompilationUnit)parser.createAST(null);
    List importList = compilationUnit.imports();
    ImportDeclaration importDeclaration = null;
    for(int i = 0; i < importList.size(); i++){
      importDeclaration = (ImportDeclaration)importList.get(i);
      int lineNumber = compilationUnit.getLineNumber(importDeclaration.getStartPosition());
      if(lineNumber == 25)
        break;
      //System.out.println(importDeclaration);
      //System.out.println(lineNumber);
    }
    AST ast = compilationUnit.getAST();
    ASTRewrite rewrite = ASTRewrite.create(ast);
    ListRewrite listRewrite = rewrite.getListRewrite(compilationUnit, CompilationUnit.IMPORTS_PROPERTY);
    listRewrite.remove(importDeclaration, null);
    TextEdit edits = rewrite.rewriteAST(document, null);
    UndoEdit undo = null;
    try {
      undo = edits.apply(document);
    } catch(MalformedTreeException e) {
        e.printStackTrace();
    } catch(BadLocationException e) {
        e.printStackTrace();
    }
    System.out.println(document.get());
  }

  public static void addImport() throws Exception{
    String path = "/Users/zhangchen/projects/projectanalysis/test/cp.txt";
    List<String> list = Files.readAllLines(Paths.get(path));
    for(int i = 0; i < list.size(); i++){
      String jarPath = list.get(i);
      if(jarPath.endsWith("jar")){
        List<String> list2 = searchJar(jarPath);
      }
      //System.out.println(list.get(i));
    }
  }

  public static List<String> searchJar(String jarPath) throws Exception{
    List<String> list = new ArrayList<>();
    JarFile jarFile = new JarFile(jarPath);
    Enumeration<JarEntry> entries = jarFile.entries();
    while (entries.hasMoreElements()) {
      JarEntry jarEntry = entries.nextElement();
      if(jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")){
        continue;
      }
      String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6);
      className = className.replace('/', '.');
      if(className.endsWith("Document"))
        list.add(className);
    }
    jarFile.close();
    return list;
  }

  public static void changeImport() throws Exception{
    String repoPath = "/Users/zhangchen/projects/projectanalysis/pinpoint/.git";
    GitRepo gitRepo = new GitRepo(repoPath);
    //String fileContent = gitRepo.getFileContent("f20c6c68a28c91c33e8c010fc23372366417fa4d", "plugins/httpclient4/src/main/java/com/navercorp/pinpoint/plugin/httpclient4/interceptor/CloseableHttpAsyncClientExecuteMethodWithHttpAsyncRequestProducerInterceptor.java");
    List<String> list = gitRepo.getFileList("f20c6c68a28c91c33e8c010fc23372366417fa4d");
    Similarity.identifierSimilarity("com.navercorp.pinpoint.bootstrap.context.AttributeScope.class", "com.navercorp.pinpoint.bootstrap.context.AttributeScoaabnhghgf.java");
    //list.forEach(System.out::println);
    //System.out.println(fileContent);
    //Document document = new Document(fileContent);
    //ASTParser parser = ASTParser.newParser(AST.JLS10);
    //parser.setSource(document.get().toCharArray());
    //CompilationUnit compilationUnit = (CompilationUnit)parser.createAST(null);
   // List importList = compilationUnit.imports();
  }


}