package com.helloar.activity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.example.helloarrecording.R;
import com.helloar.user.LoginActivity;
import com.helloar.user.MyConst;
import com.helloar.user.RegisterActivity;
import com.helloar.utils.SmsUtils;
import com.mob.MobSDK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ForgetPasActivity extends Activity {

	protected static final int SUCCESS = 0;
	protected static final int ERROR = 1;
	private EditText username;
	private EditText psw;
	private EditText rpsw;
	private EditText codeEt;
	private Button confirmBtn;
	private Button getBtn;
	private CheckBox checkBox;
	private ImageView finishIV;
	private EventHandler eventHandler;
	private boolean isSuc = false;
	private String phoneNumber;
	private String code;

	String urlString = MyConst.REGISTER_URL + "/username/" + username + "/psw/"
			+ psw + "/action/reset";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_main);
		initData();
		MobSDK.init(this, "1ff30d61ab038", "06d27e220743b839c14b215cd715d94b");

		// ����EventHandler����
		eventHandler = new EventHandler() {
			@Override
			public void afterEvent(int arg0, int arg1, Object arg2) {

				super.afterEvent(arg0, arg1, arg2);
				Message msg = new Message();
				msg.arg1 = arg0;
				msg.arg2 = arg1;
				msg.obj = arg2;
				mHandler.sendMessage(msg);
			}
		};

		// ע�������
		SMSSDK.registerEventHandler(eventHandler);
		initEvent();
		Log.i("test2", "������֤��");

	}

	private void initData() {
		// TODO Auto-generated method stub
		username = (EditText) findViewById(R.id.fusername_et);
		psw = (EditText) findViewById(R.id.fpassword_et);
		rpsw = (EditText) findViewById(R.id.rpassword_et);
		codeEt = (EditText) findViewById(R.id.et_fcode);
		confirmBtn = (Button) findViewById(R.id.fconfirm_btn);
		getBtn = (Button) findViewById(R.id.bt_fget);
		finishIV = (ImageView) findViewById(R.id.ffinish_iv);
		checkBox = (CheckBox) findViewById(R.id.checkBox1);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					// ���ѡ�У���ʾ����
					psw.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
					rpsw.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
				} else {
					// ������������
					psw.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
					rpsw.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
				}
			}
		});
		codeEt.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					// �˴�Ϊ�õ�����ʱ�Ĵ�������
					Log.i("test2", "�˴�Ϊ�õ�����ʱ�Ĵ�������");
				} else {
					// �˴�Ϊʧȥ����ʱ�Ĵ�������
					Log.i("test", "�˴�Ϊʧȥ����ʱ�Ĵ�������");
					code = codeEt.getText().toString().trim();
//					String number = username.getText().toString().trim();
//					SMSSDK.submitVerificationCode("86", number, code);
					SMSSDK.submitVerificationCode("86", phoneNumber, code);
					Log.i("test", "��õ绰���룺"+ phoneNumber);
				}
			}
		});
		 username.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if(!hasFocus){
		                if(username.getText().toString().trim().length()!=11){
		                    Toast.makeText(ForgetPasActivity.this, "��������ȷ���ֻ���", Toast.LENGTH_SHORT).show();
		                }
		            }
				}
			});
