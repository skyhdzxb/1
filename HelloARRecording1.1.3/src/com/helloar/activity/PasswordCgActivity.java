package com.helloar.activity;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.helloarrecording.R;
import com.helloar.user.LoginActivity;
import com.helloar.user.MyConst;
import com.helloar.user.RegisterActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PasswordCgActivity extends Activity{
	private ImageView back ;
	private EditText phoEText;
	private EditText ppwsEText;
	private EditText npwsEText;
	private EditText rpwsEText;
	private CheckBox checkBox;
	private Button cButton;
	protected static final int SUCCESS = 1;
	protected static final int ERROR = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cg_password);
        phoEText = (EditText) findViewById(R.id.phonum_et);
        ppwsEText = (EditText) findViewById(R.id.prepws_et);
        npwsEText = (EditText) findViewById(R.id.newpws_et);
        rpwsEText = (EditText) findViewById(R.id.rpws_et);
        back = (ImageView) findViewById(R.id.cgpassword_back_iv);
        back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//finish();
				Intent i = new Intent(PasswordCgActivity.this, MineInfoActivity.class); 
				startActivity(i);
			}
        	
        });
        checkBox = (CheckBox) findViewById(R.id.checkBox1);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					// ���ѡ�У���ʾ����
					npwsEText.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
					ppwsEText.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
					rpwsEText.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
				} else {
					// ������������
					npwsEText.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
					ppwsEText.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
					rpwsEText.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
				}
			}
		});
    }
    
    Handler handler=new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case ERROR:
				Toast.makeText(getApplicationContext(), "�û��������������", 0).show();
			case SUCCESS:
				int state = -1;
				try {			
					JSONObject result = (JSONObject) msg.obj;
					Log.i("test5", "*******result:"+result);
					state = result.getInt("state");
					Log.i("test5", "��õ�״̬��:"+state);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (state==1) {
					Toast.makeText(getApplicationContext(), "�����޸ĳɹ�", 0).show();
				finish();
				} else if (state==0) {

					Toast.makeText(getApplicationContext(), "�����޸�ʧ��", 0).show();
				}
			}
		}
	};
    
    public void confirm(View view) {
    	
    	 if(!checkEdit()){
             return;
         }
    	final String phonum = phoEText.getText().toString().trim();
		final String ppws = ppwsEText.getText().toString().trim();
		final String npws = npwsEText.getText().toString().trim();
		//final String urlString  = MyConst.CGPWS_URL + "/username/" + phonum + "/old_psw/"
		//		+ ppws + "/new_psw/"+ npws;
		//Log.i("test5", "**************url:"+urlString);
		new Thread(){
			public void run(){
				Log.i("test5", "��ȥ��******");
				try {
					URL url=new URL(MyConst.CGPWS_URL + "/username/" + phonum + "/old_psw/"
							+ ppws + "/new_psw/"+ npws);
					Log.i("test5", "**************url:"+url);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);//��ʱ����
					int code = conn.getResponseCode();
					Log.i("test5", "**************code:"+code);
					if(code==200){//http-200�ɹ�
    					System.out.println("�M����");
    					InputStream in = conn.getInputStream();
    					BufferedReader br=new BufferedReader(new InputStreamReader(in));
    					String result=br.readLine();
    					Log.i("test5", "**************result:"+result);
    					FileOutputStream out=openFileOutput("targetJson.json",Context.MODE_PRIVATE);   					
    					out.write(result.getBytes());   					
    					out.close();
    					br.close();
    					in.close();  //���ص�������ռ�ڴ� 	
    					JSONObject object = new JSONObject(result);
    					Message msg=Message.obtain();
    					msg.what=SUCCESS;
    					msg.obj=object;
    					Log.i("test5", "**************succuse:"+msg.what);
    					handler.sendMessage(msg);
    				}else{
    				System.out.println("�B��ʧ��");
    				Message msg=Message.obtain();
    				msg.what=ERROR;
    				handler.sendMessage(msg);
    				}
    					}
				catch (MalformedURLException e) {
					// TODO: handle exception
					Log.i("test5", "urlת������*********");
				}catch (Exception e) {
    						Log.i("test5", "������*********");
    				// TODO Auto-generated catch block
    					e.printStackTrace();
    					Message msg=Message.obtain();
        				msg.what=ERROR;
        				handler.sendMessage(msg);
    					}
    		}
		}.start();
	}
	private boolean checkEdit() {
		// TODO Auto-generated method stub
		boolean equals = npwsEText.getText().toString().trim().equals(rpwsEText.getText().toString().trim());
		  if(phoEText.getText().toString().trim().equals("")){
	            Toast.makeText(PasswordCgActivity.this, "��������ȷ���ֻ���", Toast.LENGTH_SHORT).show();
	        }else if(ppwsEText.getText().toString().trim().equals("")&&npwsEText.getText().toString().trim().equals("")){
	            Toast.makeText(PasswordCgActivity.this, "���벻��Ϊ��", Toast.LENGTH_SHORT).show();
	        }else if(!equals){
	            Toast.makeText(PasswordCgActivity.this, "�����������벻һ��", Toast.LENGTH_SHORT).show();
	        }else{
	            return true;
	        } 
		return false;
	}
}

