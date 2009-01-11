package org.mg8.pushr.droid;

import org.mg8.pushr.flickr.FlickrApi;
import org.mg8.pushr.flickr.FlickrRest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AuthorizePushr extends Activity {
  public static final String AUTH_PREF = "auth_pref";
  public static final String PREF_TOKEN = "auth_token";
  
  private String apiKey;
  private String sharedSecret;
  
  private SharedPreferences prefs;
  private TextView info;
  private Button button;
  
  private String frob, token;
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.authorize);
    
    apiKey = getString(R.string.api_key);
    sharedSecret = getString(R.string.shared_secret);

    info = (TextView) findViewById(R.id.auth_info);
    button = (Button) findViewById(R.id.auth_button);
  }
  
  
  @Override protected void onResume() {
    super.onResume();

    prefs = getSharedPreferences(AUTH_PREF, 0);
    frob = prefs.getString(AuthUtil.PREF_FROB, null);
    token = prefs.getString(AuthUtil.PREF_TOKEN, null);
    
    if (frob != null && token == null) {
      attemptFrobExchange();
    }
    
    int infoText, buttonText;
    OnClickListener buttonListener;
    if (token != null) {
      infoText = R.string.auth_complete;
      buttonText = R.string.auth_complete_button;
      buttonListener = finishListener;
    } else if (frob != null) {
      infoText = R.string.auth_error;
      buttonText = R.string.auth_error_button;
      buttonListener = startListener;
    } else {
      infoText = R.string.auth_intro;
      buttonText = R.string.auth_intro_button;
      buttonListener = startListener;
    }
    
    info.setText(getText(infoText));
    button.setText(getText(buttonText));
    button.setOnClickListener(buttonListener);
  }

  private final OnClickListener startListener = new OnClickListener() {
    @Override public void onClick(View v) {
      FlickrRest rest = new FlickrRest(apiKey, sharedSecret, null);
      FlickrApi api = new FlickrApi(rest);
      String frob;
      try {
        frob = api.getFrob();
      } catch (Exception e) {
        Toast.makeText(AuthorizePushr.this, "Flickr call failed.", Toast.LENGTH_LONG).show();
        e.printStackTrace();
        return;
      }
      
      prefs.edit().putString("frob", frob).commit();
      
      String authUrl = api.getAuthorizationUrl(frob);
      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
    }};

  private final OnClickListener finishListener = new OnClickListener() {
    @Override public void onClick(View v) {
      setResult(RESULT_OK);
      finish();
    }};
  
  
  private void attemptFrobExchange() {
    FlickrRest rest = new FlickrRest(apiKey, sharedSecret, null);
    FlickrApi api = new FlickrApi(rest);
    try {
      token = api.getTokenForFrob(frob);
    } catch (Exception e) {
      Toast.makeText(AuthorizePushr.this, "Flickr call failed.", Toast.LENGTH_LONG).show();
      e.printStackTrace();
      return;
    }

    prefs.edit().putString(PREF_TOKEN, token).commit();
  }
  
}
