package com.insthub.ecmobile.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.insthub.ecmobile.ErrorCodeConst;
import com.insthub.ecmobile.R;
import com.insthub.ecmobile.protocol.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;

import com.external.androidquery.callback.AjaxStatus;
import com.insthub.BeeFramework.model.BaseModel;
import com.insthub.BeeFramework.model.BeeCallback;
import com.insthub.BeeFramework.view.MyProgressDialog;
import com.insthub.BeeFramework.view.ToastView;

import org.w3c.dom.Comment;

public class CommentAddModel extends BaseModel {

    public CommentAddModel(Context context) {
        super(context);
    }
    
    public void CommentAddRequest(int goods_id, String score, String content, String username) {
        commentsRequest request = new commentsRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                CommentAddModel.this.callback(url, jo, status);
                try {
                    commentsResponse response = new commentsResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                        if (response.status.succeed == 1) {
                            ArrayList<COMMENTS> data = response.data;
                        }
                        CommentAddModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        };
        request.session = SESSION.getInstance();
        request.goods_id = goods_id;
        request.comment_rank = score;
        request.content = content;
        request.user_name = username;
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            // TODO: handle exception
        }

        cb.url(ApiInterface.COMMENT_ADD).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }

}
