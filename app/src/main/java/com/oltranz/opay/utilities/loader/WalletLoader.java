package com.oltranz.opay.utilities.loader;

import com.oltranz.opay.apiclient.ServiceGen;
import com.oltranz.opay.apiclient.WalletManagerServices;
import com.oltranz.opay.apiclient.WalletServiceGen;
import com.oltranz.opay.models.wallet.Wallet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/10/2017.
 */

public class WalletLoader {
    private String merchantId;
    private OnWalletLoader mListener;
    private String message;
    private List<Wallet> wallets = new ArrayList<>();

    public WalletLoader(String merchantId,OnWalletLoader mListener) {
        this.merchantId = merchantId;
        this.mListener = mListener;
    }

    public void startLoading(){
        BackLoading backLoading = new BackLoading();
        backLoading.execute("");
    }

    public interface OnWalletLoader{
        void onWalletLoader(boolean isLoaded, Object object);
    }

    private class BackLoading {
        void execute(String... parms) {
            try {
                final WalletManagerServices walletServices = WalletServiceGen.createService(WalletManagerServices.class, WalletManagerServices.BASE_URL);
                Call<List<Wallet>> callService = walletServices.getWallet(merchantId);
                callService.enqueue(new Callback<List<Wallet>>() {
                    @Override
                    public void onResponse(Call<List<Wallet>> call, Response<List<Wallet>> response) {
                        int statusCode = response.code();

                        if(statusCode == 500){
                            message = "Internal server error";
                            onPostExecute(wallets);
                            return;
                        }else if(statusCode != 200){
                            message = response.message();
                            onPostExecute(wallets);
                            return;
                        }

                        if(response.body() == null){
                            message = "Empty server response";
                            onPostExecute(wallets);
                            return;
                        }
                        message = response.body() == null ? "Empty body":"Response with body";
                        onPostExecute(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Wallet>> call, Throwable t) {
                        message = "Network call failure. "+t.getLocalizedMessage();
                        onPostExecute(wallets);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                message = e.getLocalizedMessage();
                onPostExecute(wallets);
            }
        }

        protected void onPostExecute(List<Wallet> wallets) {
            try{
                if(wallets == null || wallets.isEmpty())
                    mListener.onWalletLoader(false, message);
                else{
                    Wallet wallet = wallets.get(0);
                    if(wallet.getId() != null){
                        mListener.onWalletLoader(true, wallet);
                    }else{
                        mListener.onWalletLoader(false, "Wallet error. "+message);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                mListener.onWalletLoader(false, "Error: "+e.getMessage());
            }

        }
    }
}
