package com.insthub.ecmobile.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.external.activeandroid.query.Select;
import com.external.androidquery.callback.AjaxStatus;
import com.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.BeeFramework.model.BusinessResponse;
import com.insthub.BeeFramework.view.ToastView;
import com.insthub.ecmobile.EcmobileManager;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.adapter.F0_AddressListAdapter;
import com.insthub.ecmobile.model.AddressModel;
import com.insthub.ecmobile.protocol.ADDRESS;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.umeng.analytics.MobclickAgent;
/**
 * 收货地址管理
 */
public class F0_AddressListActivity extends BaseActivity implements BusinessResponse, OnClickListener {
	
	private ImageView back;
	private Button add;
//	private ListView listView;
	private TextView top_view_text ;
//	private Button top_view_right_btn; 
	private ImageView bg;
	private F0_AddressListAdapter addressManageAdapter;
	private AddressModel addressModel;
	private LinearLayout listlayout; 
	private View footerView;
	private Map<Integer, Boolean> isSelected;
	public  Handler messageHandler;
	public int flag;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.f0_address_list);
		
		Intent intent = getIntent();
		flag = intent.getIntExtra("flag", 0);
		
		top_view_text = (TextView) findViewById(R.id.top_view_text);
		top_view_text.setText(getBaseContext().getResources().getString(R.string.manage_address));
		
//		top_view_right_btn = (Button) findViewById(R.id.top_view_right_btn);
//		top_view_right_btn.setText("添加");
//		top_view_right_btn.setVisibility(View.VISIBLE);
//		top_view_right_btn.setOnClickListener(this);
		
		back = (ImageView) findViewById(R.id.top_view_back);
		back.setOnClickListener(this);
		
		isSelected = new HashMap<Integer, Boolean>();
		
		footerView = LayoutInflater.from(this).inflate(R.layout.f0_address_manage_add, null);
		listlayout = (LinearLayout) findViewById(R.id.listlayout);
		listlayout.addView(footerView, 0);
		
		add = (Button) footerView.findViewById(R.id.address_manage_add);
		add.setOnClickListener(this);
//		
		addressModel = new AddressModel(this);
		addressModel.addResponseListener(this);

		messageHandler = new Handler(){

            public void handleMessage(Message msg) {
            	switch (msg.what) {
				case 1:
			         Integer address_id = (Integer) msg.arg1;
	                  addressModel.addressDefault(address_id+"");
					break;
				case 2:
					Integer delete_id = (Integer) msg.arg1;
					addressModel.addressDelete(delete_id + "");
					break;
				default:
					break;
				}
            }
        };
		
	}
	
	public void setAddress() {
		
		if(addressModel.addressList.size() == 0) {
            Resources resource = (Resources) getBaseContext().getResources();
            String non=resource.getString(R.string.non_address);
            ToastView toast = new ToastView(this, non);
	        toast.setGravity(Gravity.CENTER, 0, 0);
	        toast.show();
		} else {
//			addressManageAdapter = new F0_AddressListAdapter(this, addressModel.addressList, flag);
//			listView.setAdapter(addressManageAdapter);
			listlayout.removeAllViews();
			setItemView();
			addressManageAdapter.parentHandler = messageHandler;
		}
	}
	
	private void setItemView (){
		for (int i = 0; i < addressModel.addressList.size(); i++) {
			ViewHolder holder = new ViewHolder();
			View convertView = LayoutInflater.from(this).inflate(R.layout.f0_address_list_item, null);
			holder.layout = (LinearLayout) convertView.findViewById(R.id.address_manage_item_layout);
			holder.name = (TextView) convertView.findViewById(R.id.address_manage_item_name);
			holder.mobile = (TextView) convertView.findViewById(R.id.address_manage_item_mobile);
			holder.tel = (TextView) convertView.findViewById(R.id.address_manage_item_tel);
			holder.detail = (TextView) convertView.findViewById(R.id.address_manage_item_detail);
			holder.select_layout = (LinearLayout) convertView.findViewById(R.id.f0_address_list_item_select);
			holder.editor_layout = (LinearLayout) convertView.findViewById(R.id.f0_address_list_item_editor);
			holder.delete_layout = (LinearLayout) convertView.findViewById(R.id.f0_address_list_item_delete);
			holder.select = (ImageView) convertView.findViewById(R.id.address_manage_itme_select);
			listlayout.addView(convertView, i);
			
			final ADDRESS address = addressModel.addressList.get(i);
			if(address.default_address == 1) {
				isSelected.put(i, true);
			}else {
				isSelected.put(i, false);
			}
			
			holder.name.setText(address.consignee);
			
			holder.mobile.setText(address.mobile);
			
			holder.tel.setText(address.tel);
			
			holder.detail.setText(address.province_name + (address.city_name.equals("null") ? "" : address.city_name) + 
					(address.district_name.equals("null") ? "" : address.district_name) + address.address);
			
			if(isSelected.get(i)) {
				holder.select.setImageResource(R.drawable.f0_address_list_item_select_h);
				holder.name.setTextColor(Color.parseColor("#666699"));
				holder.mobile.setTextColor(Color.parseColor("#666699"));
				holder.tel.setTextColor(Color.parseColor("#666699"));
				holder.detail.setTextColor(Color.parseColor("#666699"));
			} else {
				holder.select.setImageBitmap(null);
				holder.name.setTextColor(Color.parseColor("#000000"));
				holder.mobile.setTextColor(Color.parseColor("#000000"));
				holder.tel.setTextColor(Color.parseColor("#000000"));
				holder.detail.setTextColor(Color.parseColor("#000000"));
			}
			
			holder.select_layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Message msg = new Message();
	                msg.what = 1;
	                msg.arg1 = Integer.valueOf(address.id);
	                messageHandler.handleMessage(msg);
				}
			});
			
			
			holder.layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					if(flag == 1) {
//						Message msg = new Message();
//		                msg.what = 1;
//		                msg.arg1 = Integer.valueOf(address.id);
//		                messageHandler.handleMessage(msg);
//					} else {
						Intent intent = new Intent(F0_AddressListActivity.this, F2_EditAddressActivity.class);
						intent.putExtra("address_id", address.id+"");
						F0_AddressListActivity.this.startActivity(intent);
//					}
				}
			});
			
			holder.editor_layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
