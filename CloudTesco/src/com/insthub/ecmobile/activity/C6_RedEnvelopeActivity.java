package com.insthub.ecmobile.activity;

import android.content.res.Resources;
import android.widget.AdapterView;
import com.external.maxwin.view.XListView;
import com.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.ecmobile.adapter.C6_RedEnvelopeAdapter;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.insthub.ecmobile.protocol.BONUS;
import com.umeng.analytics.MobclickAgent;
import org.json.JSONArray;
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

import java.util.ArrayList;

public class C6_RedEnvelopeActivity extends BaseActivity implements BusinessResponse {

	private ImageView back;
	private TextView top_view_text; 
	private Button submit;
	
	private EditText input;
	
	private ShoppingCartModel shoppingCartModel;
    XListView redPacketsList;

    C6_RedEnvelopeAdapter redPacketsAdapter;
    ArrayList<BONUS> dataList = new ArrayList<BONUS>();

    BONUS selectedBONUS = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c6_red_envelope);

		shoppingCartModel = new ShoppingCartModel(this);
		shoppingCartModel.addResponseListener(this);
		
		top_view_text = (TextView) findViewById(R.id.top_view_text);
		top_view_text.setText(getBaseContext().getResources().getString(R.string.balance_redpocket));
		
		back = (ImageView) findViewById(R.id.top_view_back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 
				finish();
			}
		});
		
		input = (EditText) findViewById(R.id.red_paper_input);

        redPacketsList = (XListView)findViewById(R.id.red_packet_list);
        redPacketsList.setPullLoadEnable(false);
        redPacketsList.setPullRefreshEnable(false);

        redPacketsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0)
                {
                    selectedBONUS = dataList.get(position -1);
                    redPacketsAdapter.setSelectedPosition(position);
                    redPacketsAdapter.notifyDataSetInvalidated();
                }

            }
        });


        redPacketsAdapter = new C6_RedEnvelopeAdapter(this,dataList);

        redPacketsList.setAdapter(redPacketsAdapter);
		
		submit = (Button) findViewById(R.id.top_view_right_btn);
		submit.setVisibility(View.VISIBLE);
		submit.setText(getBaseContext().getResources().getString(R.string.score_submit));
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				if(null == selectedBONUS) {
                    Resources resource = (Resources) getBaseContext().getResources();
                    String red=resource.getString(R.string.redpaper);				
					ToastView toast = new ToastView(C6_RedEnvelopeActivity.this, red);
			        toast.setGravity(Gravity.CENTER, 0, 0);
			        toast.show();
				}
                else
                {					
                    try
                    {
                        Intent intent = new Intent();
                        if (null != selectedBONUS)
                        {
                            intent.putExtra("bonus",selectedBONUS.toJson().toString());
                        }

                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                    catch (JSONException e)
                    {

                    }
				}
				
			}
		});

        Intent intent = getIntent();
        String payment = intent.getStringExtra("payment");

        if (null != payment)
        {
            try
            {
                JSONObject jo = new JSONObject(payment);
                JSONArray dataJsonArray = jo.optJSONArray("bonus");
                if (null != dataJsonArray && dataJsonArray.length() > 0)
                {
                    dataList.clear();
                    for (int i = 0; i < dataJsonArray.length(); i++)
                    {
                        JSONObject bonusJsonObject = dataJsonArray.getJSONObject(i);
                        BONUS bonus_list_Item =new  BONUS();
                        bonus_list_Item.fromJson(bonusJsonObject);
                        dataList.add(bonus_list_Item);
                    }
                }
                else
                {
                    redPacketsList.setVisibility(View.GONE);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
		
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {		
		if(url.endsWith(ApiInterface.VALIDATE_BONUS))
        {

		}
		
	}
}
