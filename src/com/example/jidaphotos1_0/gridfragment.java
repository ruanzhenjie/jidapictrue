package com.example.jidaphotos1_0;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.jidaphoto.activity.ActivityPhotoMain;
import com.example.jidaphoto.activity.PhotoScaleActivity;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class gridfragment extends Fragment {


	GridView mgridview;
	Handler mHandler;
	//ArrayList<ItemPhoto> list;
	ArrayList<ItemList> list;
	listadaptergrid adapter;
	ImgControl mimgcontrol;

	//private String strurl="http://192.168.0.33/jidaphotos/listjsonphoto.php";
	//private String strurlget="http://192.168.0.33/jidaphotos/getphoto.php";
	//private String strurl="http://172.26.0.227/jidaphotos/listjsonphoto.php";
	//private String strurlget="http://172.26.0.227/jidaphotos/getphoto.php";
	private String strurl="jidaphotos/listjsonphoto.php";
	private String strurlget="jidaphotos/getphoto.php";
	private final static String GRIDTAG="gridfragment"; 
	private String urlid;
	private String midfrom;
	
	
	public static gridfragment NewInstance(String urlid,String idfrom){
		Bundle arg=new Bundle();
		arg.putString("urlid", urlid);
		arg.putString("idfrom", idfrom);
		gridfragment gfg=new gridfragment();
		gfg.setArguments(arg);
		return gfg;
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d(GRIDTAG, "into oncreate view");
		View v=LayoutInflater.from(getActivity()).inflate(R.layout.gridfrag, container, false);
		
		mgridview=(GridView) v.findViewById(R.id.mygrid);
		list=new ArrayList<ItemList>();
		adapter=new listadaptergrid(getActivity(),0,list,mgridview,strurlget);
		mgridview.setAdapter(adapter);
		mgridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getActivity(),PhotoScaleActivity.class);
				//i.putExtra(PhotoScaleActivity.PHOTO_ITEM, list.get(position));
				i.putExtra(PhotoScaleActivity.PHOTO_NUM, position);
				i.putExtra(PhotoScaleActivity.PHOT0_LIST, list);
				startActivity(i);
			}
		});
		Log.d(GRIDTAG, "after adapter");
		
		
		mHandler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					//mEditText.append((String)msg.obj);
					Log.d(GRIDTAG, "into handler");
					adapter.notifyDataSetChanged();
					break;

				default:
					break;
				}
			}
			
		};
		Log.d(GRIDTAG, "after handler");
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Log.d(GRIDTAG, "bigin thread");
				HttpURLConnection con = null;

				try {
				URL url = new URL(strurl);
				con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(5 * 1000);
				con.setReadTimeout(10 * 1000);
					con.setRequestMethod("POST");
				con.setDoOutput(true);// 是否输入参数
				StringBuffer params = new StringBuffer();
		        // 表单参数与get形式一样
		        params.append("first").append("=").append("0");
	            params.append("&");  
		        params.append("length").append("=").append("-1");
	            params.append("&");  
		        params.append("id").append("=").append(midfrom);
	            params.append("&");  
		        params.append("from").append("=").append("albume"); 
		        byte[] bypes = params.toString().getBytes();
		        con.getOutputStream().write(bypes);// 输入参数
		        Log.d(GRIDTAG,"ento thread");
		        
		        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));  
	            String lines="";
	            String line="";
	            while ((line= bufferedReader.readLine()) != null) {
	            	lines+=line;
	            }
	            
	            JSONObject jans=new JSONObject(lines);
	            int num=jans.getInt("result_num");
	            Log.d(GRIDTAG, "num=="+num);
	            if(num>0){
	            	JSONArray jay=jans.getJSONArray("list");
	            	for(int i=0;i<num;i++){
	            		JSONObject joj=jay.getJSONObject(i);
	            		list.add(new ItemPhoto(joj.getString("photo_id"),joj.getString("photo_name"),joj.getString("photo_place"),joj.getString("photograther_id"),joj.getString("photograther_name"),joj.getString("albume_id")));
	            	}
	            }
	            
	            
				
	            //Thread.sleep(10000);
				Message msg=mHandler.obtainMessage();
				msg.what=0;
				//msg.obj=new String(line);
				mHandler.sendMessage(msg);

				} catch (ProtocolException e) {
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
		adapter.quitthread();
		super.onDestroy();
	}


}
