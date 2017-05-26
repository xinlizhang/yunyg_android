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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.insthub.ecmobile.EcmobileApp;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.activity.B1_ProductListActivity;
import com.insthub.ecmobile.protocol.HOMEBESTGOODS.HOMEBESTGOODSCELL;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.insthub.ecmobile.protocol.FILTER;
import com.insthub.ecmobile.protocol.SIMPLEGOODS;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 热销商品的一行
 * @author Administrator
 */
@SuppressLint("NewApi")
public class HomeBestGoodsCell extends LinearLayout
{
	protected ImageLoader imageLoader = ImageLoader.getInstance();
//	private ViewPager groupView;
	private SharedPreferences shared;
    ArrayList<SIMPLEGOODS> cellData = new ArrayList<SIMPLEGOODS>();
    
    ImageView sell_like_hot_cakes_item_imgview;
    TextView discount_number ;
    TextView goods_name ;
    TextView goods_price; 
    TextView b0_The_header_name ; 
    
	Context mContext;
    Handler mHandler;
    int v_witch = 0; 

	public HomeBestGoodsCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext = context ;
		mHandler = new Handler(new Handler.Callback() {
	            @Override
	            public boolean handleMessage(Message msg) {
	                bindDataDelay();
	                return false;
	            }
	    }) ;
	}
    private void bindDataDelay() 
    {
    	init();
    }
    private void init (){
    	shared = mContext.getSharedPreferences("userInfo", 0);
    	LinearLayout layout = (LinearLayout)this.findViewById(R.id.scrollView_horizontal); 
    	
    	for (int i = 0; i < cellData.size(); i++) {
        	View mview  = (FrameLayout)LayoutInflater.from(mContext).inflate(R.layout.b0_home_best_goods_cell_item, null);
        	RelativeLayout discount_bg = (RelativeLayout) mview.findViewById(R.id.discount_bg);
        	discount_bg.setVisibility(View.GONE);
        	
        	b0_The_header_name = (TextView) findViewById(R.id.b0_The_header_name); 
        	sell_like_hot_cakes_item_imgview = (ImageView) mview.findViewById(R.id.sell_like_hot_cakes_item_imgview);
        	discount_number = (TextView) mview.findViewById(R.id.discount_number);
        	goods_name = (TextView) mview.findViewById(R.id.goods_name);
        	goods_price = (TextView) mview.findViewById(R.id.goods_price); 
        	
    		String netType = shared.getString("netType", "wifi");
			if(netType.equals("wifi")) {
                imageLoader.displayImage(cellData.get(i).img.thumb, sell_like_hot_cakes_item_imgview, EcmobileApp.options);
			} else {
                imageLoader.displayImage(cellData.get(i).img.small, sell_like_hot_cakes_item_imgview, EcmobileApp.options);
			}
			
			String srr = cellData.get(i).shop_price ;
			
			goods_name.setText(cellData.get(i).name);
			goods_price.setText(cellData.get(i).market_price);
//			discount_number.setText(cellData.get(i).promote_end_date);
//			v_witch = mview.getWidth(); 
			mview.setTag(i);
			mview.setOnClickListener(mViewistener);
			layout.addView(mview);
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
			it.putExtra(B1_ProductListActivity.GENRE, ApiInterface.LIST_BEST);
			mContext.startActivity(it);
			((Activity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			
		}
	};
    
    
    public void bindData(ArrayList<SIMPLEGOODS> listData)
    {
        cellData.clear();
        cellData.addAll(listData);
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0,30);
    }
}
