
package com.insthub.ecmobile.protocol;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "CATEGORYGOODS")
public class CATEGORYGOODS  extends Model
{
     @Column(name = "category_img")
     public String   category_img;
	
     @Column(name = "CATEGORYGOODS_id")
     public String id;

     @Column(name = "category_thumb")
     public String category_thumb;
     
     @Column(name = "category_icon")
     public String category_icon;
     
     @Column(name = "name")
     public String name;


 public void fromJson(JSONObject jsonObject)  throws JSONException
 {
     if(null == jsonObject){
       return ;
      }

     this.category_img = jsonObject.optString("category_img");
     
     this.id = jsonObject.optString("id");
     
     this.category_thumb = jsonObject.optString("category_thumb");
     
     this.category_icon = jsonObject.optString("category_icon");
     
     this.name = jsonObject.optString("name");

     return ;
 }

 public JSONObject  toJson() throws JSONException 
 {
     JSONObject localItemObject = new JSONObject();
     
     localItemObject.put("category_img", category_img);
     
     localItemObject.put("id", id);
     
     localItemObject.put("category_thumb", category_thumb);
     
     localItemObject.put("category_icon", category_icon);
     
     localItemObject.put("name", name);
     
     return localItemObject;
 }

}
