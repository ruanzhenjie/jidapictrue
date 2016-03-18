package com.example.jidaphoto.secondui;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.jidaphoto.activity.ActivityPhotoMain;
import com.example.jidaphotos1_0.ImgControl;
import com.example.jidaphotos1_0.ItemAlbume;
import com.example.jidaphotos1_0.ItemList;
import com.example.jidaphotos1_0.ItemPhoto;
import com.example.jidaphotos1_0.R;
import com.example.jidaphotos1_0.R.color;
import com.example.jidaphotos1_0.R.drawable;
import com.example.jidaphotos1_0.R.id;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class fragmentphotomain extends fragmentList{
	
	
	public static fragmentphotomain NewInstance(String urlid){
		Bundle arg=new Bundle();
		arg.putString(PARAMS_IP, urlid);
		fragmentphotomain lfg=new fragmentphotomain();
		lfg.setArguments(arg);
		return lfg;
	}

	@Override
	void setBtnColor() {
		// TODO Auto-generated method stub
		//mbtnfir.setBackgroundColor(getActivity().getResources().getColor(R.color.mycolorclick));
		//mbtnsec.setBackgroundColor(getActivity().getResources().getColor(R.color.mycolor0));
		//mbtnthr.setBackgroundColor(getActivity().getResources().getColor(R.color.mycolor0));
		/*mbtnfir.setBackgroundResource(R.drawable.iconfont_jingxuan_press);
		mbtnfir.getBackground().setAlpha(alpha);
		mbtnsec.setBackgroundColor(getActivity().getResources().getColor(R.color.mycolor0));
		mbtnsec.getBackground().setAlpha(alpha);
		mbtnthr.setBackgroundResource(R.drawable.iconfont_sheyingshi);
		mbtnthr.getBackground().setAlpha(alpha);*/
		mbtnimgfir.setImageResource(R.drawable.iconfont_jingxuan_press);
		mbtntxtfir.setTextColor(Color.parseColor("#ff0000"));
		//mbtnimgfir.setAlpha(alpha);
		mbtnimgsec.setImageResource(R.drawable.iconfont_zhuanti);
		//mbtnimgsec.setAlpha(alpha);
		mbtnimgthr.setImageResource(R.drawable.iconfont_sheyingshi);
		//mbtnimgthr.setAlpha(alpha);
	}

	@Override
	void setBtnFirClick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	void setBtnSecClick() {
		// TODO Auto-generated method stub

		Fragment fg=fragmentthememain.NewInstance(urlid);
		FragmentManager fm=getActivity().getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		ft.replace(R.id.fragmentcontainer, fg);
		//ft.addToBackStack(null);
		ft.commit();
	}

	@Override
	void setBtnThrClick() {
		// TODO Auto-generated method stub

		Fragment fg=fragmentgrahtermain.NewInstance(urlid);
		FragmentManager fm=getActivity().getSupportFragmentManager();
		FragmentTransaction ft=fm.beginTransaction();
		ft.replace(R.id.fragmentcontainer, fg);
		//ft.addToBackStack(null);
		ft.commit();
	}

	@Override
	Intent setItemClickIntent(int position) {
		// TODO Auto-generated method stub
		Intent i=new Intent(getActivity(),ActivityPhotoMain.class);
		i.putExtra(ActivityPhotoMain.PARAMS_URLIP, urlid);
		i.putExtra(ActivityPhotoMain.PARAMS_ITEMPHOTO, list.get(position));
		return i;
	}

	@Override
	StringBuffer setInitThreadParams() {
		// TODO Auto-generated method stub
		StringBuffer params = new StringBuffer();
        // 表单参数与get形式一样
        params.append("first").append("=").append("0");
        params.append("&");  
        params.append("length").append("=").append("-1");
        params.append("&");  
        params.append("id").append("=").append("0");
        params.append("&");  
        params.append("from").append("=").append("begin"); 
		return params;
	}

	@Override
	ItemList setItemFromJson(JSONObject joj) {
		// TODO Auto-generated method stub
		try {
			return new ItemAlbume(joj.getString("albume_id"),joj.getString("albume_name"),joj.getString("albume_introduction"),joj.getString("photograther_id"),joj.getString("best_photo_place"),joj.getString("photograther_name"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	String setlisturl() {
		// TODO Auto-generated method stub
		return "jidaphotos/listjsonalbume.php";
	}

	@Override
	int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.photo_item_layout;
	}

	@Override
	void init() {
		// TODO Auto-generated method stub
		ImgControl.InitProduct(list, adapter, ItemAlbume.getproducter(), new String[]{urlid}, scrwidth, bootview);
		//ImgControl.some();
	}

}
