package org.mg8.pushr.flickr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentResolver;
import android.net.Uri;

public class MultipartStream {
  public static final String MIME_BOUNDARY = "----16c17a9ea1d7b327e7489190e394d411----",
      CONTENT_TYPE = "multipart/form-data; boundary=" + MIME_BOUNDARY;
  
  private final OutputStream con;
  
  public MultipartStream(OutputStream con) {
    this.con = con;
  }
  
  public void writeMimeParts(Map<String, String> pairs) throws IOException {
    for (Map.Entry<String, String> entry : pairs.entrySet()) {
      writeMimePart(entry.getKey(), string(entry.getValue()));
    }
  }
  
  public void writeMimePart(String name, Writeable w) throws IOException {
    writeMimePart(name, w, "");
  }
  
  public void writeMimePart(String name, Writeable w, String extras) throws IOException {
    if (extras.length() > 0) extras = "; " + extras;
    
    con.write(("--" + MIME_BOUNDARY + "\r\n").getBytes());
    con.write(
        String.format("Content-Disposition: form-data; name=\"%s\"%s\r\n\r\n", name, extras).getBytes());
    w.writeTo(con);
    con.write("\r\n".getBytes());
  }
  
  public void close() throws IOException {
    con.write(("--" + MIME_BOUNDARY + "\r\n").getBytes());
    con.write("--\r\n".getBytes());
    con.close();
  }
  
  public interface Writeable {
    void writeTo(OutputStream out) throws IOException;
  }

  public static Writeable string(final String value) {
    return bytes(value.getBytes());
  }
  
  public static Writeable bytes(final byte[] bytes) {
    return new Writeable() {
      @Override public void writeTo(OutputStream out) throws IOException {
        out.write(bytes);
      }};
  }
  
}
