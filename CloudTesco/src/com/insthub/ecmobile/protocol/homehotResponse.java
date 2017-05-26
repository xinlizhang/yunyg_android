
package com.insthub.ecmobile.protocol;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.external.activeandroid.Model;
import com.external.activeandroid.annotation.Column;
import com.external.activeandroid.annotation.Table;

public class homehotResponse extends Model {
	@Column(name = "data")
	public ArrayList<HOMEBOTGOODS> data = new ArrayList<HOMEBOTGOODS>();

	@Column(name = "status")
	public STATUS status;

	public void fromJson(JSONObject jsonObject) throws JSONException {
		if (null == jsonObject) {
			return;
		}

		JSONArray subItemArray;
		subItemArray = jsonObject.optJSONArray("data");
		if (null != subItemArray) {
			for (int i = 0; i < subItemArray.length(); i++) {
				JSONObject subItemObject = subItemArray.getJSONObject(i);
				HOMEBOTGOODS subItem = new HOMEBOTGOODS();
				subItem.fromJson(subItemObject);
				this.data.add(subItem);
			}
		}

		STATUS status = new STATUS();
		status.fromJson(jsonObject.optJSONObject("status"));
		this.status = status;

		return;
	}

	public JSONObject toJson() throws JSONException {
		JSONObject localItemObject = new JSONObject();
		JSONArray itemJSONArray = new JSONArray();

		for (int i = 0; i < data.size(); i++) {
			HOMEBOTGOODS itemData = data.get(i);
			JSONObject itemJSONObject = itemData.toJson();
			itemJSONArray.put(itemJSONObject);
		}
		localItemObject.put("data", itemJSONArray);

		if (null != status) {
			localItemObject.put("status", status.toJson());
		}

		return localItemObject;
	}
}
