package fdse.zc.fix;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ITrackedNodePosition;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.UndoEdit;

import fdse.zc.git.GitRepo;

public class FixDiamond{
  public static void main(String[] args) throws Exception {
    int lineNumber = 42;
    String repoPath = "/Users/zhangchen/projects/projectanalysis/java-stellar-sdk/.git";
    GitRepo gitRepo = new GitRepo(repoPath);
    String fileContent = gitRepo.getFileContent("ebdba1e960536e34c006c62ebe025f33508c7198", "src/main/java/org/stellar/sdk/requests/OrderBookRequestBuilder.java");
    //String fileContent = gitRepo.getFileContent("242abf15b5d1ceaa97d1213443aabd177ccc4e77", "src/main/java/org/stellar/sdk/requests/OrderBookRequestBuilder.java");

    //System.out.println(fileContent);
    Document document = new Document(fileContent);
    ASTParser parser = ASTParser.newParser(AST.JLS10);
    parser.setSource(document.get().toCharArray());
    CompilationUnit root = (CompilationUnit)parser.createAST(null);
    List list = root.types();
    AbstractTypeDeclaration atd = null;
    for (int i = 0; i < list.size(); i++) {
      atd = (AbstractTypeDeclaration)list.get(i);
      int startLineNumber = root.getLineNumber(atd.getStartPosition());
      int endLineNumber = root.getLineNumber(atd.getStartPosition() + atd.getLength() - 1);
      if (lineNumber >= startLineNumber && lineNumber <= endLineNumber) {
        break;
      }
    }

    MethodDeclaration methodDeclaration = null;
    if (atd instanceof TypeDeclaration) {
      TypeDeclaration td = (TypeDeclaration)atd;
      MethodDeclaration[] mdArray = td.getMethods();
      for(MethodDeclaration md : mdArray) {
        //System.out.println(md);
        int startLineNumber = root.getLineNumber(md.getStartPosition());
        int endLineNumber = root.getLineNumber(md.getStartPosition() + md.getLength() - 1);
        if (lineNumber >= startLineNumber && lineNumber <= endLineNumber) {
          methodDeclaration = md;
          break;
        }
        //System.out.println(startLineNumber);
        //System.out.println(endLineNumber);
      }
    }

    Statement st = null;
    List statements = methodDeclaration.getBody().statements();
    for (int i = 0; i < statements.size(); i++) {
      Statement statement = (Statement)statements.get(i);
      int startLineNumber = root.getLineNumber(statement.getStartPosition());
      int endLineNumber = root.getLineNumber(statement.getStartPosition() + statement.getLength() - 1);
      if (lineNumber >= startLineNumber && lineNumber <= endLineNumber) {
        st = statement;
        break;
      }
    }
    VariableDeclarationStatement vds = (VariableDeclarationStatement)st;
    System.out.println(vds.getType());
    VariableDeclarationFragment vdf = (VariableDeclarationFragment)vds.fragments().get(0);
    System.out.println(vdf);
    Expression expression = vdf.getInitializer();
    System.out.println(expression);
    System.out.println(expression.getClass());
    ClassInstanceCreation cic = (ClassInstanceCreation)expression;
    ParameterizedType pt = (ParameterizedType)cic.getType();
    List l = pt.typeArguments();
    System.out.println(l.size());
    AST ast = pt.getAST();
    ASTRewrite rewrite = ASTRewrite.create(ast);
    l.add(ast.newSimpleType(ast.newName("zc")));
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
    System.out.println(root);
    //System.out.println(pt.typeArguments().get(0));

    /*
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