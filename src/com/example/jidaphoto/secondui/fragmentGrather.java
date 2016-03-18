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

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.jidaphoto.activity.ActivityPhotoMain;
import com.example.jidaphotos1_0.BackTitleListenerAdder;
import com.example.jidaphotos1_0.ImgControl;
import com.example.jidaphotos1_0.ItemAlbume;
import com.example.jidaphotos1_0.ItemGrather;
import com.example.jidaphotos1_0.ItemList;
import com.example.jidaphotos1_0.ItemPhoto;
import com.example.jidaphotos1_0.R;

public class fragmentGrather  extends Fragment {
	//int fragid;
	ListView mListView;
	EditText mEditText;
	Handler mHandler;
	ArrayList<ItemList> list;
	adapterGrather adapter;
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
	private ItemGrather mitemgrather;
	protected View bootview;
	protected int scrwidth;
	
	
	
	public static fragmentGrather NewInstance(String urlid,ItemGrather itemgrather){
		Bundle arg=new Bundle();
		arg.putSerializable("itemgrather", itemgrather);
		arg.putSerializable("urlid", urlid);
		fragmentGrather gfg=new fragmentGrather();
		gfg.setArguments(arg);
		return gfg;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mitemgrather=(ItemGrather) getArguments().getSerializable("itemgrather");
		urlid=getArguments().getString("urlid");
		strurlget=new String(urlid+strurlget);
		strurl=new String(urlid+strurl);
		mimgcontrol=ImgControl.get(strurlget, getActivity());
		mid=new String(mitemgrather.getid());
		mfrom=new String("photograther");
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
		View v=inflater.inflate(R.layout.list_detail_grather, container, false);
		getActivity().setTitle("相片集");
		
		mListView=(ListView) v.findViewById(R.id.listview);
		list=new ArrayList<ItemList>();
		adapter=new adapterGrather(getActivity(),0,list,mListView,strurlget,mitemgrather);
		
		mListView.addHeaderView(getHeaderView());
		
		LayoutInflater lyif=(LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		bootview=lyif.inflate(R.layout.list_add_more, mListView,false);
		bootview.getLayoutParams().width=scrwidth;
		mListView.addFooterView(bootview);
		
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(position!=0)
				{
				Intent i=new Intent(getActivity(),ActivityPhotoMain.class);
				i.putExtra(ActivityPhotoMain.PARAMS_URLIP, urlid);
				i.putExtra(ActivityPhotoMain.PARAMS_ITEMPHOTO, list.get(position-1));
				startActivity(i);
				}
				
			}
			
		});

		BackTitleListenerAdder.ListenerAdd(getActivity(), v, mitemgrather.getname(), adapter);
		
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
		

		String[] initstr={urlid,"0","-1",mid,"photograther"};
		
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
		}).start();*/
		
		return v;
	}
	
	View getHeaderView(){
		LayoutInflater lyif=(LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View headerView=lyif.inflate(R.layout.detail_grather, mListView,false);

		String urlplace=mitemgrather.getphotoplace();
		ImageView imgview;
		TextView txtview,mtxtdis;
		txtview=(TextView) headerView.findViewById(R.id.mytxtgrather);
		mtxtdis=(TextView) headerView.findViewById(R.id.mytxtgratherdis);
		String str=mitemgrather.getname();
		if(mitemgrather.getwechat().equals("null"))
		{
			str+="\n微信:无";
		}
		else{
			str+="\n微信:\n"+mitemgrather.getwechat();
		}

		if(mitemgrather.getemail().equals("null"))
		{
			str+="\n邮箱:无";
		}
		else{
			str+="\n邮箱:\n"+mitemgrather.getemail();
		}


		if(mitemgrather.getphone().equals("null"))
		{
			str+="\n电话:无";
		}
		else{
			str+="\n电话:\n"+mitemgrather.getphone();
		}
		
//		str+="\n简介:"+mitemgrather.getintroduction();
		
		txtview.setText(str);
		mtxtdis.setText("简介:\n"+mitemgrather.getintroduction());
		//txtview.setText(mitemgrather.getname()+"\nwechat:"+mitemgrather.getwechat()+"\nemail:"+mitemgrather.getemail()+"\n"+mitemgrather.getintroduction()+"\nphone:"+mitemgrather.getphone());
		/*txtview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String phoneString=new String(mitemgrather.getphone());

				Intent intent=new Intent();

				intent.setAction("android.intent.action.CALL");

				intent.setData(Uri.parse("tel:"+phoneString));

				getActivity().startActivity(intent);
			}
		});*/
		
		imgview=(ImageView) headerView.findViewById(R.id.myimggrahter);
		Bitmap bitmap=mimgcontrol.getfromcache(urlplace);
		if(null==bitmap){
			imgview.setImageResource(R.drawable.loading);
		}
		else{
			imgview.setImageBitmap(bitmap);
		}
		imgview.setTag(urlplace);
	
	
	
		return headerView;
	}


}
