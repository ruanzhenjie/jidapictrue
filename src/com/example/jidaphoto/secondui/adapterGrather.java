package com.example.jidaphoto.secondui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout.LayoutParams;

import com.example.jidaphotos1_0.ImgControl;
import com.example.jidaphotos1_0.ItemGrather;
import com.example.jidaphotos1_0.ItemList;
import com.example.jidaphotos1_0.ItemPhoto;
import com.example.jidaphotos1_0.R;
import com.example.jidaphotos1_0.ImgControl.onimgdownloadlistener;
import com.example.jidaphotos1_0.listadapter;

//public class adapterGrather extends ArrayAdapter<ItemPhoto> implements OnScrollListener{
public class adapterGrather extends listadapter{
	private Context context;
	ArrayList<ItemList> mlist;
	ImgControl mimgcontrol;
	ListView mlistview;
	int mfirstVisibleItem;
	int mvisibleItemCount;
	boolean mfirstenter;
	int scrwidth;
	private ItemGrather mitemgrather;
	
	private ImageView mimggrather;
	private TextView mtxtgrather;
	
	private String strurlget="http://192.168.0.33/jidaphotos/getphoto.php";
	
	//public static final String LISTADTAG="listadapter";

	public adapterGrather(Activity context, int textViewResourceId,ArrayList<ItemList> objects,ListView listview,String url,ItemGrather itemgrather) {
		super(context, textViewResourceId, objects,listview,url,0);
		// TODO Auto-generated constructor stub
		this.context=context;
		mlist=objects;
		mlistview=listview;
		mfirstenter=true;
		//mlistview.setOnScrollListener(this);
		strurlget=new String(url);
		mimgcontrol=ImgControl.get(strurlget,this.context);

        DisplayMetrics dm=new DisplayMetrics();  
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		scrwidth=dm.widthPixels;
		mitemgrather=itemgrather;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v;
		ImageView imgview;
		//Log.i(LISTADTAG, "position=="+position);
		String urlplace=mlist.get(position).getphotoplace();
		//Log.i(LISTADTAG, "1");
		if(convertView==null){
			v=LayoutInflater.from(context).inflate(R.layout.photo_item_layout, null);
			LayoutParams lyparams=(LayoutParams) v.findViewById(R.id.itemmyimgview).getLayoutParams();
			LayoutParams lyparams2=(LayoutParams) v.findViewById(R.id.itemimgtxt).getLayoutParams();
			//LayoutParams lyparams=(LayoutParams) v.getLayoutParams();
			
			lyparams.width=scrwidth;
			lyparams2.width=scrwidth;
			//lyparams.height=200;
			
		}
		else{
			v=convertView;
		}
		
		//Log.i(LISTADTAG, "2");
		TextView txtview;
		txtview=(TextView) v.findViewById(R.id.itemimgtxt);
		//Log.i(LISTADTAG, "2.1");
		txtview.setText(mlist.get(position).getintroduction());
		//Log.i(LISTADTAG, "3");
		
		imgview=(ImageView) v.findViewById(R.id.itemmyimgview);
		Bitmap bitmap=mimgcontrol.getfromcache(urlplace);
		if(null==bitmap){
			//Log.d(LISTADTAG, "getfromcahce fail");
			imgview.setImageResource(R.drawable.loading);
		}
		else{
			//Log.d(LISTADTAG, "getfromcahce successd");
			imgview.setImageBitmap(bitmap);
		}
		imgview.setTag(urlplace);
		//Log.i(LISTADTAG, "VireTypeCount=="+getViewTypeCount());
		
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
		String urlplace;
		for(i=visibleItemCount;i<(visibleItemCount+totalItemCount);i++){
			if(i==0)
			{
				urlplace=mitemgrather.getphotoplace();
			}
			else
				urlplace=mlist.get(i-1).getphotoplace();
			final ImageView imgview=(ImageView) mlistview.findViewWithTag(urlplace);
			final int j=i;
			mimgcontrol.dowloadimg(urlplace,scrwidth/2, new onimgdownloadlistener() {
				
				@Override
				public void onimgdownload(Bitmap bm, String url) {
					// TODO Auto-generated method stub
					if(bm!=null && imgview!=null){
						imgview.setImageBitmap(bm);
						//Log.d(LISTADTAG, "bitmap url="+url+"id="+j);
					}
				}
			});
		}
	}

	public void quitthread(){
		mimgcontrol.canceltask();
	}





}
