package com.insthub.ecmobile.activity;

import android.content.res.Resources;
import com.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.external.androidquery.callback.AjaxStatus;
import com.insthub.BeeFramework.model.BusinessResponse;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.model.OrderModel;
import com.insthub.ecmobile.model.ProtocolConst;

public class LogisticsQueryActivity extends BaseActivity  {
	private ImageView top_view_back; 
	private TextView top_view_text; 
	private WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logistics_query);
		webView = (WebView) findViewById(R.id.webview);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		WebSettings settings = webView.getSettings();
		settings.setUseWideViewPort(false);
		settings.setLoadWithOverviewMode(true);
		settings.setSupportZoom(false);
		settings.setJavaScriptEnabled(true);
		String loadurl = getIntent().getStringExtra("loadurl"); 
		String toptext = getIntent().getStringExtra("toptext"); 
//		webView.loadUrl("http://m.kuaidi100.com/index_all.html");
		webView.loadUrl(loadurl);
		
		top_view_back = (ImageView) findViewById(R.id.top_view_back);
		top_view_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LogisticsQueryActivity.this.finish();
			}
		});
		
		top_view_text= (TextView) findViewById(R.id.top_view_text);
		top_view_text.setText(toptext);
	}

}
