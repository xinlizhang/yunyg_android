package com.insthub.ecmobile.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.external.androidquery.callback.AjaxStatus;
import com.insthub.BeeFramework.model.BaseModel;
import com.insthub.BeeFramework.model.BeeCallback;
import com.insthub.ecmobile.protocol.ApiInterface;
import com.insthub.ecmobile.protocol.CATEGORY;
import com.insthub.ecmobile.protocol.CATEGORYGOODS;
import com.insthub.ecmobile.protocol.FILTER;
import com.insthub.ecmobile.protocol.HOMEBOTGOODS;
import com.insthub.ecmobile.protocol.HOME_DATA;
import com.insthub.ecmobile.protocol.PAGINATED;
import com.insthub.ecmobile.protocol.PAGINATION;
import com.insthub.ecmobile.protocol.PLAYER;
import com.insthub.ecmobile.protocol.SESSION;
import com.insthub.ecmobile.protocol.SIMPLEGOODS;
import com.insthub.ecmobile.protocol.articleRequest;
import com.insthub.ecmobile.protocol.articleResponse;
import com.insthub.ecmobile.protocol.categoryResponse;
import com.insthub.ecmobile.protocol.checkupdateRequest;
import com.insthub.ecmobile.protocol.homecategoryResponse;
import com.insthub.ecmobile.protocol.homedataRequest;
import com.insthub.ecmobile.protocol.homedataResponse;
import com.insthub.ecmobile.protocol.homehotResponse;
import com.insthub.ecmobile.protocol.listpromoteResponse;
import com.insthub.ecmobile.protocol.searchRequest;


public class HomeModel extends BaseModel {
	public ArrayList<PLAYER> playersList = new ArrayList<PLAYER>();
	public ArrayList<CATEGORY> categorygoodsList = new ArrayList<CATEGORY>();
	public ArrayList<HOMEBOTGOODS> homehotgoodsList = new ArrayList<HOMEBOTGOODS>();
	public ArrayList<SIMPLEGOODS> homebestgoodsList = new ArrayList<SIMPLEGOODS>();
	public ArrayList<SIMPLEGOODS> homenewgoodsList = new ArrayList<SIMPLEGOODS>();
	public ArrayList<SIMPLEGOODS> simplegoodsList = new ArrayList<SIMPLEGOODS>();
	public checkupdateRequest checkupate ; 

    String pkName;

    public String rootpath;

    public HomeModel(Context context) {
        super(context);
        pkName = mContext.getPackageName();

        rootpath = context.getCacheDir() + "/YGMobile/cache";

        readHomeDataCache();
        readPromoteDataCache();
        readhomehotDataCache();
        readhomebestDataCache();
        readhomeNewDataCache();
        readGoodsDataCache();
    }

