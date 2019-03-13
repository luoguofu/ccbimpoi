package com.weqia.utils.data;

import com.weqia.data.UtilData;


public class ThrowableData extends UtilData {

	private static final long serialVersionUID = 1L;

	/**
	 * 程序版本信息
	 */
	private String versionInfo;
	/**
	 * 错误信息
	 */
	private String errorInfo;
	/**
	 * 手机信息
	 */
	private String mobileInfo;

	public String getVersionInfo() {
		return versionInfo;
	}

	public void setVersionInfo(String versionInfo) {
		this.versionInfo = versionInfo;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public String getMobileInfo() {
		return mobileInfo;
	}

	public void setMobileInfo(String mobileInfo) {
		this.mobileInfo = mobileInfo;
	}

	public ThrowableData() {
	}

	public ThrowableData(String versionString, String errorInfo, String mobileInfo) {
		setVersionInfo(versionString);
		setErrorInfo(errorInfo);
		setMobileInfo(mobileInfo);
	}

	// public JSONObject toJson() {
	// JSONObject object = null;
	// try {
	// JSONStringer stringer = new JSONStringer();
	// String string =
	// stringer.object().key(memberAbbrNames[0]).value(versionInfo)
	// .key(memberAbbrNames[1]).value(errorInfo).key(memberAbbrNames[2])
	// .value(mobileInfo).endObject().toString();
	// object = new JSONObject(string);
	// return object;
	// } catch (JSONException e) {
	// CheckedExceptionHandler.handleException(DataThrowable.class.getName(),
	// e);
	// }
	// return null;
	// }
}
