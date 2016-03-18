package com.example.jidaphoto.activity;


import com.example.jidaphotos1_0.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_splash);
		//Handler x = new Handler();  
        //x.postDelayed(new splashhandler(), 1000);
		
		final Handler mhandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 1:
					Log.d("SplashActivity", "intohandlermag");
		            startActivity(new Intent(getApplication(),ActivityMain.class));  
		            SplashActivity.this.finish();  
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg=new Message();
				msg.what=1;
				mhandler.sendMessage(msg);
				Log.d("SplashActivity", "sendmsg");
			}
			
		}.start();
          
    }  
    class splashhandler implements Runnable{  
  
        public void run() {  
            startActivity(new Intent(getApplication(),ActivityMain.class));  
            SplashActivity.this.finish();  
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
