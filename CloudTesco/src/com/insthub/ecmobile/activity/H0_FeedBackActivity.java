package com.insthub.ecmobile.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.external.androidquery.callback.AjaxStatus;
import com.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.BeeFramework.model.BusinessResponse;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.model.ConfigModel;
import com.insthub.ecmobile.model.FeedBackModel;
import com.insthub.ecmobile.protocol.ApiInterface;

public class H0_FeedBackActivity extends BaseActivity implements OnClickListener,BusinessResponse{
	
	Button score_submit ;//�ύ
	EditText feed_back_content;//����
	EditText nameoremail;//���֣����䣬�ֻ��ŵ�
	FeedBackModel feedBackModel; 
	ImageView top_view_back ; //���ذ�ť

	@Override
	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.h0_feedback);
		feed_back_content = (EditText) findViewById(R.id.feed_back_content);
		nameoremail = (EditText) findViewById(R.id.nameoremail);
		//���ذ�ť
		top_view_back = (ImageView) findViewById(R.id.top_view_back);
		top_view_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		//�ύ��ť
		score_submit = (Button) findViewById(R.id.top_view_right_btn);
		score_submit.setVisibility(View.VISIBLE);
		score_submit.setText(getBaseContext().getResources().getString(R.string.score_submit));
		score_submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String FeedBackText = feed_back_content.getText().toString();
				String NameOrEmail = nameoremail.getText().toString();
				feedBackModel = new FeedBackModel(H0_FeedBackActivity.this);
				feedBackModel.FeedBack(FeedBackText, NameOrEmail);
			}
		});
	}

	@Override
	public void onClick(View v) {		
		switch(v.getId()) {}
		
	}

    @Override

    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException
    {
        if (url.endsWith(ApiInterface.CONFIG))
        {}
    }

    @Override
    protected void onDestroy()
    {
        ConfigModel.getInstance().removeResponseListener(this);
        super.onDestroy();
    }
}
