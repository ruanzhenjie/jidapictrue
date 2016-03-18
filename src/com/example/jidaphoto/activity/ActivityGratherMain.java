package com.example.jidaphoto.activity;

import com.example.jidaphoto.secondui.fragmentGrather;
import com.example.jidaphoto.secondui.fragmentGratherFlie;
import com.example.jidaphotos1_0.ItemGrather;
import com.example.jidaphotos1_0.R;
import com.example.jidaphotos1_0.R.id;
import com.example.jidaphotos1_0.R.layout;
import com.example.jidaphotos1_0.R.menu;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class ActivityGratherMain extends FragmentActivity {
	private String urlid="http://119.29.108.252/";
	//private String urlid="http://192.168.0.33/";
	//private String urlid="http://172.26.0.227/";
	public static String PARRAMS_ITEMGRATHER="itemgrather";
	public static String PARAMS_URLIP="urlid";
	ItemGrather mItemGrather;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView(R.layout.grather_activity_main);
		setContentView(R.layout.activity_grather_main);
		
		FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
		//mItemGrather= new ItemGrather("020000", "Ä³ÈË", "Ä³ÈË¼ò½é", "E:\\jidapictures\\020000\\020000.jpg", "123456");
		
		mItemGrather=(ItemGrather) getIntent().getSerializableExtra(PARRAMS_ITEMGRATHER);
		
		//fragmentGratherFlie gfg=fragmentGratherFlie.NewInstance(urlid, mItemGrather);
		fragmentGrather gfg=fragmentGrather.NewInstance(urlid, mItemGrather);

		ft.add(R.id.container1, gfg).commit();
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