    public void readHomeDataCache() {

        String path = rootpath + "/" + pkName + "/homeData.dat";
        File f1 = new File(path);
        if (f1.exists()) {
            try {
                InputStream is = new FileInputStream(f1);
                InputStreamReader input = new InputStreamReader(is, "UTF-8");
                BufferedReader bf = new BufferedReader(input);

                homeDataCache(bf.readLine());

                bf.close();
                input.close();
                is.close();

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

    }

    public String homeDataCache() {
        String path = rootpath + "/" + pkName + "/homeData.dat";
        File f1 = new File(path);
        String s = null;
        if (f1.exists()) {
            try {
                InputStream is = new FileInputStream(f1);
                InputStreamReader input = new InputStreamReader(is, "UTF-8");
                BufferedReader bf = new BufferedReader(input);

                s = bf.readLine();

                bf.close();
                input.close();
                is.close();

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        return s;
    }

    public void readGoodsDataCache() {
        String path = rootpath + "/" + pkName + "/goodsData.dat";
        File f1 = new File(path);
        if (f1.exists()) {
            try {
                InputStream is = new FileInputStream(f1);
                InputStreamReader input = new InputStreamReader(is, "UTF-8");
                BufferedReader bf = new BufferedReader(input);

                goodsDataCache(bf.readLine());

                bf.close();
                input.close();
                is.close();

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
    
    public void readPromoteDataCache(){
        String path = rootpath + "/" + pkName + "/promote.dat";
        File f1 = new File(path);
        if (f1.exists()) {
            try {
                InputStream is = new FileInputStream(f1);
                InputStreamReader input = new InputStreamReader(is, "UTF-8");
                BufferedReader bf = new BufferedReader(input);
                JSONObject jsonObject = new JSONObject(bf.readLine());
               	listpromoteResponse response = new listpromoteResponse();
                response.fromJson(jsonObject);
                if (response.status.succeed == 1) {
                    ArrayList<SIMPLEGOODS> data = response.data;
                    simplegoodsList.clear();
                    if (null != data && data.size() > 0) {
                        simplegoodsList.clear();
                        simplegoodsList.addAll(data);
                    }
                }
                bf.close();
                input.close();
                is.close();
            } catch (JSONException e) {
				e.printStackTrace();
			}catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    
    public void readhomehotDataCache() {
        String path = rootpath + "/" + pkName + "/homehot.dat";
        File f1 = new File(path);
        if (f1.exists()) {
            try {
                InputStream is = new FileInputStream(f1);
                InputStreamReader input = new InputStreamReader(is, "UTF-8");
                BufferedReader bf = new BufferedReader(input);
                homehotDataCache(bf.readLine());
                bf.close();
                input.close();
                is.close();

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
    
    public void homehotDataCache(String result) {
        try {
            if (result != null) {
                JSONObject jsonObject = new JSONObject(result);
                homehotResponse response = new homehotResponse();
                response.fromJson(jsonObject);
                if (response.status.succeed == 1) {
                	ArrayList<HOMEBOTGOODS> simplegoodses = response.data;
                    if (null != simplegoodses && simplegoodses.size() > 0) {
                    	homehotgoodsList.clear();
                    	homehotgoodsList.addAll(simplegoodses);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void readhomebestDataCache() {
        String path = rootpath + "/" + pkName + "/homebost.dat";
        File f1 = new File(path);
        if (f1.exists()) {
            try {
                InputStream is = new FileInputStream(f1);
                InputStreamReader input = new InputStreamReader(is, "UTF-8");
                BufferedReader bf = new BufferedReader(input);
                homebestDataCache(bf.readLine());
                bf.close();
                input.close();
                is.close();

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
    
    public void homebestDataCache(String result) {
        try {
            if (result != null) {
                JSONObject jsonObject = new JSONObject(result);
                listpromoteResponse response = new listpromoteResponse();
                response.fromJson(jsonObject);
                if (response.status.succeed == 1) {
                	ArrayList<SIMPLEGOODS> data= response.data;
                	  if (null != data && data.size() > 0) {
                    	homebestgoodsList.clear();
                    	homebestgoodsList.addAll(data);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void readhomeNewDataCache() {
        String path = rootpath + "/" + pkName + "/homenew.dat";
        File f1 = new File(path);
        if (f1.exists()) {
            try {
                InputStream is = new FileInputStream(f1);
                InputStreamReader input = new InputStreamReader(is, "UTF-8");
                BufferedReader bf = new BufferedReader(input);
                HomeNewDataCache(bf.readLine());
                bf.close();
                input.close();
                is.close();

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
    
	public void HomeNewDataCache(String result) {
		try {
			if (result != null) {
				JSONObject jsonObject = new JSONObject(result);
				listpromoteResponse response = new listpromoteResponse();
				response.fromJson(jsonObject);
				if (response.status.succeed == 1) {
					ArrayList<SIMPLEGOODS> data = response.data;
					if (null != data && data.size() > 0) {
						homenewgoodsList.clear();
						homenewgoodsList.addAll(data);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public void homeDataCache(String result) {

        try {
            if (result != null) {
                JSONObject jsonObject = new JSONObject(result);
                homedataResponse response = new homedataResponse();
                response.fromJson(jsonObject);
                if (response.status.succeed == 1) {
                    HOME_DATA data = response.data;
                    if (null != data) {
                        ArrayList<PLAYER> players = data.player;
                        if (null != players && players.size() > 0) {
                            playersList.clear();
                            playersList.addAll(players);
                        }

//                        ArrayList<SIMPLEGOODS> promote_goods = data.promote_goods;

//                        if (null != promote_goods && promote_goods.size() > 0) {
//                            simplegoodsList.clear();
//
//                            simplegoodsList.addAll(promote_goods);
//                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goodsDataCache(String result) {
        try {
            if (result != null) {
                JSONObject jsonObject = new JSONObject(result);
                categoryResponse response = new categoryResponse();
                response.fromJson(jsonObject);
                if (response.status.succeed == 1) {
                	ArrayList<CATEGORY> simplegoodses = response.data;
                    if (null != simplegoodses && simplegoodses.size() > 0) {
                        categorygoodsList.clear();
                        categorygoodsList.addAll(simplegoodses);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void fetchHotSelling() {
        homedataRequest request = new homedataRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                try {
                    homedataResponse response = new homedataResponse();
                    response.fromJson(jo);
                    if (response.status.succeed == 1) {
                        fileSave(jo.toString(), "homeData");
                        HOME_DATA home_data = response.data;
                        if (null != home_data) {
                            ArrayList<PLAYER> players = home_data.player;
                            if (null != players && players.size() > 0) {
                                playersList.clear();
                                playersList.addAll(players);
                            }

                  /*          ArrayList<SIMPLEGOODS> promote_goods = home_data.promote_goods;
                            if (null != promote_goods && promote_goods.size() > 0) {
                                simplegoodsList.clear();
                                simplegoodsList.addAll(promote_goods);
                            } else {
                                simplegoodsList.clear();
                            }*/

                            HomeModel.this.OnMessageResponse(url, jo, status);

                        }
                    }

                } catch (JSONException e) {
                }

            }

        };

        cb.url(ApiInterface.HOME_DATA).type(JSONObject.class);
        aq.ajax(cb);
    }
    
    /**
     * 限时打折
     */
    public void HomePromoteGoods (){
        searchRequest request = new searchRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
            	HomeModel.this.callback(url, jo, status);
                try {
                	listpromoteResponse response = new listpromoteResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                        PAGINATED paginated = response.paginated;
                        if (response.status.succeed == 1) {
                        	fileSave(jo.toString(), "promote");
                            ArrayList<SIMPLEGOODS> data = response.data;
                            simplegoodsList.clear();
                            if (null != data && data.size() > 0) {
                                simplegoodsList.clear();
                                simplegoodsList.addAll(data);
                            }
                            HomeModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                	System.out.println("ListGeneralSearch====== "+e);
                }

            }
        };
        request.filter = new FILTER();
        request.filter.sort_by =  "price_desc";
        request.pagination = new PAGINATION();
        request.pagination.page = 1;
        request.pagination.count = 10;
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cb.url(ApiInterface.LIST_PROMOTE).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }
    
    
    
    /**
     * 热销商品
     */
    public void HomeHotGoods() {
    	searchRequest request = new searchRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                HomeModel.this.callback(url, jo, status);
                try {
                	homehotResponse response = new homehotResponse();
					JSONObject mjs = new JSONObject();
					response.fromJson(jo);
		            if (jo != null) {
		                if (response.status.succeed == 1) {
		                    fileSave(jo.toString(), "homehot");
		                	ArrayList<HOMEBOTGOODS> simplegoodses = response.data;
                            if (null != simplegoodses && simplegoodses.size() > 0) {
                            	homehotgoodsList.clear();
                            	homehotgoodsList.addAll(simplegoodses);
                            	HomeModel.this.OnMessageResponse(url, jo, status);
                            }
                        } else {
                        	homebestgoodsList.clear();
                        }
		            }
				} catch (Exception e) {
					// TODO: handle exception
			           e.printStackTrace();
				}
            }
        };
        request.filter = new FILTER();
        request.filter.sort_by =  "price_desc";
        request.pagination = new PAGINATION();
        request.pagination.page = 1;
        request.pagination.count = 10;
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cb.url(ApiInterface.LIST_HOT).type(JSONObject.class).params(params);
//        cb.url(ApiInterface.LIST_HOT).type(JSONObject.class);
        aq.ajax(cb);
    }
    
    
    public void HomeBostGoods() {
    	searchRequest request = new searchRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

            	HomeModel.this.callback(url, jo, status);
                try {
                	listpromoteResponse response = new listpromoteResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                        if (response.status.succeed == 1) {
                        	fileSave(jo.toString(), "homebost");
                            ArrayList<SIMPLEGOODS> data = response.data;
                            homebestgoodsList.clear();
                            if (null != data && data.size() > 0) {
                            	homebestgoodsList.clear();
                            	homebestgoodsList.addAll(data);
                            }
                            HomeModel.this.OnMessageResponse(url, jo, status);
                        }
                    }
                } catch (JSONException e) {
                    // TODO: handle exception
                	System.out.println("ListGeneralSearch====== "+e);
                }
            }
        };
        
        request.filter = new FILTER();
        request.filter.sort_by =  "price_desc";
        request.pagination = new PAGINATION();
        request.pagination.page = 1;
        request.pagination.count = 10;
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cb.url(ApiInterface.LIST_BEST).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }
    
    
    public void HomeNewGoods() {
    	searchRequest request = new searchRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
             	HomeModel.this.callback(url, jo, status);
                try {
                	listpromoteResponse response = new listpromoteResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                        if (response.status.succeed == 1) {
                           	fileSave(jo.toString(), "homenew");
                            ArrayList<SIMPLEGOODS> data = response.data;
                            homenewgoodsList.clear();
                            if (null != data && data.size() > 0) {
                            	homenewgoodsList.clear();
                            	homenewgoodsList.addAll(data);
                            }
                            HomeModel.this.OnMessageResponse(url, jo, status);
                        }
                    }

                } catch (JSONException e) {
                    // TODO: handle exception
                	System.out.println("ListGeneralSearch====== "+e);
                }
            }
        };
        request.filter = new FILTER();
        request.filter.sort_by =  "price_desc";
        request.pagination = new PAGINATION();
        request.pagination.page = 1;
        request.pagination.count = 10;
        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        cb.url(ApiInterface.LIST_NEW).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }
    

    public void fetchCategoryGoods() {
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                done(url, jo, status);
                try {
                    categoryResponse response = new categoryResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                    	categorygoodsList.clear();
                        if (response.status.succeed == 1) {
                            fileSave(jo.toString(), "goodsData");
                            ArrayList<CATEGORY> data = response.data;
                            if (null != data && data.size() > 0) {
                            	categorygoodsList.addAll(data);
                            }
                            HomeModel.this.OnMessageResponse(url, jo, status);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        cb.url(ApiInterface.CATEGORY).type(JSONObject.class);
        aq.ajax(cb);

    }
    
    /**
     * 版本更新
     */
    public void checkupdate(String course_version) {
    	checkupate = new checkupdateRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
                HomeModel.this.callback(url, jo, status);
                try {
					checkupate.fromJson(jo);
		            if (jo != null) {
		                if (checkupate.status.succeed == 1) {
		                	HomeModel.this.OnMessageResponse(url, jo, status);
		                }
		            }
				} catch (Exception e) {
					// TODO: handle exception
			           e.printStackTrace();
				}
            }
        };

        Map<String, String> params = new HashMap<String, String>();
        checkupate.client_version = course_version;  
        try {
            params.put("json", checkupate.toJson().toString());
        } catch (JSONException e) {
            // TODO: handle exception
        }
        cb.url(ApiInterface.CHECK_UPDATE).type(JSONObject.class).params(params);
        aq.ajax(cb);
    }
    
    
    


    protected void done(String url, JSONObject jo, AjaxStatus status) {
        String localUrl = url;
        JSONObject result = jo;
    }

    public String web;

    public void helpArticle(int article_id) {

        articleRequest request = new articleRequest();
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {

                HomeModel.this.callback(url, jo, status);

                try {
                    articleResponse response = new articleResponse();
                    response.fromJson(jo);
                    if (jo != null) {
                        web = response.data;

                        HomeModel.this.OnMessageResponse(url, jo, status);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }

        };

        request.session = SESSION.getInstance();
        request.article_id = article_id;

        Map<String, String> params = new HashMap<String, String>();
        try {
            params.put("json", request.toJson().toString());
        } catch (JSONException e) {
            // TODO: handle exception
        }

        cb.url(ApiInterface.ARTICLE).type(JSONObject.class).params(params);
        aq.ajax(cb);

    }

    // 缓存数据
    private PrintStream ps = null;

    public void fileSave(String result, String name) {

        String path = rootpath + "/" + pkName;

        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        File file = new File(filePath + "/" + name + ".dat");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            ps = new PrintStream(fos);
            ps.print(result);
            ps.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
