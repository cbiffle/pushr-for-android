package org.mg8.pushr.flickr;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class SimpleResponseParser {
  
  public static int checkForFailure(InputStream response) throws IOException,
      SAXException {
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    SAXParser parser;
    XMLReader reader;
    try {
      parser = parserFactory.newSAXParser();
      reader = parser.getXMLReader();
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    }
    
    ResponseHandler handler = new ResponseHandler();
    reader.setContentHandler(handler);
    reader.parse(new InputSource(response));
    
    return handler.getErrCode();
  }
  
  static class ResponseHandler implements ContentHandler {
    private int status;
    private int errCode = 0;
    private String errMessage;
   
    int getStatus() {
      return status;
    }
    
    int getErrCode() {
      return errCode;
    }
    
    String getErrMessage() {
      return errMessage;
    }
    
    @Override
    public void startElement(String uri, String localName, String name,
        Attributes atts) throws SAXException {
      if (localName.equals("rsp")) {
        String stat = atts.getValue("", "stat");
        if (stat.equals("ok")) {
          this.status = 0; 
        } else {
          this.status = -1;
        }
      } else if (localName.equals("err")) {
        errCode = Integer.valueOf(atts.getValue("", "code"));
        errMessage = atts.getValue("", "msg");
      }
    }

    // All the parsing hooks that we don't care about:
    @Override public void setDocumentLocator(Locator locator) { }
    @Override public void processingInstruction(String target, String data)
        throws SAXException { }
    @Override public void startDocument() throws SAXException { }
    @Override public void startPrefixMapping(String prefix, String uri)
        throws SAXException { }
    @Override public void characters(char[] ch, int start, int length)
        throws SAXException { }
    @Override public void ignorableWhitespace(char[] ch, int start, int length)
        throws SAXException { }
    @Override public void skippedEntity(String name) throws SAXException { }
    @Override public void endPrefixMapping(String prefix) throws SAXException { }
    @Override public void endElement(String uri, String localName, String name)
        throws SAXException { }
    @Override public void endDocument() throws SAXException { }
  }
}
