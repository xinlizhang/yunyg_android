package com.insthub.ecmobile.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.insthub.ecmobile.EcmobileApp;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.activity.B2_ProductDetailActivity;
import com.insthub.ecmobile.protocol.SIMPLEGOODS;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * ��ʱ������Ʒ��������
 * 
 * @author Administrator
 *
 */
public class PromoteGoodsListCell extends LinearLayout {
    Context mContext;
    ImageView b1_promote_goods_list_img;//��Ʒ��ͼƬ
    TextView b1_promote_goods_list_name ;//��Ʒ������ 
    TextView b1_promote_goods_list_price ;//��Ʒ�ļ۸�
    Button b1_promote_goods_list_spare ; //��ʡ��Ǯ��
    Button b1_promote_goods_list_purchase ;//����
    
    private SharedPreferences shared;
	private SharedPreferences.Editor editor;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

	public PromoteGoodsListCell(Context context, AttributeSet attrs) {
		super(context, attrs);
        mContext = context;
	}

    void init()
    {
    	if(null == b1_promote_goods_list_img)
    	{
    		b1_promote_goods_list_img = (ImageView) this.findViewById(R.id.b1_promote_goods_list_img);
    	}
    	if(null == b1_promote_goods_list_name)
    	{
    		b1_promote_goods_list_name = (TextView) this.findViewById(R.id.b1_promote_goods_list_name);
    	}
    	if(null == b1_promote_goods_list_price)
    	{
    		b1_promote_goods_list_price = (TextView) this.findViewById(R.id.b1_promote_goods_list_price);
    	}
    	if(null == b1_promote_goods_list_spare)
    	{
    		b1_promote_goods_list_spare = (Button) this.findViewById(R.id.b1_promote_goods_list_spare);
    	}
    	if(null == b1_promote_goods_list_purchase)
    	{
    		b1_promote_goods_list_purchase = (Button) this.findViewById(R.id.b1_promote_goods_list_purchase);
    	}
    	
    }

    @SuppressLint("NewApi") public void bindData(final SIMPLEGOODS goodOne)
 {
		init();
		shared = mContext.getSharedPreferences("userInfo", 0);
		editor = shared.edit();

		String imageType = shared.getString("imageType", "mind");

		if (null != goodOne && null != goodOne.img && null != goodOne.img.thumb
				&& null != goodOne.img.small) {
			if (imageType.equals("high")) {
				imageLoader.displayImage(goodOne.img.thumb, b1_promote_goods_list_img, EcmobileApp.options);

			} else if (imageType.equals("low")) {
				imageLoader.displayImage(goodOne.img.small, b1_promote_goods_list_img, EcmobileApp.options);
			} else {
				String netType = shared.getString("netType", "wifi");
				if (netType.equals("wifi")) {
					imageLoader.displayImage(goodOne.img.thumb, b1_promote_goods_list_img, EcmobileApp.options);
				} else {
					imageLoader.displayImage(goodOne.img.small, b1_promote_goods_list_img, EcmobileApp.options);
				}
			}
		}
		
		if(null != goodOne.name && goodOne.name.length() > 0)
		{
			b1_promote_goods_list_name.setText(goodOne.name);
		}
		
		if (null != goodOne.promote_price && goodOne.promote_price.length() > 0) {
			b1_promote_goods_list_price.setText("��" + goodOne.promote_price + "Ԫ");
			String market_price = goodOne.market_price.substring(1, goodOne.market_price.length());
			float price = (new Float(market_price) - new Float(goodOne.promote_price)) > 0 ? (new Float(market_price) - new Float(goodOne.promote_price)) : 0 ;
			b1_promote_goods_list_spare.setText("ʡ" + price  +"Ԫ");
		} else if(goodOne.market_price != null && !goodOne.market_price.isEmpty()){
			b1_promote_goods_list_price.setText("��" + goodOne.market_price + "Ԫ");
			b1_promote_goods_list_spare.setText("ʡ0.0Ԫ");
		} else if(goodOne.shop_price != null && !goodOne.shop_price.isEmpty()){
			b1_promote_goods_list_price.setText("��" + goodOne.shop_price + "Ԫ");
			b1_promote_goods_list_spare.setText("ʡ0.0Ԫ");
		} 
		
		b1_promote_goods_list_purchase.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(mContext,B2_ProductDetailActivity.class);
		    	it.putExtra("good_id", goodOne.goods_id);
		        mContext.startActivity(it);
			}
		});
		
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
