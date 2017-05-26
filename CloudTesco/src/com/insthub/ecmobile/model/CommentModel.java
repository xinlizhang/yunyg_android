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

public class CommentModel extends BaseModel {

    public PAGINATED paginated;

    public ArrayList<COMMENTS> comment_list = new ArrayList<COMMENTS>();
    public GOODS goodDetail = new GOODS();
    public CommentModel(Context context) {
        super(context);

    }

    public void getCommentList(int goods_id) {
        commentsRequest request = new commentsRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                CommentModel.this.callback(url, jo, status);
                try {
                    commentsResponse response = new commentsResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                        if (response.status.succeed == 1) {
                            ArrayList<COMMENTS> data = response.data;
                            comment_list.clear();
                            if (null != data && data.size() > 0) {
                                 comment_list.addAll(data);
                            }

                            paginated = response.paginated;
                        }
                        CommentModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        };

        PAGINATION pagination = new PAGINATION();
        pagination.page = 1;
        pagination.count = 10;
        request.session = SESSION.getInstance();
        request.pagination = pagination;
        request.goods_id = goods_id;
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            // TODO: handle exception
        }

        cb.url(ApiInterface.COMMENTS).type(JSONObject.class).params(params);
        aq.ajax(cb);

    }
    
    
    public void fetchGoodDetail(int goodId) {
        goodsRequest request = new goodsRequest();

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

            	CommentModel.this.callback(url, jo, status);
                try {
                    goodsResponse response = new goodsResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                        if (response.status.succeed == 1) {
                            GOODS goods = response.data;
                            if (null != goods) {
                            	CommentModel.this.goodDetail = goods;
                            	CommentModel.this.OnMessageResponse(url, jo, status);
                            }

                        }
                    }
                } catch (JSONException e) {
                    // TODO: handle exception
                }

            }
        };
        request.session = SESSION.getInstance();
        request.goods_id = goodId;
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cb.url(ApiInterface.GOODS).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }
    
    public void collectCreate(int goodId) {
        usercollectcreateRequest request = new usercollectcreateRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
            	CommentModel.this.callback(url, jo, status);
                try {
                    usercollectdeleteResponse response = new usercollectdeleteResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                        if (response.status.succeed == ErrorCodeConst.ResponseSucceed) {
                        	CommentModel.this.OnMessageResponse(url, jo, status);

                        } else if (response.status.error_code == ErrorCodeConst.UnexistInformation) {
                            Resources resource = mContext.getResources();
                            String registration_closed = resource.getString(R.string.unexist_information);
                            ToastView toast = new ToastView(mContext, registration_closed);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                } catch (JSONException e) {
                    // TODO: handle exception
                	System.out.println("collectCreate =============  "+e);
                }
            }
        };

        request.session = SESSION.getInstance();
        request.goods_id = goodId;
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cb.url(ApiInterface.USER_COLLECT_CREATE).type(JSONObject.class).params(params);
        MyProgressDialog pd = new MyProgressDialog(mContext, mContext.getResources().getString(R.string.hold_on));
        aq.progress(pd.mDialog).ajax(cb);
    }
    
    
    

    public void getCommentsMore(int goods_id) {
        commentsRequest request = new commentsRequest();

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                CommentModel.this.callback(url, jo, status);
                try {
                    commentsResponse response = new commentsResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                        if (response.status.succeed == 1) {
                            ArrayList<COMMENTS> data = response.data;
                            comment_list.clear();
                            if (null != data && data.size() > 0) {
                                comment_list.addAll(data);
                            }
                            paginated = response.paginated;
                        }
                        CommentModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        };


        PAGINATION pagination = new PAGINATION();
        pagination.page = comment_list.size() / 10 + 1;
        pagination.count = 10;
        request.session = SESSION.getInstance();
        request.pagination = pagination;
        request.goods_id = goods_id;

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            // TODO: handle exception
        }

        cb.url(ApiInterface.COMMENTS).type(JSONObject.class).params(params);
        aq.ajax(cb);

    }

}
