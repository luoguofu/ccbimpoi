package com.weqia.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

public class TispToastFactory {

	private static Context context = null;
	private static Toast toast = null;

	/**
	 *
	 * @param context
	 *            使用时的上下文
	 *
	 * @param hint
	 *            在提示框中需要显示的文本
	 *
	 * @return 返回一个不会重复显示的toast
	 *
	 * */

	@SuppressLint("ShowToast")
	public static Toast getToast(Context context, String hint, int duration) {

		if (TispToastFactory.context == context) {
//			toast.cancel();
		    if (toast != null) {
		        toast.setText(hint);
            }
			return toast;
		} else {
			TispToastFactory.context = context;
			toast = Toast.makeText(context, hint, duration);
		}
		return toast;
	}
}