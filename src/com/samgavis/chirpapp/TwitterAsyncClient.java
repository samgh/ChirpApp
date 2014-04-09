package com.samgavis.chirpapp;

import java.util.concurrent.ExecutionException;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import android.os.AsyncTask;

public class TwitterAsyncClient {

	static final String TAG = "ChirpApp";
	
	private Twitter mTwitter;
	private User mUser;
	
	public TwitterAsyncClient(AppData appData) {
 		try {
			ConfigurationBuilder builder = new ConfigurationBuilder();
	        builder.setOAuthConsumerKey(Constants.TWITTER_CONSUMER_KEY);
	        builder.setOAuthConsumerSecret(Constants.TWITTER_CONSUMER_SECRET);
	    	builder.setOAuthAccessToken(appData.getAccessToken().getToken()) 
	        .setOAuthAccessTokenSecret(appData.getAccessToken().getTokenSecret()); 
	    	
	        twitter4j.conf.Configuration configuration = builder.build();
	        
	        TwitterFactory factory = new TwitterFactory(configuration);
	        mTwitter = factory.getInstance();
	        new GetUser().execute().get();
 		} catch (InterruptedException e) {
 			//Log.d(TAG, e.toString());
		} catch (ExecutionException e) {
			//Log.d(TAG, e.toString());
		}
        return;
	}
	
	public User getUser() {
		return mUser;
	}
	
	public ResponseList<User> getUsers(String s) {
		try {
			return new GetUsers().execute(s).get();
		} catch (Exception e) {
			//Log.d(TAG, "Exception: " + e.toString());
		}
		return null;
	}
	
	public String getUsername() {
		return mUser.getName();
	}
	
	public String getHandle() {
		return mUser.getScreenName();
	}
	
	private class GetUser extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... arg0) {
    		try {
    			mUser = mTwitter.verifyCredentials();
	    	} catch (TwitterException e) {
	    		//Log.d(TAG, "Exception: " + e.toString());
	    	} catch (Exception e) {
	    		//Log.d(TAG, "Exception: " + e.toString());
	    	}
			return null;
		}
		
	}
	
	private class GetUsers extends AsyncTask<String, Void, ResponseList<User>> {
		@Override
		protected ResponseList<User> doInBackground(String... strings) {
			try {
				String query = "twitter";
				//Log.d(TAG, "Query: " + query);
				ResponseList<User> result = mTwitter.searchUsers(query, 1);
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
