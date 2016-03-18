package com.example.jidaphotos1_0;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemAlbume  extends ItemList implements Serializable{
	public String albume_id;
	public String albume_name;
	public String albume_introduction;
	public String photograther_id;
	public String best_photo_place;
	public String photograther_name;
	private static String downlisturl="jidaphotos/listjsonalbume.php";
	
	public ItemAlbume(String a,String b,String c,String d,String e,String f){
		albume_id=new String(a);
		albume_name=new String(b);
		albume_introduction=new String(c);
		photograther_id=new String(d);
		best_photo_place=new String(e);
		photograther_name=new String(f);
	}

	private ItemAlbume(){}


	@Override
	public String geturl(String ip) {
		// TODO Auto-generated method stub
		return new String(ip+downlisturl);
	}

	@Override
	public String getid() {
		// TODO Auto-generated method stub
		return albume_id;
	}

	@Override
	public String getphotoplace() {
		// TODO Auto-generated method stub
		return best_photo_place;
	}

	@Override
	public String getintroduction() {
		// TODO Auto-generated method stub
		return albume_introduction;
	}

	public String getgrather(){
		return photograther_id;
	}

	@Override
	public ItemList productFromJson(JSONObject joj) {
		// TODO Auto-generated method stub
		try {
			return new ItemAlbume(joj.getString("albume_id"),joj.getString("albume_name"),joj.getString("albume_introduction"),joj.getString("photograther_id"),joj.getString("best_photo_place"),joj.getString("photograther_name"));
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



	public static ItemList getproducter() {
		// TODO Auto-generated method stub
		return new ItemAlbume();
	}


}
