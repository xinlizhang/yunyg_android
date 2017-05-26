
package com.insthub.ecmobile.protocol;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "feedbackRequest")
public class feedbackRequest  extends Model
{

     @Column(name = "content")
     public String   content;

     @Column(name = "nameoremail")
     public String   nameoremail;

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject) {
            return ;
           }

          this.content = jsonObject.optString("content");
          this.nameoremail = jsonObject.optString("nameoremail");
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          localItemObject.put("content", content);
          localItemObject.put("nameoremail", nameoremail);
          return localItemObject;
     }

}
