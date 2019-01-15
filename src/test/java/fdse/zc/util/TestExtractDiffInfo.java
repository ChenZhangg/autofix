package fdse.zc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import fdse.zc.util.changes.Change;

public class TestExtractDiffInfo {

  @Test
  @Ignore
  public void mifosMobile434Test() throws Exception {
    
    ExtractDiffInfo edi = ExtractDiffInfo.getInstance();
    edi.setGitRepo(Paths.get(System.getProperty("user.dir"), "repositories", "mifos-mobile", ".git").toString());
    String oldCommitSHA = "3a59e016e7dbed2e4e64411379d55be705ca4c3c";
    String oldFilePath = "app/src/main/java/org/mifos/selfserviceapp/presenters/BeneficiaryApplicationPresenter.java";
    String newCommitSHA = "d13cf99566d2d58bede32a684e01a96eca90b121";
    String newFilePath = "app/src/main/java/org/mifos/selfserviceapp/presenters/BeneficiaryApplicationPresenter.java";
    List<Change> changes = edi.diffFileBetweenCommit(oldCommitSHA, oldFilePath, newCommitSHA, newFilePath);
    for(Change c : changes) {
      System.out.println(c);
    }
    //assertEquals(expected, actual);
    //DiffJava df = DiffJava.getInstance();
    //df.diffFile(df.readCharArrayFromFile("/Users/zhangchen/projects/autofix/files/A.java"), df.readCharArrayFromFile("/Users/zhangchen/projects/autofix/files/B.java"));
  }

  @Test
  public void naverPinpoint265Test() throws Exception {
    ExtractDiffInfo edi = ExtractDiffInfo.getInstance();
    edi.setGitRepo(Paths.get(System.getProperty("user.dir"), "repositories", "pinpoint", ".git").toString());
    String oldCommitSHA = "df57f1165eafa3ea79b7dee6b5916f17c5973c7b";
    String newCommitSHA = "15527bb65258b3c07bddd6952eb1c0dc8c91a133";
    edi.extractAll(oldCommitSHA, newCommitSHA);
    //assertEquals(expected, actual);
    //DiffJava df = DiffJava.getInstance();
    //df.diffFile(df.readCharArrayFromFile("/Users/zhangchen/projects/autofix/files/A.java"), df.readCharArrayFromFile("/Users/zhangchen/projects/autofix/files/B.java"));
  }


}
