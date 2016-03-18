package com.example.jidaphotos1_0;



import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.jidaphotos1_0.ImgControl.onimgdownloadlistener;

public class photoTopFragment extends Fragment {
	private String word;
	ImageView mimg;
	ImgControl mimgcontrol;
	private String strurl="jidaphotos/listjsonphoto.php";
	private String strurlget="jidaphotos/getphoto.php";
	private String murlid;
	private ItemAlbume mitemalbume;
	private int scrwidth;
	
	Button mbtn0,mbtn1,mbtn2;
	
	public static photoTopFragment NewInstance(String urlid,ItemAlbume itemalbume){
		Bundle arg=new Bundle();
		arg.putSerializable("item", itemalbume);
		arg.putString("urlid", urlid);
		photoTopFragment fg=new photoTopFragment();
		fg.setArguments(arg);
		return fg;
	}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		murlid=getArguments().getString("urlid");
		strurl=new String(murlid+strurl);
		strurlget=new String(murlid+strurlget);
		mitemalbume=(ItemAlbume) getArguments().getSerializable("item");
		
		FragmentActivity f=getActivity();
		mimgcontrol=ImgControl.get(strurlget, getActivity());

        DisplayMetrics dm=new DisplayMetrics();  
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		scrwidth=dm.widthPixels;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
		//Fragment fg=new gridfragment(murlid,mitemphoto.getalbume());
		Fragment fg=gridfragment.NewInstance(murlid, mitemalbume.getid());
		ft.add(R.id.seccontainer, fg);
		ft.commit();
		
		View v=inflater.inflate(R.layout.photo_top_layout, container, false);
		BackTitleListenerAdder.ListenerAdd(getActivity(), v, mitemalbume.getintroduction(), null);
		mimg=(ImageView) v.findViewById(R.id.myimgsimple);
		mimg.setImageBitmap(mimgcontrol.getImg(mitemalbume.getphotoplace(),scrwidth, new onimgdownloadlistener() {
			
			@Override
			public void onimgdownload(Bitmap bm, String url) {
				// TODO Auto-generated method stub
				if(bm!=null && mimg!=null){
					mimg.setImageBitmap(bm);
				}
				//ft.commit();
			}
		}));

		mbtn0=(Button) v.findViewById(R.id.mybtn0);
		mbtn0.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mbtn0.setTextColor(getActivity().getResources().getColor(R.color.myclickcolor));
				//mbtn1.setTextColor(getActivity().getResources().getColor(R.color.myunclickcolor));
				mbtn2.setTextColor(getActivity().getResources().getColor(R.color.myunclickcolor));
				
				FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
				Fragment fg=gridfragment.NewInstance(murlid,mitemalbume.getid());
				ft.replace(R.id.seccontainer, fg);
				ft.commit();
			}
		});
		/*
		mbtn1=(Button) v.findViewById(R.id.mybtn1);
		mbtn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mbtn0.setTextColor(getActivity().getResources().getColor(R.color.myunclickcolor));
				mbtn1.setTextColor(getActivity().getResources().getColor(R.color.myclickcolor));
				mbtn2.setTextColor(getActivity().getResources().getColor(R.color.myunclickcolor));
				
				FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
				Fragment fg=phototoalbumefragment.NewInstance(murlid,mitemalbume.getid());
				ft.replace(R.id.seccontainer, fg);
				ft.commit();
			}
		});*/
		mbtn2=(Button) v.findViewById(R.id.mybtn2);
		mbtn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mbtn0.setTextColor(getActivity().getResources().getColor(R.color.myunclickcolor));
				//mbtn1.setTextColor(getActivity().getResources().getColor(R.color.myunclickcolor));
				mbtn2.setTextColor(getActivity().getResources().getColor(R.color.myclickcolor));
				
				FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
				Fragment fg=phototogratherfragment.NewInstance(murlid,mitemalbume.getgrather());
				ft.replace(R.id.seccontainer, fg);
				ft.commit();
			}
		});
		
		
		

		return v;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.d("activity", "quit");
		mimgcontrol.canceltask();
		super.onDestroy();
	}
	
	

}
