package fdse.zc.gumtree.xml;

import java.io.IOException;
import java.io.Reader;

public class LineReader extends Reader{
  private Reader reader;
  public LineReader(Reader reader){
    this.reader = reader;
  }
  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    return 0;
  }
  @Override
  public void close() throws IOException {
    
  }
}