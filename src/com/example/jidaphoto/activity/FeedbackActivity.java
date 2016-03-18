package com.example.jidaphoto.activity;

import com.example.jidaphoto.secondui.Feedback;
import com.example.jidaphotos1_0.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

public class FeedbackActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		FragmentManager fm=getSupportFragmentManager();
		Fragment fg=new Feedback();
		fm.beginTransaction().add(R.id.feedbackcontainer,fg).commit();
          
    }  
    class splashhandler implements Runnable{  
  
        public void run() {  
            startActivity(new Intent(getApplication(),FeedbackActivity.class));  
            FeedbackActivity.this.finish();  
        }  
          
    } 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
