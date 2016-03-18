package com.example.jidaphotos1_0;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jidaphoto.activity.ActivityGratherMain;
import com.example.jidaphotos1_0.ImgControl.onimgdownloadlistener;


public class phototogratherfragment extends Fragment {
	TextView t;
	ImageView mimg;
	ImgControl mimgcontrol;
	
	private String strurl="jidaphotos/listjsonphotograther.php";
	private String strurlget="jidaphotos/getphoto.php";
	
	private String urlid;
	private String midfrom;
	private int scrwidth;
	private ItemGrather mitemgrather;
	
	public static phototogratherfragment NewInstance(String urlid,String idfrom){
		Bundle arg=new Bundle();
		arg.putString("urlid", urlid);
		arg.putString("idfrom", idfrom);
		phototogratherfragment pfg=new phototogratherfragment();
		pfg.setArguments(arg);
		return pfg;
	}
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		urlid=getArguments().getString("urlid");
		midfrom=getArguments().getString("idfrom");
		strurl=new String(urlid+strurl);
		strurlget=new String(urlid+strurlget);
		mimgcontrol=ImgControl.get(strurlget, getActivity());
		//WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
				//scrwidth=wm.getDefaultDisplay().getWidth();

		        DisplayMetrics dm=new DisplayMetrics();  
				getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
				scrwidth=dm.widthPixels;
		mitemgrather=null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.phototogratheritemlayout, container, false);
		t=(TextView) v.findViewById(R.id.mytxtgratherinphoto);
		mimg=(ImageView) v.findViewById(R.id.myimggrahterinphoto);
		
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(mitemgrather!=null)
				{
				Intent i=new Intent(getActivity(),ActivityGratherMain.class);
				i.putExtra(ActivityGratherMain.PARAMS_URLIP, urlid);
				i.putExtra(ActivityGratherMain.PARRAMS_ITEMGRATHER, mitemgrather);
				startActivity(i);
				}
			}
		});
		
		final Handler mhandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 0:
					mitemgrather=(ItemGrather) msg.obj;
					
					String str=mitemgrather.getname();
					if(mitemgrather.getwechat().equals("null"))
					{
						str+="\n微信:无";
					}
					else{
						str+="\n微信:"+mitemgrather.getwechat();
					}

					if(mitemgrather.getemail().equals("null"))
					{
						str+="\n邮箱:无";
					}
					else{
						str+="\n邮箱:"+mitemgrather.getemail();
					}


					if(mitemgrather.getphone().equals("null"))
					{
						str+="\n电话:无";
					}
					else{
						str+="\n电话:"+mitemgrather.getphone();
					}
					
					str+="\n简介:"+mitemgrather.getintroduction();
					
					t.setText(str);
					//Log.d("handler", "STRS[1]="+strs[1]);
					mimg.setImageBitmap(mimgcontrol.getImg(mitemgrather.getphotoplace(),scrwidth, new onimgdownloadlistener() {
						
						@Override
						public void onimgdownload(Bitmap bm, String url) {
							// TODO Auto-generated method stub
							Log.d("handler", "getinlistener");
							if(bm!=null && mimg!=null){
								mimg.setImageBitmap(bm);
								Log.d("handler", "bm!=null");
							}
						}
					}));
					
					

					/*String[] strs=(String[])msg.obj;
					t.setText(strs[0]);
					Log.d("handler", "STRS[1]="+strs[1]);
					mimg.setImageBitmap(mimgcontrol.getImg(strs[1],scrwidth, new onimgdownloadlistener() {
						
						@Override
						public void onimgdownload(Bitmap bm, String url) {
							// TODO Auto-generated method stub
							Log.d("handler", "getinlistener");
							if(bm!=null && mimg!=null){
								mimg.setImageBitmap(bm);
								Log.d("handler", "bm!=null");
							}
						}
					}));*/
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection con=null;
				try {
					URL url=new URL(strurl);
					con=(HttpURLConnection) url.openConnection();
					con.setConnectTimeout(5*1000);
					con.setReadTimeout(10*1000);
					con.setDoOutput(true);
					con.setDoInput(true);
					con.setRequestMethod("POST");
					
					StringBuffer params=new StringBuffer();
					//params.append("first=0&length=1&from='photograther'&id="+midfrom);
					params.append("first").append("=").append("0");
			        params.append("&");  
				    params.append("length").append("=").append("1");
			        params.append("&");  
				    params.append("id").append("=").append(midfrom);
			        params.append("&");  
				    params.append("from").append("=").append("photograther"); 
					byte[] bytes=params.toString().getBytes();
					
					con.getOutputStream().write(bytes);
					
					String line="";
					String lines="";
					
					BufferedReader buf=new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
					
					while((line=buf.readLine())!=null){
						lines+=line;
					}
					
					JSONObject resjs=new JSONObject(lines);
					int num=resjs.getInt("result_num");
					
					if(num>0){
						JSONArray jay=resjs.getJSONArray("list");
						JSONObject joj=jay.getJSONObject(0);
						String[] strs=new String[2];
						strs[0]=new String(joj.getString("photograther_introduction"));
						strs[1]=new String(joj.getString("photograther_self_photo_place"));
						ItemGrather mitengrather=(ItemGrather) ItemGrather.getproducter().productFromJson(joj);
						Log.d("grather", "strs[1]="+strs[1]);
						
						Message msg=mhandler.obtainMessage();
						msg.what=0;
						//msg.obj=strs;
						msg.obj=mitengrather;
						
						mhandler.sendMessage(msg);
					}
					
					
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					if(con!=null){
						con.disconnect();
					}
				}
				
				
			}
		}).start();
		
		return v;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mimgcontrol.canceltask();
		super.onDestroy();
	}

}
