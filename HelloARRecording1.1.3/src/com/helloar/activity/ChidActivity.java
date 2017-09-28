package com.helloar.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.helloarrecording.MainActivity;
import com.example.helloarrecording.R;
import com.helloar.user.MyConst;
import com.helloar.utils.HttpUtils;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChidActivity extends Activity{
	
	private ListView lv_Title;
	private ArrayAdapter<String> lv_Adapter;
	protected static final int SUCCESS = 0;
	protected static final int ERROR = 1;
	private JSONArray jsonNews;
	private ImageView backIV;
	private ImageView cView;
	List<String> newsTitles = new ArrayList<String>(); 
	List<String> newsIds = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("test", this.toString());
		setContentView(R.layout.found_tianlunzhile);
		cView = (ImageView) findViewById(R.id.c_iv);
		byte buff[] = this.getIntent().getByteArrayExtra("image");
	    Bitmap bitmap = BitmapFactory.decodeByteArray(buff, 0, buff.length);  
	    cView.setImageBitmap(bitmap);  
		backIV= (ImageView) findViewById(R.id.c_back_iv);
		backIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1=new Intent(ChidActivity.this,ARMainActivity.class); 
				intent1.putExtra("mine",3);
				startActivity(intent1);
				finish();
			}
		});
		
		initData();
		lv_Title = (ListView) findViewById(R.id.ctitle_lv);
		lv_Title.setOnItemClickListener(new OnItemClickListener() {
//点击事件
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ChidActivity.this,CNewsActivity.class);
				//用Bundle携带数据
                Bundle bundle=new Bundle();
                //想根据这个url，跳转到相应界面
                bundle.putString("uri", newsIds.get(position));
                bundle.putString("title", newsTitles.get(position));
               // Log.i("test","position:"+newsIds.get(position));
                intent.putExtras(bundle);
				//intent.putExtra("position", position);
				startActivity(intent);
			}
		});
	}

		Handler handler=new Handler(){
			public void handleMessage(Message msg){
				switch (msg.what) {
				case SUCCESS:				
					try {
						
						JSONArray news=(JSONArray) msg.obj;
					    for(int i=0;i<news.length();i++){
					    	String title=news.getJSONObject(i).getString("title");
					    	String id=news.getJSONObject(i).getString("id");
					    	Log.i("test", "id:"+id);
					    	newsIds.add(MyConst.NEWS_URL+id);
					    	Log.i("test", "id:"+MyConst.NEWS_URL+id);
					    	Log.i("test", "id:"+newsIds);
					    	
					    	newsTitles.add(title);
					    }					    
	                   lv_Title.setAdapter(new MyAdapter());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case ERROR:
					Toast.makeText(getApplicationContext(), "加载失败", 0).show();
					break;
				default:
					break;
				}
			}
		};

//		                  List<String> NewsTitles = new ArrayList<String>(); 
//		                  NewsTitles.add(newsObj.getString("title"));
	

	private void initData() {
		Log.i("test", "test:");
		// TODO Auto-generated method stub
		new Thread(){
			private Message msg;
			public void run(){
				try {
					 URL url=new URL(MyConst.C_URL);
					 HttpURLConnection connection=(HttpURLConnection) url.openConnection();
					 connection.setRequestMethod("GET");
					 connection.setConnectTimeout(5000);
					 int code=connection.getResponseCode();
					
					 Log.i("test","response code:"+ code);
					 if(code==200){
						 System.out.println("ok");
						 InputStream in=connection.getInputStream();
						 BufferedReader br=new BufferedReader(new InputStreamReader(in));
						 String result=br.readLine();
						 Log.i("test", "result:"+result);
						 System.out.println(result);
						 br.close();
						 in.close();
						 jsonNews = new JSONArray(result);
						 msg = Message.obtain();
						 msg.what=SUCCESS;
						 msg.obj=jsonNews;
						 handler.sendMessage(msg);
					 }else{
						msg=Message.obtain();
						msg.what=ERROR;
					 }
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					msg=Message.obtain();
					msg.what=ERROR;
				} 
			}
		}.start();
	}
	private class MyAdapter extends BaseAdapter{

		private TextView tv_title;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return jsonNews.length();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view =View.inflate(getApplicationContext(), R.layout.item_news_title, null);
			tv_title = (TextView) view.findViewById(R.id.tv_title);
			tv_title.setText(newsTitles.get(position));
			return view;
		}
		
	}
}
