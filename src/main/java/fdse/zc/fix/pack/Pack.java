package fdse.zc.fix.pack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fdse.zc.fix.ErrorRegexp;
import fdse.zc.git.GitRepo;


public class Pack{
  public static void main(String[] args) throws Exception {
    //naver/pinpoint  688
    String errorMessage = "[ERROR] /home/travis/build/naver/pinpoint/web/src/test/java/com/navercorp/pinpoint/web/service/BFSLinkSelectorTest.java:[19,42] error: package com.navercorp.pinpoint.common.trace does not exist";
    String gitRepoPath = "/Users/zhangchen/projects/projectanalysis/pinpoint/.git";
    String commitSHA = "102eb43eaaa84604f350ed221f139b309662f17d";
    Pack.run(errorMessage, gitRepoPath, commitSHA);
  }
  public static void run(String errorMessage, String gitRepoPath, String commitSHA) throws Exception {
    Pattern messagePattern = Pattern.compile(ErrorRegexp.package_doesnt_exist);
    Matcher messageMatcher = messagePattern.matcher(errorMessage);
    String fileName = null;
    int wrongLineNumber = -1;
    if(messageMatcher.find()){
      fileName = messageMatcher.group(2) + ".java";
      wrongLineNumber = Integer.parseInt(messageMatcher.group(4));
    }else{
      throw new Exception("Cannot match " + errorMessage);
    }
    GitRepo gitRepo = new GitRepo(gitRepoPath);
    //new ChangePackage(fileName, wrongLineNumber, gitRepo, commitSHA).run();
  }

  public static List<Integer> test(){
    return new ArrayList<Integer>();
  }

  public void test1() {
  }
}