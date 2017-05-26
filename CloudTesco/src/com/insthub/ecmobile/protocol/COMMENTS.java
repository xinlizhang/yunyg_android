
package com.insthub.ecmobile.protocol;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "COMMENTS")
public class COMMENTS  extends Model
{

     @Column(name = "content")
     public String content;

     @Column(name = "COMMENTS_id",unique = true)
     public String id;

     @Column(name = "re_content")
     public String re_content;

     @Column(name = "author")
     public String author;

     @Column(name = "created")
     public String create;
     
     @Column(name = "comment_rank")
     public String comment_rank;

 public void fromJson(JSONObject jsonObject)  throws JSONException
 {
     if(null == jsonObject){
       return ;
      }


     JSONArray subItemArray;

     this.content = jsonObject.optString("content");

     this.id = jsonObject.optString("id");

     this.re_content = jsonObject.optString("re_content");

     this.author = jsonObject.optString("author");

     this.create = jsonObject.optString("create");
     
     this.comment_rank = jsonObject.optString("comment_rank");
     return ;
 }

 public JSONObject  toJson() throws JSONException 
 {
     JSONObject localItemObject = new JSONObject();
     JSONArray itemJSONArray = new JSONArray();
     localItemObject.put("content", content);
     localItemObject.put("id", id);
     localItemObject.put("re_content", re_content);
     localItemObject.put("author", author);
     localItemObject.put("create", create);
     localItemObject.put("comment_rank", comment_rank);
     return localItemObject;
 }

}
