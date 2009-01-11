package org.mg8.pushr.droid.svc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.http.entity.mime.MIME;
import org.mg8.pushr.droid.svc.AndroidContentBody.ProgressListener;

import android.net.Uri;

public class AndroidContentBodyTest extends TestCase {
  private static final int LENGTH = 42;

  private static final String DISPLAY_NAME = "DISPLAY_NAME", MIME_TYPE = "MIME_TYPE";
  
  private ImageStore imageStore;
  private Uri contentUri;
  private AndroidContentBody body;
  
  @Override protected void setUp() throws Exception {
    super.setUp();
    
    imageStore = new MockImageStore(); 
    contentUri = Uri.parse("foo://bar/baz");
    
    body = new AndroidContentBody(imageStore, contentUri, DISPLAY_NAME, MIME_TYPE);
  }
  
  public void testSimpleGetters() {
    assertEquals(DISPLAY_NAME, body.getFilename());
    assertNull(body.getCharset());
    assertEquals(MIME.ENC_BINARY, body.getTransferEncoding());
  }
  
  public void testTransferWithoutProgress() throws Exception {
    ByteArrayOutputStream sink = new ByteArrayOutputStream();
    body.writeTo(sink, 0);
    
    assertEquals(4095, sink.toByteArray().length);
  }
  
  public void testTransferWithProgress() throws Exception {
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
  
  static class MockImageStore extends ImageStore {

    public MockImageStore() {
      super(null);
    }

    @Override
    public InputStream openImage(Uri uri) throws FileNotFoundException {
      return new ByteArrayInputStream(new byte[4095]);
    }
    
    @Override public long getImageSize(Uri uri) {
      return LENGTH;
    }
    
  }
}
