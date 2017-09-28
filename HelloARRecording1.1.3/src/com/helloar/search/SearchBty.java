package com.helloar.search;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.helloarrecording.MainActivity;
import com.example.helloarrecording.R;
import com.helloar.search.Search_View.backClickListener;

public class SearchBty extends Activity {

	protected static final int SUCCESS = 0;
	protected static final int ERROR = 1;
    private Search_View sView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        sView = (Search_View) findViewById(R.id.search_layout);
        sView.setBackClickListener(new backClickListener() {
			
			@Override
			public void backIvClick() {
				// TODO Auto-generated method stub
				Intent i = new Intent (SearchBty.this,MainActivity.class);
				startActivity(i);
				finish();
			}
		});
	}
}