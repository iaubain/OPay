package com.oltranz.opay.utilities.loader;

import com.oltranz.opay.apiclient.ServiceGen;
import com.oltranz.opay.apiclient.WalletManagerServices;
import com.oltranz.opay.apiclient.WalletServiceGen;
import com.oltranz.opay.models.wallet.Balance;
import com.oltranz.opay.models.wallet.Wallet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/10/2017.
 */

public class BalanceLoader {
    private String token;
    private String walletId;
    private OnBalanceLoader mListener;
    private String message;
    private Balance balance = null;

    public BalanceLoader(String token, String walletId, OnBalanceLoader mListener) {
        this.walletId = walletId;
        this.mListener = mListener;
    }

    public void startLoading(){
        BackLoading backLoading = new BackLoading();
        backLoading.execute("");
    }

    public interface OnBalanceLoader{
        void onBalance(boolean isLoaded, Object object);
    }

    private class BackLoading {
        void execute(String... parms) {
            try {
                final WalletManagerServices walletServices = WalletServiceGen.createService(WalletManagerServices.class, WalletManagerServices.BASE_URL);
                Call<Balance> callService = walletServices.getBalance(walletId, token);
                callService.enqueue(new Callback<Balance>() {
                    @Override
                    public void onResponse(Call<Balance> call, Response<Balance> response) {
                        int statusCode = response.code();

                        if(statusCode == 500){
                            message = "Internal server error";
                            onPostExecute(balance);
                            return;
                        }else if(statusCode != 200){
                            message = response.message();
                            onPostExecute(balance);
                            return;
                        }

                        if(response.body() == null){
                            message = "Empty server response";
                            onPostExecute(balance);
                            return;
                        }
                        message = response.body() == null ? "Empty body":"Response with body";
                        onPostExecute(response.body());
                    }

                    @Override
                    public void onFailure(Call<Balance> call, Throwable t) {
                        t.printStackTrace();
                        message = "Network call failure. ";
                        onPostExecute(balance);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                message = e.getLocalizedMessage();
                onPostExecute(balance);
            }
        }

        protected void onPostExecute(Balance balance) {
            try{
                if(balance == null)
                    mListener.onBalance(false, message);
                else{
                    if(balance.getCommission() != null && balance.getDueAmount() != null){
                        mListener.onBalance(true, balance);
                    }else{
                        mListener.onBalance(false, "Balance error error. "+message);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                mListener.onBalance(false, "Error: "+e.getMessage());
            }

        }
    }
}
