package com.example.jidaphoto.activity;

import com.example.jidaphotos1_0.R;
import com.example.jidaphotos1_0.R.layout;
import com.example.jidaphotos1_0.fragmentLianxi;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

public class ActivityLianxi extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.lianxi_activity);
		FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
		Fragment fl=fragmentLianxi.NewInstance();
		ft.add(R.id.activitylianxi, fl).commit();
	}
	

}
