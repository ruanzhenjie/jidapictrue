package com.example.jidaphotos1_0;

import java.io.Serializable;

import org.json.JSONObject;

public abstract class ItemList implements Serializable{

	public abstract String getid();
	

	public abstract String getphotoplace();
	

	public abstract String getintroduction();
	
	
	public abstract ItemList productFromJson(JSONObject joj);
	
	
	public abstract StringBuffer getInitThreadParams(String param[]);
	
	public abstract String geturl(String ip);
	
	//
}
