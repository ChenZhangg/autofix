package fdse.zc.git;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.junit.Test;

public class TestGitRepo {
  @Test
  public void mifosMobile438Test() throws IOException, GitAPIException {
    String oldCommitSHA = "3a59e016e7db";
    String newCommitSHA = "d13cf99566d2";
    GitRepo gitRepo = new GitRepo(Paths.get(System.getProperty("user.dir"), "repositories", "mifos-mobile", ".git").toString());
    ArrayList<DiffEntry> list = gitRepo.getChangedFiles(oldCommitSHA, newCommitSHA);
    for(DiffEntry diff : list) {
        System.out.println("Diff: " + diff.getChangeType() + ": " +
                  (diff.getOldPath().equals(diff.getNewPath()) ? diff.getNewPath() : diff.getOldPath() + " -> " + diff.getNewPath()));
    }
  }
  @Test
  public void mifosMobile4Test() throws IOException, GitAPIException {
    String oldCommitSHA = "1c9879c90fc0";
    String newCommitSHA = "d42785ea2129";
    GitRepo gitRepo = new GitRepo(Paths.get(System.getProperty("user.dir"), "repositories", "mifos-mobile", ".git").toString());
    ArrayList<DiffEntry> list = gitRepo.getChangedFiles(oldCommitSHA, newCommitSHA);
    for(DiffEntry diff : list) {
        System.out.println("Diff: " + diff.getChangeType() + ": " +
                  (diff.getOldPath().equals(diff.getNewPath()) ? diff.getNewPath() : diff.getOldPath() + " -> " + diff.getNewPath()));
    }
  }


}