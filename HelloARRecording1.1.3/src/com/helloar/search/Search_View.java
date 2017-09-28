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

    /*UI���*/
    private TextView tv_clear;
    private EditText et_search;
    private TextView tv_tip;
    private ImageView iv_search;
    private ImageView iv_back;

    /*�б���������*/
    private Search_Listview listView;
    private BaseAdapter adapter;

    /*���ݿ����*/
    private RecordSQLiteOpenHelper helper ;
    private SQLiteDatabase db;
    
    
    public interface backClickListener{
    	void backIvClick();
	}
   
    private backClickListener listener;
    
    public void setBackClickListener(backClickListener ls) {
		this.listener = ls;
	}


    /*�������캯��*/
    //�ڹ��캯����ֱ�Ӷ���������г�ʼ�� - init()
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


    /*��ʼ��������*/
    private void init(){

        //��ʼ��UI���
        initView();


        //ʵ�������ݿ�SQLiteOpenHelper�������
        helper = new RecordSQLiteOpenHelper(context);

        // ��һ�ν���ʱ��ѯ���е���ʷ��¼
        queryData("");

        //"���������ʷ"��ť
        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //������ݿ�
                deleteData();
                queryData("");
            }
        });

        //��������ı��仯ʵʱ����
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //�������ø÷���
            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().trim().length() == 0) {
                    //��������Ϊ��,��ģ���������ַ�,����ʾ���е�������ʷ
                    tv_tip.setText("������ʷ");
                } else {
                    tv_tip.setText("�������");
                }

                //ÿ������󶼲�ѯ���ݿⲢ��ʾ
                //���������ֵȥģ����ѯ���ݿ�����û������
                String tempName = et_search.getText().toString();
                queryData(tempName);

            }
        });


        // ������ļ���������
        // ����ص�
        et_search.setOnKeyListener(new View.OnKeyListener() {// ������󰴼����ϵ�������

            // �޸Ļس�������
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    // ���ؼ��̣�����getCurrentFocus()��Ҫ����Activity�������ʵ�ʲ���Ҫ�Ļ��Ͳ������ؼ����ˣ���ô���Activity����������Ȳ�ʵ����
//                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
//                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    // �����������󽫵�ǰ��ѯ�Ĺؼ��ֱ�������,����ùؼ����Ѿ����ھͲ�ִ�б���
                    boolean hasData = hasData(et_search.getText().toString().trim());
                    if (!hasData) {
                        insertData(et_search.getText().toString().trim());

                        queryData("");
                    }
                    //�������������ģ����ѯ��Ʒ������ת����һ�����棬�����Ҫ��������ʵ��
                    Toast.makeText(context, "�������", Toast.LENGTH_SHORT).show();

                }
                return false;
            }
        });



        //�б����
        //�����û����������ʷ����ֶκ�,��ֱ�ӽ�������������ֶν�������
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //��ȡ���û�����б��������,���Զ���䵽��������
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                final String name = textView.getText().toString();
                et_search.setText(name);
            	new Thread(){
        			public void run(){
        				try {
        					URL url=new URL(MyConst.TLOGIN_URL+name);
        					Log.i("test4", "��������url��"+url);
        					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        					conn.setRequestMethod("GET");
        					conn.setConnectTimeout(5000);
        					int code = conn.getResponseCode();
        					Log.i("test4", "��������code��"+code);
        					if(code==200){
            					System.out.println("������");
            					Log.i("test4", "������");
            					InputStream in = conn.getInputStream();
            					BufferedReader br=new BufferedReader(new InputStreamReader(in));
            					String result=br.readLine();
            					Log.i("test4", "��������result��"+result);
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
            					Log.i("test4", "msg.what��"+msg.what);
            					Log.i("test4", "��������msg.obj��"+msg.obj);
            					handler.sendMessage(msg);
            				}else{
            					Log.i("test4", "������");
            				System.out.println("����");
            				Message msg=Message.obtain();
            				msg.what=ERROR;
            				Log.i("test4", "msg.what��"+msg.what);
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

//        // �������ݣ����ڲ��ԣ������һ�ν���û��������ô����ѽ��
//        Date date = new Date();
//        long time = date.getTime();
//        insertData("Leo" + time);

        //���������ť����¼�
        iv_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasData = hasData(et_search.getText().toString().trim());
                if (!hasData) {
                    insertData(et_search.getText().toString().trim());

                    //��������ʾ���ݿ�������������ʷ��Ϊ�˲���
                    queryData("");
                }

                //�������������ģ����ѯ��Ʒ������ת����һ�����棬�����������ʵ��
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
     * ��װ�ĺ���
     */

    /*��ʼ�����*/
    private void initView(){
        LayoutInflater.from(context).inflate(R.layout.search_layout,this);
        et_search = (EditText) findViewById(R.id.et_search);
        tv_clear = (TextView) findViewById(R.id.tv_clear);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        listView = (Search_Listview) findViewById(R.id.listView);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        iv_back =  (ImageView) findViewById(R.id.iv_searchback);
    }

    /*��������*/
    private void insertData(String tempName) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + tempName + "')");
        db.close();
    }

    /*ģ����ѯ���� ����ʾ��ListView�б���*/
    private void queryData(String tempName) {

        //ģ������
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
        // ����adapter����������,װ��ģ�������Ľ��
        adapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1, cursor, new String[] { "name" },
                new int[] { android.R.id.text1 }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // ����������
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /*������ݿ����Ƿ��Ѿ��и�����¼*/
    private boolean hasData(String tempName) {
        //��Record��������ҵ�name=tempName��id
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //�ж��Ƿ�����һ��
        return cursor.moveToNext();
    }

    /*�������*/
    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }
    Handler handler=new Handler(){
		public void handleMessage(Message msg){
			 Log.i("test4"," Handler�õ���msg.what��"+msg.what);
			switch(msg.what){
			case ERROR:
				 Toast.makeText(context, "���û���������", Toast.LENGTH_SHORT).show();
				 Log.i("test4"," ����error");
				 break;
			case SUCCESS:
				Log.i("test4"," ����success");
//				SharedPreferences sPreferences=getContext().getSharedPreferences("config", 0x0000);
//				Boolean isLogin= sPreferences.edit().putBoolean("is_login",false).commit();
//			    Log.i("test7"," isLogin��"+isLogin);
				load();
				Toast.makeText(context, "�����ɹ�", Toast.LENGTH_SHORT).show();
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
		Log.i("test4"," ������VideoCount()��"+tbean.getVideoCount());
		System.out.println(tbean.getVideoCount());
		new Thread(){
			public void run(){
				for(int i=0;i<tbean.getVideoCount();i++){
					try {
						System.out.println("ss");
						URL url=new URL(MyConst.BASE_URL1+tbean.imageName.get(i));
						Log.i("test4"," ��������url��"+url);
						System.out.println(MyConst.BASE_URL1+tbean.imageName.get(i));
						HttpURLConnection conn=(HttpURLConnection) url.openConnection();
						System.out.println("��");
						conn.setRequestMethod("GET");
						System.out.println("GET");
						conn.setConnectTimeout(5000);
						System.out.println("5000");
						int code =conn.getResponseCode();
						System.out.println("code");
						if(code==200){
							System.out.println("������");
							FileOutputStream fs=new FileOutputStream(MyConst.FILE_PATH+"s"+i+".jpg");
							Log.i("test4"," ��������fs��"+fs);
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
							System.out.println("û����");
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
