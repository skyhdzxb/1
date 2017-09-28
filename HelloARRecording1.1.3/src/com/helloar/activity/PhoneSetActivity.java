package com.helloar.activity;

import com.example.helloarrecording.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Dan on 2017/6/5.
 */

public class PhoneSetActivity extends Activity{
	private ImageView back ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_setting);
        back = (ImageView) findViewById(R.id.phoneset_back_iv);
        back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//finish();
				Intent i = new Intent(PhoneSetActivity.this, MineInfoActivity.class); 
				startActivity(i);
			}
        	
        });
    }
}
