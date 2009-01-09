package org.mg8.pushr.droid;

import org.mg8.pushr.droid.svc.UploadService;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class PushSinglePhoto extends Activity {
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.upload_status);
    Uri contentUri = (Uri) getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
    
    startService(new Intent(this, UploadService.class)
        .putExtra(Intent.EXTRA_STREAM, contentUri));
  }

}
