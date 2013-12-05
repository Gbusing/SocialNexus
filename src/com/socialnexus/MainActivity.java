package com.socialnexus;

import java.util.List;

import twitter4j.Status;
import twitter4j.TwitterException;

import com.socialnexus.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Toast;
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
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
