package fdse.zc.fix.expected;

import fdse.zc.git.GitRepo;

public class FixComma {
  public static void main(String[] args) throws Exception {
    String gitRepoPath = "/Users/zhangchen/projects/projectanalysis/orbit/.git";
    String commitSHA = "c1feb2623f3cd48b91f0418b306f6acd8f19cfb9";
    String filePath = "actors/runtime/src/main/java/cloud/orbit/actors/runtime/DefaultLocalObjectsCleaner.java";
    int lineNum = 170;
    FixComma.addComma(gitRepoPath, commitSHA, filePath, lineNum);
  }

  public static void addComma(String gitRepoPath, String commitSHA, String filePath, int wrongLineNum) throws Exception {
    GitRepo gitRepo = new GitRepo(gitRepoPath);
    String fileContent = gitRepo.getFileContent(commitSHA, filePath);
    System.out.print(fileContent);

  }
  
}