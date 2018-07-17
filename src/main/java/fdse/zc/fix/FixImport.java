package fdse.zc.fix;

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

public class FixImport {
  public static void add(){

  }

  public static void delete(String repoPath){
   
  }
  public static void update(ASTNode astNode, String path){
    Document document = new Document("import java.util.List;\nclass X {}\n");
    ASTParser parser = ASTParser.newParser(AST.JLS10);
    parser.setSource(document.get().toCharArray());
    CompilationUnit root = (CompilationUnit)parser.createAST(null);
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
    System.out.println(document.get());
  }
}