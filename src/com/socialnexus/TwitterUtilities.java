package com.socialnexus;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.webkit.WebView;
import android.widget.Toast;

public class TwitterUtilities
{
	/** Name to store the users access token */
	public static final String PREF_ACCESS_TOKEN = "accessToken";
	/** Name to store the users access token secret */
	public static final String PREF_ACCESS_TOKEN_SECRET = "accessTokenSecret";
	/** Consumer Key generated when you registered your app at https://dev.twitter.com/apps/ */
	private static final String CONSUMER_KEY = "h62uYdnyVRfCw7fgi1SRA";
	/** Consumer Secret generated when you registered your app at https://dev.twitter.com/apps/ */
	private static final String CONSUMER_SECRET = "TNud0gKhwJIgblVJ04h4Pe1Gj8AkhwHW2Fi32om5c";
	/**
	 * The url that Twitter will redirect to after a user log's in - this will be picked up by your app manifest and redirected into this activity
	 */
	public static final String CALLBACK_URL = "social-nexus-android:///";

	public static String twitterAccessToken = "";
	public static String twitterAccessSecret = "";

	public static Twitter mTwitter;
	/** The request token signifies the unique ID of the request you are sending to twitter */
	public static RequestToken mReqToken;

	public static SharedPreferences creds;
	private static Activity act;
	private static Context ctx;

	public TwitterUtilities(Activity activity, SharedPreferences prefs)
	{
		creds = prefs;
		act = activity;
		ctx = (Context) activity;

		mTwitter = new TwitterFactory().getInstance();
		mTwitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
	}

	public void loginTwitter()
	{
		try
		{
			mReqToken = mTwitter.getOAuthRequestToken(CALLBACK_URL);

			WebView twitterSite = new WebView(act);
			twitterSite.loadUrl(mReqToken.getAuthenticationURL());

			act.setContentView(twitterSite);
		}
		catch (TwitterException e)
		{
			Toast.makeText(ctx, "error: " + e, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Twitter has sent us back into our app</br> Within the intent it set back we have a 'key' we can use to authenticate the user
	 * 
	 * @param intent
	 */
	public void dealWithTwitterResponse(Intent intent)
	{
		// If the user has just logged in
		String oauthVerifier = intent.getData().getQueryParameter("oauth_verifier");

		authoriseNewUser(oauthVerifier);
	}

	/**
	 * Create an access token for this new user</br> Fill out the Twitter4j helper</br> And save these credentials so we can log the user straight in next time
	 * 
	 * @param oauthVerifier
	 */
	private void authoriseNewUser(String oauthVerifier)
	{
		try
		{
			AccessToken at = mTwitter.getOAuthAccessToken(mReqToken, oauthVerifier);
			mTwitter.setOAuthAccessToken(at);

			// stores access Token and Secret into Twitter Info for later use
			twitterAccessToken = at.getToken();
			twitterAccessSecret = at.getTokenSecret();

			creds.edit().putString(creds.getString("LoggedIn", null).concat("-twAccessToken"), twitterAccessToken).commit();
			creds.edit().putString(creds.getString("LoggedIn", null).concat("-twAccessSecret"), twitterAccessSecret).commit();

			Intent intent = act.getIntent();
			intent.removeExtra("com.socialnexus.twitter");
			// Set the content view back after we changed to a webview
			// act.setContentView(R.layout.main);

		}
		catch (TwitterException e)
		{
			Toast.makeText(ctx, "Twitter auth error x01, try again later", Toast.LENGTH_SHORT).show();
		}
	}
}
