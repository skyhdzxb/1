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

		// 创建EventHandler对象
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

		// 注册监听器
		SMSSDK.registerEventHandler(eventHandler);
		initEvent();
		Log.i("test2", "发送验证码");

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
					// 如果选中，显示密码
					psw.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
					rpsw.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
				} else {
					// 否则隐藏密码
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
					// 此处为得到焦点时的处理内容
					Log.i("test2", "此处为得到焦点时的处理内容");
				} else {
					// 此处为失去焦点时的处理内容
					Log.i("test", "此处为失去焦点时的处理内容");
					code = codeEt.getText().toString().trim();
//					String number = username.getText().toString().trim();
//					SMSSDK.submitVerificationCode("86", number, code);
					SMSSDK.submitVerificationCode("86", phoneNumber, code);
					Log.i("test", "获得电话号码："+ phoneNumber);
				}
			}
		});
		 username.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if(!hasFocus){
		                if(username.getText().toString().trim().length()!=11){
		                    Toast.makeText(ForgetPasActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
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
//	                        Toast.makeText(ForgetPasActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show(); 
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
			Log.i("test2", "**************进mHandle");
			if (result == SMSSDK.RESULT_COMPLETE) {

				System.out.println("--------result" + event);
				Log.i("test2", "--------result" + event);
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
					Toast.makeText(getApplicationContext(), "提交验证码成功",
							Toast.LENGTH_SHORT).show();
					isSuc = true;
					Log.i("test2", "**************isSuc：" + isSuc);
				}
			} else {
//				Toast.makeText(getApplicationContext(), "提交验证码失败",
//						Toast.LENGTH_SHORT).show();
				isSuc = false;
				Log.i("test2","**************isSuc：" + isSuc);
			}
		}
	};


	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			getBtn.setBackgroundColor(getResources().getColor(
					R.color.text_color_shenhui_677582));
			getBtn.setText("剩余" + msg.what + "秒");
			
			Log.i("test", "msg.what:"+msg.what);

			switch (msg.what) {
			case ERROR:
				getBtn.setClickable(true);
				getBtn.setBackgroundColor(getResources().getColor(
						R.color.text_color_normal));
				getBtn.setText("重新验证");
				//Toast.makeText(getApplicationContext(), "请重新获新验证码", 0).show();
				break;
			case SUCCESS:
				String state = "";
				try {
					JSONObject result = (JSONObject) msg.obj;
					state = result.getString("state");
					Log.i("test2", "修改密码处得到的state:"+ state);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
				if (state.equals("1")) {
					Toast.makeText(getApplicationContext(), "修改成功", 0).show();
					Intent intent = new Intent(ForgetPasActivity.this,
							LoginActivity.class);
					startActivity(intent);
					finish();
				} else if (state.equals("0")) {

					Toast.makeText(getApplicationContext(), "修改失败", 0).show();
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
							System.out.println("进来了");
							Log.i("test2", "进来了");
							InputStream in = conn.getInputStream();
							BufferedReader br = new BufferedReader(
									new InputStreamReader(in));
							String result = br.readLine();
							System.out.println(result);
							Log.i("test2", "进来后获得的result："+result);
							br.close();
							in.close();
							Message msg = Message.obtain();
							JSONObject jsonResultJ = new JSONObject(result);
							msg.obj = jsonResultJ;
							msg.what = SUCCESS;
							Log.i("test2", "进来后获得的状态码："+msg.what);
							handler.sendMessage(msg);
						} else {
							System.out.println("连接失败");
							Message msg = Message.obtain();
							msg.what = ERROR;
							Log.i("test2", "没进去获得的状态码："+msg.what);
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
			Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT)
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
	            Toast.makeText(ForgetPasActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
	        }else if(psw.getText().toString().trim().equals("")){
	            Toast.makeText(ForgetPasActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
	        }else if(!equals){
	            Toast.makeText(ForgetPasActivity.this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
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
