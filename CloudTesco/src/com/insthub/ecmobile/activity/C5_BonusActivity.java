package com.insthub.ecmobile.activity;

import android.content.res.Resources;
import com.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.insthub.ecmobile.protocol.flowcheckOrderResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
import com.insthub.ecmobile.R;
import com.insthub.BeeFramework.model.BusinessResponse;
import com.insthub.BeeFramework.view.ToastView;
import com.insthub.ecmobile.model.ProtocolConst;
import com.insthub.ecmobile.model.ShoppingCartModel;

public class C5_BonusActivity extends BaseActivity implements BusinessResponse {
	
	private ImageView back;
	private TextView top_view_text;
	private Button submit;
	private TextView num;
	private EditText input;
	private String score;
	private String max_score;
	
	private ShoppingCartModel shoppingCartModel;
	
	private int min_score;
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c5_bonus);
		
		Intent intent = getIntent();
		String s = intent.getStringExtra("payment");
		
		try{
			JSONObject jo = new JSONObject(s);
            flowcheckOrderResponse response = new flowcheckOrderResponse();
            response.fromJson(jo);
			score = response.data.your_integral;
			max_score = response.data.order_max_integral+"";
		
		} catch (JSONException e) {			
			e.printStackTrace();
		}
		
		min_score = Math.min(Integer.valueOf(score), Integer.valueOf(max_score));
		
		shoppingCartModel = new ShoppingCartModel(this);
		shoppingCartModel.addResponseListener(this);
		
		top_view_text = (TextView) findViewById(R.id.top_view_text);
		top_view_text.setText(getBaseContext().getResources().getString(R.string.score_creditsexchange));
		
		back = (ImageView) findViewById(R.id.top_view_back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				finish();
			}
		});
		
		input = (EditText) findViewById(R.id.score_input);
		num = (TextView) findViewById(R.id.score_num);
		
		submit = (Button) findViewById(R.id.top_view_right_btn);
		submit.setVisibility(View.VISIBLE);
		submit.setText(getBaseContext().getResources().getString(R.string.score_submit));
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
                Resources resource = (Resources) getBaseContext().getResources();
                String ent=resource.getString(R.string.enter_score);
                String scr=resource.getString(R.string.score_not_zero);
				if(!input.getText().toString().equals("")) {
					if(Integer.valueOf(input.getText().toString()) > min_score) {						
						ToastView toast = new ToastView(C5_BonusActivity.this, ent);
				        toast.setGravity(Gravity.CENTER, 0, 0);
				        toast.show();
					} else if(input.getText().toString().equals("0")) {						
						ToastView toast = new ToastView(C5_BonusActivity.this, scr);
				        toast.setGravity(Gravity.CENTER, 0, 0);
				        toast.show();
					} else {
						shoppingCartModel.score(input.getText().toString());
					}
				} else {					
					ToastView toast = new ToastView(C5_BonusActivity.this, ent);
			        toast.setGravity(Gravity.CENTER, 0, 0);
			        toast.show();
				}
				
			}
		});

        Resources resource = (Resources) getBaseContext().getResources();
        String all=resource.getString(R.string.all_of_you);
        String can=resource.getString(R.string.can_use) ;
        String inte=resource.getString(R.string.score);
		num.setText(all+score+inte);
		input.setHint(can+min_score+inte);
		
	}
	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException
    {		
		if(url.endsWith(ApiInterface.VALIDATE_INTEGRAL))
        {
            JSONObject data = jo.getJSONObject("data");
            String bonus = data.getString("bonus").toString();
            String bonus_formated = data.getString("bonus_formated").toString();
			Intent intent = new Intent();
			intent.putExtra("input", input.getText().toString());
			intent.putExtra("bonus", bonus);
			intent.putExtra("bonus_formated", bonus_formated);
			setResult(Activity.RESULT_OK, intent);  
            finish(); 
		}
		
		
	}
}
