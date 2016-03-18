package com.example.jidaphoto.secondui;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jidaphotos1_0.ImgControl;
import com.example.jidaphotos1_0.ItemPhoto;
import com.example.jidaphotos1_0.R;
import com.example.jidaphotos1_0.ImgControl.onimgdownloadlistener;
import com.example.jidaphotos1_0.R.drawable;
import com.example.jidaphotos1_0.R.id;
import com.example.jidaphotos1_0.R.layout;



public class adapterTheme extends ArrayAdapter<ItemPhoto> implements OnScrollListener{
	private Context context;
	ArrayList<ItemPhoto> mlist;
	ImgControl mimgcontrol;
	ListView mlistview;
	int mfirstVisibleItem;
	int mvisibleItemCount;
	boolean mfirstenter;
	int scrwidth;
	private String strurlget="http://192.168.0.33/jidaphotos/getphoto.php";
	
	public static final String LISTADTAG="listadapter";

	public adapterTheme(Context context, int textViewResourceId,ArrayList<ItemPhoto> objects,ListView listview,String url) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context=context;
		mlist=objects;
		mlistview=listview;
		mfirstenter=true;
		mlistview.setOnScrollListener(this);
		strurlget=new String(url);
		mimgcontrol=ImgControl.get(strurlget,this.context);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		scrwidth=wm.getDefaultDisplay().getWidth();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v;
		ImageView imgview;
		String urlplace=mlist.get(position).getphotoplace();
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
		


		TextView txtview;
		txtview=(TextView) v.findViewById(R.id.itemimgtxt);
		txtview.setText(mlist.get(position).getintroduction());
		
		imgview=(ImageView) v.findViewById(R.id.itemmyimgview);
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
			final ImageView imgview=(ImageView) mlistview.findViewWithTag(urlplace);
			final int j=i;
			mimgcontrol.dowloadimg(urlplace,scrwidth, new onimgdownloadlistener() {
				
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
