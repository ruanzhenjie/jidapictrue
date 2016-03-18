package com.example.jidaphotos1_0;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.jidaphotos1_0.ImgControl.onimgdownloadlistener;



public class listadaptergrid extends ArrayAdapter<ItemList> implements OnScrollListener{
	private Context context;
	ArrayList<ItemList> mlist;
	ImgControl mimgcontrol;
	GridView mgridview;
	int mfirstVisibleItem;
	int mvisibleItemCount;
	boolean mfirstenter;
	private String strurlget="http://192.168.0.33/jidaphotos/getphoto.php";
	//private String strurlget="http://172.26.0.227/jidaphotos/getphoto.php";
	private int scrwidth;
	
	public static final String LISTADTAG="listadapter";

	public listadaptergrid(Activity context, int textViewResourceId,ArrayList<ItemList> objects,GridView gridview,String url) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context=context;
		mlist=objects;
		mgridview=gridview;
		mfirstenter=true;
		mimgcontrol=ImgControl.get(null,null);
		mgridview.setOnScrollListener(this);
		strurlget=new String(url);

        DisplayMetrics dm=new DisplayMetrics();  
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		scrwidth=dm.widthPixels;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v;
		ImageView imgview;
		String urlplace=mlist.get(position).getphotoplace();
		if(convertView==null){
			v=LayoutInflater.from(context).inflate(R.layout.grid_item_layout, null);
		}
		else{
			v=convertView;
		}
		
		imgview=(ImageView) v.findViewById(R.id.mygridimg);
		Bitmap bitmap=mimgcontrol.getfromcache(urlplace);
		if(null==bitmap){
			Log.d(LISTADTAG, "getfromcahce fail");
			imgview.setImageResource(R.drawable.loading);
		}
		else{
			Log.d(LISTADTAG, "getfromcahce successd");
			imgview.setImageBitmap(bitmap);
		}
		imgview.setTag(urlplace);
		
		
		return v;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if(scrollState==SCROLL_STATE_IDLE){
			loadbitmaps(mfirstVisibleItem,mvisibleItemCount);
		}
		else{
			mimgcontrol.canceltask();
		}
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		mfirstVisibleItem=firstVisibleItem;
		mvisibleItemCount=visibleItemCount;
		
		if(mfirstenter==true && mvisibleItemCount>0){
			loadbitmaps(mfirstVisibleItem,mvisibleItemCount);
			mfirstenter=false;
			//Log.d(LISTADTAG, "onsrcoll");
		}
		
	}

	private void loadbitmaps(int visibleItemCount, int totalItemCount){
		int i;
		for(i=visibleItemCount;i<(visibleItemCount+totalItemCount);i++){
			String urlplace=mlist.get(i).getphotoplace();
			final ImageView imgview=(ImageView) mgridview.findViewWithTag(urlplace);
			final int j=i;
			mimgcontrol.dowloadimg(urlplace,scrwidth/3, new onimgdownloadlistener() {
				
				@Override
				public void onimgdownload(Bitmap bm, String url) {
					// TODO Auto-generated method stub
					if(bm!=null && imgview!=null){
						imgview.setImageBitmap(bm);
						Log.d(LISTADTAG, "bitmap url="+url+"id="+j);
					}
				}
			});
		}
	}

	public void quitthread(){
		mimgcontrol.canceltask();
	}



}
