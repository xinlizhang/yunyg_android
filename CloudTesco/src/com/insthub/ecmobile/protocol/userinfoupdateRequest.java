
package com.insthub.ecmobile.protocol;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "userinfoupdateRequest")
public class userinfoupdateRequest  extends Model
{

     @Column(name = "session")
     public SESSION   session;
     
     @Column(name = "sex")//性别
     public int sex;
     
     @Column(name = "username")//用户名
     public String username;
     
     @Column(name = "birthday")//出生日期
     public String birthday;
     
     @Column(name = "email")//邮箱
     public String email;
     
     @Column(name = "old_password")//旧密码
     public String old_password;
     
     @Column(name = "new_password")//新密码
     public String new_password;
     

     public void  fromJson(JSONObject jsonObject)  throws JSONException
     {
          if(null == jsonObject){
            return ;
           }

          JSONArray subItemArray;
          SESSION  session = new SESSION();
          session.fromJson(jsonObject.optJSONObject("session"));
          
          this.sex = Integer.parseInt(jsonObject.optString("sex"));
          
          this.username = jsonObject.optString("username");
          
          this.birthday = jsonObject.optString("birthday");
          
          this.email = jsonObject.optString("email");
          
          this.old_password = jsonObject.optString("old_password");
          
          this.new_password = jsonObject.optString("new_password");
          this.session = session;
          return ;
     }

     public JSONObject  toJson() throws JSONException 
     {
          JSONObject localItemObject = new JSONObject();
          JSONArray itemJSONArray = new JSONArray();
          if(null != session)
          {
            localItemObject.put("session", session.toJson());
          }
          
          localItemObject.put("sex", this.sex);
          
          localItemObject.put("username", this.username);
          
          localItemObject.put("birthday", this.birthday);
          
          localItemObject.put("email", this.email);
          
          localItemObject.put("old_password", this.old_password);
          
          localItemObject.put("new_password", this.new_password);
          
          return localItemObject;
     }

}
