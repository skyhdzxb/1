package com.helloar.activity;

import com.example.helloarrecording.R;
import com.example.tool.LoginCarrier;
import com.example.tool.LoginInterceptor;
import com.helloar.fragment.ARFragment;
import com.helloar.fragment.FoundFragment;
import com.helloar.fragment.MineFragment;
import com.helloar.user.LoginActivity;

import android.R.integer;
import android.R.transition;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Created by Dan on 2017/6/1.
 */

public class ARMainActivity extends FragmentActivity implements
		View.OnClickListener {

	//public static boolean is_login = false;

	// 定义3个Fragment对象；
	private FoundFragment ff;
	private ARFragment af;
	private MineFragment mf;
	// 帧布局对象，就是用来存放Fragment的容器
	private FrameLayout flayout;
	// 定义底部导航的三个布局
	private RelativeLayout found_layout;
	private RelativeLayout ar_layout;
	private RelativeLayout mine_layout;
	// 定义底部导航栏中的ImageView和TextView
	private ImageView found_image;
	private ImageView ar_image;
	private ImageView mine_image;
	private TextView found_text;
	private TextView ar_text;
	private TextView mine_text;
	// 定义要使用到的颜色值
	private int white = 0xFFFFFFFF;
	private int gray = 0xFF7597B3;
	private int blue = 0xFF0AB2FB;
	private int red = 0xFFFF6666;
	// 定义FragmentManager对象
	FragmentManager fManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("test1", "***************这里是ARMainActivity");
		setContentView(R.layout.ar_main);
		fManager = getSupportFragmentManager();
		initViews();
		trans();

	}
