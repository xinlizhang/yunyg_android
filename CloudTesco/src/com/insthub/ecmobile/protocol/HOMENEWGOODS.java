package com.insthub.ecmobile.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "HOMEBESTGOODS")
public class HOMENEWGOODS extends Model{
	   public ArrayList<HOMENEWGOODSCELL> new_goods = new ArrayList<HOMENEWGOODSCELL>();

	    public void fromJson(JSONObject jsonObject)  throws JSONException
	    {
	        if(null == jsonObject){
	          return ;
	         }

	        JSONArray subItemArray;
	        subItemArray = jsonObject.optJSONArray("new_goods");
	        if(null != subItemArray)
	         {
	            for(int i = 0;i < subItemArray.length();i++)
	             {
	                 JSONObject subItemObject = subItemArray.getJSONObject(i);
	                 HOMENEWGOODSCELL subItem = new HOMENEWGOODSCELL();
	                 subItem.fromJson(subItemObject);
	                 this.new_goods.add(subItem);
	            }
	        }
	        return ;
	    }

	    public JSONObject  toJson() throws JSONException 
	    {
	        JSONObject localItemObject = new JSONObject();
	        JSONArray itemJSONArray = new JSONArray();

	        for(int i =0; i< new_goods.size(); i++)
	        {
	        	HOMENEWGOODSCELL itemData = new_goods.get(i);
	            JSONObject itemJSONObject = itemData.toJson();
	            itemJSONArray.put(itemJSONObject);
	        }
	        localItemObject.put("new_goods", itemJSONArray);
	        return localItemObject;
	    }

	   
	   public class HOMENEWGOODSCELL extends Model{
		   
		
		     
		     @Column(name = "goods_id")
		     public String goods_id;
		     
		     @Column(name = "name")
		     public String name;
		     
		     @Column(name = "market_price")
		     public String market_price;

		     @Column(name = "shop_price")
		     public String shop_price;
		     
		     @Column(name = "promote_price")
		     public String promote_price;

		     @Column(name = "brief")
		     public String brief;
		     
		     @Column(name = "img")
		     public PHOTO   img;
		     
		     public void  fromJson(JSONObject jsonObject)  throws JSONException
		     {
		         if(null == jsonObject){
		           return ;
		          }

		         JSONArray subItemArray;
		         this.goods_id = jsonObject.optString("goods_id");
		         this.name = jsonObject.optString("name");
		         this.market_price = jsonObject.optString("market_price");
		         this.shop_price = jsonObject.optString("shop_price");
		         this.promote_price = jsonObject.optString("promote_price");
		         this.brief = jsonObject.optString("brief");
		         PHOTO  img = new PHOTO();
		         img.fromJson(jsonObject.optJSONObject("img"));
		         this.img = img;
		         return ;
		     }

		     public JSONObject  toJson() throws JSONException 
		     {
		         JSONObject localItemObject = new JSONObject();
		         JSONArray itemJSONArray = new JSONArray();
		         localItemObject.put("goods_id", goods_id);
		         localItemObject.put("name", name);
		         localItemObject.put("market_price", market_price);
		         localItemObject.put("shop_price", shop_price);
		         localItemObject.put("promote_price", promote_price);
		         localItemObject.put("brief", brief);
		         if(null!=img)
		         {
		        	 localItemObject.put("img", img.toJson());
		         }
		         return localItemObject;
		     }
		   
	   }
}