//		 rpsw.setOnFocusChangeListener(new OnFocusChangeListener()
//	        {
//	 
//	            @Override
//	            public void onFocusChange(View v, boolean hasFocus) {
//	                // TODO Auto-generated method stub
//	                if(!hasFocus){
//	                    if(!rpsw.getText().toString().trim().equals(psw.getText().toString().trim())){
//	                        Toast.makeText(ForgetPasActivity.this, "�����������벻һ��", Toast.LENGTH_SHORT).show(); 
//	                    }
//	                }
//	            }      
//	        });
	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			Log.i("test2", "**************��mHandle");
			if (result == SMSSDK.RESULT_COMPLETE) {

				System.out.println("--------result" + event);
				Log.i("test2", "--------result" + event);
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// �ύ��֤��ɹ�
					Toast.makeText(getApplicationContext(), "�ύ��֤��ɹ�",
							Toast.LENGTH_SHORT).show();
					isSuc = true;
					Log.i("test2", "**************isSuc��" + isSuc);
				}
			} else {
//				Toast.makeText(getApplicationContext(), "�ύ��֤��ʧ��",
//						Toast.LENGTH_SHORT).show();
				isSuc = false;
				Log.i("test2","**************isSuc��" + isSuc);
			}
		}
	};


	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			getBtn.setBackgroundColor(getResources().getColor(
					R.color.text_color_shenhui_677582));
			getBtn.setText("ʣ��" + msg.what + "��");
			
			Log.i("test", "msg.what:"+msg.what);

			switch (msg.what) {
			case ERROR:
				getBtn.setClickable(true);
				getBtn.setBackgroundColor(getResources().getColor(
						R.color.text_color_normal));
				getBtn.setText("������֤");
				//Toast.makeText(getApplicationContext(), "�����»�����֤��", 0).show();
				break;
			case SUCCESS:
				String state = "";
				try {
					JSONObject result = (JSONObject) msg.obj;
					state = result.getString("state");
					Log.i("test2", "�޸����봦�õ���state:"+ state);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
				if (state.equals("1")) {
					Toast.makeText(getApplicationContext(), "�޸ĳɹ�", 0).show();
					Intent intent = new Intent(ForgetPasActivity.this,
							LoginActivity.class);
					startActivity(intent);
					finish();
				} else if (state.equals("0")) {

					Toast.makeText(getApplicationContext(), "�޸�ʧ��", 0).show();
				}
				break;
			}
		}
	};


	// private EditText et_code;
	private void initEvent() {
		getBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				phoneNumber = username.getText().toString().trim();
				getBtn.setClickable(false);
				new Thread() {
					public void run() {
						try {
							for (int i = 60; i > 0; i--) {
								sleep(1000);
								handler.sendEmptyMessage(i);
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}.start();
				System.out.println(phoneNumber);
				SMSSDK.getVerificationCode("86", phoneNumber);
			}
		});
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		SMSSDK.unregisterEventHandler(eventHandler);

	}

	public void fconfirem(View view) {
		// TODO Auto-generated method stub
		 if(!checkEdit()){
             return;
         }
		final String userName = username.getText().toString().trim();
		final String PSW = psw.getText().toString().trim();
		code = codeEt.getText().toString().trim();
		SMSSDK.submitVerificationCode("86", phoneNumber, code);
		if (isSuc) {
			new Thread() {
				public void run() {
					try {

						URL url = new URL(MyConst.REGISTER_URL + "/username/"
								+ userName + "/psw/" + PSW +"/action/reset");
						Log.i("test", "url:"+url);
						HttpURLConnection conn = (HttpURLConnection) url
								.openConnection();
						conn.setRequestMethod("GET");
						conn.setConnectTimeout(5000);
						int code = conn.getResponseCode();
						if (code == 200) {
							System.out.println("������");
							Log.i("test2", "������");
							InputStream in = conn.getInputStream();
							BufferedReader br = new BufferedReader(
									new InputStreamReader(in));
							String result = br.readLine();
							System.out.println(result);
							Log.i("test2", "�������õ�result��"+result);
							br.close();
							in.close();
							Message msg = Message.obtain();
							JSONObject jsonResultJ = new JSONObject(result);
							msg.obj = jsonResultJ;
							msg.what = SUCCESS;
							Log.i("test2", "�������õ�״̬�룺"+msg.what);
							handler.sendMessage(msg);
						} else {
							System.out.println("����ʧ��");
							Message msg = Message.obtain();
							msg.what = ERROR;
							Log.i("test2", "û��ȥ��õ�״̬�룺"+msg.what);
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Message msg = Message.obtain();
						msg.what = ERROR;
						handler.sendMessage(msg);
					}
				}
			}.start();
		} else {
			Toast.makeText(getApplicationContext(), "�޸�ʧ��", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private boolean checkEdit() {
		// TODO Auto-generated method stub
		boolean equals = rpsw.getText().toString().trim().equals(psw.getText().toString().trim());
		Log.i("test6", "equals "+equals);
		Log.i("test6", "rpsw "+rpsw.getText().toString().trim());
		Log.i("test6", "psw "+psw.getText().toString().trim());
		  if(username.getText().toString().trim().equals("")){
	            Toast.makeText(ForgetPasActivity.this, "��������ȷ���ֻ���", Toast.LENGTH_SHORT).show();
	        }else if(psw.getText().toString().trim().equals("")){
	            Toast.makeText(ForgetPasActivity.this, "���벻��Ϊ��", Toast.LENGTH_SHORT).show();
	        }else if(!equals){
	            Toast.makeText(ForgetPasActivity.this, "�����������벻һ��", Toast.LENGTH_SHORT).show();
	        }else{
	            return true;
	        } 
		return false;
	}

	public void ffinish(View view) {
		Intent intent = new Intent(ForgetPasActivity.this,
				LoginActivity.class);
		startActivity(intent);
		finish();
		
	}
}
