package org.mg8.pushr.flickr;

import java.util.HashMap;

import junit.framework.TestCase;

public class FlickrRestTest extends TestCase {
  private static final String API_KEY = "API_KEY",
      SHARED_SECRET = "SHARED_SECRET",
      AUTH_TOKEN = "AUTH_TOKEN";
  
  private FlickrRest flickr;
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    
    flickr = new FlickrRest(API_KEY, SHARED_SECRET, AUTH_TOKEN);
  }
  
  public void testSignature() throws Exception {
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("foo", "bar");
    params.put("x", "");
    params.put("integer", "123");
    
    // Generated manually using md5sum(1)
    final String expectedSig = "d65537be45d8f8c908405a03e3c24b0c";
    
    String sig = flickr.signature(params);
    
    assertEquals(expectedSig, sig);
  }
}
