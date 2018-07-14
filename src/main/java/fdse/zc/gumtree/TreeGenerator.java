package fdse.zc.gumtree;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import fdse.zc.gumtree.java.TreeContext;

public abstract class TreeGenerator {
  public TreeContext getTreeContextFromFile(String path) throws IOException {
    List<String> lines = Files.readAllLines(Paths.get(path));
    StringBuilder sb = new StringBuilder();
    for(String s:lines){
        sb.append(s + "\n");
    }
    char[] ca = sb.toString().toCharArray();
    TreeContext treeContext = getTreeContext(ca);
    return treeContext;
  }

  public abstract TreeContext getTreeContext(char[] charArray);
}