package com.insthub.ecmobile.protocol;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "commentsRequest")
public class orderinfoRequest extends Model {
	@Column(name = "order_id")
	public String order_id;

	@Column(name = "session")
	public SESSION session;

	public void fromJson(JSONObject jsonObject) throws JSONException {
		if (null == jsonObject) {
			return;
		}

		this.order_id = jsonObject.optString("order_id");
		SESSION session = new SESSION();
		session.fromJson(jsonObject.optJSONObject("session"));
		this.session = session;
		return;
	}

	public JSONObject toJson() throws JSONException {
		JSONObject localItemObject = new JSONObject();
		localItemObject.put("order_id", order_id);
		if (null != session) {
			localItemObject.put("session", session.toJson());
		}
		return localItemObject;
	}

}
