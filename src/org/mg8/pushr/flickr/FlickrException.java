package org.mg8.pushr.flickr;

public class FlickrException extends Exception {
  private final int errCode;
  
  public FlickrException(String message, Throwable cause) {
    super(message, cause);
    this.errCode = -1;
  }
  
  public FlickrException(int errCode, String message) {
    super(message);
    this.errCode = errCode;
  }
  
  public int getErrCode() {
    return errCode;
  }
}
