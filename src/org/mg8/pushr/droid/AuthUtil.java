package org.mg8.pushr.droid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class AuthUtil {
  public static final String AUTH_PREFS = "auth_pref";
  public static final String PREF_TOKEN = "auth_token";
  public static final String PREF_FROB = "frob";
  
  public static final int REQUEST_CODE = 93218508;
  
  private AuthUtil() { }
  
  /**
   * Does basic (quick) checks of our Flickr authorization.  If it doesn't
   * seem legit, starts a sub-activity to authorize.
   * 
   * @param caller  caller that will be reactivated with the auth result
   *     if auth was not okay.
   * @return {@code true} if the caller should wait for {@code
   *     onActivityResult}, {@code false} if no update was required.
   */
  public static boolean updateAuthIfNeeded(Activity caller) {
    SharedPreferences prefs = caller.getSharedPreferences(AUTH_PREFS, 0);
    String token = prefs.getString(PREF_TOKEN, null);
    if (token == null) {
      caller.startActivityForResult(new Intent(caller, AuthorizePushr.class),
          REQUEST_CODE);
      return true;
    } else {
      return false;
    }
  }
}
