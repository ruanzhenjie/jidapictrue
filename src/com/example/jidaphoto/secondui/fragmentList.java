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

import com.example.jidaphoto.activity.ActivityLianxi;
import com.example.jidaphoto.activity.FeedbackActivity;
import com.example.jidaphotos1_0.ImgControl;
import com.example.jidaphotos1_0.ItemList;
import com.example.jidaphotos1_0.R;
import com.example.jidaphotos1_0.listadapter;
import com.example.jidaphotos1_0.R.id;
import com.example.jidaphotos1_0.R.layout;
import com.example.jidaphotos1_0.R.menu;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class fragmentList extends Fragment {
	//int fragid;
	ListView mListView;
	EditText mEditText;
	Handler mHandler;
	ArrayList<ItemList> list;
	listadapter adapter;
	ImageView mimgview=null;
	ImgControl mimgcontrol;
	//private String reurl="http://172.27.10.141/jidaphotos/listjsonphoto.php";
	//private String strurl="http://192.168.0.33/jidaphotos/listjsonphoto.php";
	//private String strurlget="http://192.168.0.33/jidaphotos/getphoto.php";
	private String strurl="jidaphotos/listjsonphoto.php";
	private String strurlget="jidaphotos/getphoto.php";
	private final static String FRAGTAG="listfragment";
	protected String urlid;
	protected int mlayout;
	protected int scrwidth;
	private ProgressBar mprogressbar;
	private TextView mboottxt;
	private TextView mloadtxt;
	
	
	public static String PARAMS_IP="urlip";
	
	protected ImageButton mtitleimgbtn;
	protected LinearLayout mbtnfir,mbtnsec,mbtnthr;
	protected ImageView mbtnimgfir,mbtnimgsec,mbtnimgthr;
	protected TextView mbtntxtfir,mbtntxtsec,mbtntxtthr;

	protected static int alpha=100;
	private AsyncTask<Integer, Integer, Integer> inittask;
	protected View bootview;


	abstract void setBtnColor();

	abstract void setBtnFirClick();
	abstract void setBtnSecClick();
	abstract void setBtnThrClick();
	
	abstract Intent setItemClickIntent(int position);
	
	//abstract ArrayAdapter<Object>  setAdapter();
	//abstract ArrayList<Object> setList();
	
	abstract StringBuffer setInitThreadParams();
	abstract ItemList setItemFromJson(JSONObject joj);

	abstract String setlisturl();
	abstract int setLayout();
	
	abstract void init();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		urlid=getArguments().getString(PARAMS_IP);
		strurl=new String(setlisturl());
		strurl=new String(urlid+strurl);
		strurlget=new String(urlid+strurlget);
		//WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		//scrwidth=wm.getDefaultDisplay().getWidth();

        DisplayMetrics dm=new DisplayMetrics();  
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		scrwidth=dm.widthPixels;
		Log.d(FRAGTAG, "dm.width="+dm.widthPixels);
		mimgcontrol=ImgControl.get(strurlget, getActivity());
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		adapter.quitthread();
		Log.d(FRAGTAG, "out");
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mlayout=setLayout();
		View v=inflater.inflate(R.layout.fragment_list, container, false);
		
		mtitleimgbtn=(ImageButton) v.findViewById(R.id.titleimgbtn);
		final PopupMenu titlemenu=new PopupMenu(getActivity(), mtitleimgbtn);
		titlemenu.getMenuInflater().inflate(R.menu.title_list_photo, titlemenu.getMenu());
		
		titlemenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				switch(item.getItemId()){
				case R.id.title_quit:
					adapter.quitapp();
					//list.clear();
					//list.remove(0);
					Log.d(FRAGTAG, "quit");
					
					//System.exit(0);

					Intent intent = new Intent(Intent.ACTION_MAIN);  
		            intent.addCategory(Intent.CATEGORY_HOME);  
		            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		            startActivity(intent);  
		            android.os.Process.killProcess(android.os.Process.myPid());
					break;
				case R.id.title_feedback:
					Intent i=new Intent();
					i.setClass(getActivity(), FeedbackActivity.class);
					startActivity(i);
					break;
				case R.id.title_connect:
					Intent i2=new Intent();
					i2.setClass(getActivity(), ActivityLianxi.class);
					startActivity(i2);
					break;
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
		
		mListView=(ListView) v.findViewById(R.id.listview);

		list=new ArrayList<ItemList>();
		adapter=new listadapter(getActivity(),0,list,mListView,strurlget,mlayout);
		//list=setList();
		//adapter=setAdapter();

		LayoutInflater lyif=(LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		bootview=lyif.inflate(R.layout.list_add_more, mListView,false);
		//mprogressbar=(ProgressBar) bootview.findViewById(R.id.progressbar_add);
		///mboottxt=(TextView) bootview.findViewById(R.id.txt_add);
		//mloadtxt=(TextView) bootview.findViewById(R.id.load_add);
		
		bootview.getLayoutParams().width=scrwidth;
		
		mListView.addFooterView(bootview);
		
		
		mListView.setAdapter(adapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				startActivity(setItemClickIntent(position));
			}
		});
		
		

		mbtnimgfir=(ImageView) v.findViewById(R.id.mbtnimgfir);
		mbtnimgsec=(ImageView) v.findViewById(R.id.mbtnimgsec);
		mbtnimgthr=(ImageView) v.findViewById(R.id.mbtnimgthr);

		mbtntxtfir=(TextView) v.findViewById(R.id.mbtntxtfir);
		mbtntxtsec=(TextView) v.findViewById(R.id.mbtntxtsec);
		mbtntxtthr=(TextView) v.findViewById(R.id.mbtntxtthr);
		
		
		
		mbtnfir=(LinearLayout) v.findViewById(R.id.mfirstbtn);
		mbtnfir.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setBtnFirClick();
			}
		});
		
		mbtnsec=(LinearLayout) v.findViewById(R.id.msecondbtn);
		mbtnsec.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setBtnSecClick();
			}
		});
		
		mbtnthr=(LinearLayout) v.findViewById(R.id.mthreebtn);
		mbtnthr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setBtnThrClick();
			}
			
		});

		setBtnColor();
		
		
		
		init();
		/*
		inittask= new AsyncTask<Integer, Integer, Integer>(){

			@Override
			protected Integer doInBackground(Integer... params) {
				// TODO Auto-generated method stub
				HttpURLConnection con = null;
				try {
					URL url = new URL(strurl);
					con = (HttpURLConnection) url.openConnection();
					con.setConnectTimeout(5 * 1000);
					con.setReadTimeout(10 * 1000);
						con.setRequestMethod("POST");
					con.setDoOutput(true);// 是否输入参数
			        byte[] bypes = setInitThreadParams().toString().getBytes();
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
		            		list.add(setItemFromJson(joj));
		            	}
		            }
		            
		            for(int i=0;i<num;i++){
		            	mimgcontrol.dowloadimgWithoutThread(list.get(i).getphotoplace(), scrwidth/2);
		            	publishProgress(100*(i+1)/num);
		            }
		            
					Log.d(FRAGTAG, "enddownload");

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
				return null;
			}

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				mprogressbar.setProgress(0);
			}

			@Override
			protected void onPostExecute(Integer result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				Log.d(FRAGTAG, "end init");
				adapter.notifyDataSetChanged();
				mprogressbar.setVisibility(View.GONE);
				mloadtxt.setVisibility(View.GONE);
				//mboottxt.setVisibility(View.VISIBLE);
				bootview.getLayoutParams().height=0;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);
				mprogressbar.setProgress(values[0]);
			}
			
		}.execute(0);
		*/
		
		
		/*
		mHandler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					//mEditText.append((String)msg.obj);
					Log.d(FRAGTAG, "end init");
					adapter.notifyDataSetChanged();
					break;

				default:
					break;
				}
			}
			
		};
		
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
		        byte[] bypes = setInitThreadParams().toString().getBytes();
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
	            		list.add(setItemFromJson(joj));
	            	}
	            }
	            
	            for(int i=0;i<num;i++){
	            	mimgcontrol.dowloadimgWithoutThread(list.get(i).getphotoplace(), scrwidth/2);
	            }
	            
				Log.d(FRAGTAG, "enddownload");
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

	
	



}
