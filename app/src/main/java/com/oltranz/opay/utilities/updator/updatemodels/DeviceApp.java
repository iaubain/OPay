package com.oltranz.opay.utilities.updator.updatemodels;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 8/15/2017.
 */

public class DeviceApp {
    private String countryCode;
    private String serialNumber;
    private String deviceImei;
    private String appName;
    private String versionName;
    private String versionCode;
    private String packageInfo;

    public DeviceApp() {
    }

    public DeviceApp(String countryCode, String serialNumber, String deviceImei, String appName, String versionName, String versionCode, String packageInfo) {
        this.countryCode = countryCode;
        this.serialNumber = serialNumber;
        this.deviceImei = deviceImei;
        this.appName = appName;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.packageInfo = packageInfo;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDeviceImei() {
        return deviceImei;
    }

    public void setDeviceImei(String deviceImei) {
        this.deviceImei = deviceImei;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(String packageInfo) {
        this.packageInfo = packageInfo;
    }
}
