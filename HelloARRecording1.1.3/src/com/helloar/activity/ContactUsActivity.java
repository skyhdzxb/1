package com.helloar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.example.helloarrecording.R;

public class ContactUsActivity extends Activity{
	private ImageView cback ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_contact);
        cback = (ImageView) findViewById(R.id.contact_back_iv);
        cback.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//finish();
				Intent i = new Intent(ContactUsActivity.this, MineHelpActivity.class); 
				startActivity(i);
			
			}
        	
        });
    }
}

