package fdse.zc.gumtree.xml;

import java.io.Reader;
import java.io.StringReader;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import fdse.zc.git.GitRepo;

public class DiffXML{

  public static void main(String[] args) throws Exception {
    DiffXML diff = new DiffXML();
    String repoPath = "/Users/zhangchen/projects/projectanalysis/dynjs/.git";
    String filePath = "src/main/java/org/dynjs/runtime/GlobalObject.java";
    String preCommit = "b2f956cc967377c3ed4d46f24bbf49f37adbca52";
    String nextCommit = "7fa2fd932ab7b5f0cb79f62cdedb8d051b2d2e37";
    diff.diffFile(repoPath, filePath, preCommit, nextCommit);
  }

  public void diffFile(String repoPath, String filePath, String oldCommit, String newCommit) throws Exception{
    GitRepo repo = new GitRepo(repoPath);
    char[] oldCharArray = repo.getChars(oldCommit, filePath);
    char[] newCharArray = repo.getChars(newCommit, filePath);
    Reader oldReader = new StringReader(String.valueOf(oldCharArray));
    XMLReader xmlReader = XMLReaderFactory.createXMLReader();
    LineReader lineReader = new LineReader(oldReader);
    XMLHandle xmlHandle = new XMLHandle(lineReader);
    xmlReader.setContentHandler(xmlHandle);
    xmlReader.setErrorHandler(xmlHandle);
    xmlReader.parse(new InputSource(lineReader));
  }

  
}