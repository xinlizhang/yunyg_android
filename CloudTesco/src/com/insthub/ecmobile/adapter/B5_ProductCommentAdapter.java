package com.insthub.ecmobile.adapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.insthub.ecmobile.R;
import com.insthub.ecmobile.protocol.COMMENTS;

@SuppressLint("NewApi")
public class B5_ProductCommentAdapter extends BaseAdapter {

	private Context context;
	public List<COMMENTS> list;
	private LayoutInflater inflater;
	private int[] stars_id = {R.id.stars1, R.id.stars2, R.id.stars3, R.id.stars4, R.id.stars5}; 
			
	public B5_ProductCommentAdapter(Context context, List<COMMENTS> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		int number = list.size();
		return number;
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
	public View getView(int position, View convertView, ViewGroup parent) {		
		final ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.b5_product_comment_cell, null);
			
			holder.name = (TextView) convertView.findViewById(R.id.comment_item_name);
			holder.time = (TextView) convertView.findViewById(R.id.comment_item_time);
			holder.cont = (TextView) convertView.findViewById(R.id.comment_item_cont);
			holder.reply = (TextView) convertView.findViewById(R.id.comment_item_reply);
//			holder.stars1 = (View) convertView.findViewById(R.id.stars1);
//			holder.stars2 = (View) convertView.findViewById(R.id.stars2);
//			holder.stars3 = (View) convertView.findViewById(R.id.stars3);
//			holder.stars4 = (View) convertView.findViewById(R.id.stars4);
//			holder.stars5 = (View) convertView.findViewById(R.id.stars5);
			
			for (int i = 0; i < holder.stars.length; i++) {
				holder.stars[i] = (View) convertView.findViewById(stars_id[i]);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		COMMENTS comments = list.get(position);
		holder.name.setText(comments.author+":");
		holder.cont.setText(comments.content);
		if(comments.re_content != null && !comments.re_content.isEmpty()){
			holder.reply.setVisibility(View.VISIBLE);
			holder.reply.setText("²ÍÌü»Ø¸´£º " + comments.re_content);
		}else {
			holder.reply.setVisibility(View.GONE);
		}
		int stars_number = Integer.parseInt(comments.comment_rank);
		for (int i = 0; i < stars_id.length; i++) {
			holder.stars[i].setBackgroundResource(R.drawable.discuss_start_showed);
		}
		
		for (int i = 0; i < stars_number; i++) {
			holder.stars[i].setBackgroundResource(R.drawable.discuss_start_show);
		}
		
		if(comments.create != null && !comments.create.isEmpty()) {
			holder.time.setText(comments.create.substring(0, comments.create.length() - 5));
		}
		
		return convertView;
	}
	
	class ViewHolder {
		private TextView name;
		private TextView time;
		private TextView cont;
		private TextView reply;
//		private View stars1;
//		private View stars2;
//		private View stars3;
//		private View stars4;
//		private View stars5;
		private View[] stars = new View[stars_id.length];
	}

}
