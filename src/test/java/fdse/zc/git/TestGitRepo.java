package fdse.zc.git;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.Test;

public class TestGitRepo {
  @Test
  public void mifosMobile438Test() throws IOException, GitAPIException {
    String oldCommitSHA = "3a59e016e7db";
    String newCommitSHA = "d13cf99566d2";
    GitRepo gitRepo = new GitRepo(Paths.get(System.getProperty("user.dir"), "repositories", "mifos-mobile", ".git").toString());
    ArrayList<String> list = gitRepo.getChangedFiles(oldCommitSHA, newCommitSHA);
    for(String s : list) {
      System.out.println(s);
    }
  }
}