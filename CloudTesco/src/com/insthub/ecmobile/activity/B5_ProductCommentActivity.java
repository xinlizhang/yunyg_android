package com.insthub.ecmobile.activity;


import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.external.androidquery.callback.AjaxStatus;
import com.external.maxwin.view.XListView;
import com.external.maxwin.view.XListView.IXListViewListener;
import com.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.BeeFramework.model.BusinessResponse;
import com.insthub.BeeFramework.view.ToastView;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.adapter.B5_ProductCommentAdapter;
import com.insthub.ecmobile.model.CommentModel;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.insthub.ecmobile.protocol.COMMENTS;

public class B5_ProductCommentActivity extends BaseActivity implements IXListViewListener, BusinessResponse {

	private TextView title;
	private ImageView back;
	
	private XListView xlistView;
	private B5_ProductCommentAdapter commentAdapter;
    private View null_paView;
	private int goods_id;
	private Button top_view_right_btn;
	
	private Button all_comments ; //全部
	private Button good_comments ; //好评
	private Button medium_comments ; //中评
	private Button pour_comments ; //差评
	
	private CommentModel commentModel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.b5_product_comment);
		
		Intent intent = getIntent();
		goods_id = intent.getIntExtra("id", 0);
		
		title = (TextView) findViewById(R.id.top_view_text);
		
        Resources resource = (Resources) getBaseContext().getResources();
        String com = resource.getString(R.string.gooddetail_commit);
		title.setText(com);
		back = (ImageView) findViewById(R.id.top_view_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				finish();
			}
		});
        null_paView = findViewById(R.id.null_pager);
		xlistView = (XListView) findViewById(R.id.comment_list);
		xlistView.setPullLoadEnable(true);
		xlistView.setRefreshTime();
		xlistView.setXListViewListener(this,1);
		
		commentModel = new CommentModel(this);
		commentModel.addResponseListener(this);
		commentModel.getCommentList(goods_id);
		commentModel.fetchGoodDetail(goods_id);
		
		all_comments = (Button) findViewById(R.id.all_comments);
		good_comments = (Button) findViewById(R.id.good_comments);
		medium_comments = (Button) findViewById(R.id.medium_comments);
		pour_comments = (Button) findViewById(R.id.pour_comments);
		all_comments.setOnClickListener(commentsListener);
		good_comments.setOnClickListener(commentsListener);
		medium_comments.setOnClickListener(commentsListener);
		pour_comments.setOnClickListener(commentsListener);
		
	    top_view_right_btn = (Button) findViewById(R.id.top_view_right_btn);
        top_view_right_btn.setVisibility(View.VISIBLE);
        top_view_right_btn.setBackgroundResource(R.drawable.discuss_collection);
        top_view_right_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				SharedPreferences shared = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            	String uid = shared.getString("uid", "");
                if(uid.equals(""))
                {
                    Intent intent = new Intent(B5_ProductCommentActivity.this, A0_SigninActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_buttom_in,R.anim.push_buttom_out);
                    String nol= getBaseContext().getResources().getString(R.string.no_login);                    
                    ToastView toast = new ToastView(B5_ProductCommentActivity.this, nol);
        	        toast.setGravity(Gravity.CENTER, 0, 0);
        	        toast.show();
                } else {
                    if(commentModel.goodDetail.collected==1){
                        ToastView toast = new ToastView(B5_ProductCommentActivity.this, R.string.favorite_added);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else{
                    	commentModel.collectCreate(goods_id);
                    	
//                        collectionButton.setImageResource(R.drawable.item_info_pushed_collect_btn);
//                        collectionButton.setBackgroundResource(R.drawable.item_info_pushed_collect_btn);
                    }
                }
			}
		});
		
	}

	@Override
	public void onRefresh(int id) {				
		commentModel.getCommentList(goods_id);
		commentModel.fetchGoodDetail(goods_id);
	}

	@Override
	public void onLoadMore(int id) {		
		commentModel.getCommentsMore(goods_id);
		commentModel.fetchGoodDetail(goods_id);
	}
	
	OnClickListener commentsListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			switch (arg0.getId()) {
			case R.id.all_comments:
				all_comments.setBackgroundResource(R.drawable.btn_all);
				good_comments.setBackgroundResource(R.drawable.btn_white);
				medium_comments.setBackgroundResource(R.drawable.btn_white);
				pour_comments.setBackgroundResource(R.drawable.btn_white);
				break;
			case R.id.good_comments:
				all_comments.setBackgroundResource(R.drawable.btn_white);
				good_comments.setBackgroundResource(R.drawable.btn_all);
				medium_comments.setBackgroundResource(R.drawable.btn_white);
				pour_comments.setBackgroundResource(R.drawable.btn_white);
				break;
			case R.id.medium_comments:
				all_comments.setBackgroundResource(R.drawable.btn_white);
				good_comments.setBackgroundResource(R.drawable.btn_white);
				medium_comments.setBackgroundResource(R.drawable.btn_all);
				pour_comments.setBackgroundResource(R.drawable.btn_white);
				break;
			case R.id.pour_comments:
				all_comments.setBackgroundResource(R.drawable.btn_white);
				good_comments.setBackgroundResource(R.drawable.btn_white);
				medium_comments.setBackgroundResource(R.drawable.btn_white);
				pour_comments.setBackgroundResource(R.drawable.btn_all);
				break;
			default:
				break;
			}
		}
	};
	
	public void setComment() {
		if(commentModel.comment_list.size() > 0) {
			xlistView.setVisibility(View.VISIBLE);
			if(commentAdapter == null) {
				commentAdapter = new B5_ProductCommentAdapter(this, commentModel.comment_list);
				xlistView.setAdapter(commentAdapter);
			} else {
				commentAdapter.list = commentModel.comment_list;
				commentAdapter.notifyDataSetChanged();
			}
			
		} else {
            null_paView.setVisibility(View.VISIBLE);
            xlistView.setVisibility(View.GONE);
		}
		
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {		
		if(url.endsWith(ApiInterface.COMMENTS)) {
			xlistView.setRefreshTime();
			xlistView.stopRefresh();
			xlistView.stopLoadMore();
			if(commentModel.paginated.more == 0) {
				xlistView.setPullLoadEnable(false);
			} else {
				xlistView.setPullLoadEnable(true);
			}
			setComment();
		} else if(url.endsWith(ApiInterface.USER_COLLECT_CREATE)) {
			commentModel.goodDetail.collected = 1;
        	ToastView toast = new ToastView(this, R.string.collection_success);
	        toast.setGravity(Gravity.CENTER, 0, 0);
	        toast.show();
        }
		
	}

	@Override
	public void onLoadState(int state) {
		// TODO Auto-generated method stub
		
	}
}
