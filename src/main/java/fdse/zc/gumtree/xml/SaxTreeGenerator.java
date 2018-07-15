package fdse.zc.gumtree.xml;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import fdse.zc.gumtree.TreeContext;
import fdse.zc.gumtree.TreeGenerator;

class SaxTreeGenerator extends TreeGenerator {

	@Override
	public TreeContext getTreeContext(char[] charArray) {
    Reader reader = new StringReader(String.valueOf(charArray));
    XMLReader xmlReader;
    try {
      xmlReader = XMLReaderFactory.createXMLReader();
      LineReader lineReader = new LineReader(reader);
      XMLHandle xmlHandle = new XMLHandle(lineReader);
      xmlReader.setContentHandler(xmlHandle);
      xmlReader.setErrorHandler(xmlHandle);
      xmlReader.parse(new InputSource(lineReader));
    } catch (SAXException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

		return null;
	}

}