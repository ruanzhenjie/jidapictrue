package com.example.jidaphoto.activity;

import com.example.jidaphoto.secondui.fragmentphotomain;
import com.example.jidaphotos1_0.ImgControl;
import com.example.jidaphotos1_0.R;
import com.example.jidaphotos1_0.R.id;
import com.example.jidaphotos1_0.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class ActivityMain extends  FragmentActivity{
	//private String urlid="http://172.26.0.227/";
	//private String urlid="http://192.168.0.33/";
	private String urlid="http://119.29.108.252/";
	ImgControl mimgcontrol;
	private long exitTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		//mimgcontrol=new imgcontrol(urlid+"jidaphotos/getphoto.php", this);
		mimgcontrol=ImgControl.get(urlid+"jidaphotos/getphoto.php", ActivityMain.this);
		
		FragmentManager fm=getSupportFragmentManager();
		Fragment fragment=fm.findFragmentById(R.id.fragmentcontainer);
		
		if(null==fragment){
			//fragment=listfragmentphoto.NewInstance(urlid);
			fragment=fragmentphotomain.NewInstance(urlid);
			//fragment=fragmentthememain.NewInstance(urlid);
			fm.beginTransaction().add(R.id.fragmentcontainer, fragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		/*int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}*/
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stubif(keyCode == KeyEvent.KEYCODE_BACK)  
	       {    
	           exitBy2Click();      //调用双击退出函数  
	       }  
		return true;
		
	}
	
	
	void exitBy2Click(){
		if ((System.currentTimeMillis() - exitTime) > 2000)
        {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
        } else
        {
                this.finish();
        }
	}
	
}
