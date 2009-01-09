package org.mg8.pushr.flickr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import junit.framework.TestCase;

public class MultipartStreamTest extends TestCase {
  private ByteArrayOutputStream sink;
  private MultipartStream stream;
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    
    sink = new ByteArrayOutputStream();
    stream = new MultipartStream(sink);
  }
  
  public void testMapWriting() throws Exception {
    Map<String, String> params = new TreeMap<String, String>();
    params.put("foo", "bar");
    params.put("x", "y");
    
    String expected
        = "--" + MultipartStream.MIME_BOUNDARY + "\r\n"
        + "Content-Disposition: form-data; name=\"foo\"\r\n\r\n"
        + "bar\r\n"
        + "--" + MultipartStream.MIME_BOUNDARY + "\r\n"
        + "Content-Disposition: form-data; name=\"x\"\r\n\r\n"
        + "y\r\n";
    byte[] expectedBytes = expected.getBytes();
    
    stream.writeMimeParts(params);
    stream.close();
    
    byte[] output = sink.toByteArray();
    
    assertEquals(expectedBytes.length, output.length);
    for (int i = 0; i < expectedBytes.length; i++) {
      assertEquals("Output must match fixture at index " + i,
          expectedBytes[i], output[i]);
    }
  }
}
