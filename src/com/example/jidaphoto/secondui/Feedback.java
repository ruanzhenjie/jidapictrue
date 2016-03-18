package com.example.jidaphoto.secondui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.jidaphotos1_0.BackTitleListenerAdder;
import com.example.jidaphotos1_0.R;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Feedback extends Fragment {

	private String strurl="http://119.29.108.252/jidaphotos/feedback.php";
	
	private Button mbtnOk;
	private EditText medtxt;
	private Handler mhandler;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		View v=inflater.inflate(R.layout.feedback, container, false);
		BackTitleListenerAdder.ListenerAdd(getActivity(), v,getActivity().getResources().getString(R.string.title_feedback), null);
		//BackTitleListenerAdder.ListenerAdd(getActivity(), v,"反馈", null);
		mbtnOk=(Button) v.findViewById(R.id.feedback_ok);
		medtxt=(EditText) v.findViewById(R.id.feedback_edittxt);
		mhandler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					Toast.makeText(getActivity(), "提交成功\n感谢您的宝贵意见", Toast.LENGTH_SHORT).show();
					getActivity().finish();
					break;
				case 1:
					Toast.makeText(getActivity(), "提交失败", Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
				}
			}
		};
		
		
		mbtnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//final Context context=getActivity();
				
				new Thread(new Runnable() {
					HttpURLConnection con = null;
					
					@Override
					public void run() {
						// TODO Auto-generated method stub

						try {
						URL url = new URL(strurl);
						
						con = (HttpURLConnection) url.openConnection();
						con.setConnectTimeout(5 * 1000);
						con.setReadTimeout(10 * 1000);
							con.setRequestMethod("POST");
						con.setDoOutput(true);// 是否输入参数

						StringBuffer params = new StringBuffer();
				        // 表单参数与get形式一样
				        params.append("feedback").append("=").append(medtxt.getText()); 
				        byte[] bypes = params.toString().getBytes();
				        con.getOutputStream().write(bypes);// 输入参数
				        Log.d("ento thread", "");
				        
				        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));  
			            String lines="";
			            String line="";
			            while ((line= bufferedReader.readLine()) != null) {
			            	lines+=line;
			            }
			            
			            JSONObject jans=new JSONObject(lines);
			            String result=jans.getString("result");
			            if(result.equals("ok"))
			            {
			            	Message msg=mhandler.obtainMessage();
			            	msg.what=0;
			            	mhandler.sendMessage(msg);
			            }
			            else{
			            	
			            	Message msg=mhandler.obtainMessage();
			            	msg.what=1;
			            	mhandler.sendMessage(msg);
			            }
			           

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
				
				
				
				
				//Toast.makeText(getActivity(), medtxt.getText(), Toast.LENGTH_LONG).show();
				//getActivity().finish();
			}
		});
		
		
		return v;
	}
	

}
