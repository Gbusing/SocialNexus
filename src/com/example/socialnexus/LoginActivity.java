package com.example.socialnexus;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Code to check if already logged in.
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
 
    public void login(View view)
    {
    	//Login stuff
    }
    
    public void register(View view)
    {
    	Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
