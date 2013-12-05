package com.socialnexus;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import com.example.socialnexus.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

public class AddtwitterActivity extends Activity
{
	/** Twitter4j object */
	private Twitter mTwitter;
	/**
	 * The request token signifies the unique ID of the request you are sending
	 * to twitter
	 */
	private RequestToken mReqToken;
	/**
	 * The url that Twitter will redirect to after a user log's in - this will
	 * be picked up by your app manifest and redirected into this activity
	 */
	public static final String CALLBACK_URL = "social-nexus-android:///";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addtwitter);

		mTwitter = new TwitterFactory().getInstance();
		mTwitter.setOAuthConsumer(TwitterInfo.CONSUMER_KEY, TwitterInfo.CONSUMER_SECRET);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.addtwitter, menu);
		return true;
	}

	public void verify(View view)
	{

	}

	private void loginTwitter()
	{
		try
		{
			mReqToken = mTwitter.getOAuthRequestToken(CALLBACK_URL);

			WebView twitterSite = new WebView(this);
			twitterSite.loadUrl(mReqToken.getAuthenticationURL());
			setContentView(twitterSite);

		}
		catch (TwitterException e)
		{
			Toast.makeText(this, "error: " + e, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Catch when Twitter redirects back to our {@link CALLBACK_URL}</br> We use
	 * onNewIntent as in our manifest we have singleInstance="true" if we did
	 * not the getOAuthAccessToken() call would fail
	 */
	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		dealWithTwitterResponse(intent);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	/**
	 * Twitter has sent us back into our app</br> Within the intent it set back
	 * we have a 'key' we can use to authenticate the user
	 * 
	 * @param intent
	 */
	private void dealWithTwitterResponse(Intent intent)
	{
		Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith(CALLBACK_URL))
		{ // If the
			// user
			// has
			// just
			// logged
			// in
			String oauthVerifier = uri.getQueryParameter("oauth_verifier");

			authoriseNewUser(oauthVerifier);
		}

	}

	/**
	 * Create an access token for this new user</br> Fill out the Twitter4j
	 * helper</br> And save these credentials so we can log the user straight in
	 * next time
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
			TwitterInfo.twitterAccessToken = at.getToken();
			TwitterInfo.twitterAccessSecret = at.getTokenSecret();

			// Set the content view back after we changed to a webview
			setContentView(R.layout.main);

		}
		catch (TwitterException e)
		{
			Toast.makeText(this, "Twitter auth error x01, try again later", Toast.LENGTH_SHORT).show();
		}
	}
}
