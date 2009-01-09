package org.mg8.pushr.flickr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.SAXException;

public class Flickr {
  public static final String META_DESCRIPTION = "description";
  
  public static final String FLICKR_HOST = "api.flickr.com",
      FLICKR_SERVICE_URL = "http://api.flickr.com/services/",
      FLICKR_UPLOAD_URL = FLICKR_SERVICE_URL + "upload/";  
  private final String apiKey;
  private final String sharedSecret;
  private String authToken;
  
  public Flickr(String apiKey, String sharedSecret,
      String authToken) {
    this.apiKey = apiKey;
    this.sharedSecret = sharedSecret;
    this.authToken = authToken;
  }
  
  public void makeApiCall(String service, String method, Map<String, String> params) throws IOException {
    HashMap<String, String> allParams = new HashMap<String, String>(params);
    if (method != null) allParams.put("method", method);
    allParams.put("api_key", apiKey);
    if (authToken != null) allParams.put("auth_token", authToken);
    
    byte[] sig = signParams(allParams);
    StringBuilder hexString = new StringBuilder();
    for (byte bSigned : sig) {
      int b = bSigned & 0xFF;
      if (b < 0x10) hexString.append('0');
      hexString.append(Integer.toHexString(b));
    }
    allParams.put("api_sig", hexString.toString());
    
    URL url = new URL(FLICKR_SERVICE_URL + service + "/" + query(allParams));
    URLConnection connection = url.openConnection();
    HttpURLConnection httpCon = (HttpURLConnection) connection;
    httpCon.setInstanceFollowRedirects(true);
    httpCon.setRequestMethod("GET");
    
    httpCon.connect();
    System.out.println("Resp = " + httpCon.getResponseCode() + " " + httpCon.getResponseMessage());
    
    SAXParserFactory.newInstance();

    InputStream in = connection.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    String line;
    while ((line = reader.readLine()) != null) {
      System.out.println("Server sez: " + line);
    }
    reader.close();

  }
  
  private String query(HashMap<String, String> allParams) {
    boolean first = true;
    StringBuilder query = new StringBuilder("?");
    for (Map.Entry<String, String> entry : allParams.entrySet()) {
      if (!first) query.append('&');
      query.append(entry.getKey());
      query.append('=');
      query.append(entry.getValue());
      first = false;
    }
    return query.toString();
  }

  public void pushPhoto(String name, ContentBody photo, Map<String, String> meta) throws IOException, FlickrException {
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("api_key", apiKey);
    params.put("auth_token", authToken);
    params.putAll(meta);
    params.put("api_sig", signature(params));
   
    MultipartEntity body = new MultipartEntity();
    for (Map.Entry<String, String> entry : params.entrySet()) {
      body.addPart(entry.getKey(), new StringBody(entry.getValue()));
    }
    
    body.addPart("photo", photo);
    
    HttpClient http = new DefaultHttpClient();
    HttpPost post = new HttpPost(FLICKR_UPLOAD_URL);
    post.setEntity(body);
    HttpResponse response = http.execute(post);
    HttpEntity responseEntity = response.getEntity();

    InputStream in = responseEntity.getContent();
    try {
      int code = SimpleResponseParser.checkForFailure(in);
      if (code != 0) {
        throw new FlickrException(code, "err " + code);
      }
    } catch (SAXException e) {
      // Treat as failure.
      throw new FlickrException("Could not parse response.", e);
    }
  }

  String signature(Map<String, String> parameters) {
    final byte[] result = signParams(parameters);
    StringBuilder hex = new StringBuilder();
    for (byte bSigned : result) {
      int b = bSigned & 0xFF;
      if (b < 0x10) {
        hex.append('0');
      }
      hex.append(Integer.toHexString(b));
    }
    return hex.toString();
  }

  private byte[] signParams(Map<String, String> parameters)
      throws AssertionError {
    ArrayList<String> sortedKeys = new ArrayList<String>(parameters.keySet());
    Collections.sort(sortedKeys);
    
    StringBuilder toSign = new StringBuilder(sharedSecret);
    for (int i = 0; i < sortedKeys.size(); i++) {
      String key = sortedKeys.get(i);
      String value = parameters.get(key);
      toSign.append(key);
      toSign.append(value);
    }
    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      throw new AssertionError("What kind of backwater platform lacks MD5?!");
    }
    final byte[] result = digest.digest(toSign.toString().getBytes());
    return result;
  }
}
