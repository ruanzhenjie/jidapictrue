package com.example.jidaphotos1_0;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemPhoto extends ItemList implements Serializable{
	public String photo_id;
	public String photo_name;
	public String photo_place;
	public String photograther_id;
	public String photograther_name;
	public String albume_id;
	private static String downlisturl="jidaphotos/listjsonphoto.php";
	
	public ItemPhoto(String a,String b,String c,String d,String e,String f){
		photo_id=new String(a);
		photo_name=new String(b);
		photo_place=new String(c);
		photograther_id=new String(d);
		photograther_name=new String(e);
		albume_id=new String(f);
	}

	private ItemPhoto(){}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return photo_id+"|"+photo_name+"|"+photograther_name;
	}
	
	public String getphotoplace(){
		return photo_place;
	}
	
	public String getintroduction(){
		return photo_name;
	}
	
	public String getalbume(){
		return albume_id;
	}
	
	public String getgrather(){
		return photograther_id;
	}

	@Override
	public String getid() {
		// TODO Auto-generated method stub
		return photo_id;
	}

	@Override
	public ItemList productFromJson(JSONObject joj) {
		// TODO Auto-generated method stub
		try {
			return new ItemPhoto(joj.getString("photo_id"),joj.getString("photo_name"),joj.getString("photo_place"),joj.getString("photograther_id"),joj.getString("photograther_name"),joj.getString("albume_id"));
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
		if(param==null){
        // 表单参数与get形式一样
        params.append("first").append("=").append("0");
        params.append("&");  
        params.append("length").append("=").append("-1");
        params.append("&");  
        params.append("id").append("=").append("0");
        params.append("&");  
        params.append("from").append("=").append("begin"); 
		}
		else{
	        params.append("first").append("=").append(param[0]);
	        params.append("&");  
	        params.append("length").append("=").append(param[1]);
	        params.append("&");  
	        params.append("id").append("=").append(param[2]);
	        params.append("&");  
	        params.append("from").append("=").append(param[3]); 
		}
		return params;
	}


	@Override
	public String geturl(String ip) {
		// TODO Auto-generated method stub
		return new String(ip+downlisturl);
	}


	
	public static ItemList getproducter() {
		// TODO Auto-generated method stub
		return new ItemPhoto();
	}


}
