package org.mg8.pushr.droid.svc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.james.mime4j.MimeException;
import org.mg8.pushr.droid.svc.ImageStore;

import android.net.Uri;

/**
 * An implementation of {@link ContentBody} that pulls its data from
 * Android's ContentResolver and sends out progress reports.
 * 
 * @author Cliff L. Biffle
 */
public class AndroidContentBody extends AbstractContentBody {
  private static final int CHUNK_SIZE = 1024;
  
  private final ImageStore imageStore;
  private final Uri contentUri;
  private final String displayName;
  
  private ProgressListener progress = new ProgressListener() {
    @Override public void progressHasBeenMade(long bytes) {
      // I don't care!
    }};
  
  public AndroidContentBody(ImageStore imageStore, Uri contentUri,
      String displayName, String mimeType) {
    super(mimeType);
    this.imageStore = imageStore;
    this.contentUri = contentUri;
    this.displayName = displayName;
  }

  @Override public void writeTo(OutputStream out, int mode)
      throws IOException, MimeException {
    InputStream in = imageStore.openImage(contentUri);
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
    return displayName;
  }

  @Override public String getCharset() {
    return null; // legal for binary types
  }

  @Override public long getContentLength() {
    return imageStore.getImageSize(contentUri);
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
