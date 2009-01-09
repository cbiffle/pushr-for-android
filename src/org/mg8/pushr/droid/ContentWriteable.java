package org.mg8.pushr.droid;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.mg8.pushr.flickr.MultipartStream;

import android.content.ContentResolver;
import android.net.Uri;

public class ContentWriteable implements MultipartStream.Writeable {
  private static final int CHUNK_SIZE = 1024;
  
  private final ImageStore imageStore;
  private final Uri contentUri;
  
  private ProgressListener progress = new ProgressListener() {
    @Override public void progressHasBeenMade(long bytes) {
      // I don't care!
    }};
  
  public ContentWriteable(ImageStore imageStore, Uri contentUri) {
    this.imageStore = imageStore;
    this.contentUri = contentUri;
  }

  @Override public void writeTo(OutputStream out) throws IOException {
    long size = imageStore.getImageSize(contentUri);
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

  public interface ProgressListener {
    void progressHasBeenMade(long bytes);
  }

  public void setProgressListener(ProgressListener progressListener) {
    progress = progressListener;
  }
}
