package org.mg8.pushr.droid;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Images.Thumbnails;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class PushPhotos extends ListActivity implements OnItemClickListener {
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
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
  }
}