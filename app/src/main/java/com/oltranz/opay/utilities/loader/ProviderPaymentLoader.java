package com.oltranz.opay.utilities.loader;

import com.oltranz.opay.apiclient.WalletManagerServices;
import com.oltranz.opay.apiclient.WalletServiceGen;
import com.oltranz.opay.models.serviceprovider.ProviderValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/10/2017.
 */

public class ProviderPaymentLoader {
    private String walletId;
    private OnProvider mListener;
    private String message;
    private List<ProviderValue> providerValues = new ArrayList<>();

    public ProviderPaymentLoader(String walletId, OnProvider mListener) {
        this.walletId = walletId;
        this.mListener = mListener;
    }

    public void startLoading() {
        BackLoading backLoading = new BackLoading();
        backLoading.execute("");
    }

    public interface OnProvider {
        void onProvider(boolean isLoaded, Object object, List<ProviderValue> providerValues);
    }

    private class BackLoading {
        void execute(String... parms) {
            try {
                final WalletManagerServices walletServices = WalletServiceGen.createService(WalletManagerServices.class, WalletManagerServices.BASE_URL);
                Call<List<ProviderValue>> callService = walletServices.getPaymentValues(walletId);
                callService.enqueue(new Callback<List<ProviderValue>>() {
                    @Override
                    public void onResponse(Call<List<ProviderValue>> call, Response<List<ProviderValue>> response) {
                        int statusCode = response.code();

                        if (statusCode == 500) {
                            message = "Internal server error";
                            onPostExecute(providerValues);
                            return;
                        } else if (statusCode != 200) {
                            message = response.message();
                            onPostExecute(providerValues);
                            return;
                        }

                        if (response.body() == null) {
                            message = "Empty server response";
                            onPostExecute(providerValues);
                            return;
                        }
                        message = response.body() == null ? "Empty body" : "Response with body";
                        onPostExecute(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<ProviderValue>> call, Throwable t) {
                        message = "Network call failure. " + t.getLocalizedMessage();
                        onPostExecute(providerValues);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                message = e.getLocalizedMessage();
                onPostExecute(providerValues);
            }
        }

        protected void onPostExecute(List<ProviderValue> providerValues) {
            try {
                if (providerValues == null || providerValues.isEmpty())
                    mListener.onProvider(false, message, providerValues);
                else {
                    Collections.sort(providerValues);
                    mListener.onProvider(true, providerValues, providerValues);
                }
            } catch (Exception e) {
                e.printStackTrace();
                mListener.onProvider(false, "Error: " + e.getMessage(), providerValues);
            }

        }
    }
}
