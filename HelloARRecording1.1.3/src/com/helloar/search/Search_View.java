package com.helloar.search;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.helloarrecording.MainActivity;
import com.example.helloarrecording.R;
import com.helloar.activity.ARMainActivity;
import com.helloar.user.MyConst;
import com.helloar.user.TUserBean;
import com.helloar.user.UserBean;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
public class Search_View extends LinearLayout {

    private Context context;
	protected static final int SUCCESS = 0;
	protected static final int ERROR = 1;

    /*UI组件*/
    private TextView tv_clear;
    private EditText et_search;
    private TextView tv_tip;
    private ImageView iv_search;
    private ImageView iv_back;

    /*列表及其适配器*/
    private Search_Listview listView;
    private BaseAdapter adapter;

    /*数据库变量*/
    private RecordSQLiteOpenHelper helper ;
    private SQLiteDatabase db;
    
    
    public interface backClickListener{
    	void backIvClick();
	}
   
    private backClickListener listener;
    
    public void setBackClickListener(backClickListener ls) {
		this.listener = ls;
	}


    /*三个构造函数*/
    //在构造函数里直接对搜索框进行初始化 - init()
    public Search_View(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public Search_View(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public Search_View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }


    /*初始化搜索框*/
    private void init(){

        //初始化UI组件
        initView();


        //实例化数据库SQLiteOpenHelper子类对象
        helper = new RecordSQLiteOpenHelper(context);

        // 第一次进入时查询所有的历史记录
        queryData("");

        //"清空搜索历史"按钮
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //清空数据库
                deleteData();
                queryData("");
            }
        });

        //搜索框的文本变化实时监听
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //输入后调用该方法
            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().trim().length() == 0) {
                    //若搜索框为空,则模糊搜索空字符,即显示所有的搜索历史
                    tv_tip.setText("搜索历史");
                } else {
                    tv_tip.setText("搜索结果");
                }

                //每次输入后都查询数据库并显示
                //根据输入的值去模糊查询数据库中有没有数据
                String tempName = et_search.getText().toString();
                queryData(tempName);

            }
        });


        // 搜索框的键盘搜索键
        // 点击回调
        et_search.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键

            // 修改回车键功能
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // 隐藏键盘，这里getCurrentFocus()需要传入Activity对象，如果实际不需要的话就不用隐藏键盘了，免得传入Activity对象，这里就先不实现了
//                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
//                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                    boolean hasData = hasData(et_search.getText().toString().trim());
                    if (!hasData) {
                        insertData(et_search.getText().toString().trim());

                        queryData("");
                    }
                    //根据输入的内容模糊查询商品，并跳转到另一个界面，这个需要根据需求实现
                    Toast.makeText(context, "点击搜索", Toast.LENGTH_SHORT).show();

                }
                return false;
            }
        });



        //列表监听
        //即当用户点击搜索历史里的字段后,会直接将结果当作搜索字段进行搜索
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //获取到用户点击列表里的文字,并自动填充到搜索框内
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                final String name = textView.getText().toString();
                et_search.setText(name);
            	new Thread(){
        			public void run(){
        				try {
        					URL url=new URL(MyConst.TLOGIN_URL+name);
        					Log.i("test4", "搜索出的url："+url);
        					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        					conn.setRequestMethod("GET");
        					conn.setConnectTimeout(5000);
        					int code = conn.getResponseCode();
        					Log.i("test4", "搜索出的code："+code);
        					if(code==200){
            					System.out.println("进来了");
            					Log.i("test4", "进来了");
            					InputStream in = conn.getInputStream();
            					BufferedReader br=new BufferedReader(new InputStreamReader(in));
            					String result=br.readLine();
            					Log.i("test4", "搜索出的result："+result);
            					JSONArray obArray = new JSONArray(result);
            					//JSONObject object = new JSONObject(result);
            					
            					//FileOutputStream out= openFileOutput("targetJson.json",Context.MODE_PRIVATE);  
            					FileOutputStream out = getContext().openFileOutput("ttargetJson.json",Context.MODE_PRIVATE);
            					out.write(result.getBytes());   					
            					out.close();
            					br.close();
            					in.close();   					
            					Message msg=Message.obtain();
            					msg.obj = obArray;
            					msg.what=SUCCESS;
            					Log.i("test4", "msg.what："+msg.what);
            					Log.i("test4", "搜索出的msg.obj："+msg.obj);
            					handler.sendMessage(msg);
            				}else{
            					Log.i("test4", "出错啦");
            				System.out.println("出错");
            				Message msg=Message.obtain();
            				msg.what=ERROR;
            				Log.i("test4", "msg.what："+msg.what);
            				handler.sendMessage(msg);
            				}
            					} catch (Exception e) {
            				// TODO Auto-generated catch block
            					e.printStackTrace();
            					Message msg=Message.obtain();
                				msg.what=ERROR;
                				handler.sendMessage(msg);
            					}
            		}
        		}.start();
            }
        });

