package com.weqia.wq.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public abstract class ScreenReceiver extends BroadcastReceiver {
	private boolean isRegisterReceiver = false;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_SCREEN_ON)) {
			screenOnDo();
		} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
			screenOnOff();

		}
	}

	public abstract void screenOnDo();
	public abstract void screenOnOff();

	public void registerScreenReceiver(Context mContext) {
		if (!isRegisterReceiver) {
			isRegisterReceiver = true;
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_SCREEN_OFF);
			filter.addAction(Intent.ACTION_SCREEN_ON);
			mContext.registerReceiver(ScreenReceiver.this, filter);
		}
	}

	public void unregisterScreenReceiver(Context mContext) {
		if (isRegisterReceiver) {
			isRegisterReceiver = false;
			mContext.unregisterReceiver(ScreenReceiver.this);
		}
	}
}