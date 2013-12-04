package com.socialnexus;

import android.content.SharedPreferences;
import twitter4j.*;


public class TwitterInfo {	
	/** Name to store the users access token */
	public static final String PREF_ACCESS_TOKEN = "accessToken";
	/** Name to store the users access token secret */
	public static final String PREF_ACCESS_TOKEN_SECRET = "accessTokenSecret";
	/** Consumer Key generated when you registered your app at https://dev.twitter.com/apps/ */
	public static final String CONSUMER_KEY = "h62uYdnyVRfCw7fgi1SRA";
	/** Consumer Secret generated when you registered your app at https://dev.twitter.com/apps/  */
	public static final String CONSUMER_SECRET = "TNud0gKhwJIgblVJ04h4Pe1Gj8AkhwHW2Fi32om5c"; // XXX Encode in your app
	/** The url that Twitter will redirect to after a user log's in - this will be picked up by your app manifest and redirected into this activity */
	//public static final String CALLBACK_URL = "social-nexus-android:///";
	/** Preferences to store a logged in users credentials */
	public static SharedPreferences mPrefs;
	
	public static String twitterAccessToken = "";
	public static String twitterAccessSecret = "";
	
	/** Twitter4j object */
	//public static Twitter mTwitter;
	/** The request token signifies the unique ID of the request you are sending to twitter  */
	//public static RequestToken mReqToken;
	

}
