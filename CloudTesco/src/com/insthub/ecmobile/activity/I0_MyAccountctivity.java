package com.insthub.ecmobile.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.external.androidquery.callback.AjaxStatus;
import com.insthub.BeeFramework.Utils.DoubleDatePickerDialog;
import com.insthub.BeeFramework.activity.BaseActivity;
import com.insthub.BeeFramework.model.BusinessResponse;
import com.insthub.BeeFramework.view.RoundedWebImageView;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.model.ConfigModel;
import com.insthub.ecmobile.model.ProtocolConst;
import com.insthub.ecmobile.model.UserInfoModel;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.insthub.ecmobile.protocol.userinfoupdateRequest;

@SuppressLint("NewApi") 
public class I0_MyAccountctivity extends BaseActivity implements OnClickListener,BusinessResponse{
	
	 private RelativeLayout account_portrait;
//	 private RelativeLayout account_nickname;
	 private RelativeLayout account_gender;
	 private RelativeLayout account_birthday;
	 private RelativeLayout account_address;
	 private RelativeLayout account_security;
	 private Button top_view_right_btn; 
	 private TextView nickname ; 
	 private TextView gender ;
	 private TextView birthday;
	 private RoundedWebImageView portrait ;
	 private UserInfoModel userinfomodel;
	 private String uid;
	 
	 private File file;
	 private SharedPreferences shared;
	 private SharedPreferences.Editor editor;
	 private ImageView back;
	 private TextView title;
	 private String textString;

	@Override
	protected void onCreate(Bundle savedInstanceState)
    {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.i0_myaccount);
		
		initView();
		
		userinfomodel = new UserInfoModel(this);
		userinfomodel.addResponseListener(this);
		userinfomodel.getUserInfo();
		 
		shared = getSharedPreferences("userInfo", 0); 
		editor = shared.edit();
		uid = shared.getString("uid", "");
		
		File files = new File(this.getCacheDir()+"/YGMobile/cache"+"/"+uid+"-temp.jpg");
        if(files.exists() && !uid.equals("")) 
        {
        	portrait.setImageBitmap(BitmapFactory.decodeFile(this.getCacheDir()+"/YGMobile/cache"+"/"+uid+"-temp.jpg"));
        } 
		
		//设置昵称
//		if(getIntent().getStringExtra("nickname") != null){
//			nickname.setText(getIntent().getStringExtra("nickname"));
//		}
		
