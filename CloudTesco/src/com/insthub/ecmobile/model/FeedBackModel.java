package com.insthub.ecmobile.model;

import java.util.HashMap;
import java.util.Map;

import android.content.res.Resources;
import com.insthub.BeeFramework.BeeFrameworkConst;
import com.insthub.BeeFramework.view.MyProgressDialog;
import com.insthub.ecmobile.ErrorCodeConst;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.protocol.*;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;

import com.external.androidquery.callback.AjaxStatus;
import com.insthub.BeeFramework.Utils.Utils;
import com.insthub.BeeFramework.model.BaseModel;
import com.insthub.BeeFramework.model.BeeCallback;
import com.insthub.BeeFramework.view.ToastView;
import com.insthub.ecmobile.activity.AppOutActivity;

public class FeedBackModel extends BaseModel {

    public FeedBackModel(Context context) {
        super(context);
    }

    public void FeedBack(String content, String nameoremail) {
    	feedbackRequest request = new feedbackRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                FeedBackModel.this.callback(url, jo, status);
                try {
                    usersigninResponse response = new usersigninResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                        FeedBackModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
        request.content = content;
        request.nameoremail = nameoremail;
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.USER_SIGNIN).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext,mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);

    }
}
