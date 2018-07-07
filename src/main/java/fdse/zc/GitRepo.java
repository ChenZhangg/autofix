package fdse.zc;

import java.io.File;
import java.io.IOException;

import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

public class GitRepo{
  private String repoPath;
  private Repository repository;
  public GitRepo(String repoPath){
    this.repoPath = repoPath;
    File repoDir = new File(repoPath);
    FileRepositoryBuilder builder = new FileRepositoryBuilder();
    try {
      repository = builder.setGitDir(repoDir).readEnvironment().findGitDir().build();
    } catch (IOException e) {
      System.out.println("Can not find repo: " + repoPath);
    }  
  }

  public char[] getFile(String commitSHA, String filePath){
    try(RevWalk walk = new RevWalk(repository)){
      String objectId = "4462b9831f3b003c224c20d5c5efa9304a2815fc";
      RevCommit commit = walk.parseCommit(ObjectId.fromString(objectId));
      RevTree tree = commit.getTree();
      TreeWalk treeWalk = TreeWalk.forPath(repository, "src/main/java/org/dynjs/runtime/GlobalObject.java", tree);
      byte[] data = repository.open(treeWalk.getObjectId(0)).getBytes();
    } catch (Exception e){

    }
  }
}