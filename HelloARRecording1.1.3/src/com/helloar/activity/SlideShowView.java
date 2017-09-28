package com.helloar.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.example.helloarrecording.R;
import com.helloar.user.LoginActivity;
import com.helloar.user.MyConst;
import com.helloar.utils.HttpUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class SlideShowView extends FrameLayout  implements OnClickListener{  
    
  // ʹ��universal-image-loader�����ȡ����ͼƬ����Ҫ���̵���universal-image-loader-1.8.6-with-sources.jar  
  private ImageLoader imageLoader = ImageLoader.getInstance();  

  //�ֲ�ͼͼƬ����  
  private final static int IMAGE_COUNT = 9;  
  //�Զ��ֲ���ʱ����  
  private final static int TIME_INTERVAL = 9;  
  //�Զ��ֲ����ÿ���  
  private final static boolean isAutoPlay = true;   
    
  //�Զ����ֲ�ͼ����Դ  
  private List<String> imageUrls = new ArrayList<String>();  
//  private List<Map<String, String>> imageUrls= new ArrayList<Map<String,String>>();
  private List<String> webUrls = new ArrayList<String>();  
  private List<String> keywordsList = new ArrayList<String>();  
  //���ֲ�ͼƬ��ImageView ��list  
  private List<ImageView> imageViewsList;  
  //��Բ���View��list  
  private List<View> dotViewsList;  
    
  private ViewPager viewPager;  
  //��ǰ�ֲ�ҳ  
  private int currentItem  = 0;  
  //��ʱ����  
  private ScheduledExecutorService scheduledExecutorService;  
    
  private Context context;  
    
  //Handler  
  private Handler handler = new Handler(){  

      @Override  
      public void handleMessage(Message msg) {  
          // TODO Auto-generated method stub  
          super.handleMessage(msg);  
          viewPager.setCurrentItem(currentItem);  
      }  
        
  };  
    
  public SlideShowView(Context context) {  
      this(context,null);  
      // TODO Auto-generated constructor stub  
  }  
  public SlideShowView(Context context, AttributeSet attrs) {  
      this(context, attrs, 0);  
      // TODO Auto-generated constructor stub  
  }  
  public SlideShowView(Context context, AttributeSet attrs, int defStyle) {  
      super(context, attrs, defStyle);  
      this.context = context;  

      initImageLoader(context);  
        
      initData();  
      if(isAutoPlay){  
          startPlay();  
      }  
        
  }  
  /** 
   * ��ʼ�ֲ�ͼ�л� 
   */  
  private void startPlay(){  
      scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();  
      scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);  
  }  
  /** 
   * ֹͣ�ֲ�ͼ�л� 
   */  
  private void stopPlay(){  
      scheduledExecutorService.shutdown();  
  }  
  /** 
   * ��ʼ�����Data 
   */  
  private void initData(){  
      imageViewsList = new ArrayList<ImageView>();  
      dotViewsList = new ArrayList<View>();  
      // һ�������ȡͼƬ  
      new GetListTask().execute("");  
      initUI(context);
  }  
  public void setImageUrls(List<String> imageList){
      this.imageUrls = imageList;
     initData();
  }
  //�ֲ�ͼ�ĵ���¼�  
 
  //�޸�
  @Override
  public void onClick(View v) {
  	// TODO Auto-generated method stub
	  Intent intent = null;
      Bundle bundle = null;
      switch (v.getId()) {
      case 0:
          intent = new Intent(context,AdViewActivity.class);
          bundle = new Bundle();
          bundle.putString("url", webUrls.get(0));
          bundle.putString("keywords", keywordsList.get(0));
          intent.putExtras(bundle);
          context.startActivity(intent);
          break;
      case 1:
          intent = new Intent(context,AdViewActivity.class);
          bundle = new Bundle();
          bundle.putString("url", webUrls.get(1));
          bundle.putString("key", keywordsList.get(1));
          intent.putExtras(bundle);
          context.startActivity(intent);
          break;
      case 2:
          intent = new Intent(context,AdViewActivity.class);
          bundle = new Bundle();
          bundle.putString("url", webUrls.get(2));
          bundle.putString("key", keywordsList.get(2));
          intent.putExtras(bundle);
          context.startActivity(intent);
          break;
      case 3:
          intent = new Intent(context,AdViewActivity.class);
          bundle = new Bundle();
          bundle.putString("url", webUrls.get(3));
          bundle.putString("key", keywordsList.get(3));
          Log.i("ADtest", "���͵�key��"+ keywordsList.get(3));
          intent.putExtras(bundle);
          context.startActivity(intent);
          break;
//      case 4:
//          intent = new Intent(context,AdViewActivity.class);
//          bundle = new Bundle();
//          bundle.putString("url", webUrls.get(4));
//          bundle.putString("key", keywordsList.get(4));
//          intent.putExtras(bundle);
//          context.startActivity(intent);
//          break;
//      case 5:
//          intent = new Intent(context,AdViewActivity.class);
//          bundle = new Bundle();
//          bundle.putString("url", webUrls.get(5));
//          bundle.putString("key", keywordsList.get(5));
//          intent.putExtras(bundle);
//          context.startActivity(intent);
//          break;
      }
  }  
  
 /** 
   * ��ʼ��Views��UI 
   */  
  private void initUI(Context context){  
      if(imageUrls == null || imageUrls.size() == 0)  
          return;  
        
      LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this, true);  
        
      LinearLayout dotLayout = (LinearLayout)findViewById(R.id.dotLayout);  
      dotLayout.removeAllViews();  
        
      // �ȵ������ͼƬ�������  
      for (int i = 0; i < imageUrls.size(); i++) {  
          ImageView view =  new ImageView(context);
          view.setId(i);   
          view.setTag(imageUrls.get(i));  
          if(i==0)//��һ��Ĭ��ͼ  
              view.setBackgroundResource(R.drawable.appmain_subject_1);  
          view.setScaleType(ScaleType.FIT_XY);  
          view.setOnClickListener(this);
          imageViewsList.add(view);  
          ImageView dotView =  new ImageView(context);  
          LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
          params.leftMargin = 6;  
          params.rightMargin = 6;  
          dotLayout.addView(dotView, params);  
          dotViewsList.add(dotView);  
      }  
        
      viewPager = (ViewPager) findViewById(R.id.viewPager);  
      viewPager.setFocusable(true);     
      viewPager.setAdapter(new MyPagerAdapter());  
      viewPager.setOnPageChangeListener(new MyPageChangeListener());  
  }  
    
  /** 
   * ���ViewPager��ҳ�������� 
   *  
   */  
  private class MyPagerAdapter  extends PagerAdapter{  

      @Override  
      public void destroyItem(View container, int position, Object object) {  
          // TODO Auto-generated method stub  
          //((ViewPag.er)container).removeView((View)object);  
          ((ViewPager)container).removeView(imageViewsList.get(position));  
      }  

      @Override  
      public Object instantiateItem(View container, int position) {  
          ImageView imageView = imageViewsList.get(position);  

          imageLoader.displayImage(imageView.getTag() + "", imageView);  
            
          ((ViewPager)container).addView(imageViewsList.get(position));  
          return imageViewsList.get(position);  
      }  

      @Override  
      public int getCount() {  
          // TODO Auto-generated method stub  
          return imageViewsList.size();  
      }  

      @Override  
      public boolean isViewFromObject(View arg0, Object arg1) {  
          // TODO Auto-generated method stub  
          return arg0 == arg1;  
      }  
      @Override  
      public void restoreState(Parcelable arg0, ClassLoader arg1) {  
          // TODO Auto-generated method stub  

      }  

      @Override  
      public Parcelable saveState() {  
          // TODO Auto-generated method stub  
          return null;  
      }  

      @Override  
      public void startUpdate(View arg0) {  
          // TODO Auto-generated method stub  

      }  

      @Override  
      public void finishUpdate(View arg0) {  
          // TODO Auto-generated method stub  
            
      }  
        
  }  
  /** 
   * ViewPager�ļ����� 
   * ��ViewPager��ҳ���״̬�����ı�ʱ���� 
   *  
   */  
  private class MyPageChangeListener implements OnPageChangeListener{  

      boolean isAutoPlay = false;  

      @Override  
      public void onPageScrollStateChanged(int arg0) {  
          // TODO Auto-generated method stub  
          switch (arg0) {  
          case 1:// ���ƻ�����������  
              isAutoPlay = false;  
              break;  
          case 2:// �����л���  
              isAutoPlay = true;  
              break;  
          case 0:// �������������л���ϻ��߼������  
              // ��ǰΪ���һ�ţ���ʱ�������󻬣����л�����һ��  
              if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {  
                  viewPager.setCurrentItem(0);  
              }  
              // ��ǰΪ��һ�ţ���ʱ�������һ������л������һ��  
              else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {  
                  viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);  
              }  
              break;  
              
      }  
      }  

      @Override  
      public void onPageScrolled(int arg0, float arg1, int arg2) {  
          // TODO Auto-generated method stub  
            
      }  

      @Override  
      public void onPageSelected(int pos) {  
          // TODO Auto-generated method stub  
            
          currentItem = pos;  
          for(int i=0;i < dotViewsList.size();i++){  
              if(i == pos){  
                  ((View)dotViewsList.get(pos)).setBackgroundResource(R.drawable.dot_focus);  
              }else {  
                  ((View)dotViewsList.get(i)).setBackgroundResource(R.drawable.dot_blur);  
              }  
          }  
         
      }  
        
  }  
    
  /** 
   *ִ���ֲ�ͼ�л����� 
   * 
   */  
  private class SlideShowTask implements Runnable{  

      @Override  
      public void run() {  
          // TODO Auto-generated method stub  
          synchronized (viewPager) {  
              currentItem = (currentItem+1)%imageViewsList.size();  
              handler.obtainMessage().sendToTarget();  
          }  
      }  
        
  }  
    
  /** 
   * ����ImageView��Դ�������ڴ� 
   *  
   */  
  private void destoryBitmaps() {  

      for (int i = 0; i < IMAGE_COUNT; i++) {  
          ImageView imageView = imageViewsList.get(i);  
          Drawable drawable = imageView.getDrawable();  
          if (drawable != null) {  
              //���drawable��view������  
              drawable.setCallback(null);  
          }  
      }  
  }  
 

  /** 
   * �첽����,��ȡ���� 
   *  
   */  
  class GetListTask extends AsyncTask<String, Integer, Boolean> {  

      @Override  
      protected Boolean doInBackground(String... params) {  
          try {        	 
              String response= HttpUtils.sendGet(MyConst.AD_URL); 
//            JSONObject jsonObject = new JSONObject(response));
              JSONArray jsonArray = new JSONArray(response);
              for (int i = 0; i < jsonArray.length(); i++) {
            	  JSONObject jsonObject = jsonArray.getJSONObject(i);
            	  String adUrl = jsonObject.getString("ad_url");
            	  String webUrl = jsonObject.getString("related_web_url");
            	  String keyWords = jsonObject.getString("key_words");
            	  adUrl = adUrl.substring(adUrl.indexOf(".")+1,adUrl.length());
            	  Log.i("ADtest", "*********ad_adurl*********:"+adUrl);
            	  Log.i("ADtest", "*********ad_weburl*********:"+webUrl);
            	  Log.i("ADtest", "*********ad_key*********:"+webUrl);
				  imageUrls.add(MyConst.HOST_URL+adUrl); 
				  webUrls.add(webUrl);
				  keywordsList.add(keyWords);
            	  Log.i("ADtest", "*********imageUrls*********:"+imageUrls);
            	  Log.i("ADtest", "*********webUrls*********:"+webUrls);
            	  Log.i("ADtest", "********* keywordsList*********:"+ keywordsList);
			}

              return true;  
          } catch (Exception e) {  
              e.printStackTrace();  
              return false;  
          }  
      }  

      @Override  
      protected void onPostExecute(Boolean result) {  
          super.onPostExecute(result);  
          if (result) {  
              initUI(context);  
          }  
      }  
  }  
    
  /** 
   * ImageLoader ͼƬ�����ʼ�� 
   *  
   * @param context 
   */  
  public static void initImageLoader(Context context) {  
      // This configuration tuning is custom. You can tune every option, you  
      // may tune some of them,  
      // or you can create default configuration by  
      // ImageLoaderConfiguration.createDefault(this);  
      // method.  
      ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove  
                                                                                                                                                                                                                                                                                              // for  
                                                                                                                                                                                                                                                                                              // release  
                                                                                                                                                                                                                                                                                              // app  
              .build();  
      // Initialize ImageLoader with configuration.  
      ImageLoader.getInstance().init(config);  
  }

}  
