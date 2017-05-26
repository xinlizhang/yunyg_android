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

public class A2_ForgotPasswordActivity extends BaseActivity {
	
	private WebView webView;
	private ImageView top_view_back; 
	private TextView top_view_text; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a2_forgot_password);
		top_view_back = (ImageView) findViewById(R.id.top_view_back);
		top_view_text = (TextView) findViewById(R.id.top_view_text);
		top_view_text.setText("’“ªÿ√‹¬Î");
		top_view_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				A2_ForgotPasswordActivity.this.finish();
			}
		});
		
		String shipping_name = getIntent().getStringExtra("shipping_name");
		String order_sn = getIntent().getStringExtra("order_sn");
		
		webView = (WebView) findViewById(R.id.webview);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		WebSettings settings = webView.getSettings();
		settings.setUseWideViewPort(false);
		settings.setLoadWithOverviewMode(true);
		settings.setSupportZoom(false);
		settings.setJavaScriptEnabled(true);
		webView.loadUrl("https://passport.taobao.com/ac/password_find.htm?spm=a2107.1.0.0.xV9LM4&from_site=0");
		
	}
	
}
