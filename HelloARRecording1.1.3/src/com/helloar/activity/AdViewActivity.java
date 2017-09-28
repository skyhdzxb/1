package com.helloar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.helloarrecording.R;

public class AdViewActivity extends Activity  {
	
	private WebView mWebView;
    private TextView mKeyWords;
    private Bundle mBundle;
    private int white = 0xFFFFFFFF;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advert_main);	
	    mBundle = getIntent().getExtras();
	    initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		mKeyWords = (TextView) findViewById(R.id.ad_tv);
		mKeyWords.setText( getIntent().getExtras().getString("key"));
		mKeyWords.setTextColor(white);
		mWebView = (WebView) findViewById(R.id.ad_wv);
        mWebView.loadUrl(mBundle.getString("url"));
        mWebView.requestFocusFromTouch();
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        /**���ǵ���ϵͳ���Դ��������Ϊ����ҳ*/
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }
        });
        /**�жϼ��ع���*/
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // ��ҳ�������

                } else {
                    // ������

                }
            }
        });
        initListener();
	}
	private void initListener() {
		// TODO Auto-generated method stub
		  /**��ҳ��ʱ�� ����Ӧ��Ļ*/
        WebSettings webSettings =  mWebView .getSettings();       
        webSettings.setUseWideViewPort(true);//���ô����ԣ��������������
        webSettings.setLoadWithOverviewMode(true);

        /**��ҳ��֧������*/
        webSettings.setJavaScriptEnabled(true);  
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

	}
	public void ADback(View view) { 
		Intent intent1=new Intent(AdViewActivity.this,ARMainActivity.class); 
		intent1.putExtra("mine",3);
		startActivity(intent1);
		finish();	
	}

	

}
