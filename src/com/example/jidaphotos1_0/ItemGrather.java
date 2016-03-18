package com.example.jidaphotos1_0;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class ItemGrather extends ItemList implements Serializable{
	public String photograther_id;
	public String photograther_name;
	public String photograther_introduction;
	public String photograther_self_photo_place;
	public String phone;
	public String wechat;
	public String email;
	private static String downlisturl="jidaphotos/listjsonphotograther.php";
	
	public ItemGrather(String a,String b,String c,String d,String e,String f,String g){
		photograther_id=new String(a);
		photograther_name=new String(b);
		photograther_introduction=new String(c);
		photograther_self_photo_place=new String(d);
		phone=new String(e);
		wechat=new String(f);
		email=new String(g);
	}
	
	private ItemGrather(){}


	@Override
	public String geturl(String ip) {
		// TODO Auto-generated method stub
		return new String(ip+downlisturl);
	}
	
	public String getid(){
		return photograther_id;
	}
	
	public String getphone(){
		return phone;
	}
	
	public String getphotoplace(){
		return photograther_self_photo_place;
	}
	
	public String getname(){
		return photograther_name;
	}

	public String getintroduction(){
		return photograther_introduction;
	}

	public String getwechat(){
		return wechat;
	}

	public String getemail(){
		return email;
	}

	@Override
	public ItemList productFromJson(JSONObject joj) {
		// TODO Auto-generated method stub
		try {
			return new ItemGrather(joj.getString("photograther_id"),joj.getString("photograther_name"),joj.getString("photograther_introduction"),joj.getString("photograther_self_photo_place"),joj.getString("photograther_phone"),joj.getString("photograther_wechat"),joj.getString("photograther_email"));
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
		return new ItemGrather();
	}

}
