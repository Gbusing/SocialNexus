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
	private SharedPreferences creds;
	private Intent intent;
	private String saveduser = null;
	private String activeuser = null;
	private String recentemail = null;
	private String twToken = null;
	private String twSecret = null;

	Button loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		creds = getSharedPreferences("Preferences", MODE_PRIVATE);

		// Attempt to pull credentials from creds, and use it for active session
		saveduser = creds.getString("LoggedIn", null);
		activeuser = getIntent().getStringExtra("com.socialnexus.loggedin");
		recentemail = creds.getString("RecentEmail", "");

		if (saveduser != null)
		{
			intent = new Intent(this, MainActivity.class);
			intent.putExtras(getIntent());
			intent.putExtra("com.socialnexus.loggedin", saveduser);
		}
		// Direct to log in page if no login credentials available.
		else
		{
			intent = new Intent(this, LoginActivity.class);
			intent.putExtras(getIntent());
			intent.putExtra("com.socialnexus.recentemail", recentemail);
		}

		startActivity(intent);
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
		// Check for an application exit flag
		if (newintent.getBooleanExtra("com.socialnexus.exit", false))
		{
			finish();
			return;
		}

		super.onNewIntent(newintent);
		// Intent previntent = getIntent();
		setIntent(newintent);

		saveduser = creds.getString("LoggedIn", null);
		activeuser = getIntent().getStringExtra("com.socialnexus.loggedin");
		intent = new Intent(this, this.getClass());
		intent.putExtras(getIntent());

		// If no one is actively logged in...
		if (activeuser == null)
		{

			// Check persistent credential storage
			// If none available clear the program and close
			if (saveduser == null)
			{
				intent.putExtra("com.socialnexus.exit", true);
			}
			// Pass persistent credential storage to active session
			else
			{
				intent.putExtra("com.socialnexus.loggedin", saveduser);
			}

			startActivity(intent);
		}
		// Load auth keys
		else if ((twToken == null || twSecret == null) && creds.contains(activeuser.concat("-twAccessToken"))
				&& creds.contains(activeuser.concat("-twAccessSecret")))
		{
			twToken = creds.getString(activeuser.concat("-twAccessToken"), null);
			twSecret = creds.getString(activeuser.concat("-twAccessSecret"), null);

			intent.putExtra("com.socialnexus.".concat(activeuser.concat("-twAccessToken")), twToken);
			intent.putExtra("com.socialnexus.".concat(activeuser.concat("-twAccessSecret")), twSecret);
			startActivity(intent);
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

		intent = new Intent(this, MainActivity.class);
		intent.putExtras(getIntent());
		intent.putExtra("com.socialnexus.exit", true);
		startActivity(intent);
	}

	private void logout()
	{
		creds.edit().remove("LoggedIn").commit();

		intent = new Intent(this, MainActivity.class);
		intent.putExtras(getIntent());
		intent.putExtra("com.socialnexus.exit", true);
		startActivity(intent);
	}
}
