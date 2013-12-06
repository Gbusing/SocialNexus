package com.socialnexus;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class RegisterActivity extends Activity
{
	private SharedPreferences creds;

	private EditText mEmailView;
	private EditText mPasswordView;
	private EditText mPasswordView2;

	private CheckBox facebookBox;
	private CheckBox twitterBox;
	private CheckBox gplusBox;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		creds = getSharedPreferences("Preferences", MODE_PRIVATE);

		mEmailView = (EditText) findViewById(R.id.registeremail);
		mPasswordView = (EditText) findViewById(R.id.registerpassword1);
		mPasswordView2 = (EditText) findViewById(R.id.registerpassword2);
		facebookBox = (CheckBox) findViewById(R.id.FacebookBox);
		twitterBox = (CheckBox) findViewById(R.id.TwitterBox);
		gplusBox = (CheckBox) findViewById(R.id.GPlusBox);

		// Populate fields from values passed by login page.
		mEmailView.setText(getIntent().getStringExtra("com.socialnexus.email"));
		mPasswordView.setText(getIntent().getStringExtra("com.socialnexus.password"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	protected void onNewIntent(Intent newintent)
	{
		super.onNewIntent(newintent);
		setIntent(newintent);
	}

	public void register(View view)
	{
		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);
		mPasswordView2.setError(null);

		// Store values at the time of the login attempt.
		String mEmail = mEmailView.getText().toString();
		String mPassword = mPasswordView.getText().toString();
		String mPassword2 = mPasswordView2.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword))
		{
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		}
		else if (mPassword.length() < 4)
		{
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}
		// Check for a valid password in the second box.
		if (TextUtils.isEmpty(mPassword2))
		{
			mPasswordView2.setError(getString(R.string.error_field_required));
			focusView = mPasswordView2;
			cancel = true;
		}
		else if (mPassword2.length() < 4)
		{
			mPasswordView2.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView2;
			cancel = true;
		}

		// Confirm that password match
		if (mPassword.compareTo(mPassword2) != 0)
		{
			mPasswordView.setError(getString(R.string.password_mismatch));
			mPasswordView2.setError(getString(R.string.password_mismatch));
			focusView = mPasswordView2;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail))
		{
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		}
		else if (!mEmail.contains("@"))
		{
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}
		else
		{
			creds.edit().putString("RecentEmail", mEmail).commit();
		}

		// There was an error; don't attempt login and focus the first
		// form field with an error.
		if (cancel)
		{
			focusView.requestFocus();
		}
		// There were no errors, proceed with registration.
		else
		{
			creds.edit().putString(mEmail, mPassword).commit();

			Intent intent = new Intent(this, SplashActivity.class);
			intent.putExtras(getIntent());
			intent.putExtra("com.socialnexus.facebookregister", facebookBox.isChecked());
			intent.putExtra("com.socialnexus.twitterregister", twitterBox.isChecked());
			intent.putExtra("com.socialnexus.gplusregister", gplusBox.isChecked());
			startActivity(intent);
		}
	}
}
