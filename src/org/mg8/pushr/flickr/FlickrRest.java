package org.mg8.pushr.flickr;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * A simple implementation of Flickr's REST APIs.
 * 
 * <p>There are two main methods here, {@link #makeApiCall} and
 * {@link #pushPhoto}.  Use {@code makeApiCall} for calling any
 * of Flickr's REST methods that don't require a file upload;
 * use {@code pushPhoto} to upload a file.
 * 
 * @author Cliff L. Biffle
 */
public class FlickrRest {
  /**
   * Map key for callers to use when providing a description
   * for uploaded photos.
   */
  public static final String META_DESCRIPTION = "description";
  
  private static final String META_API_KEY = "api_key",
      META_API_SIG = "api_sig",
      META_AUTH_TOKEN = "auth_token",
      PHOTO_KEY = "photo";
  
  private static final String FLICKR_SERVICE_URL = "http://api.flickr.com/services/",
      FLICKR_UPLOAD_URL = FLICKR_SERVICE_URL + "upload/";
  
  private final String apiKey;
  private final String sharedSecret;
  private String authToken;
  
  public FlickrRest(String apiKey, String sharedSecret,
      String authToken) {
    this.apiKey = apiKey;
    this.sharedSecret = sharedSecret;
    this.authToken = authToken;
  }
  
  public String getSignedUrl(String base, Map<String, String> params) {
    HashMap<String, String> allParams = new HashMap<String, String>(params);
    allParams.put(META_API_KEY, apiKey);
    if (authToken != null) allParams.put(META_AUTH_TOKEN, authToken);
    
    byte[] sig = signParams(allParams);
    StringBuilder hexString = new StringBuilder();
    for (byte bSigned : sig) {
      int b = bSigned & 0xFF;
      if (b < 0x10) hexString.append('0');
      hexString.append(Integer.toHexString(b));
    }
    allParams.put(META_API_SIG, hexString.toString());
    
    System.out.println(base + query(allParams));
    return base + query(allParams);
  }
  
  public void makeApiCall(String service, String method, Map<String, String> params, 
      ContentHandler responseHandler) throws IOException, FlickrException {
    HashMap<String, String> allParams = new HashMap<String, String>(params);
    if (method != null) allParams.put("method", method);
    
    URL url = new URL(getSignedUrl(FLICKR_SERVICE_URL + service, allParams));
    URLConnection connection = url.openConnection();
    HttpURLConnection httpCon = (HttpURLConnection) connection;
    httpCon.setInstanceFollowRedirects(true);
    httpCon.setRequestMethod("GET");
    
    httpCon.connect();
    
    SAXParserFactory parserFactory = SAXParserFactory.newInstance();
    SAXParser parser;
    XMLReader reader;
    try {
      parser = parserFactory.newSAXParser();
      reader = parser.getXMLReader();
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    } catch (SAXException e) {
      throw new RuntimeException(e);
    }
    
    reader.setContentHandler(responseHandler);
    try {
      reader.parse(new InputSource(httpCon.getInputStream()));
    } catch (SAXException e) {
      throw new FlickrException("Unexpected error parsing XML", e);
    }
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
    params.put(META_API_KEY, apiKey);
    params.put(META_AUTH_TOKEN, authToken);
    params.putAll(meta);
    params.put(META_API_SIG, signature(params));
   
    MultipartEntity body = new MultipartEntity();
    for (Map.Entry<String, String> entry : params.entrySet()) {
      body.addPart(entry.getKey(), new StringBody(entry.getValue()));
    }
    
    body.addPart(PHOTO_KEY, photo);
    
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
