package com.example.tool;

import java.io.ObjectOutputStream.PutField;

import cn.easyar.Target;

import com.helloar.user.LoginActivity;
import com.helloar.activity.ARMainActivity;
import com.helloar.activity.MineInfoActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


/** 
* 登录判断类 
*  
* @author bzl 
*  
*/  
public  class LoginInterceptor {
	public static final String mINVOKER = "INTERCEPTOR_INVOKER";

    /** 
     * 判断处理 
     *  
     * @param ctx 
     *            当前activity的上下文 
     * @param target 
     *            目标activity的target 
     * @param params 
     *            目标activity所需要的参数 
     * @param intent 
     *            目标activity 
     *  
     */  
//	public static void interceptor(Context ctx, String target, Intent intent) {
//		if (target!=null) {
//			LoginCarrier invoker = new LoginCarrier(target);
//			Log.i("test", "LoginInterceptor里面的invoker:"+invoker);
//			if (getLogin()) {
//				Log.i("test","进入已经登陆过的LoginInterceptor:"+ARMainActivity.is_login);
//				invoker.invoke(ctx);
//				Log.i("test","LoginInterceptor中的invoker执行了");
//			} else {
//				if (intent == null) {
//					intent = new Intent(ctx, LoginActivity.class);
//				}
//				login(ctx, invoker, intent);
//			}
//		} else {
//			Toast.makeText(ctx, "您还未登录", 300).show();
//		}
//	}
//
//
//    /** 
//     * 登录判断 
//     *  
//     * @param ctx 
//     *            当前activity的上下文 
//     * @param target 
//     *            目标activity的target 
//     * @param params 
//     *            目标activity所需要的参数 
//     */  
////	public static void interceptor(Context ctx, String string, Bundle bundle) {
////		interceptor(ctx, string, bundle,null);
////	}
//
//	// 杩欓噷鑾峰彇鐧诲綍鐘舵�侊紝鍏蜂綋鑾峰彇鏂规硶鐪嬮」鐩叿浣撶殑鍒ゆ柇鏂规硶
//	private  static boolean getLogin() {
//	    //Log.i("test","LoginInterceptor:"+ARMainActivity.is_login);
////		SharedPreferences sPreferences=getSharedPreferences("config", MODE_PRIVATE);
////		Boolean isLogin=sPreferences.getBoolean("is_login",false);
////		return isLogin;
//		return ARMainActivity.is_login;
//	  
//		
//	}
//
//	private static void login(Context context, LoginCarrier invoker, Intent intent) {
//		intent.putExtra(mINVOKER, invoker);
//		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		context.startActivity(intent);
//	}
}
