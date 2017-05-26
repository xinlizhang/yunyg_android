package com.insthub.ecmobile.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.external.androidquery.callback.AjaxStatus;
import com.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.BeeFramework.model.BusinessResponse;
import com.insthub.BeeFramework.view.ToastView;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.model.CommentAddModel;
import com.insthub.ecmobile.model.CommentModel;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.insthub.ecmobile.protocol.GOODORDER;

@SuppressLint("NewApi")
public class E8_CommentActivity extends BaseActivity implements BusinessResponse{
	private GOODORDER orderinfo;
	private RatingBar ratingbar;
	private TextView score_description;
	private TextView top_view_text;
	private TextView top_view_time_text ;
	private EditText review_describes;
	private Button top_view_right_btn;
	private ImageView top_view_back;
	private CommentAddModel commentaddmodel;
	private int score;
	private String[] score_description_str = {"很差","一般","好","很好","非常好"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.e8_comment);

			Intent intent = getIntent();
			String order = intent.getStringExtra("order");
			JSONObject myJsonObject = new JSONObject(order);
			orderinfo = new GOODORDER();
			orderinfo.fromJson(myJsonObject);
			
			top_view_back = (ImageView) findViewById(R.id.top_view_back);
			top_view_back.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					E8_CommentActivity.this.finish();
				}
			});
			top_view_text = (TextView) findViewById(R.id.top_view_text);
			top_view_text.setText("评论");
			top_view_time_text = (TextView) findViewById(R.id.top_view_time_text);
			
			if(orderinfo.order_time.toString() != null && !orderinfo.order_time.toString().isEmpty()){
				top_view_time_text.setText(orderinfo.order_time + "的订单");
			}else {
				top_view_time_text.setVisibility(View.GONE);
			}
			
			top_view_right_btn = (Button) findViewById(R.id.top_view_right_btn);
			top_view_right_btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(score == 0){
						 ToastView toast = new ToastView(E8_CommentActivity.this, "请为该商品打上您的评分！");
			             toast.setGravity(Gravity.CENTER, 0, 0);
			             toast.show();
			             return ;
					}
					if(review_describes.getText().toString() == null || review_describes.getText().toString().isEmpty()){
						 ToastView toast = new ToastView(E8_CommentActivity.this, "请为该商品填写您的评价！");
			             toast.setGravity(Gravity.CENTER, 0, 0);
			             toast.show();
			             return ;
					}
					
					commentaddmodel = new CommentAddModel(E8_CommentActivity.this);
					commentaddmodel.addResponseListener(E8_CommentActivity.this);
					if(orderinfo.goods_list.get(0).goods_id != null && !orderinfo.goods_list.get(0).goods_id.toString().isEmpty()){
						commentaddmodel.CommentAddRequest(Integer.parseInt(orderinfo.goods_list.get(0).goods_id),
								"" + (score + 1), review_describes.getText().toString(), E4_HistoryActivity.username);
					}
				}
			});
			
			review_describes = (EditText) findViewById(R.id.review_describes);

			ratingbar = (RatingBar) findViewById(R.id.room_ratingbar);
			ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
				@Override
				public void onRatingChanged(RatingBar arg0, float rating, boolean arg2) {
				// TODO Auto-generated method stub
					score = (int) (rating - 1) ;
					score_description.setText(score_description_str[score]);
				}
			});
			score_description = (TextView) findViewById(R.id.score_description);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("openjson ===========   " + e);
		}
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)throws JSONException {
		// TODO Auto-generated method stub
		if (url.endsWith(ApiInterface.COMMENT_ADD)) {		
			E8_CommentActivity.this.finish();
		}
	}
	
	
	
}