//					if(flag == 1) {
//						Message msg = new Message();
//		                msg.what = 1;
//		                msg.arg1 = Integer.valueOf(address.id);
//		                messageHandler.handleMessage(msg);
//					} else {
						Intent intent = new Intent(F0_AddressListActivity.this, F2_EditAddressActivity.class);
						intent.putExtra("address_id", address.id+"");
						F0_AddressListActivity.this.startActivity(intent);
//					}
				}
			});
			
			holder.delete_layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(address.default_address == 1){
				        Resources resource = (Resources) getBaseContext().getResources();
			            String non=resource.getString(R.string.non_address);
			            ToastView toast = new ToastView(F0_AddressListActivity.this,
			            		getBaseContext().getResources().getString(R.string.unable_delete_default));
				        toast.setGravity(Gravity.CENTER, 0, 0);
				        toast.show();
					}else {
						Message msg = new Message();
		                msg.what = 2;
		                msg.arg1 = Integer.valueOf(address.id);
		                messageHandler.handleMessage(msg);	
					}
				}
			});
			
		}
		listlayout.addView(footerView, listlayout.getChildCount());
	}
	
	
	@Override
	protected void onResume() {		
		super.onResume();
		addressModel.getAddressList();
        if(EcmobileManager.getUmengKey(this)!=null){
            MobclickAgent.onPageStart("AddressManage");
            MobclickAgent.onResume(this, EcmobileManager.getUmengKey(this),"");
        }
	}

	@Override
	public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status)
			throws JSONException {		
		if(url.endsWith(ApiInterface.ADDRESS_LIST)) {
			setAddress();
		} else if(url.endsWith(ApiInterface.ADDRESS_SETDEFAULT) || url.endsWith(ApiInterface.ADDRESS_DELETE)) {
			addressModel.getAddressList();
//			Intent intent = new Intent();
//			intent.putExtra("address", "address");
//			setResult(Activity.RESULT_OK, intent); 
//			finish();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {		
		if(keyCode == KeyEvent.KEYCODE_BACK){
            finish(); 
		}
		return true;
	}
	
	public static List<ADDRESS> getAll() {
		return new Select().from(ADDRESS.class).execute();
	}

    @Override
    public void onPause() {
        super.onPause();
        if(EcmobileManager.getUmengKey(this)!=null){
            MobclickAgent.onPageEnd("AddressManage");
            MobclickAgent.onPause(this);
        }
    }

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent intent; 
		switch (view.getId()) {
		case R.id.top_view_right_btn:
			intent = new Intent(F0_AddressListActivity.this, F1_NewAddressActivity.class);
			startActivity(intent);
			break;
		case R.id.top_view_back:
			if(flag == 1){
				intent = new Intent(this, C1_CheckOutActivity.class);
				startActivityForResult(intent, 1);
				finish(); 
			}else {
				finish(); 
			}
			break;
		case R.id.address_manage_add:
			intent = new Intent(F0_AddressListActivity.this, F1_NewAddressActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	
	class ViewHolder {
		private TextView name;
		private TextView mobile;
		private TextView tel;
		private TextView address_area ; 
		private TextView province;
		private TextView city;
		private TextView county;
		private TextView detail;
		private ImageView select;
		private LinearLayout select_layout;
		private LinearLayout editor_layout;
		private LinearLayout delete_layout;
		private LinearLayout layout;
	}
	
}
