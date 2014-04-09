package com.samgavis.chirpapp;


public class ShortenText {
	
	static final String TAG = "ChirpApp";
	public static String shortenText(String s) {
		s = removeWhitespace(s);
		s = replacePhrases(s);
		s = replaceWords(s);
		return s;
	}
	
	private static String removeWhitespace(String s) {
		return s.trim().replaceAll("\\s+", " ");
	}
	
	private static String replacePhrases(String s) {
		//Log.d(TAG, "s: " + s);
		for (int i = 0; i < Data.phrases.length; i++) {
			s = s.replaceAll("(?i)"+Data.phrases[i], Data.newPhrases[i].toLowerCase());
		}
		return s;
	}
	
	private static String replaceWords(String s) {
		String[] words = StringOperations.split(s);
		for (int i = 0; i < words.length; i++) {
			//Log.d(TAG, "index: " + Integer.toString(contains(s)));
			int ind = contains(words[i].toLowerCase().trim());
			if (ind > -1) words[i] = Data.newWords[ind];
		}
		return StringOperations.combine(words);
	}
	
	private static int contains(String s) {
		for (int i = 0; i < Data.words.length; i++) {
			if (s.equals(Data.words[i])) {
				return i;
			}
		}
		return -1;
	}
}
