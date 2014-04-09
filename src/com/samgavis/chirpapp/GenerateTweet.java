package com.samgavis.chirpapp;


public class GenerateTweet {

	static final String TAG = "ChirpApp";
	
	static final String HASHTAG_ENCODING = "%23";

	public static String generate(String tweet, int flags) {
		//Log.d(TAG, Integer.toString(flags));
		//Log.d(TAG, Integer.toString(flags & Constants.SHORT_FLAG));
		// Shorten text
		if ((flags & Constants.SHORT_FLAG) != 0b0000) {
			tweet = ShortenText.shortenText(tweet);
			//Log.d(TAG, tweet);
		}
		
		// Shorten URLs
		if ((flags & Constants.URL_FLAG) != 0b0000) {
			tweet = ShortenURLs.shortenURLs(tweet);
		}
		
		// Tag people
		if ((flags & Constants.TAG_FLAG) != 0b0000) {
			//tweet = TagUsers.tagUsers(tweet);
		}
		
		// Add relevant hashtags
		if ((flags & Constants.HASHTAG_FLAG) != 0b0000) {
	
		}
		
		return tweet;
	}
}
