package com.insthub.ecmobile.adapter;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.insthub.BeeFramework.view.ToastView;
import com.insthub.ecmobile.EcmobileApp;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.activity.E4_HistoryActivity;
import com.insthub.ecmobile.activity.E6_ShippingStatusActivity;
import com.insthub.ecmobile.activity.E7_OrderDetailsActivity;
import com.insthub.ecmobile.activity.E8_CommentActivity;
import com.insthub.ecmobile.protocol.GOODORDER;
import com.insthub.ecmobile.protocol.ORDER_GOODS_LIST;
import com.nostra13.universalimageloader.core.ImageLoader;

public class E4_HistoryAdapter extends BaseAdapter {

	public Context context;
	public List<GOODORDER> list;
	public int flag;
	
	private LayoutInflater inflater;
	
	public Handler parentHandler;
	
	private SharedPreferences shared;
	private SharedPreferences.Editor editor;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	public E4_HistoryAdapter(Context context, List<GOODORDER> list, int flag) {
		this.context = context;
		this.list = list;
		this.flag = flag;
		inflater = LayoutInflater.from(context);
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
		 
		return 1;
	}


	@Override
	public int getViewTypeCount() {
		 
		return 1;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 
		final ViewHolder holder;
        final Resources resource = (Resources) context.getResources();
		final GOODORDER order = list.get(position);
		
		//if(convertView == null) {
			holder = new ViewHolder();
//			convertView = inflater.inflate(R.layout.e4_history_cell, null);
			convertView = inflater.inflate(R.layout.e4_indent_cell, null);
//			holder.sno = (TextView) convertView.findViewById(R.id.trade_item_sno);
//			holder.time = (TextView) convertView.findViewById(R.id.trade_item_time);
			holder.body = (LinearLayout) convertView.findViewById(R.id.trade_item_body);
//			holder.fee = (TextView) convertView.findViewById(R.id.trade_item_fee);
//			holder.red_paper = (TextView) convertView.findViewById(R.id.trade_item_redPaper);
//			holder.score = (TextView) convertView.findViewById(R.id.trade_item_score);
//			holder.total = (TextView) convertView.findViewById(R.id.trade_item_total);
//			holder.check = (Button) convertView.findViewById(R.id.trade_item_check);
//			holder.ok = (Button) convertView.findViewById(R.id.trade_item_ok);
			
			ArrayList<ORDER_GOODS_LIST> goods_list = list.get(position).goods_list;
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			Date currentTime = new Date(order.order_time);
			
			for(int i = 0;i < goods_list.size(); i++) {
				View view = LayoutInflater.from(context).inflate(R.layout.trade_body, null);
				RelativeLayout operation = (RelativeLayout) view.findViewById(R.id.operation);
				LinearLayout order_layout = (LinearLayout) view.findViewById(R.id.order_layout);
				ImageView image = (ImageView) view.findViewById(R.id.trade_body_image);
				TextView text = (TextView) view.findViewById(R.id.trade_body_text);
				TextView payment = (TextView) view.findViewById(R.id.real_payment);
				Button operation_btn = (Button) view.findViewById(R.id.operation_btn);
				Button check_logistice = (Button) view.findViewById(R.id.check_logistice);
				TextView order_time = (TextView) view.findViewById(R.id.order_time);
				TextView order_label_name = (TextView) view.findViewById(R.id.order_label_name);
				
//				TextView total = (TextView) view.findViewById(R.id.trade_body_total);
//				TextView num = (TextView) view.findViewById(R.id.trade_body_num);
				holder.body.addView(view);
				text.setText(goods_list.get(i).name);
				
				order_time.setText(order.order_time.toString().subSequence(0, order.order_time.length() - 5));
				
				payment.setText("实付款：" + goods_list.get(i).subtotal);
				
				shared = context.getSharedPreferences("userInfo", 0); 
				editor = shared.edit();
				String imageType = shared.getString("imageType", "mind");
				
				if(imageType.equals("high")) {
                    imageLoader.displayImage(goods_list.get(i).img.thumb,image, EcmobileApp.options);
				} else if(imageType.equals("low")) {
                    imageLoader.displayImage(goods_list.get(i).img.small,image, EcmobileApp.options);
				} else {
					String netType = shared.getString("netType", "wifi");
					if(netType.equals("wifi")) {
                        imageLoader.displayImage(goods_list.get(i).img.thumb,image, EcmobileApp.options);
					} else {
                        imageLoader.displayImage(goods_list.get(i).img.small,image, EcmobileApp.options);
					}
				}
				
			if (flag == 1) {
				OnClickListener mOnClickListener = new OnClickListener() {
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						Message msg;
						switch (view.getId()) {
						case R.id.operation_btn:
							msg = new Message();
							msg.what = 1;
							msg.obj = order;
							parentHandler.handleMessage(msg);
							break;
						case R.id.check_logistice:
							msg = new Message();
							msg.what = 2;
							msg.obj = order;
							parentHandler.handleMessage(msg);
							break;
						case R.id.order_layout:
							Bundle mBundle = new Bundle();
							mBundle.putString("topviewtext", "等待付款");
							msg = new Message();
							msg.setData(mBundle);
							msg.what = 4;
							msg.obj = order;
							parentHandler.handleMessage(msg);
							break;

						default:
							break;
						}
					}
				};

				payment.setVisibility(View.VISIBLE);
				check_logistice.setVisibility(view.VISIBLE);
				check_logistice.setText("取消订单");
				operation_btn.setText("去付款");
				order_label_name.setText("等待付款");

				operation_btn.setOnClickListener(mOnClickListener);// 去付款
				check_logistice.setOnClickListener(mOnClickListener);// 取消订单
				order_layout.setOnClickListener(mOnClickListener);// 查看详情

			} else if (flag == 2) {
				operation.setVisibility(View.GONE);
				operation_btn.setVisibility(View.GONE);
				order_label_name.setText("等待发货");
				order_layout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Bundle mBundle = new Bundle();
						mBundle.putString("topviewtext", "等待发货");
						Message msg = new Message();
						msg.setData(mBundle);
						msg.what = 4;
						msg.obj = order;
						parentHandler.handleMessage(msg);

					}
				});
			} else if (flag == 3) {
				payment.setVisibility(View.VISIBLE);
				check_logistice.setVisibility(view.VISIBLE);
				operation_btn.setText("去收货");
				order_label_name.setText("等待收货");

				// 查看物流
				check_logistice.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context, E6_ShippingStatusActivity.class);
						intent.putExtra("shipping_name", order.order_info.shipping_name);
						intent.putExtra("order_sn", order.order_sn);
						intent.putExtra("order_id", order.order_id);
						context.startActivity(intent);
					}
				});

				operation_btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Message msg = new Message();
						msg.what = 3;
						msg.obj = order;
						parentHandler.handleMessage(msg);
					}
				});

				order_layout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Bundle mBundle = new Bundle();
						mBundle.putString("topviewtext", "等待收货");
						Message msg = new Message();
						msg.setData(mBundle);
						msg.what = 4;
						msg.obj = order;
						parentHandler.handleMessage(msg);
					}
				});
			} else if (flag == 4) {// 我的订单
				operation_btn.setText("去评论");
				operation_btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						try {
							Intent intent = new Intent(context,
									E8_CommentActivity.class);
							intent.putExtra("order", order.toJson().toString());
							intent.putExtra("topviewtext", "历史订单");
							context.startActivity(intent);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

				operation.setVisibility(View.VISIBLE);
				order_label_name.setText("历史订单");
				order_layout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						try {
							Intent intent = new Intent(context,
									E7_OrderDetailsActivity.class);
							intent.putExtra("order", order.toJson().toString());
							intent.putExtra("topviewtext", "历史订单");
							context.startActivity(intent);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				 }
			}
		return convertView;
	}
	
	class ViewHolder {
		private TextView sno;
		private TextView time;
		private LinearLayout body;
		private TextView fee;
		private TextView red_paper;
		private TextView score;
		private TextView total;
		private Button check;
		private Button ok;
	}
}
