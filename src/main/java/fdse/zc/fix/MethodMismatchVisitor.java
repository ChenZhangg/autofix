package fdse.zc.fix;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class MethodMismatchVisitor extends ASTVisitor {
  private CompilationUnit cu = null;
  private MethodInvocation methodInvocation = null;
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
    if(startLineNumber == 1387 && node instanceof MethodInvocation) {
      this.methodInvocation = (MethodInvocation)node;
    }
  }

  public MethodInvocation getMethodInvocation() {
    return methodInvocation;
  }
}