//        // 插入数据，便于测试，否则第一次进入没有数据怎么测试呀？
//        Date date = new Date();
//        long time = date.getTime();
//        insertData("Leo" + time);

        //点击搜索按钮后的事件
        iv_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasData = hasData(et_search.getText().toString().trim());
                if (!hasData) {
                    insertData(et_search.getText().toString().trim());

                    //搜索后显示数据库里所有搜索历史是为了测试
                    queryData("");
                }

                //根据输入的内容模糊查询商品，并跳转到另一个界面，这个根据需求实现
                //Toast.makeText(context, "clicked!", Toast.LENGTH_SHORT).show();
            }
        });
         
        iv_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.backIvClick();
				
			}
		});
    }

  
    public String getAccount() {
    	return(et_search.getText().toString().trim());	
	}


    protected void finish() {
		// TODO Auto-generated method stub
		
	}

	/**
     * 封装的函数
     */

    /*初始化组件*/
    private void initView(){
        LayoutInflater.from(context).inflate(R.layout.search_layout,this);
        et_search = (EditText) findViewById(R.id.et_search);
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        listView = (Search_Listview) findViewById(R.id.listView);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        iv_back =  (ImageView) findViewById(R.id.iv_searchback);
    }

    /*插入数据*/
    private void insertData(String tempName) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + tempName + "')");
        db.close();
    }

    /*模糊查询数据 并显示在ListView列表上*/
    private void queryData(String tempName) {

        //模糊搜索
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
        // 创建adapter适配器对象,装入模糊搜索的结果
        adapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1, cursor, new String[] { "name" },
                new int[] { android.R.id.text1 }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // 设置适配器
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /*检查数据库中是否已经有该条记录*/
    private boolean hasData(String tempName) {
        //从Record这个表里找到name=tempName的id
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        return cursor.moveToNext();
    }

    /*清空数据*/
    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }
    Handler handler=new Handler(){
		public void handleMessage(Message msg){
			 Log.i("test4"," Handler得到的msg.what："+msg.what);
			switch(msg.what){
			case ERROR:
				 Toast.makeText(context, "该用户名不存在", Toast.LENGTH_SHORT).show();
				 Log.i("test4"," 进入error");
				 break;
			case SUCCESS:
				Log.i("test4"," 进入success");
//				SharedPreferences sPreferences=getContext().getSharedPreferences("config", 0x0000);
//				Boolean isLogin= sPreferences.edit().putBoolean("is_login",false).commit();
//			    Log.i("test7"," isLogin："+isLogin);
				load();
				Toast.makeText(context, "搜索成功", Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(getContext(),ARMainActivity.class);
				intent.putExtra("mine",2);
				getContext().startActivity(intent);		
				finish();
				break;
			}
		}		
	};
	private void load() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		File file = new File(MyConst.FILE_PATH);
		File[] listFiles = file.listFiles();

		for (int i = 0; i < listFiles.length; i++) {
			if ((""+listFiles[i]).endsWith(".jpg")) {
			File delFile = listFiles[i].getAbsoluteFile();
			delFile.delete();
			}
		}
		final TUserBean tbean=new TUserBean("ss");
		tbean.setImageNames();
		tbean.setVideoNamges();
		Log.i("test4"," 搜索处VideoCount()："+tbean.getVideoCount());
		System.out.println(tbean.getVideoCount());
		new Thread(){
			public void run(){
				for(int i=0;i<tbean.getVideoCount();i++){
					try {
						System.out.println("ss");
						URL url=new URL(MyConst.BASE_URL1+tbean.imageName.get(i));
						Log.i("test4"," 搜索处的url："+url);
						System.out.println(MyConst.BASE_URL1+tbean.imageName.get(i));
						HttpURLConnection conn=(HttpURLConnection) url.openConnection();
						System.out.println("打开");
						conn.setRequestMethod("GET");
						System.out.println("GET");
						conn.setConnectTimeout(5000);
						System.out.println("5000");
						int code =conn.getResponseCode();
						System.out.println("code");
						if(code==200){
							System.out.println("进来了");
							FileOutputStream fs=new FileOutputStream(MyConst.FILE_PATH+"s"+i+".jpg");
							Log.i("test4"," 搜索处的fs："+fs);
							InputStream in=conn.getInputStream();
							byte[] buffer=new byte[1024];
							int length;			
							while((length=in.read(buffer, 0, buffer.length))!=-1){
								fs.write(buffer,0,length);
							}
							fs.close();
							in.close();
						}
						else{
							System.out.println("没进来");
						}
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						System.out.println("ioexception");
						e.printStackTrace();
					}
					}
			}
		}.start();
	}
	
}
