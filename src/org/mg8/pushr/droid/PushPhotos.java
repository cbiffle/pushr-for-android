package org.mg8.pushr.droid;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Presents a list of available camera photos and allows the user to push
 * some or all of them.
 *
 * <p>Note: does not actually push yet.
 *
 * <p>This is the application's main activity.
 *
 * @author Cliff L. Biffle
 */
public class PushPhotos extends ListActivity {
  private static final int MENU_ABOUT = 1;
  
  private Cursor cursor;
  private ListView listView;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    String[] cols = { Thumbnails._ID, Thumbnails.DATA, };
    cursor = managedQuery(Thumbnails.EXTERNAL_CONTENT_URI, cols, null, null, null);
    setListAdapter(new SimpleCursorAdapter(this,
        android.R.layout.simple_list_item_multiple_choice,
        cursor,
        new String[] { Thumbnails._ID },
        new int[] { android.R.id.text1 }));
    
    listView = (ListView) findViewById(android.R.id.list);
    
    listView.setItemsCanFocus(false);
    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    
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
  
  
  
}
