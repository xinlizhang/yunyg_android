package com.insthub.ecmobile.component;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.insthub.ecmobile.EcmobileApp;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.activity.B1_ProductListActivity;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.insthub.ecmobile.protocol.FILTER;
import com.insthub.ecmobile.protocol.HOMEBOTGOODS;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 热销商品的一行
 * @author Administrator
 */
@SuppressLint("NewApi")
public class HomeHotGoodsCell extends LinearLayout
{
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private SharedPreferences shared;
    ArrayList<HOMEBOTGOODS> cellData = new ArrayList<HOMEBOTGOODS>();
    
    private TextView b0_The_header_name ; 
    private ImageView[] b0_home_hot_img = new ImageView[4];
    private TextView[] b0_home_hot_type_name = new TextView[4];
    private TextView[] b0_home_hot_goods_name = new TextView[4];
    private FrameLayout  b0_home_hot_layout_one;
    private FrameLayout  b0_home_hot_layout_two;
    private FrameLayout  b0_home_hot_layout_three;
    private FrameLayout  b0_home_hot_layout_four;
    private int[] b0_home_hot_id = 
    	{
    		R.id.b0_home_hot_img_one,
    		R.id.b0_home_hot_img_two,
    		R.id.b0_home_hot_img_three,
    		R.id.b0_home_hot_img_four,
    		R.id.b0_home_hot_type_name_one,
    		R.id.b0_home_hot_type_name_two,
    		R.id.b0_home_hot_type_name_three,
    		R.id.b0_home_hot_type_name_four,
    		R.id.b0_home_hot_goods_name_one,
    		R.id.b0_home_hot_goods_name_two,
    		R.id.b0_home_hot_goods_name_three,
    		R.id.b0_home_hot_goods_name_four,
    	};
	private Context mContext;
    private Handler mHandler;

	public HomeHotGoodsCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext = context ;
		mHandler = new Handler(new Handler.Callback() {
	            @Override
	            public boolean handleMessage(Message msg) {
	            	InitView();
	                InitData();
	                return false;
	            }
	    }) ;
	}
    	
    private void InitView (){
    	if(null == b0_The_header_name)
    	{
    		b0_The_header_name = (TextView) findViewById(R.id.b0_The_header_name);
    	}
    	if(null == b0_home_hot_layout_one){
    		b0_home_hot_layout_one = (FrameLayout) this.findViewById(R.id.b0_home_hot_layout_one); 
    	}
    	if(null == b0_home_hot_layout_two){
    		b0_home_hot_layout_two = (FrameLayout) this.findViewById(R.id.b0_home_hot_layout_two); 
    	}
    	if(null == b0_home_hot_layout_three){
    		b0_home_hot_layout_three = (FrameLayout) this.findViewById(R.id.b0_home_hot_layout_three); 
    	}
    	if(null == b0_home_hot_layout_four){
    		b0_home_hot_layout_four = (FrameLayout) this.findViewById(R.id.b0_home_hot_layout_four); 
    	}
    	
    	for (int i = 0; i < b0_home_hot_id.length; i++) {
			if(i < ((b0_home_hot_id.length / 3) * 1))
			{
				if(null == b0_home_hot_img[i])
				b0_home_hot_img[i] = (ImageView) findViewById(b0_home_hot_id[i]);
			}
			else if(i < ((b0_home_hot_id.length / 3) * 2))
			{	
				if(null == b0_home_hot_type_name[i % 4])
				b0_home_hot_type_name[i % 4] = (TextView) findViewById(b0_home_hot_id[i]);
			}
			else if(i < ((b0_home_hot_id.length / 3) * 3))
			{
				if(null == b0_home_hot_goods_name[i % 4])
				b0_home_hot_goods_name[i % 4] = (TextView) findViewById(b0_home_hot_id[i]);
			}
		}
    }
    
    private void InitData (){
    	shared = mContext.getSharedPreferences("userInfo", 0);
  		String netType = shared.getString("netType", "wifi");
  		b0_The_header_name.setText("热门商品");
		if(cellData.size() > 0){
			for (int j = 0; j < 4; j++) {
				if(cellData.get(j) != null /*&& cellData.get(j).goods != null*/){
					b0_home_hot_img[j].setVisibility(View.VISIBLE);
					b0_home_hot_type_name[j].setVisibility(View.VISIBLE);
					b0_home_hot_goods_name[j].setVisibility(View.VISIBLE);
//					if(cellData.get(j).goods.size() == 0)
//						continue;
					if(netType.equals("wifi")) {
		                imageLoader.displayImage(cellData.get(j)./*goods.get(0).*/img.thumb, b0_home_hot_img[j], EcmobileApp.options);
					} else {
		                imageLoader.displayImage(cellData.get(j)./*goods.get(0).*/img.small, b0_home_hot_img[j], EcmobileApp.options);
					}
//					b0_home_hot_type_name[j].setText(cellData.get(j).name);
					b0_home_hot_type_name[j].setText(cellData.get(j).name);
//					b0_home_hot_goods_name[j].setText(cellData.get(j)./*goods.get(0).*/name);
					b0_home_hot_goods_name[j].setVisibility(View.GONE);
				}else {
					b0_home_hot_img[j].setVisibility(View.GONE);
					b0_home_hot_type_name[j].setVisibility(View.GONE);
					b0_home_hot_goods_name[j].setVisibility(View.GONE);
				}
				b0_home_hot_layout_one.setOnClickListener(mSkipistener);
				b0_home_hot_layout_two.setOnClickListener(mSkipistener);
				b0_home_hot_layout_three.setOnClickListener(mSkipistener);
				b0_home_hot_layout_four.setOnClickListener(mSkipistener);
			}
		}
    }
    
    
    OnClickListener mSkipistener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent it = new Intent(mContext, B1_ProductListActivity.class);
			FILTER filter = new FILTER();
			it.putExtra(B1_ProductListActivity.FILTER, "");
			it.putExtra(B1_ProductListActivity.GENRE, ApiInterface.LIST_HOT);
			mContext.startActivity(it);
			((Activity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		}
	};
    
    
    public void bindData(List<HOMEBOTGOODS> listData)
    {
        cellData.clear();
        cellData.addAll(listData);
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0,30);
    }
}
