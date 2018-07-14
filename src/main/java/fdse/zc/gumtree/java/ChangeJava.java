package fdse.zc.gumtree.java;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ITrackedNodePosition;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.UndoEdit;

import fdse.zc.GitRepo;

public class ChangeJava{
  private ASTParser parser;
  public static void main(String[] args) throws Exception {
    ChangeJava diff = new ChangeJava();
    String repoPath = "/Users/zhangchen/projects/projectanalysis/dynjs/.git";
    String filePath = "src/main/java/org/dynjs/runtime/GlobalObject.java";
    String commit = "4462b9831f3b003c224c20d5c5efa9304a2815fc";
    diff.diffFile(repoPath, filePath, commit);
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

  public void diffFile(String repoPath, String filePath, String commit) throws Exception{
    initParser();
    GitRepo repo = new GitRepo(repoPath);
    char[] oldCharArray = repo.getChars(commit, filePath);

    Document document = new Document(String.valueOf(oldCharArray));
    parser.setSource(oldCharArray);
    CompilationUnit root = (CompilationUnit)parser.createAST(null);
    AST ast = root.getAST();
    ImportDeclaration id = ast.newImportDeclaration();
    id.setName(ast.newName(new String[] {"zc", "zc", "zc"}));
    ASTRewrite rewriter = ASTRewrite.create(ast);   
    TypeDeclaration td = (TypeDeclaration)root.types().get(0);
    ITrackedNodePosition tdLocation = rewriter.track(td);
    ListRewrite lrw = rewriter.getListRewrite(root, CompilationUnit.IMPORTS_PROPERTY);
    lrw.insertLast(id, null);
    TextEdit edits = rewriter.rewriteAST(document, null);
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

}