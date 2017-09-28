package com.helloar.user;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class TUserBean {

	private String name;
	private int videoCount;
	
	public int getVideoCount(){
		return videoCount;
	}
	public TUserBean(String name){
		this.name=name;
	}
	ArrayList<String> videoName=new ArrayList<String>();
	public ArrayList<String> imageName=new ArrayList<String>();
	public ArrayList<String> getVideoNames(){
		
		return videoName;
		
	}
	public void setVideoNamges() {
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(MyConst.FILE_PATH+"ttargetJson.json")));
			String result=br.readLine();
			Log.i("test", "***************result:"+result);
			br.close();
			//JSONObject json=new JSONObject(result); 
			JSONArray jsonArray = new JSONArray(result);
			//JSONObject json=new JSONObject(result); 
			//JSONArray jsonArray = json.getJSONArray("message");
			for(int i=0;i<jsonArray.length();i++){
				String str=jsonArray.getJSONObject(i).getString("video_url");
				Log.i("ceshi", "***************str:"+str);
				String name = str.substring(str.indexOf(".")+1, str.length());
				Log.i("ceshi", "***************name:"+name);
				videoName.add(name);
			}
			videoCount=videoName.size();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<String> getImageNames(){
		
		return imageName;
		
	}
	public void setImageNames(){
		try {
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(MyConst.FILE_PATH+"ttargetJson.json")));
			String result=br.readLine();
			br.close();
			//JSONObject json=new JSONObject(result); 
			JSONArray jsonArray = new JSONArray(result);
			//JSONObject json=new JSONObject(result); 
			//JSONArray jsonArray = json.getJSONArray("message");
			for(int i=0;i<jsonArray.length();i++){
				String str=jsonArray.getJSONObject(i).getString("picture_url");
				Log.i("ceshi", "***************str:"+str);
				String name = str.substring(str.indexOf(".")+1, str.length());
				Log.i("ceshi", "***************name:"+name);
				imageName.add(name);
				Log.i("ceshi", "***************imageName:"+imageName);
			}
			//videoCount=videoName.size();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
