package com.insthub.ecmobile.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.external.androidquery.callback.AjaxStatus;
import com.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.BeeFramework.model.BusinessResponse;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.model.ConfigModel;
import com.insthub.ecmobile.model.UserInfoModel;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.insthub.ecmobile.protocol.userinfoupdateRequest;

public class I3_GenderActivity extends BaseActivity implements OnClickListener, BusinessResponse{
	private Button determine;
	private ImageView top_view_back; 
	private TextView top_view_text; 
	private RelativeLayout female_layout;
	private RelativeLayout male_layout;
	private RelativeLayout secret_layout;
	private UserInfoModel userinfomodel;
	
	private ImageView female;
	private ImageView male;
	private ImageView secret;
	
	String username ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.i3_modifygender);
		top_view_back = (ImageView) findViewById(R.id.top_view_back);
		top_view_text = (TextView) findViewById(R.id.top_view_text);
		top_view_back.setOnClickListener(this);
		determine = (Button) findViewById(R.id.top_view_right_btn);
		determine.setVisibility(View.VISIBLE);
		determine.setText(getBaseContext().getResources().getString(R.string.dialog_ensure));
		determine.setOnClickListener(this);
		
		female_layout = (RelativeLayout) findViewById(R.id.female_layout);
		male_layout = (RelativeLayout) findViewById(R.id.male_layout);
		secret_layout = (RelativeLayout) findViewById(R.id.secret_layout);
		female_layout.setOnClickListener(this);
		male_layout.setOnClickListener(this);
		secret_layout.setOnClickListener(this);
		
		female = (ImageView) findViewById(R.id.female);
		male = (ImageView) findViewById(R.id.male);
		secret = (ImageView) findViewById(R.id.secret);
		top_view_text.setText(getBaseContext().getResources().getString(R.string.gender));
		
		userinfomodel = new UserInfoModel(this);
		userinfomodel.addResponseListener(this);
		
		username = getIntent().getStringExtra("username");
	}

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException
    {
        if (url.endsWith(ApiInterface.USER_INFO_UPDATE))
        {
        	Intent intent = new Intent(I3_GenderActivity.this, I0_MyAccountctivity.class);
			I3_GenderActivity.this.startActivityForResult(intent, 3);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			I3_GenderActivity.this.finish();
        }
    }
    @Override
    protected void onDestroy()
    {
        ConfigModel.getInstance().removeResponseListener(this);
        super.onDestroy();
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (arg0.getId()) {
		case R.id.top_view_right_btn:
			userinfoupdateRequest request = new userinfoupdateRequest();
			request.username = username;
			if(female.getVisibility() == View.VISIBLE){
				request.sex = 1 ;
			}else if(male.getVisibility() == View.VISIBLE){
				request.sex = 2 ;
			}else if(secret.getVisibility() == View.VISIBLE){
				request.sex = 0 ;
			}
			userinfomodel.updateUserInfo(request);
			
		/*	Intent intent = new Intent(I3_GenderActivity.this, I0_MyAccountctivity.class);
			if(female.getVisibility() == View.VISIBLE){
				intent.putExtra("gender", "ÄÐ");
			}else if(male.getVisibility() == View.VISIBLE){
				intent.putExtra("gender", "Å®");
			}else if(secret.getVisibility() == View.VISIBLE){
				intent.putExtra("gender", "±£ÃÜ");
			}
			I3_GenderActivity.this.startActivityForResult(intent, 2);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			I3_GenderActivity.this.finish();*/
			break;
		case R.id.top_view_back:
			intent = new Intent(I3_GenderActivity.this, I0_MyAccountctivity.class);
			I3_GenderActivity.this.startActivity(intent);
			finish();
			break;
		case R.id.female_layout:
			female.setVisibility(View.VISIBLE);
			male.setVisibility(View.GONE);
			secret.setVisibility(View.GONE);
			break;
		case R.id.male_layout:
			female.setVisibility(View.GONE);
			male.setVisibility(View.VISIBLE);
			secret.setVisibility(View.GONE);
			break;
		case R.id.secret_layout:
			female.setVisibility(View.GONE);
			male.setVisibility(View.GONE);
			secret.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
		
	}
}
