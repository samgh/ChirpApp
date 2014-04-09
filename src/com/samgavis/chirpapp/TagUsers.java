package com.samgavis.chirpapp;

import java.util.concurrent.ExecutionException;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import android.os.AsyncTask;

public class TagUsers {
	
	static final String TAG = "ChirpApp";
	
	public static String tagUsers(String tweet) {
		getResults("");
		return tweet;
	}
	
	private static String getResults(String s) {
		TagUsers tu = new TagUsers();
		try {
			ResponseList<User> response = tu.new GetResults().execute(s).get();
			return response.toString();
		} catch (InterruptedException e) {
			//Log.d(TAG, "Exception: " + e.toString());
		} catch (ExecutionException e) {
			//Log.d(TAG, "Exception: " + e.toString());
		}
		return null;
	}
	
	private class GetResults extends AsyncTask<String, Void, ResponseList<User>> {

		@Override
		protected ResponseList<User> doInBackground(String... strings) {
			try {
				ConfigurationBuilder builder=new ConfigurationBuilder();
	            builder.setApplicationOnlyAuthEnabled(true);

	            // setup
	            Twitter twitter = new TwitterFactory(builder.build()).getInstance();

	            // exercise & verify
	            twitter.setOAuthConsumer(Constants.TWITTER_CONSUMER_KEY, Constants.TWITTER_CONSUMER_SECRET);
	           // OAuth2Token token = twitter.getOAuth2Token();
	            twitter.getOAuth2Token();
	            ResponseList<twitter4j.Status> statuses = twitter.getUserTimeline("Twitter");
	            
	            //Log.d(TAG, statuses.toString());
	    
				String query = "twitter";
				//Log.d(TAG, "Query: " + query);
				ResponseList<User> result = twitter.searchUsers(query, 1);
				//Log.d(TAG, "testy testy test");
				//if (result == null) Log.d(TAG, "Results null");
				//Log.d(TAG, "Results: " + result.toString());
				return result;
			} catch (TwitterException e) {
				//Log.d(TAG, "Exception: " + e.toString());
			} catch (Exception e) {
				//Log.d(TAG, "Exception: " + e.toString());
			}
			return null;
		}
		
	}
}
