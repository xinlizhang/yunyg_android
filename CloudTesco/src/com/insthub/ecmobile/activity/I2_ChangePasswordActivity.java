package com.insthub.ecmobile.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.external.androidquery.callback.AjaxStatus;
import com.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.BeeFramework.model.BusinessResponse;
import com.insthub.BeeFramework.view.ToastView;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.model.ConfigModel;
import com.insthub.ecmobile.model.UserInfoModel;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.insthub.ecmobile.protocol.userinfoupdateRequest;

public class I2_ChangePasswordActivity extends BaseActivity implements OnClickListener, BusinessResponse{
	private ImageView top_view_back; 
	private TextView top_view_text; 
	private UserInfoModel userinfomodel;
	private Button top_view_right_btn;
	private EditText old_password;
	private EditText new_password;
	private TextView name_text;
	private TextView password_text;
	private Button confirm/*login_login */; 
	private TextView login_register; 
	private TextView retrieve_password; 
	
	
	String username ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.i2_passwordchange);
		setContentView(R.layout.a0_signin);
		top_view_back = (ImageView) findViewById(R.id.top_view_back);
		top_view_text = (TextView) findViewById(R.id.top_view_text);
		top_view_text.setText(getBaseContext().getResources().getString(R.string.change_password));
		top_view_right_btn = (Button) findViewById(R.id.top_view_right_btn);
		top_view_right_btn.setVisibility(View.GONE);
		top_view_right_btn.setText("–ﬁ∏ƒ");
		top_view_right_btn.setOnClickListener(this);
		top_view_back.setOnClickListener(this);
		userinfomodel = new UserInfoModel(this);
		userinfomodel.addResponseListener(this);
		
//		old_password = (EditText) findViewById(R.id.old_password);
//		new_password = (EditText) findViewById(R.id.new_password);
		old_password = (EditText) findViewById(R.id.login_name);
		old_password.setPressed(true);
		old_password.setHint("«Î»Á»Îæ…√‹¬Î");
		new_password = (EditText) findViewById(R.id.login_password);
		new_password.setHint("«Î»Á»Î–¬√‹¬Î");
		
		name_text = (TextView) findViewById(R.id.nametext);
		password_text = (TextView) findViewById(R.id.passwordtext);
		name_text.setText("æ…√‹¬Î");
		password_text.setText("–¬√‹¬Î");
		
		confirm =  (Button) findViewById(R.id.login_login);
		confirm.setText(getBaseContext().getResources().getString(R.string.dialog_ensure));
		confirm.setOnClickListener(this);
		
		login_register = (TextView) findViewById(R.id.login_register);
		retrieve_password = (TextView) findViewById(R.id.retrieve_password);
		login_register.setVisibility(View.GONE);
		retrieve_password.setVisibility(View.GONE);
		
		username = getIntent().getStringExtra("username");
	}

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException
    {
        if (url.endsWith(ApiInterface.USER_INFO_UPDATE))
        {
        	Intent intent = new Intent(I2_ChangePasswordActivity.this, I0_MyAccountctivity.class);
			I2_ChangePasswordActivity.this.startActivityForResult(intent, 3);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			I2_ChangePasswordActivity.this.finish();
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
		case R.id.top_view_back:
			intent = new Intent(I2_ChangePasswordActivity.this, I0_MyAccountctivity.class);
			I2_ChangePasswordActivity.this.startActivity(intent);
			finish();
			break;
		case R.id.login_login:
			userinfoupdateRequest request = new userinfoupdateRequest();
			request.username = username;
			request.old_password = old_password.getText().toString();
			request.new_password = new_password.getText().toString();
			
			if (request.old_password.length() < 6) {
				ToastView toast = new ToastView(this, "æ…" + getBaseContext().getResources().getString(R.string.password_too_short));
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				break;
			}
			if (request.old_password.length() > 20) {
				ToastView toast = new ToastView(this, "æ…" + getBaseContext().getResources().getString(R.string.password_too_long));
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				break;
			}
			
			if (request.new_password.length() < 6) {
				ToastView toast = new ToastView(this, "–¬" + getBaseContext().getResources().getString(R.string.password_too_short));
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				break;
			}
			if (request.new_password.length() > 20) {
				ToastView toast = new ToastView(this, "–¬" + getBaseContext().getResources().getString(R.string.password_too_long));
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				break;
			}
			
			userinfomodel.updateUserInfo(request);			
			break;
/*		case R.id.female_layout:
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
			break;*/
		default:
			break;
		}
		
	}
}
