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
import android.widget.TextView;

import com.external.androidquery.callback.AjaxStatus;
import com.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.BeeFramework.model.BusinessResponse;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.model.ConfigModel;

public class I2_ModifyNicknameActivity extends BaseActivity implements OnClickListener, BusinessResponse{
	private EditText enter_nickname;
	private TextView top_view_text; 
	private Button determine;
	private Button delete ; 
	private ImageView top_view_back ; 

	@Override
	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.i2_modifynickname);
		enter_nickname = (EditText) findViewById(R.id.enter_nickname);
		top_view_text = (TextView) findViewById(R.id.top_view_text);
		top_view_back = (ImageView) findViewById(R.id.top_view_back);
		top_view_back.setOnClickListener(this);
		delete = (Button) findViewById(R.id.delete);
		delete.setOnClickListener(this);
		
		determine = (Button) findViewById(R.id.top_view_right_btn);
		determine.setVisibility(View.VISIBLE);
		top_view_text.setText(getBaseContext().getResources().getString(R.string.nickname));
		determine.setText(getBaseContext().getResources().getString(R.string.dialog_ensure));
		determine.setOnClickListener(this);
		
	}

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) throws JSONException
    {
/*        if (url.endsWith(ApiInterface.CONFIG))
        {
            if (null != ConfigModel.getInstance().config &&
                    null != ConfigModel.getInstance().config.service_phone)
            {
                mobile.setText(ConfigModel.getInstance().config.service_phone);
            }
        }*/
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
		case R.id.delete:
			enter_nickname.setText(null);
			enter_nickname.setHint("«Î ‰»Îƒ˙µƒÍ«≥∆");
			break;
		case R.id.top_view_right_btn:
			intent = new Intent(I2_ModifyNicknameActivity.this, I0_MyAccountctivity.class);
			intent.putExtra("nickname", enter_nickname.getText().toString());
			I2_ModifyNicknameActivity.this.startActivityForResult(intent, 2);
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			I2_ModifyNicknameActivity.this.finish();
			break;
		case R.id.top_view_back:
			intent = new Intent(I2_ModifyNicknameActivity.this, I0_MyAccountctivity.class);
			I2_ModifyNicknameActivity.this.startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		
	}
}
