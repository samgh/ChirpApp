package com.samgavis.chirpapp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.content.Context;

import com.google.gson.Gson;

public class AppData {
	private static final String JSON_LOGGED_IN = "loggedin";
	private static final String JSON_OAUTH_TOKEN = "oauthtoken";
	private static final String JSON_OAUTH_TOKEN_SECRET = "oauthtokensecret";
	private static final String JSON_OAUTH_VERIFIER = "oauthverifier";
	private static final String JSON_REQUEST_TOKEN = "requesttoken";
	private static final String JSON_REQUEST_TOKEN_SECRET = "requestsecrettoken";
	private static final String JSON_ACCESS_TOKEN = "accesstoken";
	private static final String JSON_ACCESS_TOKEN_SECRET="accesstokensecret";
	private static final String JSON_SHORTEN_URLS = "shortenurls";
	private static final String JSON_SHORTEN_TEXT = "shortentext";
	private static final String JSON_TAG_PEOPLE = "tagpeople";
	private static final String JSON_HASHTAG = "hashtag";
	
	private boolean mIsTwitterLoggedIn = false;
	private String mOauthToken = "";
	private String mOauthTokenSecret = "";
	private String mOauthVerifier = "";
	private RequestToken mRequestToken = null;
	private AccessToken mAccessToken = null;
	
	private boolean mShortenURLs = true;
	private boolean mShortenText = true;
	private boolean mTagPeople = true;
	private boolean mHashtags = true;
	
	private Context mContext;
	private String mFilename = Constants.APP_DATA_FILENAME;
	
	public AppData(Context c) {
		mContext = c;
		JSONObject json = readFile();
		if (json != null) {
			try {
				mIsTwitterLoggedIn = (boolean) json.get(JSON_LOGGED_IN);
				mOauthToken = (String) json.get(JSON_OAUTH_TOKEN);
				mOauthTokenSecret = (String) json.get(JSON_OAUTH_TOKEN_SECRET);
				mOauthVerifier = (String) json.get(JSON_OAUTH_VERIFIER);
				mShortenURLs = (boolean) json.get(JSON_SHORTEN_URLS);
				mShortenText = (boolean) json.get(JSON_SHORTEN_TEXT);
				mTagPeople = (boolean) json.get(JSON_TAG_PEOPLE);
				mHashtags = (boolean) json.get(JSON_HASHTAG);
				String rt = (String) json.get(JSON_REQUEST_TOKEN);
				String rts = (String) json.getString(JSON_REQUEST_TOKEN_SECRET);
				if (rt != null && rts != null) {
					mRequestToken = new RequestToken(rt, rts);
				} else {
					mRequestToken = null;
				}
				String at = (String) json.get(JSON_ACCESS_TOKEN);
				String ats = (String) json.getString(JSON_ACCESS_TOKEN_SECRET);
				if (at != null && ats != null) {
					mAccessToken = new AccessToken(at, ats);
				} else {
					mAccessToken = null;
				}

			} catch (JSONException e) {
				// do nothing
			}
		}
	}
	
	public void saveAppData() {
		try {
			JSONObject json = new JSONObject();
			json.put(JSON_LOGGED_IN, mIsTwitterLoggedIn);
			json.put(JSON_OAUTH_TOKEN, mOauthToken);
			json.put(JSON_OAUTH_TOKEN_SECRET, mOauthTokenSecret);
			json.put(JSON_OAUTH_VERIFIER, mOauthVerifier);
			json.put(JSON_SHORTEN_URLS, mShortenURLs);
			json.put(JSON_SHORTEN_TEXT, mShortenText);
			json.put(JSON_TAG_PEOPLE, mTagPeople);
			json.put(JSON_HASHTAG, mHashtags);
			if (mRequestToken == null) {
				json.put(JSON_REQUEST_TOKEN,null);
				json.put(JSON_REQUEST_TOKEN_SECRET, null);
			} else {
				json.put(JSON_REQUEST_TOKEN, mRequestToken.getToken());
				json.put(JSON_REQUEST_TOKEN_SECRET, mRequestToken.getTokenSecret());
			}
			if (mAccessToken == null) {
				json.put(JSON_ACCESS_TOKEN, null);
				json.put(JSON_ACCESS_TOKEN_SECRET, null);
			} else {
				json.put(JSON_ACCESS_TOKEN, mAccessToken.getToken());
				json.put(JSON_ACCESS_TOKEN_SECRET, mAccessToken.getTokenSecret());
			}
			writeFile(json);
		} catch (JSONException e) {
			// do nothing
		}
	}
	
	private void writeFile(JSONObject json) {
		Writer writer = null;
		try {
			OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(json.toString());
		} catch (IOException e) {
			// do nothing
		} finally {
			try {
				if (writer != null) writer.close();
			} catch (IOException e) {
				// do nothing
			}
		}
	}
	
	private JSONObject readFile() {
		BufferedReader reader = null;
		try {
			InputStream in = mContext.openFileInput(mFilename);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				jsonString.append(line);
			}
			
			JSONObject json = (JSONObject) new JSONTokener(jsonString.toString()).nextValue();
			return json;
		} catch (FileNotFoundException e) {
			// do nothing
		} catch (JSONException e) {
			// do nothing
		} catch (IOException e) {
			// do nothing
		}
		return null;
	}

	public boolean isTwitterLoggedIn() {
		return mIsTwitterLoggedIn;
	}

	public void setIsTwitterLoggedIn(boolean isTwitterLoggedIn) {
		mIsTwitterLoggedIn = isTwitterLoggedIn;
	}

	public String getOauthToken() {
		return mOauthToken;
	}

	public void setOauthToken(String oauthToken) {
		mOauthToken = oauthToken;
	}

	public String getOauthTokenSecret() {
		return mOauthTokenSecret;
	}

	public void setOauthTokenSecret(String oauthTokenSecret) {
		mOauthTokenSecret = oauthTokenSecret;
	}

	public String getOauthVerifier() {
		return mOauthVerifier;
	}

	public void setOauthVerifier(String oauthVerifier) {
		mOauthVerifier = oauthVerifier;
	}

	public RequestToken getRequestToken() {
		return mRequestToken;
	}

	public void setRequestToken(RequestToken requestToken) {
		mRequestToken = requestToken;
	}
	
	public AccessToken getAccessToken() {
		return mAccessToken;
	}

	public void setAccessToken(AccessToken accessToken) {
		mAccessToken = accessToken;
	}

	public boolean isShortenURLs() {
		return mShortenURLs;
	}

	public void setShortenURLs(boolean shortenURLs) {
		mShortenURLs = shortenURLs;
	}

	public boolean isShortenText() {
		return mShortenText;
	}

	public void setShortenText(boolean shortenText) {
		mShortenText = shortenText;
	}

	public boolean isTagPeople() {
		return mTagPeople;
	}

	public void setTagPeople(boolean tagPeople) {
		mTagPeople = tagPeople;
	}

	public boolean isHashtags() {
		return mHashtags;
	}

	public void setHashtags(boolean hashtags) {
		mHashtags = hashtags;
	}

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context context) {
		mContext = context;
	}

	public String getFilename() {
		return mFilename;
	}

	public void setFilename(String filename) {
		mFilename = filename;
	}

}
