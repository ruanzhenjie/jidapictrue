package com.example.jidaphotos1_0;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemTheme extends ItemList implements Serializable {

	public String theme_id;
	public String theme_name;
	public String best_photo_place;
	public String theme_introduction;
	private static String downlisturl="jidaphotos/listjsontheme.php";
	
	public ItemTheme(String a,String b,String c,String d){
		theme_id=new String(a);
		theme_name=new String(b);
		best_photo_place=new String(c);
		theme_introduction=new String(d);
	}
	
	private ItemTheme(){}
	

	@Override
	public String geturl(String ip) {
		// TODO Auto-generated method stub
		return new String(ip+downlisturl);
	}
	
	public String getid(){
		return theme_id;
	}
	
	public String getphotoplace(){
		return best_photo_place;
	}
	

	public String getintroduction(){
		return theme_introduction;
	}

	@Override
	public ItemList productFromJson(JSONObject joj) {
		// TODO Auto-generated method stub
		try {
			return new ItemTheme(joj.getString("theme_id"),joj.getString("theme_name"),joj.getString("best_photo_place"),joj.getString("theme_introduction"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public StringBuffer getInitThreadParams(String param[]) {
		// TODO Auto-generated method stub
		StringBuffer params = new StringBuffer();
        // 表单参数与get形式一样
		if(param==null){
	        // 表单参数与get形式一样
	        params.append("first").append("=").append("0");
	        params.append("&");  
	        params.append("length").append("=").append("-1");
			}
			else{
		        params.append("first").append("=").append(param[0]);
		        params.append("&");  
		        params.append("length").append("=").append(param[1]);
			}
		return params;
	}

	public static ItemList getproducter() {
		// TODO Auto-generated method stub
		return new ItemTheme();
	}

}
