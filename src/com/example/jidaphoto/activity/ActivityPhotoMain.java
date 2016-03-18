package com.example.jidaphoto.activity;

import com.example.jidaphotos1_0.ItemAlbume;
import com.example.jidaphotos1_0.ItemPhoto;
import com.example.jidaphotos1_0.R;
import com.example.jidaphotos1_0.photoTopFragment;
import com.example.jidaphotos1_0.R.id;
import com.example.jidaphotos1_0.R.layout;
import com.example.jidaphotos1_0.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class ActivityPhotoMain extends FragmentActivity{
	private String urlid="http://119.29.108.252/";
	//private String urlid="http://192.168.0.33/";
	//private String urlid="http://172.26.0.227/";
	private ItemAlbume mitemalbume;
	public static String PARAMS_ITEMPHOTO="itemphoto";
	public static String PARAMS_URLIP="itemphoto";
	public static String PARAMS_BUNDLE="bundle";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.photo_activity_main);
		
		//ImgControl.get(urlid+"jidaphotos/getphoto.php", this);
		//mitemphoto=new ItemPhoto("photo_id", "photo_name", "photo_place", "010000", "photograther_name","010200");
		//mitemphoto=(ItemPhoto) getIntent().getExtras().getSerializable(PARAMS_ITEMPHOTO);
		//urlid=getIntent().getExtras().getString(PARAMS_URLIP);
		Intent i=getIntent();
		mitemalbume=(ItemAlbume) i.getSerializableExtra(PARAMS_ITEMPHOTO);
		//urlid=new String(i.getStringExtra(PARAMS_URLIP));
		/*Bundle arg=getIntent().getBundleExtra(PARAMS_BUNDLE);
		urlid=arg.getString(PARAMS_URLIP);
		mitemphoto=(ItemPhoto) arg.getSerializable(PARAMS_ITEMPHOTO);*/

		
		FragmentManager fm=getSupportFragmentManager();
		Fragment fg1=photoTopFragment.NewInstance(urlid, mitemalbume);
		FragmentTransaction ft=fm.beginTransaction();
		ft.add(R.id.fircontainer, fg1);
		ft.commit();
		
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
