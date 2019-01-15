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
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;

import fdse.zc.git.GitRepo;
import fdse.zc.gumtree.Action;
import fdse.zc.gumtree.Delete;
import fdse.zc.gumtree.Insert;
import fdse.zc.gumtree.Move;
import fdse.zc.gumtree.Update;
import fdse.zc.util.changes.Change;
import fdse.zc.util.changes.UpdateClassName;
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
      if(ct == DiffEntry.ChangeType.ADD) {

      } else if(ct == DiffEntry.ChangeType.COPY) {

      } else if(ct == DiffEntry.ChangeType.DELETE) {

      } else if(ct == DiffEntry.ChangeType.MODIFY) {

      } else if(ct == DiffEntry.ChangeType.RENAME) {

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
    String oldPackageName = getPackageName(oldChars);
    String newPackageName = getPackageName(newChars);
    DiffJava diffJava = DiffJava.getInstance();
    List<Action> actions = diffJava.diffFile(oldChars, newChars);
    for(Action action : actions) {
      if(action instanceof Delete) {

      } else if(action instanceof Insert) {

      }  else if(action instanceof Move) {

      } else if(action instanceof Update) {
        changes.add(updateAction((Update)action, oldFilePath, oldPackageName, newFilePath, newPackageName));
      }     
    }
    return changes;
  }

  private Change updateAction(Update action, String oldFilePath, String oldPackageName, String newFilePath, String newPackageName) {
    Change c = null;
    ASTNode node = action.getNode().getASTNode();
    ASTNode parentNode = action.getNode().getParent().getASTNode();
    String label = action.getLabel();
    if(node instanceof SimpleName) {
      if(parentNode instanceof TypeDeclaration){
        c = new UpdateClassName(oldPackageName + node.toString(), oldFilePath, newPackageName + label, newFilePath);
      }
    } else if(node instanceof Modifier) {

    } else if(node instanceof QualifiedName) {
      if(parentNode instanceof PackageDeclaration){
        c = new UpdateClassName(node.toString(), oldFilePath, label, newFilePath);
      }
    }

    if(parentNode instanceof MethodDeclaration) {
      MethodDeclaration mdParent = (MethodDeclaration)parentNode;
      if(mdParent.getName() == action.getNode().getASTNode()) {
        c = new UpdateMethodName(mdParent.getName().toString(), oldFilePath, action.getLabel(), newFilePath);
      }
    } else if(parentNode instanceof TypeDeclaration  ) {
      //CompilationUnit cu = (CompilationUnit)parentAstNode;
      //if(mdParent.getName() == action.getNode().getASTNode()) {
    }
    return c;
  }

  private String getPackageName(String commitSHA, String filePath) throws Exception {
    char[] chars = gitRepo.getFileChars(commitSHA, filePath);
    return getPackageName(chars);
  }

  private String getPackageName(char[] chars) throws Exception {
    ParseJava pj = ParseJava.getInstance();
    CompilationUnit astRoot = pj.pareChars(chars);
    String pack = astRoot.getPackage().getName().getFullyQualifiedName();
    return pack;
  }

}