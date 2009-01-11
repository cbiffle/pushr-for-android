package org.mg8.pushr.droid.svc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.http.entity.mime.MIME;
import org.mg8.pushr.droid.svc.AndroidContentBody.ProgressListener;

public class AndroidContentBodyTest extends TestCase {
  private static final int LENGTH = 42;

  private static final String DISPLAY_NAME = "DISPLAY_NAME", MIME_TYPE = "MIME_TYPE";

  private InputStream stream;
  private AndroidContentBody body;
  
  @Override protected void setUp() throws Exception {
    super.setUp();

    // Test cases should overwrite this if they want data.
    stream = new ByteArrayInputStream(new byte[0]);
    
    body = new AndroidContentBody(DISPLAY_NAME, LENGTH, MIME_TYPE, new Openable() {
      @Override public InputStream open() throws IOException {
        return stream;
      }});
  }
  
  public void testSimpleGetters() {
    assertEquals(DISPLAY_NAME, body.getFilename());
    assertNull(body.getCharset());
    assertEquals(MIME.ENC_BINARY, body.getTransferEncoding());
  }
  
  public void testTransferWithoutProgress() throws Exception {
    // Not an even multiple of the chunk size:
    stream = new ByteArrayInputStream(new byte[4095]);
    
    ByteArrayOutputStream sink = new ByteArrayOutputStream();
    body.writeTo(sink, 0);
    
    assertEquals(4095, sink.toByteArray().length);
  }
  
  public void testTransferWithProgress() throws Exception {
    // Not an even multiple of the chunk size:
    stream = new ByteArrayInputStream(new byte[4095]);

    final List<Long> chunks = new ArrayList<Long>();
    body.setProgressListener(new ProgressListener() {
      @Override public void progressHasBeenMade(long bytes) {
        chunks.add(bytes);
      }});
    
    ByteArrayOutputStream sink = new ByteArrayOutputStream();
    body.writeTo(sink, 0);
    
    assertEquals(4095, sink.toByteArray().length);
    assertEquals(4, chunks.size());
    
    assertEquals(1024, chunks.get(0).longValue());
    assertEquals(1024, chunks.get(1).longValue());
    assertEquals(1024, chunks.get(2).longValue());
    assertEquals(1023, chunks.get(3).longValue());
  }
  
  public void testGetContentLength() {
    assertEquals(LENGTH, body.getContentLength());
  }
  
}
