package com.helloar.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.helloarrecording.R;
import com.example.helloarrecording.R.id;
import com.example.helloarrecording.R.string;
import com.helloar.user.MyConst;
import com.helloar.utils.HttpUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FNewsActivity extends Activity {

	protected static final int SUCCESS = 0;
	protected static final int ERROR = 1;
    private int white = 0xFFFFFFFF;
	private TextView tView;
	private WebView wView;
	private JSONArray jsonNews;
	private String ww_url;
	List<String> newsIds = 	new ArrayList<String>();
	List<String> newsTitles = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("test", this.toString());
		setContentView(R.layout.activity_fnews);
		TextView tView = (TextView) findViewById(R.id.f_tv);
		Bundle bundle = this.getIntent().getExtras();
		String name = bundle.getString("uri"); 
		String title=bundle.getString("title");
		tView.setText(title);
		tView.setTextColor(white);
		Log.i("test", "获取到的name值为" + name);
		wView = (WebView) findViewById(R.id.w_wv);
		wView.loadUrl(name);
	}

	public void Nback(View view) {
		Intent intent = new Intent(FNewsActivity.this, FamActivity.class);
		startActivity(intent);
		Log.i("test", "从fnews详页回到列表页");
		finish();
	}

}
