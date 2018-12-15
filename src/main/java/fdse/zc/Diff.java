package fdse.zc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

public class Diff{
  private String repoPath;
  private String preCommit;
  private String nextCommit;
  public Diff(String repoPath, String preCommit, String nextCommit){
    this.repoPath = repoPath;
    this.preCommit = preCommit;
    this.nextCommit = nextCommit;
  }

  public static void main(String[] args) throws Exception {
    Diff diff = new Diff("/Users/zhangchen/projects/autofix/repositories/mifos-mobile/.git", "3a59e016e7dbed2e4e64411379d55be705ca4c3c", "4462b9831f3b003c224c20d5c5efa9304a2815fc");
    diff.getFiles();
  }

  public ArrayList<String> getFiles() throws IOException, GitAPIException{
    ArrayList<String> list = new ArrayList<>();
    File repoDir = new File(repoPath);
    FileRepositoryBuilder builder = new FileRepositoryBuilder();
    try(Repository repository = builder.setGitDir(repoDir).readEnvironment().findGitDir().build()){
      try(Git git = new Git(repository)){
        List<DiffEntry> diffs = git.diff()
        .setOldTree(prepareTreeParser(repository, preCommit))
        .setNewTree(prepareTreeParser(repository, nextCommit))
        .call();
        System.out.println("Found: " + diffs.size() + " differences");
        for (DiffEntry diff : diffs) {
            System.out.println("Diff: " + diff.getChangeType() + ": " +
                    (diff.getOldPath().equals(diff.getNewPath()) ? diff.getNewPath() : diff.getOldPath() + " -> " + diff.getNewPath()));
        }
      }
    }
    return list;
  }
  private AbstractTreeIterator prepareTreeParser(Repository repository, String objectId) throws IOException {
    try (RevWalk walk = new RevWalk(repository)) {
        RevCommit commit = walk.parseCommit(repository.resolve(objectId));
        RevTree tree = walk.parseTree(commit.getTree().getId());

        CanonicalTreeParser treeParser = new CanonicalTreeParser();
        try (ObjectReader reader = repository.newObjectReader()) {
            treeParser.reset(reader, tree.getId());
        }

        walk.dispose();

        return treeParser;
    }
  }
}