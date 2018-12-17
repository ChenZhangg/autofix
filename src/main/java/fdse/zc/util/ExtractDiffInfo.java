package fdse.zc.util;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;

import fdse.zc.git.GitRepo;

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

  public void extractAll(String oldCommitSHA, String newCommitSHA) throws IOException, GitAPIException {
    if(gitRepo == null)
      return;
    ArrayList<DiffEntry> diffList = gitRepo.getChangedFiles(oldCommitSHA, newCommitSHA);
    for(DiffEntry diff : diffList) {
      if(true) {
        
      }
    }
  }
}