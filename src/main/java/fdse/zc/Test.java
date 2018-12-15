package fdse.zc;

import java.util.List;

import fdse.zc.fix.pack.Pack;

//import fdse.zc.fix.pack.Pack;

//import com.forgeessentials.api.APIRegistry; 

public class Test{
   public static void main(String[] args) throws Exception {
    DiffJava diff = new DiffJava();
    String repoPath = "/Users/zhangchen/projects/autofix/repositories/mifos-mobile/.git";
    String filePath = "app/src/main/java/org/mifos/selfserviceapp/presenters/BeneficiaryApplicationPresenter.java";
    String preCommit = "3a59e016e7dbed2e4e64411379d55be705ca4c3c";
    String nextCommit = "d13cf99566d2d58bede32a684e01a96eca90b121";
    diff.diffFile(repoPath, filePath, preCommit, nextCommit);
  }
}