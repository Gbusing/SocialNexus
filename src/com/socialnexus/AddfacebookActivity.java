package com.socialnexus;

import com.socialnexus.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class AddfacebookActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addfacebook);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.addfacebook, menu);
		return true;
	}

	public void verify(View view)
	{
		
	}
}