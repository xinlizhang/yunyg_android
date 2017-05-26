package com.insthub.ecmobile.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.insthub.ecmobile.R;
import com.insthub.ecmobile.activity.LogisticsQueryActivity;
import com.insthub.ecmobile.protocol.PLAYER;

public class B1_ViewPagerAdapter extends PagerAdapter{
	private ImageView[] mImageViews;
	private Context context ; 
	private ArrayList<PLAYER> playersList = new ArrayList<PLAYER>();
	
	public B1_ViewPagerAdapter(Context context, ImageView[] mImageViews, ArrayList<PLAYER> playersList ){
		this.mImageViews = mImageViews;
		this.playersList = playersList ;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);
		
	}

	/**
	 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
	 */
	@Override
	public Object instantiateItem(View container, final int position) {
		ImageView mImageView = mImageViews[position % mImageViews.length] ;
		int i = mImageView.getDrawable().getMinimumWidth();; 
		int j = mImageView.getDrawable().getMinimumHeight() ;
		int di = mImageView.getDrawable().getIntrinsicWidth();; 
		int dj = mImageView.getDrawable().getIntrinsicHeight() ;
		((ViewPager)container).addView(mImageView , 0);
		mImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(	context, LogisticsQueryActivity.class);
				intent.putExtra("loadurl",  playersList.get( position % mImageViews.length).url);
				intent.putExtra("toptext",  "您感兴趣的");
				context.startActivity(intent);
				((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
		});
		return mImageViews[position % mImageViews.length];
	}
}
