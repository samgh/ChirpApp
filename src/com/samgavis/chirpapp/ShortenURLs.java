package com.samgavis.chirpapp;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.services.urlshortener.Urlshortener;
import com.google.api.services.urlshortener.model.Url;

public class ShortenURLs {
	
	static final String TAG = "ChirpApp";
	
	public static String shortenURLs(String s) {
		return StringOperations.combine((replaceURLs(s)));
	}
	
	private static String[] replaceURLs(String s) {
		String[] substrings = StringOperations.split(s);
		for (int i = 0; i < substrings.length; i++) {
			//Log.d(TAG, "Substring: " + substrings[i]);
			String shortened;
			if (isURL(substrings[i])) {
				//Log.d(TAG, "URL");
				shortened = shorten(substrings[i]).replace("http://","");
				if (shortened.length() < substrings[i].length()) substrings[i] = shortened;
			}
		}
		return substrings;
	}
	
	private static boolean isURL(String s) {
		Pattern p = Pattern.compile("(((https?|ftp|file)://)?[-a-zA-Z0-9+&@#/%?=~_|!:,.;]+[.][-a-zA-Z0-9+&@#/%?=~_|!:,.;]+[.][-a-zA-Z0-9+&@#/%=~_|]+)|([-a-zA-Z0-9+&@#/%?=~_|!:,.;]+[.](com|org|net|biz|us|co|info|co.uk|de)[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*)");
		Matcher m = p.matcher(s);
		return m.matches();
	}
	
	private static String shorten(String s) {
		ShortenURLs surl = new ShortenURLs();
		try {
			return surl.new Shorten().execute(s).get();
		} catch (Exception e) {
			return s;
		}
	}
	
	private class Shorten extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... s) {
			Urlshortener.Builder builder = new Urlshortener.Builder (AndroidHttp.newCompatibleTransport(), AndroidJsonFactory.getDefaultInstance(), null);
			builder.setApplicationName("com.samgavis.chirpapp");
			Urlshortener urlshortener = builder.build();

			com.google.api.services.urlshortener.model.Url url = new Url();
			url.setLongUrl(s[0]);
			try {
				url = urlshortener.url().insert(url).execute();
				return url.getId();
			} catch (IOException e) {
				//Log.d(TAG, e.toString());
				return s[0];
			}
		}
	}
}
