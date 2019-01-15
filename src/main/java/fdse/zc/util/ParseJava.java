package fdse.zc.util;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class ParseJava {
  private static ParseJava parseJava;
  private ASTParser parser;
  private ParseJava() {
    ASTParser parser = ASTParser.newParser(AST.JLS10);
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    Map<String, String> options = JavaCore.getOptions();
    options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_10);
    options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_10);
    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
    options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
    parser.setCompilerOptions(options);
    this.parser = parser;
  }

  public static ParseJava getInstance() {
    if(parseJava == null) {
      parseJava = new ParseJava();
    }
    return parseJava;
  }

  public CompilationUnit pareChars(char[] chars) {
    parser.setSource(chars);
    CompilationUnit astRoot = (CompilationUnit)parser.createAST(null);
    return astRoot;
  }
}