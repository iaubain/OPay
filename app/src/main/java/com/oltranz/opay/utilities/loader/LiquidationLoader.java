package com.oltranz.opay.utilities.loader;

import android.util.Log;

import com.oltranz.opay.apiclient.WalletManagerServices;
import com.oltranz.opay.apiclient.WalletServiceGen;
import com.oltranz.opay.models.liquidation.LiquidationRequest;
import com.oltranz.opay.models.liquidation.LiquidationResponse;
import com.oltranz.opay.models.liquidation.LiquidationWallet;
import com.oltranz.opay.models.liquidation.Origin;
import com.oltranz.opay.models.merchant.MerchantDetails;
import com.oltranz.opay.models.wallet.Wallet;
import com.oltranz.opay.utilities.DataFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/10/2017.
 */

public class LiquidationLoader {
    private LiquidationRequest request;
    private OnLiquidation mListener;
    private String message;
    private LiquidationResponse liquidationResponse = null;

    public LiquidationLoader(LiquidationRequest request, OnLiquidation mListener) {
        this.request = request;
        this.mListener = mListener;
    }

    public void startLoading(){
        BackLoading backLoading = new BackLoading();
        backLoading.execute("");
    }

    public interface OnLiquidation{
        void onLiquidation(boolean isLoaded, Object object);
    }

    private class BackLoading {
        void execute(String... parms) {
            try {
                Log.d("Request", DataFactory.objectToString(request));
                final WalletManagerServices walletServices = WalletServiceGen.createService(WalletManagerServices.class, WalletManagerServices.BASE_URL);
                Call<LiquidationResponse> callService = walletServices.liquidation(request);
                callService.enqueue(new Callback<LiquidationResponse>() {
                    @Override
                    public void onResponse(Call<LiquidationResponse> call, Response<LiquidationResponse> response) {
                        int statusCode = response.code();
                        Log.d("Response", DataFactory.objectToString(response.body()));
                        Log.d("Response_error", response.message());
                        if(statusCode == 500){
                            message = "Internal server error";
                            onPostExecute(liquidationResponse);
                            return;
                        }else if(statusCode != 200){
                            message = response.message();
                            onPostExecute(liquidationResponse);
                            return;
                        }

                        if(response.body() == null){
                            message = "Empty server response";
                            onPostExecute(liquidationResponse);
                            return;
                        }
                        message = response.body() == null ? "Empty body":"Response with body";
                        onPostExecute(response.body());
                    }

                    @Override
                    public void onFailure(Call<LiquidationResponse> call, Throwable t) {
                        message = "Network call failure. "+t.getLocalizedMessage();
                        onPostExecute(liquidationResponse);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                message = e.getLocalizedMessage();
                onPostExecute(liquidationResponse);
            }
        }

        protected void onPostExecute(LiquidationResponse liquidationResponse) {
            try{
                if(liquidationResponse == null)
                    mListener.onLiquidation(false, message);
                else{
                    if(!liquidationResponse.getDescription().isEmpty()){
                        mListener.onLiquidation(true, liquidationResponse);
                    }else{
                        mListener.onLiquidation(false, "Liquidation error. "+message);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                mListener.onLiquidation(false, "Error: "+e.getLocalizedMessage());
            }

        }
    }
}
