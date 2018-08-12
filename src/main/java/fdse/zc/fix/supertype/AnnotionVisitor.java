package fdse.zc.fix.supertype;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NormalAnnotation;

public class AnnotionVisitor extends ASTVisitor {
  private CompilationUnit cu = null;
  private FieldDeclaration fieldDeclaration  = null;
  @Override
  public boolean visit(CompilationUnit node) {
    this.cu = node;
    return super.visit(node);
  }

  @Override
  public void preVisit(ASTNode node) {
    if(cu == null) return;
    int startLineNumber = cu.getLineNumber(node.getStartPosition());
    int endLineNumber = cu.getLineNumber(node.getStartPosition() + node.getLength() - 1);

    if(startLineNumber <= 45 && endLineNumber >= 45) {
      if(node instanceof FieldDeclaration){
        fieldDeclaration = (FieldDeclaration)node;
      }
    }
  }
  public FieldDeclaration getFieldDeclaration(){
    return fieldDeclaration;
  }

}