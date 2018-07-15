package fdse.zc.gumtree.java;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayDeque;
import java.util.Deque;

public class JdtVisitor extends ASTVisitor {
    private CompilationUnit compilationUnit = null;
    private TreeContext context = new TreeContext();
    private Deque<JavaTree> treeDeque = new ArrayDeque<>();

    public JdtVisitor(CompilationUnit compilationUnit){
      super();
      this.compilationUnit = compilationUnit;
    }

    public TreeContext getTreeContext() {
        return context;
    }

    @Override
    public void preVisit(ASTNode node) {
        int nodeTypeNumber = node.getNodeType();
        String nodeTypeName = node.getClass().getSimpleName();
        String nodeLabel = getLabel(node);
        int startPosition = node.getStartPosition();
        int length = node.getLength();
        int startLineNumber = compilationUnit.getLineNumber(startPosition);
        int endLineNumber = compilationUnit.getLineNumber(startPosition + length - 1);
        pushNode(nodeTypeNumber, nodeTypeName, nodeLabel, startPosition, length);
    }

    protected void pushNode(int nodeTypeNumber, String nodeTypeName, String nodeLabel, int startPosition, int length) {
        JavaTree t = context.createTreeNode(nodeTypeNumber, nodeTypeName, nodeLabel, startPosition, length);
        if (treeDeque.isEmpty())
            context.setRoot(t);
        else {
            JavaTree parent = treeDeque.peek();
            t.setParentAndUpdateChildren(parent);
        }
        treeDeque.push(t);
    }

    protected String getLabel(ASTNode n) {
        if (n instanceof Name) return ((Name) n).getFullyQualifiedName();
        if (n instanceof Type) return n.toString();
        if (n instanceof Modifier) return n.toString();
        if (n instanceof StringLiteral) return ((StringLiteral) n).getEscapedValue();
        if (n instanceof NumberLiteral) return ((NumberLiteral) n).getToken();
        if (n instanceof CharacterLiteral) return ((CharacterLiteral) n).getEscapedValue();
        if (n instanceof BooleanLiteral) return ((BooleanLiteral) n).toString();
        if (n instanceof InfixExpression) return ((InfixExpression) n).getOperator().toString();
        if (n instanceof PrefixExpression) return ((PrefixExpression) n).getOperator().toString();
        if (n instanceof PostfixExpression) return ((PostfixExpression) n).getOperator().toString();
        if (n instanceof Assignment) return ((Assignment) n).getOperator().toString();
        if (n instanceof TextElement) return n.toString();
        if (n instanceof TagElement) return ((TagElement) n).getTagName();

        return "";
    }
    @Override
    public boolean visit(TagElement e) {
        return true;
    }

    @Override
    public boolean visit(QualifiedName name) {
        return false;
    }

    @Override
    public void postVisit(ASTNode n) {
        treeDeque.pop();
    }
}
