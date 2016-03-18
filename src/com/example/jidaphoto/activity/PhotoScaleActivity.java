package com.example.jidaphoto.activity;

import java.util.ArrayList;

import com.example.jidaphotos1_0.ImgControl;
import com.example.jidaphotos1_0.ImgControl.onimgdownloadlistener;
import com.example.jidaphotos1_0.ItemAlbume;
import com.example.jidaphotos1_0.ItemList;
import com.example.jidaphotos1_0.ItemPhoto;
import com.example.jidaphotos1_0.MatrixImageView;
import com.example.jidaphotos1_0.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public class PhotoScaleActivity extends Activity {
	
	public static String PHOTO_ITEM="photoitem";
	public static String PHOT0_LIST="list";
	public static String PHOTO_NUM="num";
	private ItemPhoto mitem;
	private MatrixImageView mimgview;
	private ImgControl mimgcontrol;
	private String urlid="http://119.29.108.252/";
	private int scrwidth;
    private ArrayList<ItemList> list;
    private int num;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.matrixview_layout);
		mimgview=(MatrixImageView) findViewById(R.id.mscaleimgview);
		mimgcontrol=ImgControl.get(urlid, this);

		Intent i=getIntent();
		//mitem= (ItemPhoto) i.getSerializableExtra(PHOTO_ITEM);
		list=(ArrayList<ItemList>) i.getSerializableExtra(PHOT0_LIST);
		num=(Integer) i.getSerializableExtra(PHOTO_NUM);
		

        DisplayMetrics dm=new DisplayMetrics();  
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		scrwidth=dm.widthPixels;
		/*mimgview.setImageBitmap(mimgcontrol.getImg(mitem.getphotoplace(), -1, new onimgdownloadlistener() {
			
			@Override
			public void onimgdownload(Bitmap bm, String url) {
				// TODO Auto-generated method stub
				if(bm!=null && mimgview!=null)
				{
					mimgview.setImageBitmap(bm);
				}
			}
		}));*/
		
		/*mimgview.setImageBitmap(mimgcontrol.getrealImg(mitem.getphotoplace(), new onimgdownloadlistener() {
			
			@Override
			public void onimgdownload(Bitmap bm, String url) {
				// TODO Auto-generated method stub
				if(bm!=null && mimgview!=null)
				{
					mimgview.setImageBitmap(bm);
				}
				
			}
		}));*/
		
		mimgview.setImageBitmap(list, num);
	}

}
