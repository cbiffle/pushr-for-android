package org.mg8.pushr.droid;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.james.mime4j.MimeException;

import android.net.Uri;

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

  public interface ProgressListener {
    void progressHasBeenMade(long bytes);
  }
}
