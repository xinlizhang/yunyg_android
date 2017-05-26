package com.insthub.ecmobile.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.insthub.ecmobile.EcmobileApp;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.activity.B2_ProductDetailActivity;
import com.insthub.ecmobile.protocol.SIMPLEGOODS;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 热门商品，精品推荐，新品上架，商品分类，列表的数据类。
 * 
 * @author Administrator
 *
 */
public class MoreGoodsListCell extends LinearLayout
{
    Context mContext;
    ImageView b1_more_goods_list_img;//商品的图片
    TextView b1_more_goods_list_name ;//商品的名字 
    TextView b1_more_goods_list_price ;//商品的价格
    TextView b1_more_goods_list_prase ; //好评度
    
    private SharedPreferences shared;
	private SharedPreferences.Editor editor;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

	public MoreGoodsListCell(Context context, AttributeSet attrs) {
		super(context, attrs);
        mContext = context;
	}

    void init()
    {
    	if(null == b1_more_goods_list_img)
    	{
    		b1_more_goods_list_img = (ImageView) this.findViewById(R.id.b1_more_goods_list_img);
    	}
    	if(null == b1_more_goods_list_name)
    	{
    		b1_more_goods_list_name = (TextView) this.findViewById(R.id.b1_more_goods_list_name);
    	}
    	if(null == b1_more_goods_list_price)
    	{
    		b1_more_goods_list_price = (TextView) this.findViewById(R.id.b1_more_goods_list_price);
    	}
    	if(null == b1_more_goods_list_prase)
    	{
    		b1_more_goods_list_prase = (TextView) this.findViewById(R.id.b1_more_goods_list_prase);
    	}
    }

    @SuppressLint("NewApi")
	public void bindData(final SIMPLEGOODS goodOne)
    {
		init();
		shared = mContext.getSharedPreferences("userInfo", 0);
		editor = shared.edit();

		String imageType = shared.getString("imageType", "mind");

		if (null != goodOne && null != goodOne.img && null != goodOne.img.thumb
				&& null != goodOne.img.small) {
			if (imageType.equals("high")) {
				imageLoader.displayImage(goodOne.img.thumb, b1_more_goods_list_img, EcmobileApp.options);

			} else if (imageType.equals("low")) {
				imageLoader.displayImage(goodOne.img.small, b1_more_goods_list_img, EcmobileApp.options);
			} else {
				String netType = shared.getString("netType", "wifi");
				if (netType.equals("wifi")) {
					imageLoader.displayImage(goodOne.img.thumb, b1_more_goods_list_img, EcmobileApp.options);
				} else {
					imageLoader.displayImage(goodOne.img.small, b1_more_goods_list_img, EcmobileApp.options);
				}
			}
		}
		
		if(null != goodOne.name && goodOne.name.length() > 0)
		{
			b1_more_goods_list_name.setText(goodOne.name);
		}
		
		if (null != goodOne.promote_price && goodOne.promote_price.length() > 0) {
			b1_more_goods_list_price.setText(goodOne.promote_price);
		} else if (null != goodOne.market_price && !goodOne.market_price.isEmpty()) {
			b1_more_goods_list_price.setText(goodOne.shop_price);
		} else {
			b1_more_goods_list_price.setText(goodOne.market_price);
		}
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(mContext,B2_ProductDetailActivity.class);
		    	it.putExtra("good_id", goodOne.goods_id);
		        mContext.startActivity(it);
			}
		});		

	}
}
