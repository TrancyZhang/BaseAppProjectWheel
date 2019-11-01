package com.project.base.app.tz;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.text.TextUtils;

import com.kuaitianshi.o2o.SaaS.Nurse.util.BaseSetting;

/**
 * Created by An4 on 2019-09-20.
 */
public class BaseApp extends Application {

    private static BaseApp mBaseApp;
    String mSid = "";

    public static BaseApp getInstance() {
        if (mBaseApp == null)
            throw new NullPointerException("app not create or be terminated!");
        return mBaseApp;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mBaseApp = this;
    }


    public String getSid() {
        if (TextUtils.isEmpty(mSid)) {
            String basesid = "xxxxxx";
            try {
                ApplicationInfo appInfo = getPackageManager()
                        .getApplicationInfo(getPackageName(),
                                PackageManager.GET_META_DATA);
                String metainfo = appInfo.metaData.getString("MYCHANNEL_ID");
                mSid = metainfo == null ? basesid : metainfo;
                if (mSid.equals("MYCHANNEL_ID_VALUE")) {
                    mSid = basesid;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return mSid;
    }

    public String getMacCode() {
        String androidId = Settings.System.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (TextUtils.isEmpty(androidId)) {
            return "";
        }
        return "IMSI_" + androidId;
    }


}
