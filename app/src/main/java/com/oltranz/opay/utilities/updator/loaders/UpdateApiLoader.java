package com.oltranz.opay.utilities.updator.loaders;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.oltranz.opay.utilities.updator.apiclient.UpdateRestApi;
import com.oltranz.opay.utilities.updator.apiclient.UpdateServerClient;
import com.oltranz.opay.utilities.updator.factoring.UpdatorDataFactory;
import com.oltranz.opay.utilities.updator.logic.DirManager;
import com.oltranz.opay.utilities.updator.updatemodels.DeviceApp;
import com.oltranz.opay.utilities.updator.updatemodels.TokenModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 8/16/2017.
 */

public class UpdateApiLoader {
    private OnUpdateApiLoader mListener;
    private Context context;
    private String message;

    public UpdateApiLoader(OnUpdateApiLoader mListener, Context context) {
        this.mListener = mListener;
        this.context = context;
    }

    public void checkUpdate(DeviceApp deviceApp){
       try {
           final UpdateRestApi downloadService = UpdateServerClient.createService(UpdateRestApi.class, UpdateRestApi.BASE_URL);
           Call<String> call = downloadService.checkUpdates(deviceApp.getCountryCode(),
                   deviceApp.getSerialNumber(),
                   deviceApp.getAppName(),
                   deviceApp.getVersionName(),
                   deviceApp.getVersionCode(),
                   deviceApp.getPackageInfo());
           call.enqueue(new Callback<String>() {
               @Override
               public void onResponse(Call<String> call, Response<String> response) {
                   try {
                       int statusCode = response.code();
                       if(statusCode != 200){
                           message = "Network status: "+statusCode+" and message: "+response.message();
                           Log.d("CheckUpdates", message);
                           mListener.onCheckUpdate(false, message);
                           return;
                       }

                       TokenModel tokenModel = (TokenModel) UpdatorDataFactory.stringToObject(TokenModel.class, response.body());
                       if(tokenModel != null && tokenModel.getToken() != null)
                           mListener.onCheckUpdate(true, tokenModel);
                       else {
                           message = "Empty server result";
                           Log.d("CheckUpdate", message);
                           mListener.onCheckUpdate(false, message);
                       }
                   }catch (Exception e){
                       message = "Internal application error: "+e.getMessage();
                       Log.d("CheckUpdate", message);
                       mListener.onCheckUpdate(false, message);
                   }
               }

               @Override
               public void onFailure(Call<String> call, Throwable t) {

               }
           });
       }catch (Exception e){
           message = "Internal application error: "+e.getMessage();
           Log.d("CheckUpdate", message);
           mListener.onCheckUpdate(false, message);
       }

    }

    public void downloadUpdates(TokenModel tokenModel){
        try {
            final UpdateRestApi downloadService = UpdateServerClient.createService(UpdateRestApi.class, UpdateRestApi.BASE_URL);
            Call<ResponseBody> call = downloadService.downloadUpdateFile(tokenModel.getToken());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                    try{
                        int statusCode = response.code();
                        if(statusCode != 200){
                            message = "Network status: "+statusCode+" and message: "+response.message();
                            Log.d("Download", message);
                            mListener.onDownloadUpdate(false, message);
                            return;
                        }
                        if (response.isSuccessful()) {
                            Log.d("Download", "server contacted and has file");

                            new AsyncTask<Object, Boolean, Boolean>() {
                                @Override
                                protected Boolean doInBackground(Object... voids) {
                                    try {
                                        boolean writtenToDisk = new DirManager(context).saveTheUpdate(response.body());
                                        Log.d("Download", "file download was a success? " + writtenToDisk);
                                        return writtenToDisk;
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        return false;
                                    }
                                }

                                @Override
                                protected void onPostExecute(Boolean isSaved) {
                                    super.onPostExecute(isSaved);
                                    if(mListener != null){
                                        if(isSaved)
                                            mListener.onDownloadUpdate(true, "File downloaded and saved successfully");
                                        else
                                            mListener.onDownloadUpdate(false, "File is not saved to the disk");
                                    }
                                }
                            }.execute();
                        }
                        else {
                            message = "Network failed: "+statusCode+" and message: "+response.message();
                            Log.d("Download", message);
                            mListener.onDownloadUpdate(false, message);
                            return;
                        }
                    }catch (Exception e){
                        message = "Internal application error: "+e.getMessage();
                        Log.d("Download", message);
                        mListener.onDownloadUpdate(false, message);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    message = "Failure: "+t.getMessage();
                    Log.d("Download", message);
                    mListener.onDownloadUpdate(false, message);
                }
            });
        }catch (Exception e){
            message = "Internal application error: "+e.getMessage();
            Log.d("Download", message);
            mListener.onDownloadUpdate(false, message);
        }
    }

    public interface OnUpdateApiLoader{
        void onCheckUpdate(boolean isDone, Object object);
        void onDownloadUpdate(boolean isDone, Object object);
    }
}
