package org.mg8.pushr.flickr;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.mg8.pushr.flickr.SimpleResponseParser.ResponseHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A wrapper around {@link FlickrRest}'s low-level interface that provides
 * convenient ways to call Flickr APIs.
 * 
 * @author Cliff L. Biffle
 */
public class FlickrApi {
  private static final Map<String, String> NO_PARAMS = Collections.emptyMap();

  private final FlickrRest flickr;
  
  public FlickrApi(FlickrRest flickr) {
    this.flickr = flickr;
  }
  
  public String getFrob() throws IOException, FlickrException {
    final StringBuilder frob = new StringBuilder();
    ResponseHandler handler = new ResponseHandler() {
      boolean inFrob = false;
      @Override public void startElement(String uri, String localName, String name,
          Attributes atts) throws SAXException {
        if (localName.equals("frob")) {
          inFrob = true;
        } else {
          super.startElement(uri, localName, name, atts);
        }
      }
      @Override public void characters(char[] ch, int start, int length)
          throws SAXException {
        if (inFrob) {
          frob.append(ch, start, length);
        } else {
          super.characters(ch, start, length);
        }
      }
      @Override public void endElement(String uri, String localName, String name)
          throws SAXException {
        if (localName.equals("frob")) {
          inFrob = false;
        } else {
          super.endElement(uri, localName, name);
        }
      }
      
    };
    flickr.makeApiCall("rest", "flickr.auth.getFrob", NO_PARAMS, handler);
    
    if (handler.getStatus() != 0) {
      throw new FlickrException(handler.getErrCode(), handler.getErrMessage());
    }
    
    return frob.toString().trim();
  }
  
  public String getAuthorizationUrl(String frob) {
    Map<String, String> params = new HashMap<String, String>();
    params.put("frob", frob);
    params.put("perms", "write");
    return flickr.getSignedUrl("http://flickr.com/services/auth/", params);
  }
  
  public String getTokenForFrob(String frob) throws IOException, FlickrException {
    final StringBuilder token = new StringBuilder();
    ResponseHandler handler = new ResponseHandler() {
      boolean inToken = false;
      @Override public void startElement(String uri, String localName, String name,
          Attributes atts) throws SAXException {
        if (localName.equals("token")) {
          inToken = true;
        } else {
          super.startElement(uri, localName, name, atts);
        }
      }
      @Override public void characters(char[] ch, int start, int length)
          throws SAXException {
        if (inToken) {
          token.append(ch, start, length);
        } else {
          super.characters(ch, start, length);
        }
      }
      @Override public void endElement(String uri, String localName, String name)
          throws SAXException {
        if (localName.equals("token")) {
          inToken = false;
        } else {
          super.endElement(uri, localName, name);
        }
      }
    };
    Map<String, String> params = new HashMap<String, String>();
    params.put("frob", frob);
    flickr.makeApiCall("rest", "flickr.auth.getToken", params, handler);
    
    if (handler.getStatus() != 0) {
      throw new FlickrException(handler.getErrCode(), handler.getErrMessage());
    }
    
    return token.toString().trim();
  }
}
