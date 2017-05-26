package com.insthub.ecmobile.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.insthub.ecmobile.R;
import com.insthub.ecmobile.activity.F2_EditAddressActivity;
import com.insthub.ecmobile.protocol.ADDRESS;

public class F0_AddressListAdapter extends BaseAdapter {

	private Context context;
	private List<ADDRESS> list;
	private LayoutInflater inflater;
	public int a = 0;
	public Handler parentHandler;
	
	public static Map<Integer, Boolean> isSelected; 
	private int flag;
	
	public F0_AddressListAdapter(Context context, List<ADDRESS> list, int flag) {
		this.context = context;
		this.list = list;
		this.flag = flag;
		inflater = LayoutInflater.from(context);
		init(a);
	}
	
	private void init(int position) {
		isSelected = new HashMap<Integer, Boolean>();
		for (int i = 0; i < list.size(); i++){
			if(i == position) {
				isSelected.put(i, true);
			} else {
				isSelected.put(i, false);
			}
		}
	}
	
	@Override
	public int getCount() {
		 
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		 
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		 
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		 
		return position;
	}

	@Override
	public int getViewTypeCount() {
		 
		int count = list != null ? list.size() : 0;
		return count;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		 
		final ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.f0_address_list_item, null);
//			convertView = inflater.inflate(R.layout.f0_address_cell, null);
			holder.layout = (LinearLayout) convertView.findViewById(R.id.address_manage_item_layout);
			holder.name = (TextView) convertView.findViewById(R.id.address_manage_item_name);
			holder.mobile = (TextView) convertView.findViewById(R.id.address_manage_item_mobile);
			holder.tel = (TextView) convertView.findViewById(R.id.address_manage_item_tel);
//			holder.address_area = (TextView) convertView.findViewById(R.id.address_area);
//			holder.province = (TextView) convertView.findViewById(R.id.address_manage_item_province);
//			holder.city = (TextView) convertView.findViewById(R.id.address_manage_item_city);
//			holder.county = (TextView) convertView.findViewById(R.id.address_manage_item_county);
			holder.detail = (TextView) convertView.findViewById(R.id.address_manage_item_detail);
			holder.select_layout = (LinearLayout) convertView.findViewById(R.id.f0_address_list_item_select);
			holder.editor_layout = (LinearLayout) convertView.findViewById(R.id.f0_address_list_item_editor);
			holder.delete_layout = (LinearLayout) convertView.findViewById(R.id.f0_address_list_item_delete);
			holder.select = (ImageView) convertView.findViewById(R.id.address_manage_itme_select);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final ADDRESS address = list.get(position);
		if(address.default_address == 1) {
			init(position);
		}
//		
		holder.name.setText(address.consignee);
		holder.mobile.setText(address.mobile);
		holder.tel.setText(address.tel);
		
//		holder.address_area.setText(this.context.getResources().getString(R.string.address_area) + " £º ");
//		holder.province.setText(address.province_name);
//		if(!address.city_name.equals("null")) {
//			holder.city.setText(address.city_name);
//		}
//		if(!address.district_name.equals("null")) {
//			holder.county.setText(address.district_name);
//		}
		
		holder.detail.setText(/*"ÏêÏ¸µØÖ· £º " + address.address*/ 
				address.province_name + (address.city_name.equals("null") ? "" : address.city_name) + 
				(address.district_name.equals("null") ? "" : address.district_name) + address.address);
		
		if(isSelected.get(position)) {
//			holder.select.setVisibility(View.VISIBLE);
			holder.select.setImageResource(R.drawable.f0_address_list_item_select_h);
			holder.name.setTextColor(Color.parseColor("#666699"));
			holder.mobile.setTextColor(Color.parseColor("#666699"));
			holder.tel.setTextColor(Color.parseColor("#666699"));
//			holder.address_area.setTextColor(Color.parseColor("#666699"));
//			holder.province.setTextColor(Color.parseColor("#666699"));
//			holder.city.setTextColor(Color.parseColor("#666699"));
//			holder.county.setTextColor(Color.parseColor("#666699"));
			holder.detail.setTextColor(Color.parseColor("#666699"));
		} else {
//			holder.select.setVisibility(View.GONE);
			holder.select.setImageBitmap(null);
			holder.name.setTextColor(Color.parseColor("#000000"));
			holder.mobile.setTextColor(Color.parseColor("#000000"));
			holder.tel.setTextColor(Color.parseColor("#000000"));
//			holder.address_area.setTextColor(Color.parseColor("#000000"));
//			holder.province.setTextColor(Color.parseColor("#000000"));
//			holder.city.setTextColor(Color.parseColor("#000000"));
//			holder.county.setTextColor(Color.parseColor("#000000"));
			holder.detail.setTextColor(Color.parseColor("#000000"));
		}
		
		holder.select_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Message msg = new Message();
                msg.what = 1;
                msg.arg1 = Integer.valueOf(address.id);
                parentHandler.handleMessage(msg);
			}
		});
		
		
		holder.layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(flag == 1) {
					Message msg = new Message();
	                msg.what = 1;
	                msg.arg1 = Integer.valueOf(address.id);
	                parentHandler.handleMessage(msg);
				} else {
					Intent intent = new Intent(context, F2_EditAddressActivity.class);
					intent.putExtra("address_id", address.id+"");
					context.startActivity(intent);
				}
			}
		});
		
		holder.editor_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(flag == 1) {
					Message msg = new Message();
	                msg.what = 1;
	                msg.arg1 = Integer.valueOf(address.id);
	                parentHandler.handleMessage(msg);
				} else {
					Intent intent = new Intent(context, F2_EditAddressActivity.class);
					intent.putExtra("address_id", address.id+"");
					context.startActivity(intent);
				}
			}
		});
		
		holder.delete_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Message msg = new Message();
                msg.what = 2;
                msg.arg1 = Integer.valueOf(address.id);
                parentHandler.handleMessage(msg);
			}
		});
		return convertView;
	}
	
	class ViewHolder {
		private TextView name;
		private TextView mobile;
		private TextView tel;
		private TextView address_area ; 
		private TextView province;
		private TextView city;
		private TextView county;
		private TextView detail;
		private ImageView select;
		private LinearLayout select_layout;
		private LinearLayout editor_layout;
		private LinearLayout delete_layout;
		private LinearLayout layout;
	}

}
