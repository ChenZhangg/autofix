package fdse.zc.gumtree.xml;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public class LineReader extends Reader{
  private Reader reader;
  private int currentPos = 0;

  private ArrayList<Integer> lines = new ArrayList<>(Arrays.asList(0));
  public LineReader(Reader reader){
    this.reader = reader;
  }
  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    int r = reader.read(cbuf, off, len);
    for (int i = 0; i < len; i ++)
        if (cbuf[off + i] == '\n')
            lines.add(currentPos + i);
    currentPos += len;
    return r;
  }
  @Override
  public void close() throws IOException {
    reader.close();
  }
}