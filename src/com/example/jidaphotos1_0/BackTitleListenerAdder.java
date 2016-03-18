package com.example.jidaphotos1_0;

import com.example.jidaphoto.activity.ActivityLianxi;
import com.example.jidaphoto.activity.FeedbackActivity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class BackTitleListenerAdder {
	static public void ListenerAdd(final Activity ac,View v,String titleTxt,final listadapter adapter){
		ImageView imgvw=(ImageView) v.findViewById(R.id.title_back);
		imgvw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ac.finish();
				//System.exit(0);
			}
		});
		
		TextView txt=(TextView) v.findViewById(R.id.title_txt);
		txt.setText(titleTxt);

		
		ImageButton mtitleimgbtn=(ImageButton) v.findViewById(R.id.title_imgbtn);
		final PopupMenu titlemenu=new PopupMenu(ac, mtitleimgbtn);
		titlemenu.getMenuInflater().inflate(R.menu.title_list_photo, titlemenu.getMenu());
		
		titlemenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				switch(item.getItemId()){
				case R.id.title_quit:
					if(adapter!=null){
						adapter.quitthread();
					}
					//list.clear();
					//list.remove(0);
					
					//System.exit(0);

					Intent intent = new Intent(Intent.ACTION_MAIN);  
		            intent.addCategory(Intent.CATEGORY_HOME);  
		            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		            ac.startActivity(intent);  
		            android.os.Process.killProcess(android.os.Process.myPid());
					break;
				case R.id.title_feedback:
					Intent i=new Intent();
					i.setClass(ac, FeedbackActivity.class);
					ac.startActivity(i);
					break;
				case R.id.title_connect:
					Intent i2=new Intent();
					i2.setClass(ac, ActivityLianxi.class);
					ac.startActivity(i2);
					break;
//				case R.id.title_quit:
//					if(adapter!=null){
//						adapter.quitthread();
//					}
//					//android.os.Process.killProcess(android.os.Process.myPid());
//					//System.exit(0);
//					Intent intent = new Intent(Intent.ACTION_MAIN);  
//		            intent.addCategory(Intent.CATEGORY_HOME);  
//		            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
//		            ac.startActivity(intent);  
//		            android.os.Process.killProcess(android.os.Process.myPid());
//					break;
				
				
				
				}
				return false;
			}
		});
		mtitleimgbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				titlemenu.show();
			}
		});
	}

}
