package com.helloar.user;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

import com.example.helloarrecording.MainActivity;
import com.example.helloarrecording.R;
import com.helloar.activity.ARMainActivity;
import com.helloar.activity.ForgetPasActivity;
import com.helloar.activity.MineInfoActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity{
	protected static final int SUCCESS = 1;
	protected static final int ERROR = 0;
//	private ImageView qq;
//	private ImageView wechat;
	private EditText editText;
	private CheckBox checkBox;
	private TextView register;
	private TextView forgetpas;
	private ImageView backIV;
	private EditText ed_name;
	private EditText ed_pws;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_main);
		ed_name = (EditText) findViewById(R.id.et_username);
		ed_pws = (EditText) findViewById(R.id.et_password);
		register = (TextView) findViewById(R.id.register_tv);
		forgetpas = (TextView) findViewById(R.id.lforgetpas_tv);
		backIV = (ImageView)findViewById(R.id.l_back_iv);
		backIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent show=new Intent(LoginActivity.this,ARMainActivity.class); 
				show.putExtra("mine",2);
				startActivity(show);
				finish();
			}
		});
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
        forgetpas.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, ForgetPasActivity.class);
				startActivity(intent);
				finish();
			}
		});
	    ed_name.setOnFocusChangeListener(new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			if(!hasFocus){
                if(ed_name.getText().toString().trim().length()!=11){
                    Toast.makeText(LoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
            }
		}
	});
	}
	
	Handler handler=new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case ERROR:
				Toast.makeText(getApplicationContext(), "登录失败:用户名或密码错误！", 0).show();
			case SUCCESS:
				int state = -1;
				try {
					
					JSONObject result = (JSONObject) msg.obj;
					Log.i("test3", "*******result:"+result);
					state = result.getInt("state");
					Log.i("test3", "获得的状态码:"+state);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (state==1) {
				SharedPreferences sPreferences=getSharedPreferences("config", MODE_PRIVATE);
				Boolean isLogin= sPreferences.edit().putBoolean("is_login",true).commit();
				Log.i("test1", "登陆成功后的的is_login:"+isLogin);
				load();
				Intent intent=new Intent(LoginActivity.this,com.helloar.activity.ARMainActivity.class);
				intent.putExtra("mine",1);
				startActivity(intent);
				finish();
				} else if (state==0) {

					Toast.makeText(getApplicationContext(), "登录失败:用户名或密码错误！", 0).show();
				}
			}
		}
	};
	
	public void login(View v){
		final String name = ed_name.getText().toString().trim();
		final String pws = ed_pws.getText().toString().trim();
		new Thread(){
			public void run(){
				try {
//					String urlString = MyConst.REGISTER_URL+"/username/"+name+"/psw/"+pws;
					URL url=new URL(MyConst.LOGIN_URL + "/username/"
							+ name + "/psw/" + pws);
					Log.i("test", "**************url:"+url);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);//超时设置
					int code = conn.getResponseCode();
					Log.i("test", "**************code:"+code);
					if(code==200){//http-200成功
    					System.out.println("進來了");
    					InputStream in = conn.getInputStream();
    					BufferedReader br=new BufferedReader(new InputStreamReader(in));
    					String result=br.readLine();
    					Log.i("test3", "**************result:"+result);
    					FileOutputStream out=openFileOutput("targetJson.json",Context.MODE_PRIVATE);   					
    					out.write(result.getBytes());   					
    					out.close();
    					br.close();
    					in.close();  //都关掉，避免占内存 	
    					JSONObject object = new JSONObject(result);
    					Message msg=Message.obtain();
    					msg.what=SUCCESS;
    					msg.obj=object;
    					Log.i("test3", "**************succuse:"+msg.what);
    					handler.sendMessage(msg);
    				}else{
    				System.out.println("連接失敗");
    				Message msg=Message.obtain();
    				msg.what=ERROR;
    				handler.sendMessage(msg);
    				}
    					} catch (Exception e) {
    				// TODO Auto-generated catch block
    					e.printStackTrace();
    					Message msg=Message.obtain();
        				msg.what=ERROR;
        				handler.sendMessage(msg);
    					}
    		}
		}.start();
	}

public void load(){
	
	File file = new File(MyConst.FILE_PATH);
	File[] listFiles = file.listFiles();

	for (int i = 0; i < listFiles.length; i++) {
		if ((""+listFiles[i]).endsWith(".jpg")) {
		File delFile = listFiles[i].getAbsoluteFile();
		delFile.delete();
		}
	}
	final UserBean bean=new UserBean("ss");
	Log.i("test", "**************bean:"+bean);
	bean.setImageNames();
	//Log.i("test", "**************imageName:");
	bean.setVideoNamges();
	//Log.i("test", "**************VideoName:");
	Log.i("test", "**************VideoName:"+bean.getVideoCount());
	//System.out.println(bean.getVideoCount());
	new Thread(){
		public void run(){
			for(int i=0;i<bean.getVideoCount();i++){
				try {
					//System.out.println("ss");
					Log.i("test", "**************url:"+MyConst.BASE_URL1+bean.imageName.get(i));
					URL url=new URL(MyConst.BASE_URL1+bean.imageName.get(i));
					//System.out.println(MyConst.BASE_URL+bean.imageName.get(i));
					HttpURLConnection conn=(HttpURLConnection) url.openConnection();
					System.out.println("打开");
					conn.setRequestMethod("GET");
					System.out.println("GET");
					conn.setConnectTimeout(5000);
					System.out.println("5000");
					int code =conn.getResponseCode();
					System.out.println("code");
					Log.i("test", "*******************code:"+code);
					if(code==200){
						System.out.println("进来了");
						FileOutputStream fs=new FileOutputStream(MyConst.FILE_PATH+i+".jpg");
						InputStream in=conn.getInputStream();
						byte[] buffer=new byte[1024];
						int length;			
						while((length=in.read(buffer, 0, buffer.length))!=-1){
							fs.write(buffer,0,length);
						}
						fs.close();
						in.close();
					}
					else{
						System.out.println("没进来");
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("ioexception");
					e.printStackTrace();
				}
				}
		}
	}.start();
}
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	// TODO Auto-generated method stub
	if(keyCode == KeyEvent.KEYCODE_BACK){
		Intent intent = new Intent(this,ARMainActivity.class);
		intent.putExtra("mine", 2);
		startActivity(intent);
		finish();
	}
	return true;
}
}
