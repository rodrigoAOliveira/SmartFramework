package com.arcthos.smartframework.smartintegration;

import android.text.TextUtils;

import com.salesforce.androidsdk.smartstore.store.IndexSpec;
import com.salesforce.androidsdk.smartsync.model.SalesforceObject;
import com.salesforce.androidsdk.smartsync.util.Constants;

import org.json.JSONObject;

import java.io.Serializable;

public abstract class BaseSobject extends SalesforceObject implements Serializable {

	public static final String LOCALLY_CREATED = "__locally_created__";
	public static final String LOCALLY_UPDATED = "__locally_updated__";
	public static final String LOCALLY_DELETED = "__locally_deleted__";

	public BaseSobject(JSONObject data) {
		super(data);
		
		objectId = data.optString(Constants.ID);
		name = data.optString(Constants.NAME);
		isLocallyModified = data.optBoolean(LOCALLY_UPDATED) ||
				data.optBoolean(LOCALLY_CREATED) ||
				data.optBoolean(LOCALLY_DELETED);
	}
	
	protected boolean isLocallyModified;
	
	/**
	 * Returns whether the object has been locally modified or not.
	 *
	 * @return True - if the object has been locally modified, False - otherwise.
	 */
	public boolean isLocallyModified() {
		return isLocallyModified;
	}

	public String getObjectName() {
		return objectType;
	}
	
	
	/**
	 * Returns field of the object.
	 *
	 * @return Field of the object.
	 */
	public String get(String field) {
		return sanitizeText(rawData.optString(field));
	}
			
	public int getInt(String field) {
		return rawData.optInt(field, -1);
	}
	
	public double getDouble(String field) {
		return rawData.optDouble(field, 0);
	}
	
	public long getLong(String field) {
		return rawData.optLong(field, -1);
	}
	
	/**
	 * Returns id of the object.
	 *
	 * @return Id of the object.
	 */
	public String getId() {
		return sanitizeText(rawData.optString(Constants.ID));
	}
	
	/**
	 * Returns name of the object.
	 *
	 * @return Name of the object.
	 */
	public String getName() {
		return sanitizeText(rawData.optString(Constants.NAME));
	}

	protected String sanitizeText(String text) {
		if (TextUtils.isEmpty(text) || text.equals(Constants.NULL_STRING)) {
			return Constants.EMPTY_STRING;
		}
		return text;
	}
	
	public abstract String getSoup();
	
	public abstract String getListSyncCompleteAction();
	
	public abstract String getTag();
	
	public abstract IndexSpec[] getIndexSpec();
	
	public abstract String[] getFieldsSyncUp();
	
	public abstract String[] getFieldsSyncDown();
}
