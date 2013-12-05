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

import com.socialnexus.R;

public class RegisterActivity extends Activity
{
	SharedPreferences creds;

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

		// Populate fields from values passed by login page.
		mEmailView = (EditText) findViewById(R.id.registeremail);
		mPasswordView = (EditText) findViewById(R.id.registerpassword1);
		mPasswordView2 = (EditText) findViewById(R.id.registerpassword2);
		mEmailView.setText(getIntent().getExtras().getString("com.socialnexus.email"));
		mPasswordView.setText(getIntent().getExtras().getString("com.socialnexus.password"));
		facebookBox = (CheckBox) findViewById(R.id.FacebookBox);
		twitterBox = (CheckBox) findViewById(R.id.TwitterBox);
		gplusBox = (CheckBox) findViewById(R.id.GPlusBox);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	public void next(View view)
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
		if (mPassword != mPassword2)
		{
			mPasswordView.setError(getString(R.string.password_mismatch));
			mPasswordView2.setError(getString(R.string.password_mismatch));
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

			if (facebookBox.isChecked())
			{
				Intent intent = new Intent(this, AddfacebookActivity.class);
				intent.putExtra("twitter", twitterBox.isChecked());
				intent.putExtra("gplus", gplusBox.isChecked());
				startActivity(intent);
			}
			else if (twitterBox.isChecked())
			{
				Intent intent = new Intent(this, AddtwitterActivity.class);
				intent.putExtra("gplus", gplusBox.isChecked());
				startActivity(intent);
			}
			else if (gplusBox.isChecked())
			{
				Intent intent = new Intent(this, AddgplusActivity.class);
				startActivity(intent);
			}
		}
	}

}
