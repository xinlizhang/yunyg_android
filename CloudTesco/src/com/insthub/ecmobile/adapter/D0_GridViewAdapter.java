package com.insthub.ecmobile.adapter;

import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.insthub.ecmobile.EcmobileApp;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.activity.B1_ProductListActivity;
import com.insthub.ecmobile.activity.D1_CategoryActivity;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.insthub.ecmobile.protocol.CATEGORY;
import com.insthub.ecmobile.protocol.FILTER;
import com.nostra13.universalimageloader.core.ImageLoader;

public class D0_GridViewAdapter extends BaseAdapter {
	// 定义Context
		private ImageLoader imageLoader = ImageLoader.getInstance();
		private LayoutInflater mInflater;
	    private ArrayList<CATEGORY> list;
	    private Context context;
	    private CATEGORY type;
		public D0_GridViewAdapter(Context context, ArrayList<CATEGORY> list){
			mInflater = LayoutInflater.from(context);
			this.list = list;
			this.context = context;
		}
		
		@Override
		public int getCount() {
			if(list!= null && list.size() > 0)
				return list.size();
			else
			    return 0;
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final MyView view;
			if(convertView == null)
			{
				view = new MyView();
				convertView = mInflater.inflate(R.layout.list_pro_type_item, null);
				view.icon = (ImageView)convertView.findViewById(R.id.typeicon);
				view.name = (TextView)convertView.findViewById(R.id.typename);
				convertView.setTag(view);
			}
			else
			{
				view=(MyView)convertView.getTag();
			}
			if(list != null && list.size() > 0)
			{
				type = list.get(position);
				
				view.name.setText(type.name);//类型的名字
				
			    imageLoader.displayImage(type.category_thumb, view.icon, EcmobileApp.options);//类型的价格
			    
			    convertView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						try {
							Intent intent = new Intent(D0_GridViewAdapter.this.context, B1_ProductListActivity.class);
							FILTER filter = new FILTER();
							filter.category_id = list.get(position).id;
							intent.putExtra(B1_ProductListActivity.FILTER, filter.toJson().toString());
							intent.putExtra(B1_ProductListActivity.GENRE, ApiInterface.SEARCH);
							D0_GridViewAdapter.this.context.startActivity(intent);
							((Activity) D0_GridViewAdapter.this.context).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}
	        return convertView;
		}
		
		private class MyView{
			private ImageView icon;		
			private TextView name;
		}
		
}
