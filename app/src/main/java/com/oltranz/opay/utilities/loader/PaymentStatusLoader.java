package com.oltranz.opay.utilities.loader;

import android.content.Context;
import android.util.Log;

import com.oltranz.opay.apiclient.PaymentServiceGen;
import com.oltranz.opay.apiclient.PaymentServices;
import com.oltranz.opay.models.payment.PaymentRequest;
import com.oltranz.opay.models.status.PaymentStatus;
import com.oltranz.opay.utilities.DataFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/9/2017.
 */

public class PaymentStatusLoader {
    private OnPaymentStatus mListener;
    private Context context;
    private PaymentRequest paymentRequest;
    private String token;
    private String message;
    private PaymentStatus paymentStatus = null;

    public PaymentStatusLoader(OnPaymentStatus mListener, Context context, PaymentRequest paymentRequest, String token) {
        this.mListener = mListener;
        this.context = context;
        this.paymentRequest = paymentRequest;
        this.token = token;
    }

    public void startLoading(){
        BackLoading backLoading = new BackLoading();
        backLoading.execute("");
    }

    public interface OnPaymentStatus{
        void onPaymentStatus(boolean isLoaded, Object object, PaymentRequest paymentRequest);
    }

    private class BackLoading {
        void execute(String... parms) {
            try {
                Log.d("StatusLoader", DataFactory.objectToString(paymentRequest.getRequestRef()));
                PaymentServices paymentServices = PaymentServiceGen.createService(PaymentServices.class, PaymentServices.BASE_URL);
                Call<PaymentStatus> callService = paymentServices.getPaymentStatus(PaymentServices.CMD_PAYMENT_STATUS, token,  paymentRequest.getRequestRef());
                callService.enqueue(new Callback<PaymentStatus>() {
                    @Override
                    public void onResponse(Call<PaymentStatus> call, Response<PaymentStatus> response) {
                        int statusCode = response.code();
                        Log.d("StatusLoader", DataFactory.objectToString(response.body()));
                        Log.d("StatusLoader", response.message());
                        if(statusCode == 500){
                            message = "Internal server error";
                            onPostExecute(paymentStatus);
                            return;
                        }else if(statusCode != 200){
                            message = response.message();
                            onPostExecute(paymentStatus);
                            return;
                        }

                        if(response.body() == null){
                            message = "Empty server response";
                            onPostExecute(paymentStatus);
                            return;
                        }
                        message = response.body() == null ? "Empty body":"Response with invalid body";
                        onPostExecute(response.body());
                    }

                    @Override
                    public void onFailure(Call<PaymentStatus> call, Throwable t) {
                        message = "Network call failure. "+t.getLocalizedMessage();
                        onPostExecute(paymentStatus);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                message = e.getLocalizedMessage();
                onPostExecute(paymentStatus);
            }
        }

        protected void onPostExecute(PaymentStatus paymentStatus) {
            try{
                if(paymentStatus == null)
                    mListener.onPaymentStatus(false, message, paymentRequest);
                else{
                    if(paymentStatus.getBody() != null && !paymentStatus.getDescription().isEmpty()){
                        mListener.onPaymentStatus(true, paymentStatus, paymentRequest);
                    }else{
                        mListener.onPaymentStatus(false, "Payment status error. "+message, paymentRequest);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                mListener.onPaymentStatus(false, "Error: "+e.getMessage(), paymentRequest);
            }

        }
    }
}
