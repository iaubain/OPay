package com.oltranz.opay.utilities.updator.logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.oltranz.opay.BuildConfig;
import com.oltranz.opay.utilities.updator.apiclient.DeviceUuidFactory;
import com.oltranz.opay.utilities.updator.apiclient.UpDateConfig;
import com.oltranz.opay.utilities.updator.updatemodels.DeviceApp;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 8/16/2017.
 */

public class DeviceProvider {
    private Context context;

    public DeviceProvider(Context context) {
        this.context = context;
    }

    public DeviceApp genDevice(){
        String serialNumber = Build.SERIAL;
        DeviceUuidFactory duf = new DeviceUuidFactory(context);
        serialNumber = serialNumber == null ? String.valueOf(duf.getDeviceUuid()) : serialNumber;
        TelephonyManager tManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        String countryCode;
        countryCode = tManager.getNetworkCountryIso() != null ? tManager.getNetworkCountryIso() : "def";
        @SuppressLint("HardwareIds")
        String deviceImei = tManager.getDeviceId();
        return new DeviceApp(countryCode,
                serialNumber,
                deviceImei,
                UpDateConfig.APP_DESC,
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE+"",
                context.getPackageName());
    }
}
