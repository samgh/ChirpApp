package com.samgavis.chirpapp;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

public class LoginFragment extends Fragment {
	static final String TAG = "ChirpApp";
	
	private AppData appData;
	Button mLoginButton;
	ProgressBar mProgressBar;
//	private Twitter twitter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_login, parent, false);
		
		appData = new AppData(getActivity());
        
		mLoginButton = (Button)v.findViewById(R.id.login_button);
		mProgressBar = (ProgressBar)v.findViewById(R.id.login_progressbar);
		mProgressBar.setVisibility(View.INVISIBLE);

        if (appData.isTwitterLoggedIn()) {
        	//testLoggedIn();
        	Intent i = new Intent(getActivity(), ChirpAppActivity.class);
    		startActivity(i);
        } else {
	    	Uri uri = getActivity().getIntent().getData();
	        if (uri != null && uri.toString().startsWith(Constants.TWITTER_CALLBACK_URL)) {
	        	String verifier = uri.getQueryParameter(Constants.URL_TWITTER_OAUTH_VERIFIER);
	        	
	        	 try {
	                 // Get the access token
	                 new GetAccessToken().execute(verifier);

	                 //Log.e("Twitter OAuth Token", "> " + appData.getAccessToken().getToken());
	                
	                 testLoggedIn();
	        	 } catch (Exception e) {
	                 // Check log for login errors
	                 //Log.e("Twitter Login Error", "> " + e.toString());
	             }
	        }
        }
    	
        mLoginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ConnectionDetector cd = new ConnectionDetector(getActivity());
				if (!cd.isConnectingToInternet()) {
					AlertDialogManager.showAlertDialog(getActivity(), "No Internet Connection",
		                    "You must enable network connection to connect to Twitter.", false);
					return;
				}
				
				mProgressBar.setVisibility(View.VISIBLE);
				mLoginButton.setEnabled(false);
				new Login().execute();
			}
		});

		return v;
	}

	private void testLoggedIn() {
		Log.d(TAG, "test logged in");
 		ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(Constants.TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(Constants.TWITTER_CONSUMER_SECRET);
    	builder.setOAuthAccessToken(appData.getAccessToken().getToken()) 
        .setOAuthAccessTokenSecret(appData.getAccessToken().getTokenSecret()); 
    	
        twitter4j.conf.Configuration configuration = builder.build();
        
        TwitterFactory factory = new TwitterFactory(configuration);
        Twitter twitter = factory.getInstance();
        
    	//Log.d(TAG, "logged in");
    	LoginFragment lf = new LoginFragment();
    	boolean verified = false;
    	try {
    		verified = lf.new VerifyCredentials().execute(twitter).get();
    	} catch (Exception e) {
    		loginError();
    	}
    	if (verified) {
    		appData.setIsTwitterLoggedIn(true);
    		appData.saveAppData();
    		Intent i = new Intent(getActivity(), ChirpAppActivity.class);
    		startActivity(i);
    	} else {
    		loginError();
    	}
	}
	
	private void loginError() {
		Log.d(TAG, "login error");
	    AlertDialogManager alert = new AlertDialogManager();
	    alert.showAlertDialog(getActivity(), "Error connecting to Twitter", "Retry", false);
	    //Log.d(TAG, "error");
		appData.setIsTwitterLoggedIn(false);
	    appData.saveAppData();
	    new Login().execute();
	}
	
    private class Login extends AsyncTask<Void, Void, Void> {
    	protected Void doInBackground(Void... voids) {
    		Log.d(TAG, "logged in");
            try {
         		ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(Constants.TWITTER_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(Constants.TWITTER_CONSUMER_SECRET);
            	
                twitter4j.conf.Configuration configuration = builder.build();
                
                TwitterFactory factory = new TwitterFactory(configuration);
                Twitter twitter = factory.getInstance();
                
                RequestToken requestToken = twitter.getOAuthRequestToken(Constants.TWITTER_CALLBACK_URL);
                appData.setRequestToken(requestToken);
                appData.saveAppData();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL())));
            } catch (TwitterException e) {
            	e.printStackTrace();
            }
            return null;
    	}
    }
    
    private class GetAccessToken extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... verifiers) {
			Log.d(TAG, "get access token");
			try {
		 		ConfigurationBuilder builder = new ConfigurationBuilder();
		        builder.setOAuthConsumerKey(Constants.TWITTER_CONSUMER_KEY);
		        builder.setOAuthConsumerSecret(Constants.TWITTER_CONSUMER_SECRET);
		    	
		        twitter4j.conf.Configuration configuration = builder.build();
		        
		        TwitterFactory factory = new TwitterFactory(configuration);
		        Twitter twitter = factory.getInstance();
		        
				AccessToken accessToken = twitter.getOAuthAccessToken(appData.getRequestToken(), verifiers[0]);
				//Log.d(TAG, "token: " + accessToken.toString());
				appData.setAccessToken(accessToken);
				appData.setIsTwitterLoggedIn(true);
			} catch (TwitterException e) {
				//Log.d(TAG, "Exception2: " + e.toString());
			}
			return null;
		}
    }
    
    private class VerifyCredentials extends AsyncTask<Twitter, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Twitter...twitter) {
			Log.d(TAG, "verify credentials");
        	try {
        		User user = twitter[0].verifyCredentials();
        		//Log.d(TAG, user.getName());
        		return true;
        	} catch (TwitterException e) {
        		//Log.d(TAG, "Exception: " + e.toString());
        	} catch (Exception e) {
        		//Log.d(TAG, "Exception: " + e.toString());
        	}
        	return false;
		}
    }
    
    @Override 
    public void onPause() {
    	super.onPause();
    	appData.saveAppData();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
		mProgressBar.setVisibility(View.INVISIBLE);
		mLoginButton.setEnabled(true);
    	appData = new AppData(getActivity());
    }
}
