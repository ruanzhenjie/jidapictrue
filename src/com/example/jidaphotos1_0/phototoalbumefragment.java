package com.example.jidaphotos1_0;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.zip.Inflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class phototoalbumefragment extends Fragment {
	TextView t;
	private String strurl="jidaphotos/listjsonalbume.php";
	private String urlid;
	private String midfrom;
	
	public static phototoalbumefragment NewInstance(String urlid,String idfrom){
		Bundle arg=new Bundle();
		arg.putString("urlid", urlid);
		arg.putString("idfrom", idfrom);
		phototoalbumefragment pfg=new phototoalbumefragment();
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
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.photoitemlayout, container, false);
		t=(TextView) v.findViewById(R.id.mytxtphotoitem);
		final Handler mhandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 0:
					t.setText((String)msg.obj);
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
		        
		        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));  
	            String lines="";
	            String line="";
	            while ((line= bufferedReader.readLine()) != null) {
	            	lines+=line;
	            }
	            
	            JSONObject jans=new JSONObject(lines);
	            int num=jans.getInt("result_num");
	            if(num>0){
	            	JSONArray jay=jans.getJSONArray("list");
	            		JSONObject joj=jay.getJSONObject(0);
	            		//list.add(new ItemPhoto(joj.getString("photo_id"),joj.getString("photo_name"),joj.getString("photo_place"),joj.getString("photograther_id"),joj.getString("photograther_name"),joj.getString("albume_id")));
	            		line=new String(joj.getString("albume_introduction"));
	            }
	            
	            
				
	            //Thread.sleep(10000);
				Message msg=mhandler.obtainMessage();
				msg.what=0;
				msg.obj=line;
				mhandler.sendMessage(msg);

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

}
