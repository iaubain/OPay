package com.oltranz.opay.utilities.loader;

import com.oltranz.opay.apiclient.WalletManagerServices;
import com.oltranz.opay.apiclient.WalletServiceGen;
import com.oltranz.opay.models.history.HistoryLiquidation;
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

public class HistoryLiquidationLoader {
    private String walletId;
    private String token;
    private String startDate;
    private String endDate;
    private OnLiquidationHistory mListener;
    private String message;
    private List<HistoryLiquidation> providerValues = new ArrayList<>();

    public HistoryLiquidationLoader(String token, String walletId, String startDate, String endDate, OnLiquidationHistory mListener) {
        this.token = token;
        this.walletId = walletId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.mListener = mListener;
    }

    public void startLoading() {
        BackLoading backLoading = new BackLoading();
        backLoading.execute("");
    }

    public interface OnLiquidationHistory {
        void onLiquidationHistory(boolean isLoaded, Object object, List<HistoryLiquidation> providerValues);
    }

    private class BackLoading {
        void execute(String... parms) {
            try {
                final WalletManagerServices walletServices = WalletServiceGen.createService(WalletManagerServices.class, WalletManagerServices.BASE_URL);
                Call<List<HistoryLiquidation>> callService = walletServices.getLiquidationHistory(walletId, token, startDate,endDate);
                callService.enqueue(new Callback<List<HistoryLiquidation>>() {
                    @Override
                    public void onResponse(Call<List<HistoryLiquidation>> call, Response<List<HistoryLiquidation>> response) {
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
                        message = response.body() == null ? "Empty body" : "No data found";
                        onPostExecute(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<HistoryLiquidation>> call, Throwable t) {
                        t.printStackTrace();
                        message = "Network call failure. ";
                        onPostExecute(providerValues);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                message = e.getLocalizedMessage();
                onPostExecute(providerValues);
            }
        }

        protected void onPostExecute(List<HistoryLiquidation> providerValues) {
            try {
                if (providerValues == null || providerValues.isEmpty())
                    mListener.onLiquidationHistory(false, message, providerValues);
                else {
                    mListener.onLiquidationHistory(true, providerValues, providerValues);
                }
            } catch (Exception e) {
                e.printStackTrace();
                mListener.onLiquidationHistory(false, "Error: " + e.getMessage(), providerValues);
            }

        }
    }
}
