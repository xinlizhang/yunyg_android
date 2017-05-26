package com.insthub.ecmobile.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "HOMEBESTGOODS")
public class HOMEBESTGOODS extends Model{
	   public ArrayList<HOMEBESTGOODSCELL> best_goods = new ArrayList<HOMEBESTGOODSCELL>();

	    public void fromJson(JSONObject jsonObject)  throws JSONException
	    {
	        if(null == jsonObject){
	          return ;
	         }

	        JSONArray subItemArray;
	        subItemArray = jsonObject.optJSONArray("best_goods");
	        if(null != subItemArray)
	         {
	            for(int i = 0;i < subItemArray.length();i++)
	             {
	                 JSONObject subItemObject = subItemArray.getJSONObject(i);
	                 HOMEBESTGOODSCELL subItem = new HOMEBESTGOODSCELL();
	                 subItem.fromJson(subItemObject);
	                 this.best_goods.add(subItem);
	            }
	        }
	        return ;
	    }

	    public JSONObject  toJson() throws JSONException 
	    {
	        JSONObject localItemObject = new JSONObject();
	        JSONArray itemJSONArray = new JSONArray();

	        for(int i =0; i< best_goods.size(); i++)
	        {
	        	HOMEBESTGOODSCELL itemData = best_goods.get(i);
	            JSONObject itemJSONObject = itemData.toJson();
	            itemJSONArray.put(itemJSONObject);
	        }
	        localItemObject.put("best_goods", itemJSONArray);
	        return localItemObject;
	    }

	   
	   public class HOMEBESTGOODSCELL extends Model{
		   
		     @Column(name = "img")
		     public PHOTO   img;
		     
		     @Column(name = "goods_id")
		     public String goods_id;

		     @Column(name = "shop_price")
		     public String shop_price;

		     @Column(name = "market_price")
		     public String market_price;

		     @Column(name = "brief")
		     public String brief;
		     
		     @Column(name = "promote_price")
		     public String promote_price;

		     @Column(name = "name")
		     public String name;
		     
		     public void  fromJson(JSONObject jsonObject)  throws JSONException
		     {
		         if(null == jsonObject){
		           return ;
		          }

		         JSONArray subItemArray;
		         PHOTO  img = new PHOTO();
		         img.fromJson(jsonObject.optJSONObject("img"));
		         this.img = img;
		         this.goods_id = jsonObject.optString("id");
		         this.shop_price = jsonObject.optString("shop_price");
		         this.market_price = jsonObject.optString("market_price");
		         this.brief = jsonObject.optString("brief");
		         this.promote_price = jsonObject.optString("promote_price");
		         this.name = jsonObject.optString("name");
		         return ;
		     }

		     public JSONObject  toJson() throws JSONException 
		     {
		         JSONObject localItemObject = new JSONObject();
		         JSONArray itemJSONArray = new JSONArray();
		         if(null!=img)
		         {
		           localItemObject.put("img", img.toJson());
		         }
		         localItemObject.put("goods_id", goods_id);
		         localItemObject.put("shop_price", shop_price);
		         localItemObject.put("market_price", market_price);
		         localItemObject.put("brief", brief);
		         localItemObject.put("promote_price", promote_price);
		         localItemObject.put("name", name);
		         return localItemObject;
		     }
		   
	   }
}
