package fdse.zc.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;

import fdse.zc.git.GitRepo;
import fdse.zc.gumtree.Action;
import fdse.zc.gumtree.Update;
import fdse.zc.util.changes.Change;
import fdse.zc.util.changes.UpdateMethodName;

public class ExtractDiffInfo {
  private static ExtractDiffInfo extractDiffInfo;
  private GitRepo gitRepo;

  private ExtractDiffInfo() { }

  public static ExtractDiffInfo getInstance() {
    if(extractDiffInfo == null) {
      extractDiffInfo = new ExtractDiffInfo();
    }
    return extractDiffInfo;
  }

  public void setGitRepo(String repoPath) throws IOException {
    this.gitRepo = new GitRepo(repoPath);
  }

  public void setGitRepo(GitRepo gitRepo) throws IOException {
    this.gitRepo = gitRepo;
  }

  public void extractAll(String oldCommitSHA, String newCommitSHA) throws Exception {
    if(gitRepo == null)
      return;
    List<DiffEntry> diffList = gitRepo.getChangedFiles(oldCommitSHA, newCommitSHA);
    for(DiffEntry diff : diffList) {
      DiffEntry.ChangeType ct = diff.getChangeType();
      if(ct == DiffEntry.ChangeType.MODIFY) {

      } else if(ct == DiffEntry.ChangeType.RENAME) {
        if(diff.getOldPath().endsWith(".java") || diff.getNewPath().endsWith(".java")) {
          getFullClassName(oldCommitSHA, diff.getOldPath());
        }
      }
    }
  }

  public List<Change> diffFileBetweenCommit(String oldCommitSHA, String oldFilePath, String newCommitSHA, String newFilePath)
      throws Exception {
    if(gitRepo == null)
      return null;
    List<Change> changes = new ArrayList<>();
    char[] oldChars = gitRepo.getFileChars(oldCommitSHA, oldFilePath);
    char[] newChars = gitRepo.getFileChars(newCommitSHA, newFilePath);
    DiffJava diffJava = DiffJava.getInstance();
    List<Action> actions = diffJava.diffFile(oldChars, newChars);

    for(Action action : actions) {
      if(action instanceof Update) {
        changes.add(updateAction((Update)action, oldFilePath, newFilePath));
      }     
    }
    return changes;
  }

  private Change updateAction(Update action, String oldFilePath, String newFilePath) {
    Change c = null;
    ASTNode parentAstNode = action.getNode().getParent().getASTNode();
    if(parentAstNode instanceof MethodDeclaration) {
      MethodDeclaration mdParent = (MethodDeclaration)parentAstNode;
      if(mdParent.getName() == action.getNode().getASTNode()) {
        c = new UpdateMethodName(mdParent.getName().toString(), oldFilePath, action.getLabel(), newFilePath);
      } else {
        System.out.println("<<<<<<<<<<<<<<<<<<<<<");
      }
    }
    return c;
  }

  private String getFullClassName(String commitSHA, String filePath) throws Exception {
    char[] chars = gitRepo.getFileChars(commitSHA, filePath);
    ASTParser parser = ASTParser.newParser(AST.JLS10);
    parser.setKind(ASTParser.K_COMPILATION_UNIT);
    parser.setResolveBindings(true);
    parser.setEnvironment(null, null, null, true);
    Map<String, String> options = JavaCore.getOptions();
    options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_10);
    options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_10);
    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
    options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
    parser.setCompilerOptions(options);
    parser.setSource(chars);
    CompilationUnit astRoot = (CompilationUnit)parser.createAST(null);
    String pack = astRoot.getPackage().getName().getFullyQualifiedName();
    List list = astRoot.types();
    for(Object o : list) {
      AbstractTypeDeclaration atd = (AbstractTypeDeclaration)o;
      System.out.println(pack + "." + atd.getName().getFullyQualifiedName());
    }
    return null;
  }


}