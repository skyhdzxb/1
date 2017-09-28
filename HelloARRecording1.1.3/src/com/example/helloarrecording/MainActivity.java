//================================================================================================================================
//
//  Copyright (c) 2015-2017 VisionStar Information Technology (Shanghai) Co., Ltd. All Rights Reserved.
//  EasyAR is the registered trademark or trademark of VisionStar Information Technology (Shanghai) Co., Ltd in China
//  and other countries for the augmented reality technology developed by VisionStar Information Technology (Shanghai) Co., Ltd.
//
//================================================================================================================================

package com.example.helloarrecording;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.android.video.VideoListActivity;
import com.helloar.activity.ARMainActivity;
import com.helloar.activity.MineInfoActivity;
import com.helloar.search.Search_View;
import com.helloar.user.MyConst;


import cn.easyar.FunctorOfVoidFromPermissionStatusAndString;
import cn.easyar.FunctorOfVoidFromRecordStatusAndString;
import cn.easyar.PermissionStatus;
import cn.easyar.RecordStatus;
import cn.easyar.engine.EasyAR;


public class MainActivity extends Activity
{
    /*
    * Steps to create the key for this sample:
    *  1. login www.easyar.com
    *  2. create app with
    *      Name: HelloARRecording
    *      Package Name: cn.easyar.samples.helloarrecording
    *  3. find the created item in the list and show key
    *  4. set key string bellow
    */
    private static String key = "DvWeSRINYMdaCQsVgmYUmWeSkNbKfrr2ZcobwDDNnaW47082t6CuAWlHRNjckkXr8hmAfT32bikdfGaMkeb1kqakylMMtAY4khdfwA8crTTbTf8IHAbbNI0yIpL6E4h8uXWNGIv73Z4jcJbJXDAG4lPJY0ZvRaB0qc5xhEhcGQBRQh8wPMO0HbG4ICcjTiDQDnWgz6Mh";
    private GLView glView;
    private String url = "";
    private boolean started = false;
    private ImageView searchIV;
    private ImageView backIV;
  

    private String prepareUrl()
    {
        String timeStamp = new SimpleDateFormat("MM-dd-HH-mm").format(new Date());
        String FileName = "AR_" + timeStamp + ".mp4";
        File folder = new File("/sdcard/arvideo");
        if (!folder.exists())
            folder.mkdirs();
        return "/sdcard/arvideo/" + FileName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        backIV = (ImageView)findViewById(R.id.mback_iv);
        backIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1=new Intent(MainActivity.this,ARMainActivity.class); 
				intent1.putExtra("mine",2);
				startActivity(intent1);
				finish();
			}
		});
       
       searchIV= (ImageView) findViewById(R.id.search_iv);
       searchIV.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent1 = new Intent(MainActivity.this, com.helloar.search.SearchBty.class);
			startActivity(intent1);		
			finish();
		}
	});
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (!EasyAR.initialize(this, key)) {
            Log.e("HelloAR", "Initialization Failed.");
        }

        glView = new GLView(this);

        requestCameraPermission(new PermissionCallback() {
            @Override
            public void onSuccess() {
                ((ViewGroup) findViewById(R.id.preview)).addView(glView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                Button recordStart  = (Button)findViewById(R.id.ib_start );
                Button recordStop  = (Button)findViewById(R.id.ib_stop);
                recordStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (started) {
                            return;
                        }
                        glView.requestPermissions(new FunctorOfVoidFromPermissionStatusAndString() {
                            @Override
                            public void invoke(int status, String msg) {

                                switch (status)
                                {
                                    case PermissionStatus.Denied:
                                        started = false;
                                        Log.e(" ", "Permission Denied" + msg);
                                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                                        break;
                                    case PermissionStatus.Error:
                                        started = false;
                                        Log.e("HelloAR", "Permission Error" + msg);
                                        Toast.makeText(MainActivity.this, "Permission Error", Toast.LENGTH_SHORT).show();
                                        break;
                                    case PermissionStatus.Granted:
                                        Log.i("HelloAR", "Permission Granted");

                                        url = prepareUrl();

                                        glView.startRecording(url, new FunctorOfVoidFromRecordStatusAndString() {
                                            @Override
                                            public void invoke(int status, String value) {
                                                switch (status)
                                                {
                                                    case RecordStatus.OnStarted:
                                                        break;
                                                    case RecordStatus.OnStopped:
                                                        started = false;
                                                        break;
                                                    case RecordStatus.FileFailed:
                                                        break;
                                                    default:
                                                        break;

                                                }
                                                Log.i("HelloAR", "Recorder Callback status: " + Integer.toString(status) + ", MSG: " + value);
                                            }
                                        });
                                        Toast.makeText(MainActivity.this, "Recording...", Toast.LENGTH_SHORT).show();
                                        break;
                                }

                            }
                        });
                        started = true;
                    }
                });
                recordStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!started) {
                            return;
                        }
                        started = false;
                        glView.stopRecording();
                        Toast.makeText(MainActivity.this, "Recorded at " + url, Toast.LENGTH_LONG).show();
                    }
                });
                
                
            }

            @Override
            public void onFailure() {
            }
        });
    }

    private interface PermissionCallback
    {
        void onSuccess();
        void onFailure();
    }
    private HashMap<Integer, PermissionCallback> permissionCallbacks = new HashMap<Integer, PermissionCallback>();
    private int permissionRequestCodeSerial = 0;
    @TargetApi(23)
    public void requestCameraPermission(PermissionCallback callback)
    {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                int requestCode = permissionRequestCodeSerial;
                permissionRequestCodeSerial += 1;
                permissionCallbacks.put(requestCode, callback);
                requestPermissions(new String[]{Manifest.permission.CAMERA}, requestCode);
            } else {
                callback.onSuccess();
            }
        } else {
            callback.onSuccess();
        }
    }

    @SuppressLint("NewApi")
	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (permissionCallbacks.containsKey(requestCode)) {
            PermissionCallback callback = permissionCallbacks.get(requestCode);
            permissionCallbacks.remove(requestCode);
            boolean executed = false;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    executed = true;
                    callback.onFailure();
                }
            }
            if (!executed) {
                callback.onSuccess();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (glView != null) { glView.onResume(); }
    }

    @Override
    protected void onPause()
    {
        if (glView != null) { glView.onPause(); }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    File file;
    public void myProfile(View v){
    	file =new File(MyConst.VIDEO_PATH);
    	
    	if(file.listFiles()!=null){
    		Intent intent=new Intent(this,VideoListActivity.class);
        	startActivity(intent);
        	finish();
    	}
    	else{
    		Toast.makeText(this, "视频列表为空", 1).show();
    	}
    }
}
    
