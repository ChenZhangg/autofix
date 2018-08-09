package fdse.zc.git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

public class GitRepo{
  private String repoPath;
  private Repository repository;
  public GitRepo(String repoPath) throws IOException{
    this.repoPath = repoPath;
    File repoDir = new File(repoPath);
    FileRepositoryBuilder builder = new FileRepositoryBuilder();
    try {
      repository = builder.setGitDir(repoDir).readEnvironment().findGitDir().build();
    } catch (IOException e) {
      System.out.println("Can not find repo: " + repoPath);
      throw e;
    }  
  }

  public String getRepoPath(){
    return repoPath;
  }

  public String getFileContent(String commitSHA, String filePath) throws Exception{
    String data = null;
    try(RevWalk walk = new RevWalk(repository)){
      String objectId = commitSHA;
      RevCommit commit = walk.parseCommit(ObjectId.fromString(objectId));
      RevTree tree = commit.getTree();
      TreeWalk treeWalk = TreeWalk.forPath(repository, filePath, tree);
      byte[] fileData = repository.open(treeWalk.getObjectId(0)).getBytes();
      data = new String(fileData, "ISO-8859-1");
      //data = new String(fileData, "utf-8").toCharArray();
    } catch (Exception e){
      System.out.println("Fail to get the file: " + filePath + " at commit" + commitSHA);
      throw e;
    }
    return data;
  }

  public char[] getChars(String commitSHA, String filePath) throws Exception{
    char[] data = getFileContent(commitSHA, filePath).toCharArray();
    return data;
  }

  public List<String> getFileList(String commitSHA) throws Exception{
    List<String> list = new ArrayList<>();
    RevWalk walk = new RevWalk(repository);
    RevCommit commit = walk.parseCommit(ObjectId.fromString(commitSHA));
    RevTree tree = commit.getTree();

    TreeWalk treeWalk = new TreeWalk(repository);
    treeWalk.addTree(tree);
    treeWalk.setRecursive(true);
    while(treeWalk.next()){
      String filePath = treeWalk.getPathString();
      if(filePath.endsWith(".java")){
        list.add(filePath);
      }
    }
    treeWalk.close();
    return list;
  }
}