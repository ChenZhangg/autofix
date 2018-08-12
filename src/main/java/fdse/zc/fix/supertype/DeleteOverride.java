package fdse.zc.fix.supertype;

import java.io.IOException;

import javax.swing.text.BadLocationException;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.UndoEdit;

import fdse.zc.git.GitRepo;

public class DeleteOverride {
  public static void main(String[] args) throws Exception {
    DeleteOverride.deleteOverride();
  }
  public static void deleteOverride() throws Exception {
    String repoPath = "/Users/zhangchen/projects/projectanalysis/pircbotx/.git";
    GitRepo gitRepo = new GitRepo(repoPath);
    String fileContent = gitRepo.getFileContent("19be0684a207968ddbfeda045970eb4e876ce94d", "src/main/java/org/pircbotx/hooks/events/UnknownEvent.java");

    System.out.println(fileContent);

    
    Document document = new Document(fileContent);
    ASTParser parser = ASTParser.newParser(AST.JLS10);
    parser.setSource(document.get().toCharArray());
    CompilationUnit compilationUnit = (CompilationUnit)parser.createAST(null);
    AnnotionVisitor annotionVisitor = new AnnotionVisitor();
    compilationUnit.accept(annotionVisitor);
    FieldDeclaration fieldDeclaration = annotionVisitor.getFieldDeclaration();
    AST ast = fieldDeclaration.getAST();
    ASTRewrite rewrite = ASTRewrite.create(ast);
    NormalAnnotation normalAnnotation = null;
    for(Object o : fieldDeclaration.modifiers()){
      if(o instanceof NormalAnnotation){
        normalAnnotation = (NormalAnnotation)o;
      }
    }
    rewrite.remove(normalAnnotation, null);
    TextEdit edits = rewrite.rewriteAST(document, null);
    UndoEdit undo = null;
    try {
      undo = edits.apply(document);
    } catch(MalformedTreeException e) {
        e.printStackTrace();
    }
    System.out.println(document.get());
    /*
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
    */
  }
}