package com.helloar.search;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ListView;

//���ListView��ScrollView�ĳ�ͻ
public class Search_Listview extends ListView {
    public Search_Listview(Context context) {
        super(context);
    }

    public Search_Listview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Search_Listview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //ͨ����д��onMeasure�������ﵽ��ScrollView�����Ч��

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}

