package com.helloar.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.helloarrecording.R;

/**
 * Created by Dan on 2017/6/1.
 */

public  class ARFragment extends BaseFragment implements View.OnClickListener {

    //private TextView arbtn ;
	private RelativeLayout arrl;
	private View view;

    public static ARFragment newInstance(){
        ARFragment arFragment = new ARFragment();
        return arFragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.arfg, container,false);
        //arbtn = (TextView) view.findViewById(R.id.ar_btn);
        //arbtn.setOnClickListener(this);
        arrl = (RelativeLayout) view.findViewById(R.id.ar_rl);
        arrl.setOnClickListener(this);
        initAni();
        return view;
    }
    private void initAni() {
    	ImageView iv_in=(ImageView) view.findViewById(R.id.iv_ani_inside);
    	ImageView iv_mid=(ImageView) view.findViewById(R.id.iv_ani_middle);
    	ImageView iv_out=(ImageView) view.findViewById(R.id.iv_ani_outside);
    	
    	AlphaAnimation aa=new AlphaAnimation(0,1f);
    	aa.setRepeatMode(Animation.REVERSE);
    	aa.setRepeatCount(Animation.INFINITE);
    	RotateAnimation ra=new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    	RotateAnimation ra2=new RotateAnimation(0, -359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    	aa.setDuration(1000);
    	ra.setDuration(2000);
    	ra2.setDuration(3000);
    	ra2.setRepeatMode(Animation.RESTART);
    	ra.setRepeatCount(Animation.INFINITE);
    	ra2.setRepeatCount(Animation.INFINITE);
    	LinearInterpolator lin=new LinearInterpolator();
    	aa.setInterpolator(lin);
    	ra.setInterpolator(lin);
    	ra2.setInterpolator(lin);
    	iv_in.startAnimation(aa);
    	iv_mid.startAnimation(ra);
    	iv_out.startAnimation(ra2);
	}
    public String getFragmentName() {
        return null;
    }
    @Override
    public void onClick(View v) {
       Intent intent = new Intent(getActivity(),com.example.helloarrecording.MainActivity.class);
       startActivity(intent);
    }

}
