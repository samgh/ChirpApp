package com.samgavis.chirpapp;

public class StringOperations {
	public static String[] split(String s) {
		return s.split(" ");
	}
	
	public static String combine(String[] s) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length; i++) {
			sb.append(s[i] + " ");
		}
		return sb.toString();
	}
}
