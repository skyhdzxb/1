package com.helloar.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

import com.example.helloarrecording.R;
import com.helloar.user.LoginActivity;

public class SplashActivity extends Activity {

	private LinearLayout rl_main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		rl_main = (LinearLayout) findViewById(R.id.rl_activity_main);
		AlphaAnimation aa = new AlphaAnimation(0f, 1f);
		aa.setDuration(2000);
		aa.setFillAfter(true);
		rl_main.startAnimation(aa);

//		SharedPreferences preferences = getSharedPreferences("isFirstUse",
//				MODE_WORLD_READABLE);
//
//		Boolean isFirstUse = preferences.getBoolean("isFirstUse", true);
//		if (isFirstUse) {
//			// 如果第一次进，展示欢迎页
			aa.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(SplashActivity.this,
							ARMainActivity.class);
					startActivity(intent);
					finish();
				}
			});
//			// 标记为已经进过欢迎页了
//			Editor editor = preferences.edit();
//			editor.putBoolean("isFirstUse", false);
//			editor.commit();
//		} else {
//			// 直接startactivity进入主页面
//			Intent intent = new Intent(SplashActivity.this,
//					ARMainActivity.class);
//			startActivity(intent);
//		}

	}

}
