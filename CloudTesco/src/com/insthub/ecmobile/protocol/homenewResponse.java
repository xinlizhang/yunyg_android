package com.insthub.ecmobile.protocol;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;


public class homenewResponse extends Model {
	@Column(name = "data")
	public HOMENEWGOODS data ; 
	
	@Column(name = "status")
	public STATUS status;

	public void fromJson(JSONObject jsonObject) throws JSONException {
		if (null == jsonObject) {
			return;
		}

		HOMENEWGOODS data = new HOMENEWGOODS();
		data.fromJson(jsonObject.optJSONObject("data"));
		this.data = data ; 
		
		STATUS status = new STATUS();
		status.fromJson(jsonObject.optJSONObject("status"));
		this.status = status;

		return;
	}

	public JSONObject toJson() throws JSONException {
		JSONObject localItemObject = new JSONObject();
		if(null == data)
		{
			localItemObject.put("data", data.toJson());
		}
		if (null != status) {
			localItemObject.put("status", status.toJson());
		}

		return localItemObject;
	}
}
