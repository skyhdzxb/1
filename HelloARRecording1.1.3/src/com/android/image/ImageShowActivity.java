package com.android.image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

import com.example.helloarrecording.R;
import com.helloar.activity.ARMainActivity;


public class ImageShowActivity extends Activity{
	
	
	 private String url = "/data/data/com.example.helloarrecording/files/";
	 private List<String> imagePath=new ArrayList<String>();//图片文件的路径 
	 private ImageView image;
	 private File[] listFiles;
	 private GridView gridview;
	 private ImageView ibackIV;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		gridview=(GridView) findViewById(R.id.images_gv);
		ibackIV = (ImageView) findViewById(R.id.i_back_iv);
		ibackIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ImageShowActivity.this, ARMainActivity.class);
				i.putExtra("mine", 1);
				startActivity(i);
			}
		});

		
		File file = new File(url);
		listFiles = file.listFiles();

		for (int i = 0; i < listFiles.length; i++) {
			
			
			if ((""+listFiles[i]).endsWith(".jpg")) {
				imagePath.add(""+listFiles[i]);
//				 Bitmap b = BitmapFactory.decodeFile((""+listFiles[i]));

			}

		}
		  
		if(imagePath.size()<1){//如果不存在文件图片  
	            return;  
	        }  
		BaseAdapter adapter=new BaseAdapter(){  
			  
			  
            @Override  
            public View getView(int position, View convertView, ViewGroup parent) {  
            	SquareImageView iv;//声明ImageView的对象  
                if(convertView==null){  
                    iv=new SquareImageView(ImageShowActivity.this);//实例化ImageView的对象  
                    /**************设置图像的宽度和高度**************/  
                    iv.setAdjustViewBounds(true); 
                    //iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                   
        
                    // iv.setMaxWidth(150);  
                    //iv.setMaxHeight(150); 
                    /****************************/  
                    iv.setPadding(5, 5, 5, 5);//设置ImageView的内边距  
                    //iv.setScaleType(ScaleType.CENTER);
                }else{  
                    iv=(SquareImageView)convertView;  
                }  
                //为ImageView设置要显示的图片  
                Bitmap bm=BitmapFactory.decodeFile(imagePath.get(position));  
                iv.setImageBitmap(bm);  
                return iv;  
            }  
              
            //获得数量  
            @Override  
            public int getCount() {  
                return imagePath.size();  
            }  
  
  
            //获得当前选项  
            @Override  
            public Object getItem(int position) {  
                return position;  
            }  
  
  
            //获得当前选项的id  
            @Override  
            public long getItemId(int position) {  
                return position;  
            }  
        };  
          
        gridview.setAdapter(adapter);//将适配器与GridView关联 
	}
	public class SquareImageView extends ImageView {  
		  
	    public SquareImageView(Context context) {  
	        super(context);  
	    }  
	  
	    public SquareImageView(Context context, AttributeSet attrs) {  
	        super(context, attrs);  
	    }  
	  
	    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {  
	        super(context, attrs, defStyle);  
	    }  
	  
	    @Override  
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
	        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width  
	    }  
	}  
	}
	

	

