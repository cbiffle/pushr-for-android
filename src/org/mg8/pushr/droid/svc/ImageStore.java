package org.mg8.pushr.droid.svc;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.ImageColumns;

/**
 * Encapsulates most of our interactions with {@link ContentResolver}.
 * Mostly, this gives us a nice clean boundary for mocks.
 * 
 * @author Cliff L. Biffle
 */
public class ImageStore {
  private final ContentResolver resolver;
  
  private static final String[] COLUMNS = {
    ImageColumns.DISPLAY_NAME,
    ImageColumns.DESCRIPTION,
    ImageColumns.DATA
  };
  
  public ImageStore(ContentResolver resolver) {
    this.resolver = resolver;
  }
  
  public InputStream openImage(Uri uri) throws FileNotFoundException {
    return resolver.openInputStream(uri);
  }
  
  /**
   * Loads information on the specified {@code Uri} from the database.
   * 
   * <p>Currently only loads the DISPLAY_NAME and DESCRIPTION columns from
   * {@link ImageColumns}.
   */
  public Cursor getInfo(Uri uri) {
    return resolver.query(uri, COLUMNS, null, null, null);
  }
}