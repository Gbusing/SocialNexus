package com.socialnexus;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

/*
 * private void viewTimeline() {
 try {
 //getHomeTimeline gets the 20 most recent posts i believe
 List<Status> statuses = mTwitter.getHomeTimeline();
 for (Status status : statuses) {
 //mNewsfeed is a label, which i guess why this only shows one tweet 
 //changing it to add to a text field would probably allow to see more
 mNewsfeed.setText(status.getUser().getName() + ":" +
 status.getText());
 }

 } catch (TwitterException e) {
 Toast.makeText(this, "error: " + e, Toast.LENGTH_SHORT).show();
 }
 }
 */
public class MainActivity extends Activity
{
	SharedPreferences creds;

	Button loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		creds = getSharedPreferences("Preferences", MODE_PRIVATE);

		// Attempt to pull credentials from creds, and use it for active session
		String saveduser = creds.getString("LoggedIn", null);
		if (saveduser != null)
		{
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("com.socialnexus.loggedin", creds.getString("LoggedIn", null));
			startActivity(intent);
		}
		// Direct to log in page if no login credentials available.
		else
		{
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onNewIntent(Intent newintent)
	{
		setIntent(newintent);

		// Check for an application exit flag
		if (newintent.getBooleanExtra("com.socialnexus.exit", false))
		{
			finish();
			return;
		}

		String activeuser = newintent.getStringExtra("com.socialnexus.loggedin");
		String saveduser = creds.getString("LoggedIn", null);

		// If no one is actively logged in...
		if (activeuser == null)
		{
			// Check persistent credential storage
			// If none available clear the program and close
			if (saveduser == null)
			{
				Intent intent = new Intent(this, this.getClass());
				intent.putExtras(getIntent().getExtras());
				intent.putExtra("com.socialnexus.exit", true);
				startActivity(intent);
			}
			// Pass persistent credential storage to active session
			else
			{
				Intent intent = new Intent(this, MainActivity.class);
				intent.putExtra("com.socialnexus.loggedin", saveduser);
				startActivity(intent);
			}
		}
		// Load auth keys
		else
		{
			Boolean updated = false;

			Intent intent = new Intent(this, this.getClass());
			// Save previously stored keys.
			intent.putExtras(getIntent().getExtras());

			String twToken = creds.getString(activeuser.concat("-twAccessToken"), null);
			String twSecret = creds.getString(activeuser.concat("-twAccessSecret"), null);

			if (twToken != null && twSecret != null)
			{
				intent.putExtra("com.socialnexus.".concat(activeuser.concat("-twAccessToken")), twToken);
				intent.putExtra("com.socialnexus.".concat(activeuser.concat("-twAccessSecret")), twSecret);
				updated = true;
			}

			if (updated)
			{
				startActivity(intent);
			}

		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle item selection
		switch (item.getItemId())
		{
		case R.id.action_logout:
			logout();
			return true;
			// case R.id.action_settings:
			// // showSettings();
			// return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();

		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtras(getIntent().getExtras());
		intent.putExtra("com.socialnexus.exit", true);
		startActivity(intent);
	}

	private void logout()
	{
		creds.edit().remove("LoggedIn").commit();
		finish();
	}
}
