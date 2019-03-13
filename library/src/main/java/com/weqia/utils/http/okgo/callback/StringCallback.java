package com.weqia.utils.http.okgo.callback;

import okhttp3.Call;
import okhttp3.Response;
import android.support.annotation.Nullable;

import com.weqia.utils.http.okgo.convert.StringConvert;
import com.weqia.utils.http.okgo.request.BaseRequest;

public abstract class StringCallback extends RequestCallBack<String> {

	// private ProgressDialog dialog;
	//
	// private void initDialog(Context activity) {
	// if (activity instanceof Activity) {
	// dialog = new ProgressDialog(activity);
	// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// dialog.setCanceledOnTouchOutside(false);
	// dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	// dialog.setMessage("请求网络中...");
	// } else {
	// dialog = null;
	// }
	// }

	public StringCallback() {
	}

	// public StringCallback(Context activity) {
	// super();
	// initDialog(activity);
	// }

	@Override
	public void onSuccessData(String t, Call call, Response response) {
		onSuccess(t);
	}

	public abstract void onSuccess(String t);

	public abstract void onFailure(Throwable t, String strMsg);

	@Override
	public void onError(Call call, Response response, Exception e) {
		super.onError(call, response, e);
		if (response != null) {
//			try {
//				L.e(response.body().string());
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
		}
		onFailure(e, "");
	}

	@Override
	public void onBefore(BaseRequest request) {
		super.onBefore(request);
		// 网络请求前显示对话框
		// if (dialog != null && !dialog.isShowing()) {
		// dialog.show();
		// }
	}

	@Override
	public void onAfter(@Nullable String t, @Nullable Exception e) {
		super.onAfter(t, e);
		// 网络请求结束后关闭对话框
		// if (dialog != null && dialog.isShowing()) {
		// dialog.dismiss();
		// }
	}

	@Override
	public String convertSuccess(Response response) throws Exception {
		String s = StringConvert.create().convertSuccess(response);
		response.close();
		return s;
	}
}