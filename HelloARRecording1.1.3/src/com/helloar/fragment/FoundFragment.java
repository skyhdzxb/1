package com.helloar.fragment;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.helloarrecording.R;
import com.helloar.activity.ChidActivity;
import com.helloar.activity.FamActivity;
import com.helloar.activity.SlideShowView;
import com.helloar.activity.WedActivity;
import com.helloar.user.MyConst;
import com.helloar.utils.HttpUtils;

/**
 * Created by Dan on 2017/6/1.
 */

public class FoundFragment extends Fragment implements OnClickListener{


	private SlideShowView adSlideShowView;
     private Button fButton;
     private Button wButton;
     private Button cButton;
     private List<Button> buttonsList;
     private List<String> imageUrls = new ArrayList<String>();
     private List<String> imageViewsList; 
     private String reString;
     private ArrayList<Bitmap> bmArray =new ArrayList<Bitmap>();
  
     
     //在消息队列中实现对控件的更改  
     private Handler handle = new Handler() {  
         public void handleMessage(Message msg) {  
             switch (msg.what) {  
             case 0:  
                 System.out.println("111"); 
                 ArrayList<Bitmap> bmArrayList= (ArrayList<Bitmap>) msg.obj;
//                 Bitmap bmp1=(Bitmap)msg.obj;  
//                 Bitmap bmp2=(Bitmap)msg.obj; 
//                 Bitmap bmp3=(Bitmap)msg.obj; 
                 
                wButton.setBackgroundDrawable(new BitmapDrawable( bmArrayList.get(0))); 
                cButton.setBackgroundDrawable(new BitmapDrawable(bmArrayList.get(1))); 
                fButton.setBackgroundDrawable(new BitmapDrawable( bmArrayList.get(2))); 
                 break;  
             }  
         };  
     };  
	@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
		View view = inflater.inflate(R.layout.foundfg, container,false);
		
        fButton= (Button)view.findViewById(R.id.f_btn);
        cButton= (Button)view.findViewById(R.id.c_btn);
        wButton= (Button)view.findViewById(R.id.w_btn);      
        fButton.setOnClickListener(this);
        cButton.setOnClickListener(this);
        wButton.setOnClickListener(this);
        initData();
        return view;    
    }
	

	private void initData() {
		
		 new Thread(new Runnable() {
	            @Override
	            public void run() {
	                // 写子线程中的操作
	            	 reString= HttpUtils.sendGet(MyConst.FOUND_URL);
	     	         Log.i("test", "response:"+reString);
	     	        JSONArray jsonArray;
					try {	
						jsonArray = new JSONArray(reString);
						 for (int i = 0; i < jsonArray.length(); i++) {
			     	      	 JSONObject jsonObject = jsonArray.getJSONObject(i);
			     	        // System.out.println(jsonObject);
			     	      	 Log.i("test", "jsonObject:"+jsonObject);
			     	      	 String foundurl = jsonObject.getString("ad_block_url");
			     	      	foundurl = foundurl.substring(foundurl.indexOf(".")+1,foundurl.length());
			     	      	 Log.i("test", "foundurl:"+foundurl);
			     	      	 imageUrls.add(MyConst.HOST_URL+foundurl);
						 }
			     	      	 Log.i("test", "imagUrls:"+imageUrls);
			     	      	 Bitmap bmp1 = getURLimage(imageUrls.get(0)); 
			     	      	 Bitmap bmp2 = getURLimage(imageUrls.get(1)); 
			     	      	 Bitmap bmp3 = getURLimage(imageUrls.get(2));
			     	      	 Log.i("test", "bmp:"+bmp1);
			     	      	 bmArray.add(bmp1);
			     	      	 bmArray.add(bmp2);
			     	      	 bmArray.add(bmp3);
			     	      	 Log.i("test", "bitmaps:"+bmArray.get(0));
			     	      	 
			     	      	 Log.i("test", "imagUrls:"+imageUrls.get(0));
			     	      	
			     	         Message msg = new Message();  
	                         msg.what = 0;
	                         msg.obj = bmArray; 
	                         
	                         System.out.println("000");  
	                         handle.sendMessage(msg);  
			     	         //fButton.setImageBitmap(bmp);	 		     	     
			     	      	 //fButton.setBackgroundDrawable(new BitmapDrawable(bmp));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	  
	     		}
	                    
	        }).start();  
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()){
        case R.id.w_btn:
        	if (bmArray.size()!=3) {
				Toast.makeText(getActivity(), "数据加载失败", 0).show();
			}else {

            Intent intent1 = new Intent(getContext(),WedActivity.class);
            Log.i("test", "******************跳转到WedActivity");
            //intent1.putExtra("BITMAP", bmArray.get(0));
            Bundle b1=new Bundle();
            b1.putByteArray("image",Bitmap2Bytes( bmArray.get(0)));
            intent1.putExtras(b1); 
            startActivity(intent1);
			}
            break;
        case R.id.c_btn:
        	if (bmArray.size()!=3) {
				Toast.makeText(getActivity(), "数据加载失败", 0).show();
			}else {

            Intent intent2 = new Intent(getContext(),ChidActivity.class);
            Bundle b2=new Bundle();
            b2.putByteArray("image",Bitmap2Bytes( bmArray.get(1)));
            intent2.putExtras(b2); 
            startActivity(intent2);
            Log.i("test", "******************跳转到ChidActivity");
			}
            break;
        case R.id.f_btn:
        	if (bmArray.size()!=3) {
				Toast.makeText(getActivity(), "数据加载失败", 0).show();
			}else {

            Intent intent3 = new Intent(getContext(),FamActivity.class);
           
            Bundle b3=new Bundle();
            b3.putByteArray("image",Bitmap2Bytes( bmArray.get(2)));
          //  Log.i("new", "位图："+Bitmap2Bytes( bmArray.get(2)));
            intent3.putExtras(b3); 
           // Log.i("test", "*********FoundFragment中传的图片位地址"+ bmArray.get(2));
            startActivity(intent3);   
           // Log.i("test", "*********执行了intent3");
            Log.i("test", "******************跳转到FamActivity");
			}
            break;
        default:
            break;
    }	
}
	 //加载图片  
    public Bitmap getURLimage(String url) {  
        Bitmap bmp = null;  
        try {  
            URL myurl = new URL(url);  
            // 获得连接  
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();  
            conn.setConnectTimeout(6000);//设置超时  
            conn.setDoInput(true);  
            conn.setUseCaches(false);//不缓存  
            conn.connect();  
            InputStream is = conn.getInputStream();//获得图片的数据流  
            bmp = BitmapFactory.decodeStream(is);  
            is.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return bmp;  
    }  
    private byte[] Bitmap2Bytes(Bitmap bm){      
        ByteArrayOutputStream baos = new ByteArrayOutputStream();        
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);        
        return baos.toByteArray();      
       } 
}
	


