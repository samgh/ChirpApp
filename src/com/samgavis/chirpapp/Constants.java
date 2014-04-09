package com.samgavis.chirpapp;

public class Constants {
	//	static final String HASHTAG_ENCODING = "%23";
	static final String PERCENT_ENCODING = "%25";
	
	public static final int SHORT_FLAG = 0b0001;
	public static final int URL_FLAG = 0b0010;
	public static final int TAG_FLAG = 0b0100;
	public static final int HASHTAG_FLAG = 0b1000;
	
	// Twitter Keys
    static String TWITTER_CONSUMER_KEY = "JDRcPi3oGrSQOkKfaAwdA";
    static String TWITTER_CONSUMER_SECRET = "gIW4APOgdedlrr7JCdwu1447m7BORjUVGFFNfcihh4";
 
    // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLoggedIn";
 
    static final String TWITTER_CALLBACK_URL = "oauth://chirpapp";
 
    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
    
    static final String APP_DATA_FILENAME = "appDataFile";
}
