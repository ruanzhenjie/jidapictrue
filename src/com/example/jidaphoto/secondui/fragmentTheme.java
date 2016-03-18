package com.example.jidaphoto.secondui;

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
import com.example.jidaphotos1_0.BackTitleListenerAdder;
import com.example.jidaphotos1_0.ImgControl;
import com.example.jidaphotos1_0.ItemAlbume;
import com.example.jidaphotos1_0.ItemList;
import com.example.jidaphotos1_0.ItemPhoto;
import com.example.jidaphotos1_0.ItemTheme;
import com.example.jidaphotos1_0.R;
import com.example.jidaphotos1_0.R.id;
import com.example.jidaphotos1_0.R.layout;
import com.example.jidaphotos1_0.listadapter;

import android.R.color;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

public class fragmentTheme extends Fragment {
	//int fragid;
	ListView mListView;
	EditText mEditText;
	Handler mHandler;
	ArrayList<ItemList> list;
	//adapterTheme adapter;
	listadapter adapter;
	ImageView mimgview=null;
	ImgControl mimgcontrol;
	//private String reurl="http://172.27.10.141/jidaphotos/listjsonphoto.php";
	//private String strurl="http://192.168.0.33/jidaphotos/listjsonphoto.php";
	//private String strurlget="http://192.168.0.33/jidaphotos/getphoto.php";
	private String strurl="jidaphotos/listjsonphoto.php";
	private String strurlget="jidaphotos/getphoto.php";
	private final static String FRAGTAG="listfragment";
	private String urlid;
	private String mid;
	private String mfrom;
	protected View bootview;
	protected int scrwidth;
	

	public static fragmentTheme NewInstance(String urlid,String id,String from){
		Bundle arg=new Bundle();
		arg.putString("urlid", urlid);
		arg.putString("id", id);
		arg.putString("from", from);
		fragmentTheme lfg=new fragmentTheme();
		lfg.setArguments(arg);
		return lfg;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		urlid=getArguments().getString("urlid");
		mid=getArguments().getString("id");
		mfrom=getArguments().getString("from");
		strurl=new String(urlid+strurl);
		strurlget=new String(urlid+strurlget);
		mimgcontrol=ImgControl.get(strurlget, getActivity());
		//WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		//scrwidth=wm.getDefaultDisplay().getWidth();

		        DisplayMetrics dm=new DisplayMetrics();  
				getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
				scrwidth=dm.widthPixels;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		adapter.quitthread();
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.list_detail_theme, container, false);
		
		getActivity().setTitle("相片集");
		
		mListView=(ListView) v.findViewById(R.id.listview);
		list=new ArrayList<ItemList>();
		
		LayoutInflater lyif=(LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		bootview=lyif.inflate(R.layout.list_add_more, mListView,false);
		bootview.getLayoutParams().width=scrwidth;
		mListView.addFooterView(bootview);
		
		adapter=new listadapter(getActivity(),0,list,mListView,strurlget,R.layout.photo_item_layout);
		//LayoutInflater lyif=(LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//View headerView=lyif.inflate(R.layout.detail_grather, mListView,false);
		//mListView.addHeaderView(headerView);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getActivity(),ActivityPhotoMain.class);
				i.putExtra(ActivityPhotoMain.PARAMS_URLIP, urlid);
				i.putExtra(ActivityPhotoMain.PARAMS_ITEMPHOTO, list.get(position));
				startActivity(i);
				
			}
			
		});
		
		BackTitleListenerAdder.ListenerAdd(getActivity(), v, "定格", adapter);

		
		mHandler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					//mEditText.append((String)msg.obj);
					adapter.notifyDataSetChanged();
					break;

				default:
					break;
				}
			}
			
		};
		
		String[] initstr={urlid,"0","-1",mid,"theme"};
		
		ImgControl.InitProduct(list, adapter, ItemAlbume.getproducter(), initstr, scrwidth, bootview);
		/*
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
		        params.append("id").append("=").append(mid);
	            params.append("&");  
		        params.append("from").append("=").append(mfrom); 
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
	            int num=jans.getInt("result_num");
	            if(num>0){
	            	JSONArray jay=jans.getJSONArray("list");
	            	for(int i=0;i<num;i++){
	            		JSONObject joj=jay.getJSONObject(i);
	            		list.add(new ItemPhoto(joj.getString("photo_id"),joj.getString("photo_name"),joj.getString("photo_place"),joj.getString("photograther_id"),joj.getString("photograther_name"),joj.getString("albume_id")));
	            	}
	            }
	            
	            
				
	            //Thread.sleep(10000);
				Message msg=new Message();
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
		*/
		return v;
	}
	
	

}
