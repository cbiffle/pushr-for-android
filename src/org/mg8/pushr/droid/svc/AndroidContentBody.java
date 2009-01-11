package org.mg8.pushr.droid.svc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.james.mime4j.MimeException;

/**
 * An implementation of {@link ContentBody} that streams data from
 * an {@link InputStream} and sends progress reports as it does so.
 * 
 * @author Cliff L. Biffle
 */
public class AndroidContentBody extends AbstractContentBody {
  private static final int CHUNK_SIZE = 1024;
  
  private final String filename;
  private final long lengthInBytes;
  private final Openable stream;
  
  private ProgressListener progress = new ProgressListener() {
    @Override public void progressHasBeenMade(long bytes) {
      // I don't care!
    }};
  
  public AndroidContentBody(String filename, long lengthInBytes, String mimeType,
      Openable stream) {
    super(mimeType);
    this.filename = filename;
    this.lengthInBytes = lengthInBytes;
    this.stream = stream;
  }

  @Override public void writeTo(OutputStream out, int mode)
      throws IOException, MimeException {
    InputStream in = stream.open();
    try {
      byte[] chunk = new byte[CHUNK_SIZE];
      while (true) {
        int r = in.read(chunk);
        if (r < 0) break;
        
        out.write(chunk, 0, r);
        progress.progressHasBeenMade(r);
      }
    } finally {
      in.close();
    }
  }

  @Override
  public String getFilename() {
    return filename;
  }

  @Override public String getCharset() {
    return null; // legal for binary types
  }

  @Override public long getContentLength() {
    return lengthInBytes;
  }

  @Override public String getTransferEncoding() {
    return MIME.ENC_BINARY;
  }

  public void setProgressListener(ProgressListener progressListener) {
    progress = progressListener;
  }

  /**
   * Interface for callbacks interested in receiving progress reports.
   */
  public interface ProgressListener {
    /**
     * Indicates that another {@code bytes} of data have been sent.
     */
    void progressHasBeenMade(long bytes);
  }
}
