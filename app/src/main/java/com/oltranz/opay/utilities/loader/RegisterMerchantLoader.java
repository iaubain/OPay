package com.oltranz.opay.utilities.loader;

import android.util.Log;

import com.oltranz.opay.apiclient.MerchantServiceGen;
import com.oltranz.opay.apiclient.MerchantServices;
import com.oltranz.opay.models.merchant.MerchantDetails;
import com.oltranz.opay.models.signup.registermerchant.RegisterMerchantRequest;
import com.oltranz.opay.models.signup.registermerchant.RegisterMerchantResponse;
import com.oltranz.opay.utilities.DataFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 6/9/2017.
 */

public class RegisterMerchantLoader {
    private OnMerchantRegisterLoader mListener;
    private String token;
    private RegisterMerchantRequest request;
    private String message;
    private RegisterMerchantResponse merchantResponse = null;

    public RegisterMerchantLoader(OnMerchantRegisterLoader mListener, String token, RegisterMerchantRequest request) {
        this.mListener = mListener;
        this.token = token;
        this.request = request;
    }

    public void startLoading(){
        BackLoading backLoading = new BackLoading();
        backLoading.execute("");
    }

    public interface OnMerchantRegisterLoader{
        void onRegisterMerchant(boolean isLoaded, Object object);
    }

    private class BackLoading {
        void execute(String... parms) {
            try {
                Log.d("Merchant", DataFactory.objectToString(request));
                MerchantServices merchantServices = MerchantServiceGen.createService(MerchantServices.class, MerchantServices.BASE_URL);
                Call<RegisterMerchantResponse> callService = merchantServices.registerMerchant(MerchantServices.CMD_NEW_MERCHANT, token, request);
                callService.enqueue(new Callback<RegisterMerchantResponse>() {
                    @Override
                    public void onResponse(Call<RegisterMerchantResponse> call, Response<RegisterMerchantResponse> response) {
                        int statusCode = response.code();
                        Log.d("Merchant", DataFactory.objectToString(response.body()));
                        if(statusCode == 500){
                            message = "Internal server error";
                            onPostExecute(merchantResponse);
                            return;
                        }else if(statusCode != 200){
                            message = response.message();
                            onPostExecute(merchantResponse);
                            return;
                        }

                        if(response.body() == null){
                            message = "Empty server response";
                            onPostExecute(merchantResponse);
                            return;
                        }
                        message = response.body() == null ? "Empty body":"Response with body";
                        onPostExecute(response.body());
                    }

                    @Override
                    public void onFailure(Call<RegisterMerchantResponse> call, Throwable t) {
                        message = "Network call failure. "+t.getLocalizedMessage();
                        onPostExecute(merchantResponse);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                message = e.getLocalizedMessage();
                onPostExecute(merchantResponse);
            }
        }

        protected void onPostExecute(RegisterMerchantResponse registerMerchantResponse) {
            try{
                if(registerMerchantResponse == null)
                    mListener.onRegisterMerchant(false, message);
                else{
                    if(registerMerchantResponse.getId() != null){
                        mListener.onRegisterMerchant(true, registerMerchantResponse);
                    }else{
                        mListener.onRegisterMerchant(false, "Merchant error. "+message);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                mListener.onRegisterMerchant(false, "Error: "+e.getMessage());
            }

        }
    }
}
