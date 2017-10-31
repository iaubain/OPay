package com.oltranz.opay.utilities.loader;

import com.oltranz.opay.apiclient.WalletManagerServices;
import com.oltranz.opay.apiclient.WalletServiceGen;
import com.oltranz.opay.models.customers.CustomerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/10/2017.
 */

public class CustomerLoader {
    private String token;
    private String walletId;
    private OnCustomerLoader mListener;
    private String message;
    private CustomerResponse customerResponse = null;

    public CustomerLoader(String token, String walletId, OnCustomerLoader mListener) {
        this.token = token;
        this.walletId = walletId;
        this.mListener = mListener;
    }

    public void startLoading(){
        BackLoading backLoading = new BackLoading();
        backLoading.execute("");
    }

    public interface OnCustomerLoader{
        void onCustomerLoader(boolean isLoaded, Object object);
    }

    private class BackLoading {
        void execute(String... parms) {
            try {
                final WalletManagerServices walletServices = WalletServiceGen.createService(WalletManagerServices.class, WalletManagerServices.BASE_URL);
                Call<CustomerResponse> callService = walletServices.getCustomers(walletId, token);
                callService.enqueue(new Callback<CustomerResponse>() {
                    @Override
                    public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                        int statusCode = response.code();

                        if(statusCode == 500){
                            message = "Internal server error";
                            onPostExecute(customerResponse);
                            return;
                        }else if(statusCode != 200){
                            message = response.message();
                            onPostExecute(customerResponse);
                            return;
                        }

                        if(response.body() == null){
                            message = "Empty server response";
                            onPostExecute(customerResponse);
                            return;
                        }
                        message = response.body() == null ? "Empty body":"Response with body";
                        onPostExecute(response.body());
                    }

                    @Override
                    public void onFailure(Call<CustomerResponse> call, Throwable t) {
                        t.printStackTrace();
                        message = "Network call failure. ";
                        onPostExecute(customerResponse);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                message = e.getLocalizedMessage();
                onPostExecute(customerResponse);
            }
        }

        protected void onPostExecute(CustomerResponse customerResponse) {
            try{
                if(customerResponse == null)
                    mListener.onCustomerLoader(false, message);
                else{
                    if(customerResponse.getTotal() != null || !customerResponse.getTotal().isEmpty()){
                        mListener.onCustomerLoader(true, customerResponse);
                    }else{
                        mListener.onCustomerLoader(false, "Balance error error. "+message);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                mListener.onCustomerLoader(false, "Error: "+e.getMessage());
            }

        }
    }
}
