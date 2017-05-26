package com.insthub.ecmobile.component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.insthub.BeeFramework.Utils.TimeUtil;
import com.insthub.ecmobile.EcmobileApp;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.activity.B1_ProductListActivity;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.insthub.ecmobile.protocol.FILTER;
import com.insthub.ecmobile.protocol.SIMPLEGOODS;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("NewApi") public class LimitTimeDiscountCell extends LinearLayout
{
	protected ImageLoader imageLoader = ImageLoader.getInstance();
//	private ViewPager groupView;
	private SharedPreferences shared;
    ArrayList<SIMPLEGOODS> cellData = new ArrayList<SIMPLEGOODS>();
    private Timer timer;
    ImageView sell_like_hot_cakes_item_imgview;
    TextView discount_number ;
    TextView price ;
    
    TextView b0_The_header_name ;//标签的命
    TextView b0_limited_time_discount_when ;//限时打折的剩小时
    TextView b0_limited_time_discount_points ;//限时打折的剩分钟
    TextView b0_limited_time_discount_seconds ;//限时打折的剩秒
    LinearLayout b0_limited_time_discount_tiantian ; //每日新鲜天天秒
    
	Context mContext;
    Handler mHandler;
    int v_witch = 0; 

	public LimitTimeDiscountCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext = context ;
		initHander();
		initTimeer();
	}
    private void bindDataDelay() 
    {
    	init();
		initView();
    }
    
    
    private void initTimeer (){
    	  if (null!= timer)
          {
              timer.cancel();
              timer = null;
          }

          timer = new Timer();
          timer.schedule(new TimerTask() {
              @Override
              public void run()
              {
                  Message msg = mHandler.obtainMessage();
                  msg.what = 1;
                  mHandler.sendMessage(msg);
              }
          }, new Date(),1000);
          
    }
    
    
    private void initHander (){
    	mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
            	switch (msg.what) {
				case 0:
		            bindDataDelay();
					break;
				case 1:
					setRemaining();
					break;
				default:
					break;
				}
                return false;
            }
    	}) ;
    }
    
    private void initView (){
    	if(null == b0_The_header_name)
    	 b0_The_header_name = (TextView) this.findViewById(R.id.b0_The_header_name);
    	if(null == b0_limited_time_discount_when)
    	 b0_limited_time_discount_when = (TextView) this.findViewById(R.id.b0_limited_time_discount_when);
    	if(null == b0_limited_time_discount_points)
    	 b0_limited_time_discount_points = (TextView) this.findViewById(R.id.b0_limited_time_discount_points);
    	if(null == b0_limited_time_discount_seconds)
    	 b0_limited_time_discount_seconds = (TextView) this.findViewById(R.id.b0_limited_time_discount_seconds);
    	if(null == b0_limited_time_discount_tiantian)
    	 b0_limited_time_discount_tiantian = (LinearLayout) this.findViewById(R.id.b0_limited_time_discount_tiantian);
    }  

    
    
    private void init (){
    	shared = mContext.getSharedPreferences("userInfo", 0);
    	LinearLayout layout = (LinearLayout)this.findViewById(R.id.scrollView_horizontal); 
    	
    	for (int i = 0; i < cellData.size(); i++) {
        	View mview  = (FrameLayout)LayoutInflater.from(mContext).inflate(R.layout.b0_home_promte_goods_cell_item, null);
        	sell_like_hot_cakes_item_imgview = (ImageView) mview.findViewById(R.id.sell_like_hot_cakes_item_imgview);
        	discount_number = (TextView) mview.findViewById(R.id.discount_number);
        	price = (TextView) mview.findViewById(R.id.price);
        	
    		String netType = shared.getString("netType", "wifi");
			if(netType.equals("wifi")) {
                imageLoader.displayImage(cellData.get(i).img.thumb, sell_like_hot_cakes_item_imgview, EcmobileApp.options);
			} else {
                imageLoader.displayImage(cellData.get(i).img.small, sell_like_hot_cakes_item_imgview, EcmobileApp.options);
			}
			
			price.setText(cellData.get(i).promote_price + "￥");
			double de1 = 0.00;
			double de2 = 0.00;
			de1 = Double.valueOf(cellData.get(i).promote_price);
			de2 = Double.valueOf(cellData.get(i).market_price);
			BigDecimal mData = new BigDecimal((double)((de1 /de2) * 10)).setScale(1, BigDecimal.ROUND_HALF_UP);
			 
			discount_number.setText(""+ mData.floatValue() +"折");
			mview.setTag((i + 1));
			mview.setOnClickListener(mViewistener);
			layout.addView(mview);
		}
    }
    
    public void setRemaining (){
    	String[] str = {"时", "分", "秒"};
    	if(cellData != null && cellData.size() > 0)
    	{
    		if(cellData.get(0) != null && cellData.get(0).promote_end_date.length() > 0 )
    		{
    			for (int i = 0; i < str.length; i++) {
    				String time = TimeUtil.getFromEndWhenVehicle(cellData.get(0).promote_end_date, str[i]);
    				if(!time.isEmpty() && time.length() == 1)
    				{
    					time = "0" + time; 
    				}
    				
    				if(i == 0 && b0_limited_time_discount_when != null)
    				{
    					b0_limited_time_discount_when.setText(time);
    				}
    				else if(i == 1 && b0_limited_time_discount_points != null)
    				{
    					b0_limited_time_discount_points.setText(time);
    				}
    				else if(i == 2 && b0_limited_time_discount_seconds != null)
    				{
    					b0_limited_time_discount_seconds.setText(time);
    				}
				}
    		}
    	}
    }
    
    OnClickListener mViewistener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
//			int index = (Integer) arg0.getTag();
			Intent it = new Intent(mContext, B1_ProductListActivity.class);
			FILTER filter = new FILTER();
			it.putExtra(B1_ProductListActivity.FILTER, "");
			it.putExtra(B1_ProductListActivity.GENRE, ApiInterface.LIST_PROMOTE);
			it.putExtra("index", arg0.getTag().toString());
			mContext.startActivity(it);
			((Activity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			
		}
	};
    
    
    public void bindData(List<SIMPLEGOODS> listData)
    {
        cellData.clear();
        cellData.addAll(listData);
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0,30);
    }
}


