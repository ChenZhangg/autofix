package fdse.zc.fix;

import java.util.List;
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

public class FixError{
  public static void main(String[] args) throws Exception {
    Document document = new Document("import java.util.List;\nimport java.util.Map;\nimport java.util.Set;\nclass X {}\n");
    ASTParser parser = ASTParser.newParser(AST.JLS10);
    parser.setSource(document.get().toCharArray());
    CompilationUnit root = (CompilationUnit)parser.createAST(null);
    root.recordModifications();
    List list = root.imports();
    ImportDeclaration importDeclaration = null;
    for(int i = 0; i < list.size(); i++){
      importDeclaration = (ImportDeclaration)list.get(i);
      if(importDeclaration.getName().getFullyQualifiedName().equals("java.util.Map"))
        break;
    }
    AST ast = root.getAST();
    importDeclaration.setName(ast.newName(new String[] {"zc", "zc", "zc"}));
    TextEdit edits = root.rewrite(document, null);
    UndoEdit undo = null;
    try {
      undo = edits.apply(document);
    } catch(MalformedTreeException e) {
        e.printStackTrace();
    } catch(BadLocationException e) {
        e.printStackTrace();
    }
    System.out.println(document.get());
/*
    AST ast = root.getAST();
    ImportDeclaration id = ast.newImportDeclaration();
    id.setName(ast.newName(new String[] {"zc", "zc", "zc"}));
    ASTRewrite rewriter = ASTRewrite.create(ast);
  
    //TypeDeclaration td = (TypeDeclaration)root.types().get(0);
    //ITrackedNodePosition tdLocation = rewriter.track(td);
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
    FixImport.update();
    */
  }
}