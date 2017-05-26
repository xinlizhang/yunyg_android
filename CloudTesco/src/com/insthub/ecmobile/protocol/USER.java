
package com.insthub.ecmobile.protocol;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "USER")
public class USER  extends Model
{

    @Column(name = "collection_num")
    public String   collection_num;
/*
    @Column(name = "USER_id",unique = true)
    public String   USER_id;*/

    @Column(name = "rank_level")
    public int rank_level;

    @Column(name = "order_num")
    public ORDER_NUM   order_num;

    @Column(name = "name")
    public String   name;

    @Column(name = "rank_name")
    public String   rank_name;

    @Column(name="email")
    public String email;
    
    @Column(name="sex")
    public int sex;
    
    @Column(name="birthday")
    public String birthday;

    @SuppressLint("NewApi") public void  fromJson(JSONObject jsonObject)  throws JSONException
    {
        if(null == jsonObject){
            return ;
        }

        JSONArray subItemArray;

        this.collection_num = jsonObject.optString("collection_num");

//        this.USER_id = jsonObject.optString("id");

        this.rank_level = jsonObject.optInt("rank_level");
        ORDER_NUM  order_num = new ORDER_NUM();
        order_num.fromJson(jsonObject.optJSONObject("order_num"));
        this.order_num = order_num;

        this.name = jsonObject.optString("name");

        this.rank_name = jsonObject.optString("rank_name");
        
        if(jsonObject.optString("email") != null && !jsonObject.optString("email").isEmpty())
        	this.email = jsonObject.optString("email");
        
        if(jsonObject.optString("sex") != null && !jsonObject.optString("sex").isEmpty())
        	this.sex = Integer.parseInt(jsonObject.optString("sex"));
        
        if(jsonObject.optString("birthday") != null && !jsonObject.optString("birthday").isEmpty())
        	this.birthday = jsonObject.optString("birthday");
        return ;
    }

    public JSONObject  toJson() throws JSONException
    {
        JSONObject localItemObject = new JSONObject();
        JSONArray itemJSONArray = new JSONArray();
        localItemObject.put("collection_num", collection_num);
//        localItemObject.put("USER_id", USER_id);
        localItemObject.put("rank_level", rank_level);
        if(null != order_num)
        {
            localItemObject.put("order_num", order_num.toJson());
        }
        localItemObject.put("name", name);
        
        localItemObject.put("rank_name", rank_name);
        
        localItemObject.put("email", email);
        
        localItemObject.put("sex", sex);
        
        localItemObject.put("birthday", birthday);
        return localItemObject;
    }

}
