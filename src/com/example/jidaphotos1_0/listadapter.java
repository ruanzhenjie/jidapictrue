package com.example.jidaphotos1_0;

import java.util.ArrayList;

import com.example.jidaphotos1_0.ImgControl.onimgdownloadlistener;

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
import android.widget.ArrayAdapter;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class listadapter extends ArrayAdapter<ItemList> implements OnScrollListener{

	private Context context;
	ArrayList<ItemList> mlist;
	ImgControl mimgcontrol;
	ListView mlistview;
	int mfirstVisibleItem;
	int mvisibleItemCount;
	boolean mfirstenter;
	int scrwidth;
	int mlayout;
	private String strurlget="http://192.168.0.33/jidaphotos/getphoto.php";
	
	public static final String LISTADTAG="listadapter";

	public listadapter(Activity context, int textViewResourceId,ArrayList<ItemList> objects,ListView listview,String url,int layout) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context=context;
		mlist=objects;
		mlistview=listview;
		mfirstenter=true;
		//mlistview.setOnScrollListener(this);
		strurlget=new String(url);
		mimgcontrol=ImgControl.get(strurlget,this.context);
		mlayout=layout;

        DisplayMetrics dm=new DisplayMetrics();  
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		scrwidth=dm.widthPixels;
		Log.d(LISTADTAG, "width=="+scrwidth);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v;
		ImageView imgview;

		TextView txtview;
		String urlplace=mlist.get(position).getphotoplace();
		if(convertView==null){
			//v=LayoutInflater.from(context).inflate(R.layout.photo_item_layout, null);
			v=LayoutInflater.from(context).inflate(mlayout, null);
			
			if(mlayout==R.layout.fragment_grather_item){
				android.widget.LinearLayout.LayoutParams lyparams=(android.widget.LinearLayout.LayoutParams) v.findViewById(R.id.frameitem).getLayoutParams();
				lyparams.width=scrwidth;

//				txtview=(TextView) v.findViewById(R.id.itemimgtxt);
//				txtview.setText(((ItemGrather)mlist.get(position)).getname());
			}
			else{
				LayoutParams lyparams=(LayoutParams) v.findViewById(R.id.itemmyimgview).getLayoutParams();
				LayoutParams lyparams2=(LayoutParams) v.findViewById(R.id.itemimgtxt).getLayoutParams();
				//LayoutParams lyparams=(LayoutParams) v.getLayoutParams();
				
				lyparams.width=scrwidth;
				lyparams2.width=scrwidth;
				//lyparams.height=200;

//				txtview=(TextView) v.findViewById(R.id.itemimgtxt);
//				txtview.setText(mlist.get(position).getintroduction());
				}
		}
		else{
			v=convertView;

//			txtview=(TextView) v.findViewById(R.id.itemimgtxt);
//			
//			if(mlayout==R.layout.fragment_grather_item)
//				txtview.setText(((ItemGrather)mlist.get(position)).getname());
//			else
//				txtview.setText(mlist.get(position).getintroduction());
		}
		
		

		txtview=(TextView) v.findViewById(R.id.itemimgtxt);
		
		if(mlayout==R.layout.fragment_grather_item)
			txtview.setText(((ItemGrather)mlist.get(position)).getname());
		else
			txtview.setText(mlist.get(position).getintroduction());
		

		
		imgview=(ImageView) v.findViewById(R.id.itemmyimgview);
		Bitmap bitmap=mimgcontrol.getfromcache(urlplace);
		if(null==bitmap){
			Log.d(LISTADTAG, "getfromcahce fail "+position+" url="+urlplace);
			imgview.setImageResource(R.drawable.loading);
		}
		else{
			Log.d(LISTADTAG, "getfromcahce successd "+position+" url="+urlplace);
			imgview.setImageBitmap(bitmap);
		}
		imgview.setTag(urlplace);
		
		
		return v;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if(scrollState==SCROLL_STATE_IDLE){
			//loadbitmaps(mfirstVisibleItem,mvisibleItemCount);
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
			//loadbitmaps(mfirstVisibleItem,mvisibleItemCount);
			mfirstenter=false;
			//Log.d(LISTADTAG, "onsrcoll");
		}
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		//if(mlist.size()>10)
			//return 10;
		
		return super.getCount();
	}

	private void loadbitmaps(int visibleItemCount, int totalItemCount){
		int i;
		int lastItemCount,beginItemCount;
		
		/*int beforeCount=2;
		int aftercount=5;
		if((visibleItemCount+aftercount)>mlist.size()){
			lastItemCount=mlist.size();
			beforeCount+=(visibleItemCount+aftercount)-mlist.size();
		}
		else if(aftercount<totalItemCount){
			lastItemCount=(visibleItemCount+totalItemCount);
		}
		else{
			lastItemCount=(visibleItemCount+aftercount);
		}
		
		if(visibleItemCount-beforeCount<0){
			beginItemCount=0;
			if(lastItemCount+(beforeCount-visibleItemCount)>mlist.size()){
				lastItemCount=mlist.size();
			}
			else{
				lastItemCount+=(beforeCount-visibleItemCount);
			}
		}
		else{
			beginItemCount=visibleItemCount-beforeCount;
		}
		

		*/
		for(i=visibleItemCount;i<(visibleItemCount+totalItemCount);i++){
		//for(i=visibleItemCount;i<lastItemCount;i++){
			final String urlplace=mlist.get(i).getphotoplace();
			final ImageView imgview=(ImageView) mlistview.findViewWithTag(urlplace);
			final int j=i;
			if(null==mimgcontrol.getfromcache(urlplace)){
			mimgcontrol.dowloadimg(urlplace,scrwidth/2, new onimgdownloadlistener() {
				
				@Override
				public void onimgdownload(Bitmap bm, String url) {
					// TODO Auto-generated method stub
					if(bm!=null && imgview!=null){
						if(!imgview.getTag().equals(urlplace))
						{
							Log.d(LISTADTAG, "cancel load url="+url+" id="+j);
							return;
						}
						imgview.setImageBitmap(bm);
						Log.d(LISTADTAG, "bitmap url="+url+" id="+j);
					}
				}
			});
			}
		}
		/*for(i=(visibleItemCount+totalItemCount);i<lastItemCount;i++){
			String urlplace=mlist.get(i).getphotoplace();
			if(null==mimgcontrol.getfromcache(urlplace)){
			mimgcontrol.dowloadimg(urlplace,scrwidth/2, new onimgdownloadlistener() {
				
				@Override
				public void onimgdownload(Bitmap bm, String url) {
					// TODO Auto-generated method stub
				}
			});
			}	
		}

		for(i=beginItemCount;i<visibleItemCount;i++){
			String urlplace=mlist.get(i).getphotoplace();
			if(null==mimgcontrol.getfromcache(urlplace)){
			mimgcontrol.dowloadimg(urlplace,scrwidth/2, new onimgdownloadlistener() {
				
				@Override
				public void onimgdownload(Bitmap bm, String url) {
					// TODO Auto-generated method stub
				}
			});
			}	
		}*/
		
	}

	public void quitthread(){
		mimgcontrol.canceltask();
	}


	public void quitapp(){
		//mimgcontrol.canceltask();
		mimgcontrol.endcontrol();
	}


}
