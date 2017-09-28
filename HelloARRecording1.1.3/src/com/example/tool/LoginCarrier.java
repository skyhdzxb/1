package com.example.tool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * 鐧诲綍鍣ㄨ浇浣�
 * 
 * @author bzl
 * 
 */
public class LoginCarrier implements Parcelable {
	public String mTargetAction;
	public Bundle mbundle;

	public LoginCarrier(String target) {
		mTargetAction = target;
		Log.i("test","LoginCarries中的target:"+mTargetAction);
		
	}

	/**
	 * 鐩爣activity
	 * 
	 * @param ctx
	 */
	public void invoke(Context ctx) {
		Class class1 = null;
		try {
			class1 = Class.forName(mTargetAction);
			Log.i("test","**********");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Log.i("test","**********class1:"+class1);
		Intent intent = new Intent(mTargetAction);
		//intent.addCategory(mTargetAction);
		Log.i("test","**********Intent:"+intent);
		Log.i("test","**********ctx:"+ctx);
		intent.putExtra("mine",1);
		Log.i("test", "**************给Intent传值");
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);	
		ctx.startActivity(intent);
		Log.i("test", "**************启动了Intent");
		
	}



	public LoginCarrier(Parcel parcel) {
		// 鎸夊彉閲忓畾涔夌殑椤哄簭璇诲彇
		mTargetAction = parcel.readString();
		mbundle = parcel.readParcelable(Bundle.class.getClassLoader());
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		// 鎸夊彉閲忓畾涔夌殑椤哄簭鍐欏叆
		parcel.writeString(mTargetAction);
		parcel.writeParcelable(mbundle, flags);
	}

	public static final Parcelable.Creator<LoginCarrier> CREATOR = new Parcelable.Creator<LoginCarrier>() {

		@Override
		public LoginCarrier createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new LoginCarrier(source);
		}

		@Override
		public LoginCarrier[] newArray(int arg0) {
			// TODO Auto-generated method stub
			return new LoginCarrier[arg0];
		}
	};
}
