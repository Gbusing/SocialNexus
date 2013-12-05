package com.socialnexus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.socialnexus.R;

public class RegisterActivity extends Activity
{
	private EditText mEmailView;
	private EditText mPasswordView;

	private CheckBox facebookBox;
	private CheckBox twitterBox;
	private CheckBox gplusBox;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		// Populate fields from values passed by login page.
		mEmailView = (EditText) findViewById(R.id.registeremail);
		mPasswordView = (EditText) findViewById(R.id.registerpassword1);
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
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

}
