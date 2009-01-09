package org.mg8.pushr.flickr;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import junit.framework.TestCase;

public class FlickrTest extends TestCase {
  private static final String API_KEY = "API_KEY",
      SHARED_SECRET = "SHARED_SECRET",
      AUTH_TOKEN = "AUTH_TOKEN";
  
  private Flickr flickr;
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    
    flickr = new Flickr(API_KEY, SHARED_SECRET, AUTH_TOKEN);
  }
  
  public void testSignature() throws Exception {
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("foo", "bar");
    params.put("x", "");
    params.put("integer", "123");
    
    // Generated manually using md5sum(1)
    final String expectedSig = "d65537be45d8f8c908405a03e3c24b0c";
    
    ByteArrayOutputStream sink = new ByteArrayOutputStream(); 
    flickr.signature(params).writeTo(sink);
    byte[] sigBytes = sink.toByteArray();
    String sig = new String(sigBytes);
    
    assertEquals(expectedSig, sig);
  }
}
