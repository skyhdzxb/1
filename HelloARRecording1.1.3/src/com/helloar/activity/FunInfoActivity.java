package com.helloar.activity;

import com.example.helloarrecording.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class FunInfoActivity extends Activity{
	private ImageView funback ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_funinfo);
        funback = (ImageView) findViewById(R.id.funinfo_back_iv);
        funback.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//finish();
				Intent i = new Intent(FunInfoActivity.this, MineHelpActivity.class); 
				startActivity(i);
				
			}
        	
        });
    }
}

