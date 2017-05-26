package com.insthub.ecmobile.component;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.insthub.ecmobile.R;
import com.insthub.ecmobile.activity.LogisticsQueryActivity;
import com.insthub.ecmobile.activity.WeatherActivity;
import com.insthub.ecmobile.protocol.SIMPLEGOODS;
import com.insthub.ecmobile.zxing.CaptureActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 充值区的布局一行
 * @author Administrator
 */
public class PayAreaCell extends LinearLayout implements OnClickListener{
	ImageLoader imageLoader = ImageLoader.getInstance();
    ArrayList<SIMPLEGOODS> cellData = new ArrayList<SIMPLEGOODS>();
	Context mContext;
	Handler mHandler;
	ImageButton PayPhone ; //手机充值
	ImageButton LogisticView ; //物流查看
	ImageButton wheater ; //天气预报
	ImageButton scaner ; //扫一扫
	
	public PayAreaCell(Context context, AttributeSet attrs) 
	{
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
            	switch (msg.what) {
				case 0:
					bindDataDelay();
					break;

				default:
					break;
				}
            }
        };
	}
	
	 private void bindDataDelay()
	 {
		  initView();
	 }
	 
	private void initView() 
	{
		if(null == PayPhone)
		{
			PayPhone = (ImageButton) this.findViewById(R.id.b0_pay_btn);
		}
		if(null == LogisticView)
		{
			LogisticView = (ImageButton) this.findViewById(R.id.b0_logistics_btn);
		}
		if(null == wheater)
		{
			wheater = (ImageButton) this.findViewById(R.id.b0_wheater_btn);
		}
		if(null == scaner)
		{
			scaner = (ImageButton) this.findViewById(R.id.b0_scaner_btn);
		}
		
		PayPhone.setOnClickListener(this);
		LogisticView.setOnClickListener(this);
		wheater.setOnClickListener(this);
		scaner.setOnClickListener(this);
	/*	LogisticView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, LogisticsQueryActivity.class);
				intent.putExtra("loadurl", "http://m.kuaidi100.com/index_all.html");
				intent.putExtra("toptext",  "物流查询");
				mContext.startActivity(intent);
				((Activity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		});*/
	}

	public void bindData(List<SIMPLEGOODS> listData) 
	{
		cellData.clear();
		cellData.addAll(listData);
		mHandler.removeMessages(0);
		mHandler.sendEmptyMessageDelayed(0, 30);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (view.getId()) {
		case R.id.b0_pay_btn://手机充值
			Toast.makeText(mContext, "手机充值服务暂未开启", Toast.LENGTH_SHORT).show();
			break;
		case R.id.b0_logistics_btn://查看物流
			intent = new Intent(mContext, LogisticsQueryActivity.class);
			intent.putExtra("loadurl", "http://m.kuaidi100.com/index_all.html");
			intent.putExtra("toptext",  "物流查询");
			mContext.startActivity(intent);
			((Activity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case R.id.b0_wheater_btn:
			intent = new Intent(mContext, WeatherActivity.class);
			mContext.startActivity(intent);
			((Activity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			break;
		case R.id.b0_scaner_btn:
			Intent openCameraIntent = new Intent(mContext, CaptureActivity.class);
			mContext.startActivity(openCameraIntent);
			break;
		default:
			break;
		}
		
	}
}
