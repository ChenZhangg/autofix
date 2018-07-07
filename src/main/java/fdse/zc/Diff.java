package fdse.zc;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import fdse.zc.gumtree.JdtVisitor;

public class Diff{
  public static void main(String[] args) throws IOException, GitAPIException {
    File repoDir = new File("/Users/zhangchen/projects/projectanalysis/dynjs/.git");
    FileRepositoryBuilder builder = new FileRepositoryBuilder();
    try(Repository repository = builder.setGitDir(repoDir).readEnvironment().findGitDir().build()){
      System.out.println("Having repository: " + repository.getDirectory());
      try (RevWalk walk = new RevWalk(repository)) {
        String objectId = "4462b9831f3b003c224c20d5c5efa9304a2815fc";
        RevCommit commit = walk.parseCommit(ObjectId.fromString(objectId));
        RevTree tree = commit.getTree();
        TreeWalk treeWalk = TreeWalk.forPath(repository, "src/main/java/org/dynjs/runtime/GlobalObject.java", tree);
        byte[] data = repository.open(treeWalk.getObjectId(0)).getBytes();
        //System.out.println(new String(data, "ISO-8859-1"));
      }
    }

    ASTParser parser = ASTParser.newParser(AST.JLS10);
    parser.serKind(ASTParser.K_COMPILATION_UNIT);
    Map<String, String> options = JavaCore.getOptions();
    options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_10);
    options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_10);
    options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_6);
    options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
    parser.setCompilerOptions(options);
    char[] charArray = new String(data, "ISO-8859-1").toCharArray();
    JdtVisitor visitor = new JdtVisitor();

  }
}