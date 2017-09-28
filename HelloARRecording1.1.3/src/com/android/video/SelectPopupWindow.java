package com.android.video;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.helloarrecording.R;

public class SelectPopupWindow extends PopupWindow {
	private LinearLayout ll_sharetoQQ;
	private LinearLayout ll_sharetoWX;
	private TextView fp_cancel;
	private View mMenuView;
    public SelectPopupWindow(Activity context, OnClickListener itemsOnClickListener){
    	super(context);
        LayoutInflater inflater = (LayoutInflater) context
                 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.share_popupwindow, null);
        
        ll_sharetoQQ = (LinearLayout) mMenuView.findViewById(R.id.ll_share_qq);
        ll_sharetoWX = (LinearLayout) mMenuView.findViewById(R.id.ll_share_wechat);
        fp_cancel = (TextView) mMenuView.findViewById(R.id.fp_cancel);
        //点击取消按钮，关闭popupWindow
        fp_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ll_sharetoQQ.setOnClickListener(itemsOnClickListener);
        ll_sharetoWX.setOnClickListener(itemsOnClickListener);
        
        this.setContentView(mMenuView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(0x000000);
        this.setBackgroundDrawable(dw);
        this.setFocusable(true);
        
        //点击popupWindow之外的部分  关闭popupWindow
        mMenuView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.fp_linear_share).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }                
                return true;
            }
        });
    }
}
