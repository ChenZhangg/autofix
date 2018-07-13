package fdse.zc.gumtree.xml;

import java.io.Reader;

public class LineReader extends Reader{
  private Reader reader;
  public LineReader(Reader reader){
    this.reader = reader;
  }
}