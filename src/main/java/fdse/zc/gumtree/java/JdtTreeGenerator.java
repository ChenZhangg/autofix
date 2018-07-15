package fdse.zc.gumtree.java;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import fdse.zc.gumtree.TreeContext;
import fdse.zc.gumtree.TreeGenerator;

public class JdtTreeGenerator extends TreeGenerator {

	@Override
	public TreeContext getTreeContext(char[] charArray) {
    ASTParser parser = ASTParser.newParser(AST.JLS10);
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    Map<String, String> options = JavaCore.getOptions();
    options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_10);
    options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_10);
    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
    options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
    parser.setCompilerOptions(options);
    parser.setSource(charArray);
    CompilationUnit astRoot = (CompilationUnit)parser.createAST(null);
    JdtVisitor visitor = new JdtVisitor(astRoot);
    astRoot.accept(visitor);
    TreeContext treeContext = visitor.getTreeContext();
		return treeContext;
	}

}