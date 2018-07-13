package fdse.zc.gumtree.xml;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandle extends DefaultHandler{
  private LineReader lineReader;
  public XMLHandle(LineReader lineReader){
    this.lineReader = lineReader;
  }
}