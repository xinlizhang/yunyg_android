package com.insthub.ecmobile.adapter;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.external.maxwin.view.XListView;
import com.insthub.ecmobile.EcmobileApp;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.protocol.CHATDATA;
import com.nostra13.universalimageloader.core.ImageLoader;

public class J0_ChatListAdapter extends BaseAdapter {
	private static final int IMVT_TO_MSG = 0;// 自己发送出去的消息
	private static final int IMVT_COM_MSG = 1;// 收到对方的消息
    private ImageLoader imageLoader = ImageLoader.getInstance();
	private Context context; 
	private List<CHATDATA> list;
	private SharedPreferences shared;
	
	public J0_ChatListAdapter(Context context, List<CHATDATA> list) {
		this.context = context;
		this.list = list;
		shared = context.getSharedPreferences("userInfo", 0);
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
	
	/**
	 * 得到Item的类型，是对方发过来的消息，还是自己发送出去的
	 */
	@Override
	public int getItemViewType(int position) {
		CHATDATA entity = list.get(position);
		if (entity.orso.equals("1")) {// 收到的消息
			return IMVT_COM_MSG;
		} else {// 自己发送的消息
			return IMVT_TO_MSG;
		}
	}

	@Override
	public int getViewTypeCount() {		
		int count = list != null ? list.size() : 1;
		return count;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {		
		ViewHolder viewHolder = null;
		String imageType = shared.getString("imageType", "mind");
		CHATDATA entity = list.get(position);
		String orso = entity.orso;
		if (convertView == null || (viewHolder = (ViewHolder) convertView.getTag()).orso != entity.orso ) {
			if (orso.equals("1")) {
				convertView = LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_text_left, null);
			} else {
				convertView = LayoutInflater.from(context).inflate(R.layout.chatting_item_msg_text_right, null);
			}
			viewHolder = new ViewHolder();
			viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
			viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tv_username);
			viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.iv_userhead);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tvSendTime.setText(entity.time);
		viewHolder.tvContent.setText(entity.text);
		if(orso.equals("1")){
			viewHolder.img.setBackgroundResource(R.drawable.ecmobile_logo);
		}else {
			if (imageType.equals("high")) {
				imageLoader.displayImage(entity.img.thumb, viewHolder.img, EcmobileApp.options);
			} else if (imageType.equals("low")) {
				imageLoader.displayImage(entity.img.small, viewHolder.img, EcmobileApp.options);
			} else {
				String netType = shared.getString("netType", "wifi");
				if (netType.equals("wifi")) {
					imageLoader.displayImage(entity.img.thumb, viewHolder.img, EcmobileApp.options);
				} else {
					imageLoader.displayImage(entity.img.small, viewHolder.img, EcmobileApp.options);
				}
			}
		}
		return convertView;
	}
	

	class ViewHolder {
		public TextView tvSendTime;
		public TextView tvUserName;
		public TextView tvContent;
		public ImageView img;
		String orso; 
	}
	
}
