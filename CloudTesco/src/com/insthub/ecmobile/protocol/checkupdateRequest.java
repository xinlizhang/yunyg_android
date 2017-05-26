package com.insthub.ecmobile.protocol;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

@Table(name = "homecategoryRequest")
public class checkupdateRequest extends Model {
	@Column(name = "data")
	public DATA data;

	@Column(name = "status")
	public STATUS status;

	@Column(name = "client_version")
	public String client_version;

	public void fromJson(JSONObject jsonObject) throws JSONException {
		if (null == jsonObject) {
			return;
		}
		DATA data = new DATA();
		data.fromJson(jsonObject.optJSONObject("data"));
		this.data = data;

		STATUS status = new STATUS();
		status.fromJson(jsonObject.optJSONObject("status"));
		this.status = status;
		this.client_version = jsonObject.optString("client_version");
		return;
	}

	public JSONObject toJson() throws JSONException {
		JSONObject localItemObject = new JSONObject();
		if (null != data) {
			localItemObject.put("data", data.toJson());
		}

		if (null != status) {
			localItemObject.put("status", status.toJson());
		}
		localItemObject.put("client_version", client_version);
		return localItemObject;
	}

	public class DATA extends Model {

		@Column(name = "is_necessary")
		public int is_necessary;
		
		@Column(name = "is_hasUpdate")
		public int is_hasUpdate;

		@Column(name = "server_version")
		public String server_version;

		@Column(name = "URL_Android")
		public String URL_Android;

		public void fromJson(JSONObject jsonObject) throws JSONException {
			if (null == jsonObject) {
				return;
			}

			JSONArray subItemArray;

			this.is_necessary = jsonObject.optInt("is_necessary");
			this.is_hasUpdate = jsonObject.optInt("is_hasUpdate");
			this.server_version = jsonObject.optString("server_version");
			this.URL_Android = jsonObject.optString("URL_Android");
			return;
		}

		public JSONObject toJson() throws JSONException {
			JSONObject localItemObject = new JSONObject();
			JSONArray itemJSONArray = new JSONArray();
			localItemObject.put("is_necessary", is_necessary);
			localItemObject.put("is_hasUpdate", is_hasUpdate);
			localItemObject.put("server_version", server_version);
			localItemObject.put("URL_Android", URL_Android);
			return localItemObject;
		}
	}

}
