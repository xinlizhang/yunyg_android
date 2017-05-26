package com.insthub.ecmobile.component;

import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.insthub.ecmobile.EcmobileApp;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.activity.B1_ProductListActivity;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.insthub.ecmobile.protocol.CATEGORY;
import com.insthub.ecmobile.protocol.CATEGORYGOODS;
import com.insthub.ecmobile.protocol.FILTER;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CategorySellingCell extends LinearLayout 
{
	ArrayList<CATEGORY> categorygoods = new ArrayList<CATEGORY>();
	Context mContext;
    Handler mHandler;
    
    
    TextView[]  b0_home_category_goods_name = new TextView[6];
    ImageView[]  b0_home_category_goods_img = new ImageView[6];
    LinearLayout[] b0_home_category_goods_layout = new LinearLayout[6]; 
    int [] b0_home_category_goods_layout_id = 
    	{
    		R.id.b0_home_category_goods_layout_one,
    		R.id.b0_home_category_goods_layout_two,
    		R.id.b0_home_category_goods_layout_three,
    		R.id.b0_home_category_goods_layout_four,
    		R.id.b0_home_category_goods_layout_five,
    		R.id.b0_home_category_goods_layout_six,
    	};
    int [] b0_home_category_goods_name_id = 
    	{
    		R.id.b0_home_category_goods_name_one,
    		R.id.b0_home_category_goods_name_two,
    		R.id.b0_home_category_goods_name_three,
    		R.id.b0_home_category_goods_name_four,
    		R.id.b0_home_category_goods_name_five,
    		R.id.b0_home_category_goods_name_six,
    	};
    int [] b0_home_category_goods_img_id = {
    		R.id.b0_home_category_goods_img_one,
    		R.id.b0_home_category_goods_img_two,
    		R.id.b0_home_category_goods_img_three,
    		R.id.b0_home_category_goods_img_four,
    		R.id.b0_home_category_goods_img_five,
    		R.id.b0_home_category_goods_img_six,
    };
    
    
    private SharedPreferences shared;
	private SharedPreferences.Editor editor;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

	public CategorySellingCell(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                bindDataDelay();
            }
        };
	}

    void init()
    {
    	for (int i = 0; i < b0_home_category_goods_name.length; i++) {
    		if(null == b0_home_category_goods_name[i])
    		{
    			b0_home_category_goods_name[i] = (TextView) findViewById(b0_home_category_goods_name_id[i]);
    		}
    		if(null == b0_home_category_goods_img[i])
    		{
    			b0_home_category_goods_img[i] = (ImageView) findViewById(b0_home_category_goods_img_id[i]);
    		}
    		if(null == b0_home_category_goods_layout[i])
    		{
    			b0_home_category_goods_layout[i] = (LinearLayout) findViewById(b0_home_category_goods_layout_id[i]);
    		}
		}
    	
    }
    int count = 0;
    
    
	String[] ImageUrl = {
			"http://g-search1.alicdn.com/img/bao/uploaded/i4/i1/TB1EJZAHpXXXXa0aXXXXXXXXXXX_!!0-item_pic.jpg_250x250.jpg",
			"http://gw.alicdn.com/bao/uploaded/i4/18740080727141003/TB2uW7KcFXXXXaMXXXXXXXXXXXX_!!48828740-0-globalbuys.jpg_230x230xzQ50.jpg",
			"http://gw.alicdn.com/bao/uploaded/i1/10541080481930143/TB2gDMDcFXXXXbFXXXXXXXXXXXX_!!1816970541-0-globalbuys.jpg_230x230xzQ50.jpg",
			"http://gw.alicdn.com/bao/uploaded/i1/13326080567740632/TB28QAAcFXXXXchXXXXXXXXXXXX_!!180013326-0-globalbuys.jpg_230x230xzQ50.jpg",
			"http://gw.alicdn.com/bao/uploaded/i4/18740080727141003/TB2uW7KcFXXXXaMXXXXXXXXXXXX_!!48828740-0-globalbuys.jpg_230x230xzQ50.jpg", 
			"http://gw.alicdn.com/bao/uploaded/i1/13973080692143389/TB2TR3xcFXXXXcMXXXXXXXXXXXX_!!46303973-0-globalbuys.jpg_230x230xzQ50.jpg",};

    public void bindDataDelay()
    {
        init();
        
        shared = mContext.getSharedPreferences("userInfo", 0); 
		for (int i = 0; i < categorygoods.size(); i++) {
			if(null == categorygoods.get(i))
			{
				continue;
			}
			if(i < b0_home_category_goods_name.length)
			{	
				if(null != categorygoods.get(i)){
					b0_home_category_goods_name[i].setText(categorygoods.get(i).name);
				}
			}
			if(i < b0_home_category_goods_img.length){
				if(null != categorygoods.get(i).category_thumb)
				{
					b0_home_category_goods_img[i].setVisibility(View.VISIBLE);
                    imageLoader.displayImage(categorygoods.get(i).category_thumb, b0_home_category_goods_img[i], EcmobileApp.options);
//                  String netType = shared.getString("netType", "wifi");
//	    			if(netType.equals("wifi")) {
//	                    imageLoader.displayImage(categorygoods.get(i).img.thumb
//	                    		,b0_home_category_goods_img[i], EcmobileApp.options);
//	    			} else {
//	                    imageLoader.displayImage(categorygoods.get(i).img.small
//	                    		,b0_home_category_goods_img[i], EcmobileApp.options);
//	    			}
					
					
	    /*			b0_home_category_goods_img[i].setTag(categorygoods.get(i).id);
	    			b0_home_category_goods_img[i].setOnClickListener(new OnClickListener() {
	    					@Override
	    					public void onClick(View v) {
	    						 Intent it = new Intent(mContext, B2_ProductDetailActivity.class);
	    						 String srr = (String)v.getTag() ; 
	    			             it.putExtra("good_id", srr);
	    			             mContext.startActivity(it);
	    			             ((EcmobileMainActivity)mContext).overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
	    					}
	    			});*/
	    			
					final int index = i;
					b0_home_category_goods_layout[i]
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									try {
										Intent it = new Intent(mContext, B1_ProductListActivity.class);
										FILTER filter = new FILTER();
										filter.category_id = String.valueOf(categorygoods.get(index).id);
										String str = filter.toJson().toString();
										it.putExtra(B1_ProductListActivity.FILTER, filter.toJson().toString());
										it.putExtra(B1_ProductListActivity.GENRE, ApiInterface.SEARCH);
										// public static final String GENRE =
										// "genre";
										mContext.startActivity(it);
										((Activity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
					
				}
				else 
				{
					b0_home_category_goods_img[i].setVisibility(View.GONE);
				}
			}
		}

/*        if (null != categorygoods.name)
        {
            good_cell_name_one.setText(categorygoods.name);
            count++;
            
            good_cell_one.setOnClickListener(new OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {
    				 
    				Intent it = new Intent(mContext, B1_ProductListActivity.class);
                    FILTER filter = new FILTER();
                    filter.category_id = String.valueOf(categorygoods.id);
                    try
                    {
                        it.putExtra(B1_ProductListActivity.FILTER,filter.toJson().toString());
                    }
                    catch (JSONException e)
                    {

                    }

                    mContext.startActivity(it);
                    ((Activity) mContext).overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    			}
    		});
        }
        
        if (listData.size() >0)
        {
            SIMPLEGOODS goodOne = listData.get(0);
            
            good_cell_photo_one.setVisibility(View.VISIBLE);
            
            if(imageType.equals("high")) {
                imageLoader.displayImage(goodOne.img.thumb,good_cell_photo_one, EcmobileApp.options);
    		} else if(imageType.equals("low")) {
                imageLoader.displayImage(goodOne.img.small,good_cell_photo_one, EcmobileApp.options);
    		} else {
    			String netType = shared.getString("netType", "wifi");
    			if(netType.equals("wifi")) {
                    imageLoader.displayImage(goodOne.img.thumb,good_cell_photo_one, EcmobileApp.options);
    			} else {
                    imageLoader.displayImage(goodOne.img.small,good_cell_photo_one, EcmobileApp.options);
    			}
    		}
            
            

            if (listData.size() >1)
            {
                good_cell_two.setVisibility(View.VISIBLE);
                final SIMPLEGOODS goodTwo = listData.get(1);
                if (null != goodTwo && null != goodTwo.img && null != goodTwo.img.thumb && null != goodTwo.img.small)
                {
                	if(imageType.equals("high")) {
                        imageLoader.displayImage(goodTwo.img.thumb,good_cell_photo_two, EcmobileApp.options);
            		} else if(imageType.equals("low")) {
                        imageLoader.displayImage(goodTwo.img.small,good_cell_photo_two, EcmobileApp.options);
            		} else {
            			String netType = shared.getString("netType", "wifi");
            			if(netType.equals("wifi")) {
                            imageLoader.displayImage(goodTwo.img.thumb,good_cell_photo_two, EcmobileApp.options);
            			} else {
                            imageLoader.displayImage(goodTwo.img.small,good_cell_photo_two, EcmobileApp.options);
            			}
            		}
                	
                    good_cell_two.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							 
							 Intent it = new Intent(mContext, B2_ProductDetailActivity.class);
			                 it.putExtra("good_id",goodTwo.id);
			                 mContext.startActivity(it);
			                 ((EcmobileMainActivity)mContext).overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
						}
					});
                    
                }
                good_cell_name_two.setText(goodTwo.name);
                good_cell_price_two.setText(goodTwo.shop_price);

                if (listData.size() > 2)
                {
                    good_cell_three.setVisibility(View.VISIBLE);
                    final SIMPLEGOODS goodThree = listData.get(2);
                    if (null != goodThree && null != goodThree.img && null != goodThree.img.thumb && null != goodThree.img.small)
                    {
                    	
                    	if(imageType.equals("high")) {
                            imageLoader.displayImage(goodThree.img.thumb,good_cell_photo_three, EcmobileApp.options);
                		} else if(imageType.equals("low")) {
                            imageLoader.displayImage(goodThree.img.small,good_cell_photo_three, EcmobileApp.options);
                		} else {
                			String netType = shared.getString("netType", "wifi");
                			if(netType.equals("wifi")) {
                                imageLoader.displayImage(goodThree.img.thumb,good_cell_photo_three, EcmobileApp.options);
                			} else {
                                imageLoader.displayImage(goodThree.img.small,good_cell_photo_three, EcmobileApp.options);
                			}
                		}
                    	
                        good_cell_three.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								 
								 Intent it = new Intent(mContext, B2_ProductDetailActivity.class);
			                     it.putExtra("good_id",goodThree.id);
			                     mContext.startActivity(it);
			                     ((EcmobileMainActivity)mContext).overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
							}
						});
                       
                    }
                    good_cell_name_three.setText(goodThree.name);
                    good_cell_price_three.setText(goodThree.shop_price);
                }
                else
                {
                    good_cell_three.setVisibility(View.INVISIBLE);
                }
            }
            else
            {
                good_cell_two.setVisibility(View.INVISIBLE);
                good_cell_three.setVisibility(View.INVISIBLE);
            }
        } else {
            good_cell_photo_one.setVisibility(View.INVISIBLE);
            good_cell_two.setVisibility(View.INVISIBLE);
            good_cell_three.setVisibility(View.INVISIBLE);
        }*/
    }

    public void bindData(ArrayList<CATEGORY> categorygoods)
    {
        this.categorygoods.clear();
        this.categorygoods.addAll(categorygoods);
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0,30);

    	
    }
}
