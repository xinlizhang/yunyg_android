package com.insthub.ecmobile.model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.androidquery.callback.AjaxCallback;

import com.external.androidquery.callback.AjaxCallback;
import com.insthub.BeeFramework.model.BeeCallback;
import com.insthub.ecmobile.protocol.*;

import java.util.ArrayList;

public class MockServer
{
    private static MockServer instance;
    public static MockServer getInstance()
    {
        if (instance == null) {
            instance = new MockServer();
        }
        return instance;
    }
    
    public static <K> void ajax(AjaxCallback<K> callback)
    {
	try {
			
		
    	JSONObject responseJsonObject = new JSONObject();
    	if (callback.getUrl() == ProtocolConst.HOMEDATA) 
    	{
    		
    		STATUS status = new STATUS();
    		status.succeed = 1;
    		status.error_code= 0;
    		status.error_desc="";
    		responseJsonObject.put("status", status.toJson());
    		
    		JSONObject dataJsonObject = new JSONObject();
    		JSONArray itemJsonArray = new JSONArray();
    		for (int i = 0; i < 3; i++) 
    		{    			    		
        		PLAYER player = new PLAYER();
        		player.description = "月光光照大床";
        		player.url= "www.baidu.com";
        		player.photo = new PHOTO();

        		player.photo.url = "http://cache.miyoo.cn/attach/13/42157/1/6054/193729.jpg";
        		itemJsonArray.put(player.toJson());        		
			}
    		
    		dataJsonObject.put("player", itemJsonArray);
    		
    		JSONArray item2JsonArray = new JSONArray();
    		for (int i = 0; i < 3; i++) 
    		{    			    		
    			SIMPLEGOODS good = new SIMPLEGOODS();
    			good.img = new PHOTO();
    			good.img.url = "http://cache.miyoo.cn/attach/13/42157/1/6054/193729.jpg";
        		item2JsonArray.put(good.toJson());        		
			}
    		
    		dataJsonObject.put("promote_goods", item2JsonArray);
    		responseJsonObject.put("data", dataJsonObject);

		}
    	else if (callback.getUrl() == ProtocolConst.CATEGORYGOODS) 
    	{
    		STATUS status = new STATUS();
    		status.succeed = 1;
    		status.error_code= 0;
    		status.error_desc="";
    		responseJsonObject.put("status", status.toJson());
    		
    		JSONArray dataJsonObject = new JSONArray();    		
    		for (int i = 0; i < 3; i++) 
    		{    			    		
    			CATEGORYGOODS good = new CATEGORYGOODS();
    			        		
//        		for (int j = 0; j < 3; j++) 
//        		{    			    		
//        			SIMPLEGOODS simplegood = new SIMPLEGOODS();
//        			simplegood.img = new PHOTO();
//
//        			simplegood.img.url = "http://cache.miyoo.cn/attach/13/42157/1/6054/193729.jpg";
//        			good.goods.add(simplegood);       		
//    			}
//    			good.img = new PHOTO();
//    			good.img.url = "http://cache.miyoo.cn/attach/13/42157/1/6054/193729.jpg";
    			good.category_thumb =  "http://cache.miyoo.cn/attach/13/42157/1/6054/193729.jpg";
        		good.name = "家居";      
        		dataJsonObject.put(good.toJson());        		        		
			}
    		    		
    		responseJsonObject.put("data", dataJsonObject);
    		
		}
    	else if (callback.getUrl() == ProtocolConst.SEARCH) 
    	{
    		STATUS status = new STATUS();
    		status.succeed = 1;
    		status.error_code= 0;
    		status.error_desc="";
    		responseJsonObject.put("status", status.toJson());
    		
    		JSONArray dataJsonObject = new JSONArray();    		
    		    			        		
    		for (int j = 0; j < 3; j++) 
    		{    			    		
    			SIMPLEGOODS simplegood = new SIMPLEGOODS();
    			simplegood.img = new PHOTO();

    			simplegood.img.url = "http://cache.miyoo.cn/attach/13/42157/1/6054/193729.jpg";
    			dataJsonObject.put(simplegood.toJson());        		
			}
        	        		        		       		        	    		    		
    		responseJsonObject.put("data", dataJsonObject);
		}
        else if (callback.getUrl() == ProtocolConst.SHOPHELP)
        {
            STATUS status = new STATUS();
            status.succeed = 1;
            status.error_code= 0;
            status.error_desc="";
            responseJsonObject.put("status", status.toJson());

            JSONArray dataJsonObject = new JSONArray();

            for (int i = 0; i < 3; i++)
            {
                SHOPHELP shophelp = new SHOPHELP();
                 shophelp.name = "支付与配送";
                for (int j = 0; j < 3; j++)
                {
                   ARTICLE article = new ARTICLE();
                    article.short_title = "月光光照大床";
                    article.id = j+"";
                    article.title = "月光光照大床";
                    shophelp.article.add(article);
                }
                dataJsonObject.put(shophelp.toJson());
            }

            responseJsonObject.put("data", dataJsonObject);
        }
        else if (callback.getUrl() == ProtocolConst.GOODSDETAIL)
        {
            STATUS status = new STATUS();
            status.succeed = 1;
            status.error_code= 0;
            status.error_desc="";
            responseJsonObject.put("status", status.toJson());

            JSONObject dataJsonObject = new JSONObject();
            GOODS goods = new GOODS();

            for (int i = 0; i< 5;i++)
            {
                PHOTO photo = new PHOTO();
                photo.url =  "http://cache.miyoo.cn/attach/13/42157/1/6054/193729.jpg";
                goods.pictures.add(photo);
            }
            responseJsonObject.put("data", goods.toJson());

        }
    	
        ((BeeCallback)callback).callback(callback.getUrl(), responseJsonObject, callback.getStatus());
    
    } catch (JSONException e) {
		// TODO: handle exception
	}    	
   }
}