		//性别
//		if(getIntent().getStringExtra("gender") != null){
//			gender.setText(getIntent().getStringExtra("gender"));
//		}
	}
	
	private void initView (){
		 account_portrait = (RelativeLayout) findViewById(R.id.account_portrait);
//		 account_nickname = (RelativeLayout) findViewById(R.id.account_nickname);
		 account_gender = (RelativeLayout) findViewById(R.id.account_gender);
		 account_birthday = (RelativeLayout) findViewById(R.id.account_birthday);
		 account_address = (RelativeLayout) findViewById(R.id.account_address);
		 account_security = (RelativeLayout) findViewById(R.id.account_security);
		 portrait = (RoundedWebImageView) findViewById(R.id.portrait);
		 top_view_right_btn = (Button) findViewById(R.id.top_view_right_btn);
		 nickname = (TextView) findViewById(R.id.nickname);
		 gender = (TextView) findViewById(R.id.gender);
		 title = (TextView) findViewById(R.id.top_view_text);
		 birthday = (TextView) findViewById(R.id.birthday);
		 account_portrait.setOnClickListener(this);
//		 account_nickname.setOnClickListener(this);
		 account_gender.setOnClickListener(this);
		 account_birthday.setOnClickListener(this);
		 account_address.setOnClickListener(this);
		 account_security.setOnClickListener(this);
		 top_view_right_btn.setVisibility(View.GONE);
		 top_view_right_btn.setText(getBaseContext().getResources().getString(R.string.save));
		 top_view_right_btn.setOnClickListener(this);
	 	 back = (ImageView) findViewById(R.id.top_view_back);
	 	 back.setOnClickListener(this);
	  	 title.setText(getBaseContext().getResources().getString(R.string.myaccount));
	}
	

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.top_view_back:
			I0_MyAccountctivity.this.finish();
			break;
		case R.id.account_portrait:
			if (uid.equals("")) {
				intent = new Intent(this, A0_SigninActivity.class);
				startActivity(intent);
            	this.overridePendingTransition(R.anim.push_buttom_in,R.anim.push_buttom_out);
			} else {
			     intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	             startActivityForResult(intent, 2);
	             overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
			break;
	/*	case R.id.account_nickname:
			if (uid.equals("")) {
				intent = new Intent(this, A0_SigninActivity.class);
				startActivity(intent);
            	this.overridePendingTransition(R.anim.push_buttom_in,R.anim.push_buttom_out);
			} else {
			     intent = new Intent(I0_MyAccountctivity.this, I2_ModifyNicknameActivity.class);
	             startActivity(intent);
	             this.finish();
//	             overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
			break;*/
		case R.id.account_gender:
			if (uid.equals("")) {
				intent = new Intent(this, A0_SigninActivity.class);
				startActivity(intent);
            	this.overridePendingTransition(R.anim.push_buttom_in,R.anim.push_buttom_out);
			} else {
			     intent = new Intent(this, I3_GenderActivity.class);
			     intent.putExtra("username", nickname.getText().toString());
	             startActivity(intent);
	             overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	             this.finish();
			}
			break;
		case R.id.account_birthday:
			Calendar c = Calendar.getInstance();
			new DoubleDatePickerDialog(this, 0, new DoubleDatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
						int startDayOfMonth) {
					textString = String.format("%d-%d-%d", startYear, startMonthOfYear + 1, startDayOfMonth);
					
					userinfoupdateRequest request = new userinfoupdateRequest();
					request.username = nickname.getText().toString();
					request.birthday = textString;
					userinfomodel.updateUserInfo(request);
				}
			}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true).show();
			break;
		case R.id.account_address:
			if (uid.equals("")) {
				intent = new Intent(this, A0_SigninActivity.class);
            	startActivity(intent);
            	this.overridePendingTransition(R.anim.push_buttom_in,R.anim.push_buttom_out);
			} else {
				intent = new Intent(this, F0_AddressListActivity.class);
				startActivity(intent);
                this.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			}
			break;
		case R.id.top_view_right_btn:
			userinfoupdateRequest request = new userinfoupdateRequest();
			if(gender.getText() != null && !gender.getText().toString().isEmpty()){
				request.sex = gender.getText().toString().equals("男") ? 0 : 1;  
			}
			
			if(birthday.getText() != null && !birthday.getText().toString().isEmpty()){
				request.birthday = birthday.getText().toString();
			}
			userinfomodel.updateUserInfo(request);
			break;
		case R.id.account_security:
			if (uid.equals("")) {
				intent = new Intent(this, A0_SigninActivity.class);
				startActivity(intent);
            	this.overridePendingTransition(R.anim.push_buttom_in,R.anim.push_buttom_out);
			} else {
			     intent = new Intent(this, I2_ChangePasswordActivity.class);
			     intent.putExtra("username", nickname.getText().toString());
	             startActivity(intent);
	             overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
	             this.finish();
			}
			break;
		default:
			break;
		}
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {  
           
        super.onActivityResult(requestCode, resultCode, data);  
        if (resultCode == Activity.RESULT_OK) {
        	if(requestCode == 2) {
        		String sdStatus = Environment.getExternalStorageState();
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");

                if (file == null) {
					file = new File(ProtocolConst.FILEPATH);
					if (!file.exists()) {
						file.mkdirs();
					}
				}
                FileOutputStream b = null;
                String fileName = this.getCacheDir()+"/YGMobile/cache"+"/"+uid+"-temp.jpg";
               try {
                    b = new FileOutputStream(fileName);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        b.flush();
                        b.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
               portrait.setImageBitmap(bitmap);
        	}
        }
	}
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
        	finish();
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
            return false;
        }
        return true;
    }
	

    @Override
    public void OnMessageResponse(String url, JSONObject jo, AjaxStatus status) 
    {
    	if (url.endsWith(ApiInterface.USER_INFO))
        {
    		if(userinfomodel.user.name.toString() != null && !userinfomodel.user.name.toString().isEmpty()){
    			nickname.setText(userinfomodel.user.name.toString());
    		}
    		
    		gender.setText(userinfomodel.user.sex == 0 ? "保密" : userinfomodel.user.sex == 1 ? "男" : userinfomodel.user.sex == 2 ? "女" : "");
    		
    		if(gender.getText().toString().isEmpty()){
    			gender.setHint(getBaseContext().getResources().getString(R.string.gender));
    		}
    		
    		if(userinfomodel.user.birthday.toString() != null && !userinfomodel.user.birthday.toString().isEmpty()){
    			if(userinfomodel.user.birthday.toString().equals("0000-00-00")){
    				birthday.setHint(getBaseContext().getResources().getString(R.string.birthday));
    			}else {
    				birthday.setText(userinfomodel.user.birthday.toString());
    			}
    		}else {
    			birthday.setHint(getBaseContext().getResources().getString(R.string.birthday));
    		}
    		
        }
    	else if (url.endsWith(ApiInterface.USER_INFO_UPDATE))
    	{
			birthday.setText(textString);
    	}
    	
    }

    @Override
    protected void onDestroy()
    {
        ConfigModel.getInstance().removeResponseListener(this);
        super.onDestroy();
    }
}
