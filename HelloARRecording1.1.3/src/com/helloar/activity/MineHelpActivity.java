package com.helloar.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.helloarrecording.R;
import com.helloar.fragment.MineFragment;
import com.helloar.user.UrlBean;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Dan on 2017/6/5.
 */

public class MineHelpActivity extends Activity  implements OnClickListener{
	private TextView funinfo;
	private TextView upgrade;
	private TextView contact;
	
	private RelativeLayout funinfo_rll;
    private RelativeLayout contact_rll;
   
	protected static final int ERROR = 1;
	private static final int LOADMAIN = 2;
	private static final int SHOWUPDATEDIALOG = 3;
	protected UrlBean  jsonBean;
	protected ProgressBar pd_download;
	private int versionCode;
	private String versionName;
	
	private ImageView minehelpback;
	
	

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_help_fg);
        pd_download=(ProgressBar) findViewById(R.id.pd_splash_download_progress);
        funinfo = (TextView)findViewById(R.id.funinfo_tv);
        contact = (TextView) findViewById(R.id.contact_tv);
        funinfo.setOnClickListener((OnClickListener) this);
        contact.setOnClickListener((OnClickListener) this);
        
        initData();
        minehelpback = (ImageView) findViewById(R.id.help_back_iv);
        minehelpback.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent( MineHelpActivity.this,ARMainActivity.class); 
				i.putExtra("mine", 1);
				startActivity(i);
				finish();
			}
        });     
    }

	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOADMAIN:// ����������
				Toast.makeText(getApplicationContext(), "��ǰ�汾�Ѿ������°�", 1).show();
				break;
			case SHOWUPDATEDIALOG:
				showUpdateDialog();
				break;
			case ERROR:// ���쳣
				switch (msg.arg1) {
				case 404:// ��Դ�Ҳ���
					Toast.makeText(getApplicationContext(), "404��Դ�Ҳ���", 0).show();
					break;
				case 4001:// �Ҳ�������
					Toast.makeText(getApplicationContext(), "4001û������", 0).show();
					break;
				case 4002://
					break;
				case 4003:// json��ʽ����
					Toast.makeText(getApplicationContext(), "4003json��ʽ����", 0).show();
					break;
				}
				
				break;
			default:
				break;
			}
		}

	};
	private void initData() {
		// ��ȡ�Լ��İ汾��Ϣ
		PackageManager pm = getPackageManager();
		try {
			PackageInfo pkInfo = pm.getPackageInfo(getPackageName(), 0);
			// �汾��
			versionCode = pkInfo.versionCode;
			// �汾��
			versionName = pkInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// can not reach �쳣���ᷢ��
		}
	}
	public void check(View v) {
			
			System.out.println("�����");
			checkVersion();

	}

	private void checkVersion() {
		/**
		 * ���ʷ���������ȡ���µİ汾��Ϣ
		 */
			new Thread() {

				private long startTimeMills;

				public void run() {
					BufferedReader reader = null;
					HttpURLConnection conn = null;
					int errorCode = -1;// ������û�д���
					try {
						startTimeMills = System.currentTimeMillis();// ������ʾ��ǰ��ʱ��
						URL url = new URL("http://112.74.177.183/ar_project/apk/guardversion.json");
						conn = (HttpURLConnection) url.openConnection();
						conn.setReadTimeout(5000);// ��ȡ���ݵĳ�ʱ
						conn.setConnectTimeout(5000);// ��ȡ���ݵĳ�ʱ
						conn.setRequestMethod("GET");// ��������ķ�ʽ
						int code = conn.getResponseCode();
						// ����ɹ�
						if (code == 200) {
							InputStream in = conn.getInputStream();// ��ȡ�ֽ���
							reader = new BufferedReader(new InputStreamReader(in));// ת���ɻ����ַ���
							String line = reader.readLine();
							StringBuilder jsonString = new StringBuilder();
							while (line != null) {
								jsonString.append(line);
								// ������ȡ
								line = reader.readLine();
							}
							// ����json����
							jsonBean = parseJson(jsonString);

						} else {
							// �ļ��Ҳ���404
							errorCode = 404;
						}
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						errorCode = 4002;
						e.printStackTrace();
					} catch (ProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						// �������Ӳ��ɹ� 4001
						errorCode = 4001;
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						// json���ݸ�ʽ���� 4003
						errorCode = 4003;
						e.printStackTrace();
					} finally {
//							if (errorCode == -1) {
//								isNewVersion(jsonBean);// �Ƿ����°汾
//							} else {
//								Message msg = Message.obtain();
//								msg.what = ERROR;
//								msg.arg1 = errorCode;
//								handler.sendMessage(msg);// ���ʹ�����Ϣ
//							}
						Message msg=Message.obtain();
						if(errorCode==-1){
							msg.what=isNewVersion(jsonBean);//����Ƿ����°汾
						}else{
							msg.what=ERROR;
							msg.arg1=errorCode;
						}
						long endTimeMills = System.currentTimeMillis();// ִ�н�����ʱ��;
						// �Ա��Լ��İ汾
						if (endTimeMills - startTimeMills < 3000) {
							// �������ߵ�ʱ�䣬��֤������������
							SystemClock.sleep(3000 - (endTimeMills - startTimeMills));
						}
						handler.sendMessage(msg);//������Ϣ
						try{
							if (reader == null || conn == null) {
								return;
							}
							reader.close();// �ر�������
							conn.disconnect();// �Ͽ�����
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}.start();
		}

	protected UrlBean parseJson(StringBuilder jsonString) throws JSONException{
		
		UrlBean bean = new UrlBean();
		// ��json�ַ������ݷ�װ��json����
		JSONObject jobject = new JSONObject(jsonString + "");
		int version = jobject.getInt("version");
		String apkPath = jobject.getString("url");
		String desc = jobject.getString("desc");
		// ���ݷ�װ��bean������
		bean.setDesc(desc);
		bean.setUrl(apkPath);
		bean.setVersionCode(version);
		return bean;
		
	}
	/**
	 * �ж��Ƿ����°汾,�����߳���ִ��
	 * 
	 * @param jsonBean
	 */
	protected int isNewVersion(UrlBean jsonBean) {
		int serverCode = jsonBean.getVersionCode();// ��ȡ�������İ汾
		
		if (serverCode == versionCode) {//�汾һ��
			return LOADMAIN;
			// ����������
//			Message msg = Message.obtain();
//			msg.what = LOADMAIN;
//			handler.sendMessage(msg);
		} else {
			return SHOWUPDATEDIALOG;
			// �����Ի�����ʵ�°汾����Ϣ�����û�����Ƿ����
//			Message msg = Message.obtain();
//			msg.what = SHOWUPDATEDIALOG;
//			handler.sendMessage(msg);
		}
	}

	/**
	 * ��ʾ�Ƿ���°汾�ĶԻ���
	 */
	protected void showUpdateDialog() {
		// �Ի����������Ҫ��Activity��class
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				// ���û�����ȡ��
				// builder.setCancelable(false);
				builder.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// ȡ�����¼�����
						// ����������
						
					}
				}).setTitle("����").setMessage("�Ƿ�����°汾���°汾����������" + jsonBean.getDesc())
						.setPositiveButton("����", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// ����apk���������磬�����µ�apk
								downLoadNewApk();
							}

						}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// ����������
								
							}

						});
				builder.show();// ��ʾ�Ի���
	}

	/**
	 * �°汾�����ذ�װ
	 */
	protected void downLoadNewApk() {
		HttpUtils utils = new HttpUtils();
		// jsonBean.getUrl()���ص�url
		// target ����·��
		// ��ɾ��xx.apk
		// File file=new File("mnt/sdcard/xx.apk");
		// file.delete();
		utils.download(jsonBean.getUrl(), "/mnt/sdcard/xx.apk", new RequestCallBack<File>() {

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				pd_download.setVisibility(View.VISIBLE);//���ý���������ʾ
				pd_download.setMax((int) total);//���ý��������ֵ
				pd_download.setProgress((int) current);//���õ�ǰ����
				super.onLoading(total, current, isUploading);
			}

			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				// ���سɹ� ,�����߳���ִ��
				Toast.makeText(getApplicationContext(), "���سɹ�", 1).show();
				// ��װapk
				installApk();
				pd_download.setVisibility(View.GONE);//���ؽ�����
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// ����ʧ��
				Toast.makeText(getApplicationContext(), "�����°汾ʧ��", 1).show();
				pd_download.setVisibility(View.GONE);//���ؽ�����
			}
		});
	}

	/**
	 * ��װ���ص��°汾
	 */
	protected void installApk() {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		Uri data = Uri.fromFile(new File("/mnt/sdcard/xx.apk"));
		String type = "application/vnd.android.package-archive";
		intent.setDataAndType(data, type);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// ����û�ȡ������apk����ôֱ�ӽ���������
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		  switch (v.getId()){
        case R.id.funinfo_tv:
            Intent intent1 = new Intent(MineHelpActivity.this,FunInfoActivity.class);
            startActivity(intent1);
            //finish();
            break;
        case R.id.contact_tv:
            Intent intent2 = new Intent(MineHelpActivity.this,ContactUsActivity.class);
            startActivity(intent2);
           // finish();
            break;
        default:
            break;
    }
	}
}
		  

	
