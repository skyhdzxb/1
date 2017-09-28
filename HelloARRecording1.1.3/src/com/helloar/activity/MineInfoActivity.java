package com.helloar.activity;

import com.example.helloarrecording.R;
import com.helloar.fragment.MineFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Dan on 2017/6/5.
 */

public class MineInfoActivity extends Activity implements OnClickListener {
	
	private TextView mineset;
	//private TextView cgphone;
	private TextView cgpassword;
	private TextView clear;
	
	private RelativeLayout mineset_rll;
    //private RelativeLayout cgphone_rll;
    private RelativeLayout cgpassword_rll;
    private RelativeLayout clear_rll;

    private ImageView mback ;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_setting_fg);
        init();
        mback = (ImageView) findViewById(R.id.back_iv);
        mback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent show=new Intent(MineInfoActivity.this,ARMainActivity.class); 
				show.putExtra("mine",1);
				startActivity(show);
				finish();
			}
		});
    }
    
    private void init(){
        mineset_rll = (RelativeLayout) findViewById(R.id.mineset_rl);
       // cgphone_rll = (RelativeLayout) findViewById(R.id.cgphone_rl);
        cgpassword_rll = (RelativeLayout) findViewById(R.id.cgpassword_rl);
        clear_rll = (RelativeLayout) findViewById(R.id.clear_rl);
        mineset = (TextView)findViewById(R.id.mineset_tv);
        //cgphone = (TextView) findViewById(R.id.cgphone_tv);
        cgpassword = (TextView) findViewById(R.id.cgpassword_tv);
        clear = (TextView) findViewById(R.id.clear_tv);

        mineset.setOnClickListener((OnClickListener) this);
       // cgphone.setOnClickListener((OnClickListener) this);
        cgpassword.setOnClickListener((OnClickListener) this);
        clear.setOnClickListener((OnClickListener) this);

    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		  switch (v.getId()){
          case R.id.mineset_tv:
              Intent intent1 = new Intent(MineInfoActivity.this,MineHelpActivity.class);
              startActivity(intent1);
             // finish();
              break;
//          case R.id.cgphone_tv:
//              Intent intent2 = new Intent(MineInfoActivity.this,PhoneSetActivity.class);
//              startActivity(intent2);
//             // finish();
//              break;
          case R.id.cgpassword_tv:
              Intent intent3 = new Intent(MineInfoActivity.this,PasswordCgActivity.class);
              startActivity(intent3);
             // finish();
              break;
          case R.id.clear_tv:
              Toast.makeText(getApplicationContext(), "»º´æÒÑÇå³ý£¡",
              Toast.LENGTH_SHORT).show();
              break;
//        case R.id.phoneset_back_iv:
//			  Intent i = new Intent(MineInfoActivity.this,ARMainActivity.class);
//			  //i.setClass(MineInfoActivity.this, MineFragment.class);
//			  startActivity(i);
          default:
      }	  
  }
}

