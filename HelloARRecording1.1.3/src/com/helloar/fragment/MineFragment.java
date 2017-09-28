package com.helloar.fragment;

import javax.security.auth.PrivateCredentialPermission;

import com.android.image.ImageShowActivity;
import com.example.helloarrecording.R;
import com.helloar.activity.ARMainActivity;
import com.helloar.activity.MineInfoActivity;
import com.helloar.activity.MineHelpActivity;
import com.helloar.user.LoginActivity;
import com.helloar.user.RegisterActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Dan on 2017/6/1.
 */

public  class MineFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "MineFragment";
    private TextView mine_info;
    private TextView mine_photo;
   // private TextView mine_video;
    private TextView mine_setting;

    private Button quitButton;

    private RelativeLayout mine_info_rll;
    private RelativeLayout mine_photo_rll;
    //private RelativeLayout mine_video_rll;
    private RelativeLayout mine_setting_rll;
    
    
    
    

    public static MineFragment newInstance(){
        MineFragment mineFragment = new MineFragment();
        return mineFragment;
    }

    @Override
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
        View view = inflater.inflate(R.layout.minefg, container,false);
        find(view);
        return view;
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    private void find(View view){
        mine_info_rll = (RelativeLayout) view.findViewById(R.id.mine_info_rl);
        mine_photo_rll = (RelativeLayout) view.findViewById(R.id.mine_photo_rl);
       // mine_video_rll = (RelativeLayout) view.findViewById(R.id.mine_video_rl);
        mine_setting_rll = (RelativeLayout) view.findViewById(R.id.mine_setting_rl);
        mine_info = (TextView) view.findViewById(R.id.mine_info_tv);
        mine_photo = (TextView) view.findViewById(R.id.mine_photo_tv);
        //mine_video = (TextView) view.findViewById(R.id.mine_video_tv);
        mine_setting = (TextView) view.findViewById(R.id.mine_setting_tv);
        quitButton = (Button) view.findViewById(R.id.quit_btn);
        mine_info_rll.setOnClickListener(this);
        mine_photo_rll.setOnClickListener(this);
       // mine_video_rll.setOnClickListener(this);
        mine_setting_rll.setOnClickListener(this);
        quitButton.setOnClickListener(this);
        //login.setOnClickListener(this);
       // register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mine_info_rl:
                Intent intent1 = new Intent(getActivity(),MineInfoActivity.class);
                startActivity(intent1);
                break;
            case R.id.mine_photo_rl:
                Intent intent2 = new Intent(getActivity(),ImageShowActivity.class);
                startActivity(intent2);
                break;
//            case R.id.mine_video_rl:
//                Intent intent3 = new Intent(getActivity(),MineInfoActivity.class);
//                startActivity(intent3);
//                break;
            case R.id.mine_setting_rl:
                Intent intent4 = new Intent(getActivity(),MineHelpActivity.class);
                startActivity(intent4);
                break;
            case R.id.quit_btn:
//            	ARMainActivity.is_login = false;
//            	Log.i("test1","***************退出登录后的is_login:"+ARMainActivity.is_login);
            	SharedPreferences sPreferences = getActivity().getSharedPreferences("config",0x0000);
				Boolean isLogin1 = sPreferences.edit().putBoolean("is_login", false).commit();
				Log.i("test1","退出登陆后的isLogin:"+isLogin1);
                Intent intent5 = new Intent(getActivity(),ARMainActivity.class);
                intent5.putExtra("mine",2);
                startActivity(intent5);
                break;
            default:
                break;
        }
    }
}
