package com.example.jidaphotos1_0;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ImgControl implements Serializable{
	private LruCache<String, Bitmap> mcache;
	private ExecutorService mexecutor=null;
	private FileUtils mfilecache;
	private String strurl;
	private Context mcontext=null;
	private static ImgControl mimgcontrol=null;
	
	private final static String CONTROLTAG="imgcontrol"; 

	
	private ImgControl(String url,Context context){
		strurl=new String(url);
		mcontext=context;
		mfilecache=new FileUtils(context);
		//int MaxMemery=(int) Runtime.getRuntime().maxMemory();
		int MaxMemery=(int) Runtime.getRuntime().freeMemory();
		Log.d(CONTROLTAG, "MaxMemery=="+(int) Runtime.getRuntime().maxMemory());
		Log.d(CONTROLTAG, "MaxFreeMemery=="+MaxMemery);
		//int MaxCache=MaxMemery/32;
		int MaxCache=MaxMemery*2/3;
		Log.d(CONTROLTAG, "MaxCache=="+MaxCache);
		mcache=new LruCache<String, Bitmap>(MaxCache){

			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
				//Log.d(CONTROLTAG, "byte="+value.getByteCount()+"or byte="+value.getRowBytes() * value.getHeight());
				return value.getByteCount();
			}
			
		};

	}
	
	public static ImgControl get(String url,Context context){
		if(mimgcontrol==null){
			mimgcontrol=new ImgControl(url, context);
		}
		return mimgcontrol;
	}
	
	public interface onimgdownloadlistener{
		void onimgdownload(Bitmap bm,String url);
	} 
	
	public Bitmap getImg(String url,int width,onimgdownloadlistener listener){
		Bitmap bitmap=getfromcache(url);
		if(bitmap!=null){
			//Log.d(CONTROLTAG, "getfromcache");
		}
		if(bitmap==null){
			bitmap=BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.loading);
			dowloadimg(url,width,listener);
		}
		return bitmap;
	}

	public void dowloadimg(final String url,final int width,final onimgdownloadlistener listener){
		//if(mcache.get(url)!=null)
		if(getfromcache(url)!=null)
			return;
		
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 1:
					listener.onimgdownload((Bitmap)msg.obj, url);
					break;

				default:
					break;
				}
				
				super.handleMessage(msg);
			}
		};
		//Log.d(CONTROLTAG, "ready for thread url="+url+" strurl="+strurl);
		
		getpool().execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Bitmap bitmap=null;
				HttpURLConnection con=null;
				try {
					URL reurl=new URL(strurl);
					con=(HttpURLConnection) reurl.openConnection();
					con.setConnectTimeout(10 * 1000);
					con.setReadTimeout(10 * 1000);
					con.setDoOutput(true);
					//con.setDoInput(true);
					StringBuffer params=new StringBuffer();
					params.append("photo_place=").append(url);
			        params.append("&");  
					params.append("width=").append(width);
					byte[] bytes=params.toString().getBytes();
					con.getOutputStream().write(bytes);
					
					/*
					BitmapFactory.Options option=new BitmapFactory.Options();
					option.inJustDecodeBounds=true;
					InputStream in=con.getInputStream();
					bitmap=BitmapFactory.decodeStream(in, null, option);
					option.inJustDecodeBounds=false;
					
					int be=(int)(option.outWidth/480);
					if(be<0)
						be=1;
					option.inSampleSize=be;
					Log.d(CONTROLTAG, "url="+url+"be="+be+"outwidth="+option.outWidth);

					bitmap=BitmapFactory.decodeStream(in, null, option);*/
					
					
					
					bitmap=BitmapFactory.decodeStream(con.getInputStream());
					
				
					Message msg=handler.obtainMessage();
					msg.what=1;
					msg.obj=bitmap;
					handler.sendMessage(msg);
					putincache(url, bitmap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally{
					if(con!=null){
						con.disconnect();
					}
				}
			}
		});
	}
	
	public Bitmap getrealImg(final String url,final onimgdownloadlistener listener){
		Bitmap bitmap;
		
		bitmap=getfromcache("real_"+url);
		if(bitmap!=null){
			return bitmap;
		}
		else{
		final Handler handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 1:
					listener.onimgdownload((Bitmap)msg.obj, url);
					break;

				default:
					break;
				}
				
				super.handleMessage(msg);
			}
		};
		//Log.d(CONTROLTAG, "ready for thread url="+url+" strurl="+strurl);
		
		getpool().execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Bitmap bitmap=null;
				HttpURLConnection con=null;
				try {
					URL reurl=new URL(strurl);
					con=(HttpURLConnection) reurl.openConnection();
					con.setConnectTimeout(10 * 1000);
					con.setReadTimeout(10 * 1000);
					con.setDoOutput(true);
					//con.setDoInput(true);
					StringBuffer params=new StringBuffer();
					params.append("photo_place=").append(url);
			        params.append("&");  
					params.append("width=").append(-1);
					byte[] bytes=params.toString().getBytes();
					con.getOutputStream().write(bytes);
					
					/*
					BitmapFactory.Options option=new BitmapFactory.Options();
					option.inJustDecodeBounds=true;
					InputStream in=con.getInputStream();
					bitmap=BitmapFactory.decodeStream(in, null, option);
					option.inJustDecodeBounds=false;
					
					int be=(int)(option.outWidth/480);
					if(be<0)
						be=1;
					option.inSampleSize=be;
					Log.d(CONTROLTAG, "url="+url+"be="+be+"outwidth="+option.outWidth);

					bitmap=BitmapFactory.decodeStream(in, null, option);*/
					
					
					
					bitmap=BitmapFactory.decodeStream(con.getInputStream());
					
				
					Message msg=handler.obtainMessage();
					msg.what=1;
					msg.obj=bitmap;
					handler.sendMessage(msg);
					putincache("real_"+url, bitmap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally{
					if(con!=null){
						con.disconnect();
					}
				}
			}
		});
		}
		
		bitmap=getfromcache(url);
		if(bitmap!=null)
			return bitmap;
		else{
			bitmap=BitmapFactory.decodeResource(mcontext.getResources(), R.drawable.loading);
			return bitmap;
		}
		
	}

	public void dowloadimgWithoutThread(final String url,final int width){
		if(getfromcache(url)!=null)
			return;
		
				Bitmap bitmap=null;
				HttpURLConnection con=null;
				try {
					URL reurl=new URL(strurl);
					con=(HttpURLConnection) reurl.openConnection();
					con.setConnectTimeout(10 * 1000);
					con.setReadTimeout(10 * 1000);
					con.setDoOutput(true);
					//con.setDoInput(true);
					StringBuffer params=new StringBuffer();
					params.append("photo_place=").append(url);
			        params.append("&");  
					params.append("width=").append(width);
					byte[] bytes=params.toString().getBytes();
					con.getOutputStream().write(bytes);
					
					
					bitmap=BitmapFactory.decodeStream(con.getInputStream());
					
				
					putincache(url, bitmap);
					Log.d(CONTROLTAG, "enddowload "+url);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				finally{
					if(con!=null){
						con.disconnect();
					}
				}
	}
	
	public ExecutorService getpool(){
		synchronized(ExecutorService.class){
			if(null==mexecutor){
				mexecutor=Executors.newFixedThreadPool(3);
			}
		}
		return mexecutor;
	}
	
	public void canceltask() {
		if(null!=mexecutor){
			mexecutor.shutdownNow();
			mexecutor=null;
			Log.d(CONTROLTAG, "canceltask");
		}
	}
	
	void putincache(String url,Bitmap bitmap){
		String suburl=url.replaceAll("[^\\w]", "");
		if(mcache.get(suburl)==null && bitmap!=null){
			mcache.put(suburl, bitmap);
			mfilecache.put(suburl, bitmap);
		}
	}
	
	public Bitmap getfromcache(String url){
		String suburl=url.replaceAll("[^\\w]", "");
		Bitmap bitmap;
		bitmap=mcache.get(suburl);
		if(null!=bitmap){
			return bitmap;
		}
		bitmap=mfilecache.get(suburl);
		if(null!=bitmap){
			mcache.put(suburl, bitmap);
			return bitmap;
		}
		return null;
	}
	
	
	public void endcontrol(){
		canceltask();
		//mfilecache.deleteFile();
	}

	
	public static void InitProduct(final ArrayList<ItemList> mlist,final listadapter adapter,final ItemList produter,final String str[],final int scrwidth,View mbootview){
		Log.d(CONTROLTAG, "intoinit");
		final ProgressBar mprogressbar=(ProgressBar) mbootview.findViewById(R.id.progressbar_add);
		TextView mboottxt=(TextView) mbootview.findViewById(R.id.txt_add);
		final TextView mloadtxt=(TextView) mbootview.findViewById(R.id.load_add);
		
		AsyncTask<Integer, Integer, Integer> inittask= new AsyncTask<Integer, Integer, Integer>(){

			@Override
			protected Integer doInBackground(Integer... params) {
				// TODO Auto-generated method stub
				HttpURLConnection con = null;
				try {
					URL url = new URL(produter.geturl(str[0]));
					con = (HttpURLConnection) url.openConnection();
					con.setConnectTimeout(5 * 1000);
					con.setReadTimeout(10 * 1000);
						con.setRequestMethod("POST");
					con.setDoOutput(true);// 是否输入参数
					String[] par;
					par=null;
					if(str.length!=1){
						par=new String[4];
						for(int i=1;i<str.length;i++){
							par[i-1]=new String(str[i]);
						}
					}
			        byte[] bypes = produter.getInitThreadParams(par).toString().getBytes();
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
		            		mlist.add(produter.productFromJson(joj));
		            	}
		            }
		            
		            for(int i=0;i<num;i++){
		            	mimgcontrol.dowloadimgWithoutThread(mlist.get(i).getphotoplace(), scrwidth/2);
		            	publishProgress(100*(i+1)/num);
		            }
		            
					Log.d(CONTROLTAG, "enddownload");

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
				Log.d(CONTROLTAG, "end init");
				adapter.notifyDataSetChanged();
				mprogressbar.setVisibility(View.GONE);
				mloadtxt.setVisibility(View.GONE);
				//mboottxt.setVisibility(View.VISIBLE);
				//mbootview.getLayoutParams().height=0;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);
				mprogressbar.setProgress(values[0]);
			}
			
		}.execute(0);
		
	}
	
	
}
