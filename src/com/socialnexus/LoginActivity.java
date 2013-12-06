package com.socialnexus;

import com.socialnexus.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as well.
 */
public class LoginActivity extends Activity
{
	SharedPreferences creds;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private TextView mLoginStatusMessageView;
	private CheckBox mStayLoggedIn;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		creds = getSharedPreferences("Preferences", MODE_PRIVATE);

		mStayLoggedIn = (CheckBox) findViewById(R.id.stayloggedBox);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(getIntent().getStringExtra("com.socialnexus.recentemail"));
		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
		{
			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
			{
				if (id == R.id.login || id == EditorInfo.IME_NULL)
				{
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		if (getIntent().getStringExtra("com.socialnexus.recentemail").length() == 0)
		{
			mEmailView.requestFocus();
		}
		else
		{
			mPasswordView.requestFocus();
		}

		findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				attemptLogin();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();

		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("com.socialnexus.exit", true);
		startActivity(intent);
	}

	/**
	 * Attempts to sign in or register the account specified by the login form. If there are form errors (invalid email, missing fields, etc.), the errors are
	 * presented and no actual login attempt is made.
	 */
	public void attemptLogin()
	{

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

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
			creds.edit().putString("RecentEmail", mEmail);
		}

		if (cancel)
		{
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		}
		else
		{
			Intent intent;

			// Perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);

			// Email has no associated login, jump to register page
			if (creds.getString(mEmail, null) == null)
			{
				intent = new Intent(this, RegisterActivity.class);
				intent.putExtras(getIntent());
				intent.putExtra("com.socialnexus.email", mEmail);
				intent.putExtra("com.socialnexus.password", mPassword);
				startActivity(intent);
			}
			// Wrong password
			else if (creds.getString(mEmail, null).compareTo(mPassword) != 0)
			{
				mPasswordView.setError(getString(R.string.wrong_password));
				mPasswordView.requestFocus();
			}
			else if (mStayLoggedIn.isChecked())
			{
				creds.edit().putString("LoggedIn", mEmail).commit();
				intent = new Intent(this, MainActivity.class);
				intent.putExtras(getIntent());
				intent.putExtra("com.socialnexus.loggedin", mEmail);
				startActivity(intent);
			}

		}
	}
}
