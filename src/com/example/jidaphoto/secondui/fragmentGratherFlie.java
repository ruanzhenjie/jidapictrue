package com.example.jidaphoto.secondui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jidaphotos1_0.ImgControl;
import com.example.jidaphotos1_0.ItemGrather;
import com.example.jidaphotos1_0.R;
import com.example.jidaphotos1_0.ImgControl.onimgdownloadlistener;
import com.example.jidaphotos1_0.R.id;
import com.example.jidaphotos1_0.R.layout;


public class fragmentGratherFlie extends Fragment {
	private ItemGrather mitemgrather;
	private String murlid;
	private String strurlget="jidaphotos/getphoto.php";
	private int scrwidth;
	ImgControl mimgcontrol;
	
	TextView mtxtview;
	ImageView mimgview;
	
	public static fragmentGratherFlie NewInstance(String urlid,ItemGrather itemgrather){
		Bundle arg=new Bundle();
		arg.putSerializable("itemgrather", itemgrather);
		arg.putSerializable("urlid", urlid);
		fragmentGratherFlie gfg=new fragmentGratherFlie();
		gfg.setArguments(arg);
		return gfg;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mitemgrather=(ItemGrather) getArguments().getSerializable("itemgrather");
		murlid=getArguments().getString("urlid");
		strurlget=new String(murlid+strurlget);
		mimgcontrol=ImgControl.get(strurlget, getActivity());
		//WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
				//scrwidth=wm.getDefaultDisplay().getWidth();

		        DisplayMetrics dm=new DisplayMetrics();  
				getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
				scrwidth=dm.widthPixels;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.grather_show_file_layout, container,false);
		mtxtview=(TextView) v.findViewById(R.id.mytxtgrather);
		mimgview=(ImageView) v.findViewById(R.id.myimggrahter);
		
		mtxtview.setText(mitemgrather.getname()+"\n"+mitemgrather.getintroduction()+"\nphone:"+mitemgrather.getphone());
		mtxtview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String phoneString=new String(mitemgrather.getphone());

				Intent intent=new Intent();

				intent.setAction("android.intent.action.CALL");

				intent.setData(Uri.parse("tel:"+phoneString));

				startActivity(intent);
			}
		});
		
		mimgview.setImageBitmap(mimgcontrol.getImg(mitemgrather.getphotoplace(),scrwidth, new onimgdownloadlistener() {
			
			@Override
			public void onimgdownload(Bitmap bm, String url) {
				// TODO Auto-generated method stub
				if(bm!=null && mimgview!=null){
					mimgview.setImageBitmap(bm);
				}
				
			}
		}));
		
		FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
		fragmentTheme lfg=fragmentTheme.NewInstance(murlid, mitemgrather.getid(), "photograther");
		ft.add(R.id.container2, lfg).commit();
		return v;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mimgcontrol.canceltask();
		super.onDestroy();
	}
}
