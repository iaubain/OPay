package com.oltranz.opay.utilities.loader;

import android.content.Context;
import android.util.Log;

import com.oltranz.opay.apiclient.MerchantServiceGen;
import com.oltranz.opay.apiclient.MerchantServices;
import com.oltranz.opay.apiclient.ServiceGen;
import com.oltranz.opay.apiclient.UserManagerServices;
import com.oltranz.opay.apiclient.WalletManagerServices;
import com.oltranz.opay.models.login.LoginRequest;
import com.oltranz.opay.models.login.LoginResponse;
import com.oltranz.opay.models.merchant.MerchantDetails;
import com.oltranz.opay.utilities.DataFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 6/9/2017.
 */

public class MerchantLoader {
    private OnMerchantLoader mListener;
    private String command;
    private String token;
    private String message;
    private MerchantDetails merchantDetails = null;

    public MerchantLoader(OnMerchantLoader mListener, String command, String token) {
        this.mListener = mListener;
        this.command = command;
        this.token = token;
    }

    public void startLoading(){
        BackLoading backLoading = new BackLoading();
        backLoading.execute("");
    }

    public interface OnMerchantLoader{
        void onMerchant(boolean isLoaded, Object object);
    }

    private class BackLoading {
        void execute(String... parms) {
            try {
                MerchantServices merchantServices = MerchantServiceGen.createService(MerchantServices.class, MerchantServices.BASE_URL);
                Call<MerchantDetails> callService = merchantServices.getMerchant(command, token);
                callService.enqueue(new Callback<MerchantDetails>() {
                    @Override
                    public void onResponse(Call<MerchantDetails> call, Response<MerchantDetails> response) {
                        int statusCode = response.code();
                        Log.d("Auth", DataFactory.objectToString(response.body()));
                        if(statusCode == 500){
                            message = "Internal server error";
                            onPostExecute(merchantDetails);
                            return;
                        }else if(statusCode != 200){
                            message = response.message();
                            onPostExecute(merchantDetails);
                            return;
                        }

                        if(response.body() == null){
                            message = "Empty server response";
                            onPostExecute(merchantDetails);
                            return;
                        }
                        message = response.body() == null ? "Empty body":"Response with body";
                        onPostExecute(response.body());
                    }

                    @Override
                    public void onFailure(Call<MerchantDetails> call, Throwable t) {
                        message = "Network call failure. "+t.getLocalizedMessage();
                        onPostExecute(merchantDetails);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                message = e.getLocalizedMessage();
                onPostExecute(merchantDetails);
            }
        }

        protected void onPostExecute(MerchantDetails merchantDetails) {
            try{
                if(merchantDetails == null)
                    mListener.onMerchant(false, message);
                else{
                    if(merchantDetails.getId() != null){
                        mListener.onMerchant(true, merchantDetails);
                    }else{
                        mListener.onMerchant(false, "Merchant error. "+message);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                mListener.onMerchant(false, "Error: "+e.getMessage());
            }

        }
    }
}
