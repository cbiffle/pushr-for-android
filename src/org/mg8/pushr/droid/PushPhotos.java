package org.mg8.pushr.droid;

import java.util.ArrayList;
import java.util.List;

import org.mg8.pushr.droid.svc.IUploadService;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Lets the user push new photos.
 *
 * <p>Note: does not actually push yet.
 *
 * <p>This is the application's main activity.
 *
 * @author Cliff L. Biffle
 */
public class PushPhotos extends Activity {
  private static final int MENU_ABOUT = 1;
  
  private TextView newPhotos;
  private Button bigRed;
  
  private List<Uri> photoList;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    newPhotos = (TextView) findViewById(R.id.new_photos);
    
    bigRed = (Button) findViewById(R.id.big_red_button);
    bigRed.setOnClickListener(pushListener);
    
    AuthUtil.updateAuthIfNeeded(this);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == AuthUtil.REQUEST_CODE) {
      if (resultCode == RESULT_CANCELED) {
        finish(); // No Pushr for you.
      }
    }
  }
  
  @Override
  protected void onResume() {
    super.onResume();
    
    refreshPhotoList();
    
    newPhotos.setText(String.valueOf(photoList.size()));
    
    bigRed.setEnabled(!photoList.isEmpty());
  }

  private void refreshPhotoList() {
    photoList = new ArrayList<Uri>();
    
    long lastPush = getSharedPreferences("push", 0).getLong("timestamp", 0);
    
    String[] cols = { ImageColumns._ID };
    String[] args = { String.valueOf(lastPush) };
    Cursor c = managedQuery(Images.Media.EXTERNAL_CONTENT_URI, cols, 
        ImageColumns.DATE_TAKEN + " > ?", args, null);
    while (c.moveToNext()) {
      long id = c.getLong(c.getColumnIndexOrThrow(ImageColumns._ID));
      photoList.add(
          Uri.withAppendedPath(Images.Media.EXTERNAL_CONTENT_URI, String.valueOf(id)));
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, MENU_ABOUT, 0, R.string.about_menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case MENU_ABOUT:
      startActivity(new Intent(this, AboutPushr.class));
      return true;
    }
    return false;
  }
  
  private final OnClickListener pushListener = new OnClickListener() {
    @Override public void onClick(View v) {
      for (Uri uri : photoList) {
        startService(new Intent(IUploadService.class.getName())
            .putExtra(Intent.EXTRA_STREAM, uri));
      }
      
      getSharedPreferences("push", 0)
          .edit()
          .putLong("timestamp", System.currentTimeMillis())
          .commit();
      
      startActivity(new Intent(PushPhotos.this, CheckUploadStatus.class));
    }};
  
}
