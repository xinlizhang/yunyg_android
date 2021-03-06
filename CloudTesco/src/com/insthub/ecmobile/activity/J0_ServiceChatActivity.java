package com.insthub.ecmobile.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.BeeFramework.model.BusinessResponse;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.adapter.J0_ChatListAdapter;
import com.insthub.ecmobile.protocol.CHATDATA;
import com.insthub.ecmobile.protocol.PHOTO;
import com.insthub.ecmobile.protocol.RECENTSESSION;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("NewApi") 
public class J0_ServiceChatActivity extends BaseActivity implements BusinessResponse, IXListViewListener,OnClickListener 
{
	private RelativeLayout title_view ;
	private ImageView back ; 
	private TextView top_view_text ;
	private TextView recent_session_text;
	private Button top_view_right_btn; 
	private Button send; 
	private List<CHATDATA> chat_list = new ArrayList<CHATDATA>();
	private List<RECENTSESSION> recent_session_list = new ArrayList<RECENTSESSION>();
	
//	private XListView chatlistView;
	private XListView recentsessionlistview;
//	private J0_ChatListAdapter ChatListAdapter;
	private J0_RecentSessionAdapter RecentSessionAdapter;
	
	private RelativeLayout recent_session_layout; 
	
	SimpleDateFormat formatter;
	
	private WebView webView;
	
//	private EditText Input;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.j0_servicechat);
		
		initView();//初始化view
		
		RecentSessionAdapter = new J0_RecentSessionAdapter();
		recentsessionlistview.setAdapter(RecentSessionAdapter);
		recent_session_layout = (RelativeLayout) this.findViewById(R.id.recent_session_layout);
		recent_session_layout.setOnClickListener(this);
		
	}
	
	private void initView (){
		title_view = (RelativeLayout) this.findViewById(R.id.title_view);
		title_view.setBackgroundDrawable(getResources().getDrawable(R.color.j0_title_view_bg));
		
		back = (ImageView) this.findViewById(R.id.top_view_back);
		back.setOnClickListener(this);
		
		top_view_text = (TextView) this.findViewById(R.id.top_view_text);
		top_view_text.setText("聊天");
		
		recent_session_text = (TextView) this.findViewById(R.id.recent_session_text);
		recent_session_text.setOnClickListener(this);
		
		top_view_right_btn = (Button) this.findViewById(R.id.top_view_right_btn);
		top_view_right_btn.setVisibility(View.VISIBLE);
		top_view_right_btn.setBackgroundResource(R.drawable.btn_chat_record);
		top_view_right_btn.setOnClickListener(this);
		RelativeLayout.LayoutParams btnPara  = (LayoutParams) top_view_right_btn.getLayoutParams();
		btnPara.width = 60 ; 
		btnPara.height = 60 ;
		btnPara.rightMargin = 10 ;
		top_view_right_btn.setLayoutParams(btnPara);
		
		recentsessionlistview = (XListView) this.findViewById(R.id.recent_session_list);
		recentsessionlistview.setPullLoadEnable(false);
		recentsessionlistview.setRefreshTime();
		recentsessionlistview.setXListViewListener(this, 1);
		
		for (int i = 0; i < 1; i++) {
			RECENTSESSION MRECENTSESSION = new RECENTSESSION();
			MRECENTSESSION.name = "雲易购旗舰店";
			PHOTO photo = new PHOTO();
			photo.url = "http://www.disanqiche.com/yunyigou/images/201505/source_img/87_G_1431635977982.jpg";
			photo.small = "http://www.disanqiche.com/yunyigou/images/201505/source_img/87_G_1431635977982.jpg";
			photo.thumb = "http://www.disanqiche.com/yunyigou/images/201505/source_img/87_G_1431635977982.jpg";
			MRECENTSESSION.img = photo;
			recent_session_list.add(MRECENTSESSION);
		}
		
		webView = (WebView) findViewById(R.id.webview);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		WebSettings settings = webView.getSettings();
		settings.setUseWideViewPort(false);
		settings.setLoadWithOverviewMode(true);
		settings.setSupportZoom(false);
		settings.setJavaScriptEnabled(true);
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
			}
		});
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Intent intent = null;
				if (url.startsWith("app:")) {
					/*String value[] = url.split(":");
					if (value[1].equals("wodeshoucang")) {
						intent = new Intent(Activity_A.this, Activity_love.class);
						intent.putExtra("hid", false);
					} else if (value[1].equals("wodedingdan")) {
						intent = new Intent(Activity_A.this, Activity_order.class);
					} else if (value[1].equals("wodejifen")) {
						intent = new Intent(Activity_A.this, Activity_integrallog.class);
					} else if (value[1].equals("wodehongbao")) {
						intent = new Intent(Activity_A.this, Activity_redbag.class);
					}*/
				} else {
//					intent = new Intent(Activity_A.this, Activity_Web.class);
//					intent.putExtra("url", url);
				}
				if (intent != null)
					startActivity(intent);
				return true;
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
		});
//		webView.loadUrl("http://115.28.66.32/chat/");
		webView.loadUrl("http://zhangxinli.com.cn/service/");
		
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.top_view_right_btn:
			recent_session_layout.setVisibility(View.VISIBLE);
			break;
		case R.id.top_view_back:
			this.finish();
			break;
		case R.id.recent_session_layout://给布局设置一个空监听只有布局内的控件有点击事件，可以屏蔽其他那妞的点击事件
			
			break;
		case R.id.recent_session_text:
			recent_session_layout.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	@Override
	public void onRefresh(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadState(int state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException {
		// TODO Auto-generated method stub
		
	}
	
	
	public class J0_RecentSessionAdapter extends BaseAdapter {
	    private ImageLoader imageLoader = ImageLoader.getInstance();
		private SharedPreferences shared;
		
		public J0_RecentSessionAdapter() {
			shared = J0_ServiceChatActivity.this.getSharedPreferences("userInfo", 0);
		}
		@Override
		public int getCount() {		
			return recent_session_list.size();
		}
		@Override
		public long getItemId(int position) {		
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {		
			final ViewHolder viewHolder;
			String imageType = shared.getString("imageType", "mind");
			RECENTSESSION entity = recent_session_list.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(J0_ServiceChatActivity.this).inflate(R.layout.j0_recent_session_item, null);
				viewHolder = new ViewHolder();
				viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
				viewHolder.name = (TextView) convertView.findViewById(R.id.name);
				convertView.setTag(viewHolder);
			}else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.name.setText(entity.name);
			viewHolder.img.setBackgroundResource(R.drawable.ecmobile_logo);
			
		/*	if (imageType.equals("high")) {
				imageLoader.displayImage(entity.img.thumb, viewHolder.img, EcmobileApp.options);
			} else if (imageType.equals("low")) {
				imageLoader.displayImage(entity.img.small, viewHolder.img, EcmobileApp.options);
			} else {
				String netType = shared.getString("netType", "wifi");
				if (netType.equals("wifi")) {
					imageLoader.displayImage(entity.img.thumb, viewHolder.img, EcmobileApp.options);
				} else {
					imageLoader.displayImage(entity.img.small, viewHolder.img, EcmobileApp.options);
				}
			}*/
			return convertView;
		}
		class ViewHolder {
			public TextView name;
			public ImageView img;
		}
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	
	
	

}
