package com.oltranz.opay.utilities.updator.apiclient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 8/8/2017.
 */

public interface UpdateRestApi {
    String BASE_URL = "http://marketplace.oltranz.com/";
    String UPDATE_DOWNLOAD = "application/downloadUpdate";
    String UPDATE_CHECK = "application/checkUpdate";

    @Streaming
    @GET(UpdateRestApi.UPDATE_DOWNLOAD)
    Call<ResponseBody> downloadUpdateFile(@Header("Token") String token);

    @GET(UpdateRestApi.UPDATE_CHECK)
    Call<String> checkUpdates(@Query("countryCode") String countryCode,
                              @Query("serialNumber") String serialNumber,
                              @Query("appName") String appName,
                              @Query("versionName") String versionName,
                              @Query("versionCode") String versionCode,
                              @Query("packageName") String packageName);
}
