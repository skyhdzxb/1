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
			case LOADMAIN:// 加载主界面
				Toast.makeText(getApplicationContext(), "当前版本已经是最新版", 1).show();
				break;
			case SHOWUPDATEDIALOG:
				showUpdateDialog();
				break;
			case ERROR:// 有异常
				switch (msg.arg1) {
				case 404:// 资源找不到
					Toast.makeText(getApplicationContext(), "404资源找不到", 0).show();
					break;
				case 4001:// 找不到网络
					Toast.makeText(getApplicationContext(), "4001没有网络", 0).show();
					break;
				case 4002://
					break;
				case 4003:// json格式错误
					Toast.makeText(getApplicationContext(), "4003json格式错误", 0).show();
					break;
				}
				
				break;
			default:
				break;
			}
		}

	};
	private void initData() {
		// 获取自己的版本信息
		PackageManager pm = getPackageManager();
		try {
			PackageInfo pkInfo = pm.getPackageInfo(getPackageName(), 0);
			// 版本号
			versionCode = pkInfo.versionCode;
			// 版本名
			versionName = pkInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// can not reach 异常不会发生
		}
	}
	public void check(View v) {
			
			System.out.println("点击了");
			checkVersion();

	}

	private void checkVersion() {
		/**
		 * 访问服务器，获取最新的版本信息
		 */
			new Thread() {

				private long startTimeMills;

				public void run() {
					BufferedReader reader = null;
					HttpURLConnection conn = null;
					int errorCode = -1;// 正常，没有错误
					try {
						startTimeMills = System.currentTimeMillis();// 毫秒显示当前的时间
						URL url = new URL("http://112.74.177.183/ar_project/apk/guardversion.json");
						conn = (HttpURLConnection) url.openConnection();
						conn.setReadTimeout(5000);// 读取数据的超时
						conn.setConnectTimeout(5000);// 读取数据的超时
						conn.setRequestMethod("GET");// 设置请求的方式
						int code = conn.getResponseCode();
						// 请求成功
						if (code == 200) {
							InputStream in = conn.getInputStream();// 获取字节流
							reader = new BufferedReader(new InputStreamReader(in));// 转换成缓冲字符流
							String line = reader.readLine();
							StringBuilder jsonString = new StringBuilder();
							while (line != null) {
								jsonString.append(line);
								// 继续读取
								line = reader.readLine();
							}
							// 解析json数据
							jsonBean = parseJson(jsonString);

						} else {
							// 文件找不到404
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
						// 网络连接不成功 4001
						errorCode = 4001;
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						// json数据格式错误 4003
						errorCode = 4003;
						e.printStackTrace();
					} finally {
//							if (errorCode == -1) {
//								isNewVersion(jsonBean);// 是否有新版本
//							} else {
//								Message msg = Message.obtain();
//								msg.what = ERROR;
//								msg.arg1 = errorCode;
//								handler.sendMessage(msg);// 发送错误信息
//							}
						Message msg=Message.obtain();
						if(errorCode==-1){
							msg.what=isNewVersion(jsonBean);//检测是否有新版本
						}else{
							msg.what=ERROR;
							msg.arg1=errorCode;
						}
						long endTimeMills = System.currentTimeMillis();// 执行结束的时间;
						// 对比自己的版本
						if (endTimeMills - startTimeMills < 3000) {
							// 设置休眠的时间，保证至少休眠三秒
							SystemClock.sleep(3000 - (endTimeMills - startTimeMills));
						}
						handler.sendMessage(msg);//发送消息
						try{
							if (reader == null || conn == null) {
								return;
							}
							reader.close();// 关闭输入流
							conn.disconnect();// 断开连接
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
		// 把json字符串数据封装程json对象
		JSONObject jobject = new JSONObject(jsonString + "");
		int version = jobject.getInt("version");
		String apkPath = jobject.getString("url");
		String desc = jobject.getString("desc");
		// 数据封装到bean对象中
		bean.setDesc(desc);
		bean.setUrl(apkPath);
		bean.setVersionCode(version);
		return bean;
		
	}
	/**
	 * 判断是否有新版本,在子线程中执行
	 * 
	 * @param jsonBean
	 */
	protected int isNewVersion(UrlBean jsonBean) {
		int serverCode = jsonBean.getVersionCode();// 获取服务器的版本
		
		if (serverCode == versionCode) {//版本一致
			return LOADMAIN;
			// 进入主界面
//			Message msg = Message.obtain();
//			msg.what = LOADMAIN;
//			handler.sendMessage(msg);
		} else {
			return SHOWUPDATEDIALOG;
			// 弹出对话框，现实新版本的信息，让用户点击是否更新
//			Message msg = Message.obtain();
//			msg.what = SHOWUPDATEDIALOG;
//			handler.sendMessage(msg);
		}
	}

	/**
	 * 显示是否更新版本的对话框
	 */
	protected void showUpdateDialog() {
		// 对话框的上下文要是Activity的class
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				// 让用户禁用取消
				// builder.setCancelable(false);
				builder.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// 取消的事件处理
						// 进入主界面
						
					}
				}).setTitle("提醒").setMessage("是否更新新版本？新版本有如下特性" + jsonBean.getDesc())
						.setPositiveButton("更新", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// 更新apk，访问网络，下载新的apk
								downLoadNewApk();
							}

						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// 进入主界面
								
							}

						});
				builder.show();// 显示对话框
	}

	/**
	 * 新版本的下载安装
	 */
	protected void downLoadNewApk() {
		HttpUtils utils = new HttpUtils();
		// jsonBean.getUrl()下载的url
		// target 本地路径
		// 先删除xx.apk
		// File file=new File("mnt/sdcard/xx.apk");
		// file.delete();
		utils.download(jsonBean.getUrl(), "/mnt/sdcard/xx.apk", new RequestCallBack<File>() {

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				pd_download.setVisibility(View.VISIBLE);//设置进度条的显示
				pd_download.setMax((int) total);//设置进度条最大值
				pd_download.setProgress((int) current);//设置当前进度
				super.onLoading(total, current, isUploading);
			}

			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				// 下载成功 ,在主线程中执行
				Toast.makeText(getApplicationContext(), "下载成功", 1).show();
				// 安装apk
				installApk();
				pd_download.setVisibility(View.GONE);//隐藏进度条
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// 下载失败
				Toast.makeText(getApplicationContext(), "下载新版本失败", 1).show();
				pd_download.setVisibility(View.GONE);//隐藏进度条
			}
		});
	}

	/**
	 * 安装下载的新版本
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

		// 如果用户取消更新apk，那么直接进入至界面
		
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
		  

	
