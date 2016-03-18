package com.example.jidaphotos1_0;

import com.example.jidaphoto.secondui.fragmentGrather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class fragmentLianxi extends Fragment{
	
	public static fragmentLianxi NewInstance(){

		fragmentLianxi fl=new fragmentLianxi();
		return fl;
	}
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v=inflater.inflate(R.layout.lianxi, container, false);
		BackTitleListenerAdder.ListenerAdd(getActivity(), v, "幕后工作人", null);
		return v;
	}
	

}
