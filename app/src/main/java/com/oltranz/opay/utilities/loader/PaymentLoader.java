package com.oltranz.opay.utilities.loader;

import android.content.Context;
import android.util.Log;

import com.oltranz.opay.apiclient.MerchantServiceGen;
import com.oltranz.opay.apiclient.MerchantServices;
import com.oltranz.opay.config.Config;
import com.oltranz.opay.models.payment.PaymentRequest;
import com.oltranz.opay.models.payment.PaymentResponse;
import com.oltranz.opay.utilities.DataFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/9/2017.
 */

public class PaymentLoader {
    private OnPaymentRequest mListener;
    private Context context;
    private PaymentRequest paymentRequest;
    private String token;
    private String message;
    private PaymentResponse paymentResponse = null;

    public PaymentLoader(OnPaymentRequest mListener, Context context, PaymentRequest paymentRequest, String token) {
        this.mListener = mListener;
        this.context = context;
        this.paymentRequest = paymentRequest;
        this.token = token;
    }

    public void startLoading(){
        BackLoading backLoading = new BackLoading();
        backLoading.execute("");
    }

    public interface OnPaymentRequest{
        void onPaymentRequest(boolean isLoaded, Object object);
    }

    private class BackLoading {
        void execute(String... parms) {
            try {
                Log.d("request", DataFactory.objectToString(paymentRequest));
                MerchantServices merchantServices = MerchantServiceGen.createService(MerchantServices.class, MerchantServices.BASE_URL);
                Call<PaymentResponse> callService = merchantServices.requestPayment(MerchantServices.CMD_PAYMENT, token, paymentRequest);
                callService.enqueue(new Callback<PaymentResponse>() {
                    @Override
                    public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                        int statusCode = response.code();
                        Log.d("response", DataFactory.objectToString(response.body()));
                        Log.d("response_error", response.message());
                        if(statusCode == 500){
                            message = "Internal server error";
                            onPostExecute(paymentResponse);
                            return;
                        }else if(statusCode != 200){
                            message = response.message();
                            onPostExecute(paymentResponse);
                            return;
                        }

                        if(response.body() == null){
                            message = "Empty server response";
                            onPostExecute(paymentResponse);
                            return;
                        }
                        message = response.body() == null ? "Empty body":"Response with invalid body";
                        onPostExecute(response.body());
                    }

                    @Override
                    public void onFailure(Call<PaymentResponse> call, Throwable t) {
                        t.printStackTrace();
                        message = "Network call failure. ";
                        onPostExecute(paymentResponse);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                message = e.getLocalizedMessage();
                onPostExecute(paymentResponse);
            }
        }

        protected void onPostExecute(PaymentResponse paymentResponse) {
            try{
                if(paymentResponse == null)
                    mListener.onPaymentRequest(false, message);
                else{
                    if(paymentResponse.getDescription() != null){
                        mListener.onPaymentRequest(true, paymentResponse);
                    }else{
                        mListener.onPaymentRequest(false, "Payment error. "+message);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                mListener.onPaymentRequest(false, "Error: "+e.getMessage());
            }

        }
    }
}
