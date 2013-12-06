package com.socialnexus;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;

public class SplashActivity extends Activity
{
	private SharedPreferences creds;
	private TwitterUtilities twUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		creds = getSharedPreferences("Preferences", MODE_PRIVATE);
		twUtils = new TwitterUtilities(this, creds);

		Intent newintent = getIntent();
		if (newintent.getBooleanExtra("com.socialnexus.facebookregister", false))
		{
			newintent.removeExtra("com.socialnexus.facebookregister");
			startActivity(newintent);
		}
		else if (newintent.getBooleanExtra("com.socialnexus.twitterregister", false))
		{
			twUtils.loginTwitter();
			newintent.removeExtra("com.socialnexus.twitterregister");
			startActivity(newintent);
		}
		else if (newintent.getBooleanExtra("com.socialnexus.gplusregister", false))
		{
			newintent.removeExtra("com.gplusnexus.facebookregister");
			startActivity(newintent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	/**
	 * Catch when Twitter redirects back to our {@link CALLBACK_URL} We use onNewIntent as in our manifest we have singleInstance="true" if we did not the
	 * getOAuthAccessToken() call would fail
	 */
	@Override
	protected void onNewIntent(Intent newintent)
	{
		super.onNewIntent(newintent);
		Intent previntent = getIntent();
		setIntent(newintent);

		if (newintent.getData() != null && newintent.getData().toString().startsWith(TwitterUtilities.CALLBACK_URL))
		{
			twUtils.dealWithTwitterResponse(newintent);
			startActivity(previntent);
		}
		else if (newintent.getBooleanExtra("com.socialnexus.facebookregister", false))
		{
			newintent.removeExtra("com.socialnexus.facebookregister");
			startActivity(newintent);
		}
		else if (newintent.getBooleanExtra("com.socialnexus.twitterregister", false))
		{
			twUtils.loginTwitter();
			newintent.removeExtra("com.socialnexus.twitterregister");
			startActivity(newintent);
		}
		else if (newintent.getBooleanExtra("com.socialnexus.gplusregister", false))
		{
			newintent.removeExtra("com.gplusnexus.facebookregister");
			startActivity(newintent);
		}
	}
}
