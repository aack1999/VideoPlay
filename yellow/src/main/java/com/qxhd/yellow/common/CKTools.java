package com.qxhd.yellow.common;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.framework.core.common.AppSetting;

import java.util.UUID;

public class CKTools {

    public static String getImei() {
        String imei = null;
        try {
            TelephonyManager mTm  = (TelephonyManager) AppSetting.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            imei = mTm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(imei)) {
            imei = UUID.randomUUID().toString().replace("-", "");
        }
        return imei;
    }

}
