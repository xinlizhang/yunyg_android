
package com.insthub.ecmobile.protocol;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "PRICE")
public class CHATDATA  extends Model
{
     @Column(name = "PRICE_id")
     public int id;

     @Column(name = "img")
     public PHOTO   img;

     @Column(name = "text")
     public String text;
     
     @Column(name = "time")
     public String time;
     
     @Column(name = "orso")
     public String orso;

 public void fromJson(JSONObject jsonObject)  throws JSONException
 {
     if(null == jsonObject){
       return ;
      }
     JSONArray subItemArray;

     this.id = jsonObject.optInt("id");

     PHOTO photo=new PHOTO();
     photo.fromJson(jsonObject.optJSONObject("img"));
     this.img =photo;

     this.text = jsonObject.optString("text");
     
     this.time = jsonObject.optString("time");
     
     this.orso = jsonObject.optString("orso");
     return ;
 }

 public JSONObject  toJson() throws JSONException 
 {
     JSONObject localItemObject = new JSONObject();
     JSONArray itemJSONArray = new JSONArray();
     localItemObject.put("id", id);
     if(null!=img)
     {
       localItemObject.put("img", img.toJson());
     }
     localItemObject.put("text", text);
     
     localItemObject.put("time", time);
     
     localItemObject.put("orso", orso);
     return localItemObject;
 }

}
