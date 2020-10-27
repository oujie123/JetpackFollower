package com.example.base.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.example.base.BaseApplication;

public class ToastUtil {
	private static Toast mToast;

	public static void show(String msg) {
		try {
			if (!TextUtils.isEmpty(msg)) {
				if(mToast != null){
					mToast.cancel();
				}
				mToast = Toast.makeText(BaseApplication.sApplication, "", Toast.LENGTH_LONG);
				mToast.setText(msg);
				mToast.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