//	@Override
//	protected void onNewIntent(Intent intent) {
//		// TODO Auto-generated method stub
//		super.onNewIntent(intent);
//	    setIntent(intent);  
//	    int id = intent.getIntExtra("mine", -1);
//	    if (id==1) {
//	    	FragmentTransaction transaction = fManager.beginTransaction();
//			transaction.replace(R.id.content, new MineFragment());
//			transaction.commit();// 这里是指定跳转到指定的fragment
//		}
//	}

	private void trans() {
		// TODO Auto-generated method stub
		Intent intert = getIntent();
		int id = intert.getIntExtra("mine", -1);
		
		if (id > 0) {
			System.out.println("aaa" + id);
			if (id == 1) {
				FragmentTransaction transaction = fManager.beginTransaction();
				transaction.replace(R.id.content, new MineFragment());
				transaction.commit();// 这里是指定跳转到指定的fragment
			}
			if (id == 2) {
				FragmentTransaction transaction = fManager.beginTransaction();
				transaction.replace(R.id.content, new ARFragment());
				transaction.commit();// 这里是指定跳转到指定的fragment
			}
			if (id == 3) {
				FragmentTransaction transaction = fManager.beginTransaction();
				transaction.replace(R.id.content, new FoundFragment());
				transaction.commit();// 这里是指定跳转到指定的fragment
			}
		}

	}

	// 完成组建的初始化

	public void initViews() {
		found_image = (ImageView) findViewById(R.id.found_image);
		ar_image = (ImageView) findViewById(R.id.ar_image);
		mine_image = (ImageView) findViewById(R.id.mine_image);
		found_text = (TextView) findViewById(R.id.found_text);
		ar_text = (TextView) findViewById(R.id.ar_text);
		mine_text = (TextView) findViewById(R.id.mine_text);
		found_layout = (RelativeLayout) findViewById(R.id.found_layout);
		ar_layout = (RelativeLayout) findViewById(R.id.ar_layout);
		mine_layout = (RelativeLayout) findViewById(R.id.mine_layout);
		found_layout.setOnClickListener(this);
		ar_layout.setOnClickListener(this);
		mine_layout.setOnClickListener(this);
		ar_layout.setSelected(true);

		// 初始化顶部的Fragment
		FragmentTransaction transaction = fManager.beginTransaction();
		transaction.replace(R.id.content, new ARFragment());
		transaction.commit();
	}

	// 重写onClick事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.found_layout:
			setChioceItem(0);
			break;
		case R.id.ar_layout:
			setChioceItem(1);
			break;
		case R.id.mine_layout:
			//Log.i("test", "ARMainActivity:" + ARMainActivity.is_login);
			interceptor(ARMainActivity.this,"com.helloar.activity.ACTION_START", null);
			Log.i("test1", "ARMainActivity:执行了interceptor");
			setChioceItem(2);
			break;
		default:
			break;
		}

	}

	// 定义一个选中一个item后的处理
	private void setChioceItem(int i) {
		// 重置选项+隐藏所有Fragment
		FragmentTransaction transaction = fManager.beginTransaction();
		clearChioce();
		hideFragments(transaction);
		switch (i) {
		case 0:
			found_image.setImageResource(R.drawable.scan_found_pressed);
			found_text.setTextColor(red);
			// found_layout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);
			if (ff == null) {
				// 如果ff为空，则创建一个并添加到界面上
				ff = new FoundFragment();
				transaction.add(R.id.content, ff);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(ff);
			}
			break;

		case 1:
			ar_image.setImageResource(R.drawable.scan_scan_pressed);
			ar_text.setTextColor(red);
			// ar_layout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);
			if (af == null) {
				// 如果fg1为空，则创建一个并添加到界面上
				af = new ARFragment();
				transaction.add(R.id.content, af);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(af);
			}
			break;

		case 2:
			mine_image.setImageResource(R.drawable.scan_me_pressed);
			mine_text.setTextColor(red);
			// mine_layout.setBackgroundResource(R.drawable.ic_tabbar_bg_click);
			if (mf == null) {
				// 如果fg1为空，则创建一个并添加到界面上
				mf = new MineFragment();
				transaction.add(R.id.content, mf);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(mf);
			}
			break;
		}
		transaction.commit();

	}

	private void hideFragments(FragmentTransaction transaction) {
		if (ff != null) {
			transaction.hide(ff);
		}
		if (af != null) {
			transaction.hide(af);
		}
		if (mf != null) {
			transaction.hide(mf);
		}
	}

	private void clearChioce() {
		found_image.setImageResource(R.drawable.scan_found_normal);
		found_layout.setBackgroundColor(white);
		found_text.setTextColor(gray);

		ar_image.setImageResource(R.drawable.scan_scan_normal);
		ar_layout.setBackgroundColor(white);
		ar_text.setTextColor(gray);

		mine_image.setImageResource(R.drawable.scan_me_normal);
		mine_layout.setBackgroundColor(white);
		mine_text.setTextColor(gray);
	}
	
	 public void interceptor(Context ctx, String target, Intent intent) {
		if (target!=null) {
			LoginCarrier invoker = new LoginCarrier(target);
			Log.i("test1", "ARMainActivity:进入了LoginCarrier");
			//Log.i("test", "LoginInterceptor里面的invoker:"+invoker);
			if (getLogin()) {
				invoker.invoke(ctx);
				Log.i("test1", "已登录直接跳转");
			} else {
//				if (intent == null) {
//					intent = new Intent(ctx, LoginActivity.class);
//				}
				login(ctx);
				Log.i("test1", "跳转到登陆界面");
			}
		} else {
			Toast.makeText(ctx, "您还未登录", 300).show();
		}
	}


    /* 登录判断  */  

	private void login(Context ctx) {
		Intent intent = new Intent(ctx,LoginActivity.class);
		ctx.startActivity(intent);
	}
	private  boolean getLogin() {
		SharedPreferences sPreferences=getSharedPreferences("config", MODE_PRIVATE);
		Boolean isLogin=sPreferences.getBoolean("is_login",false);
		Log.i("test1", "is_login:"+isLogin);
		return isLogin;
	}
}
