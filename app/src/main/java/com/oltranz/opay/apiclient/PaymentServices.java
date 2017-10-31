package com.oltranz.opay.apiclient;

import com.oltranz.opay.models.merchant.MerchantDetails;
import com.oltranz.opay.models.status.PaymentStatus;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/11/2017.
 */

public interface PaymentServices {
    String BASE_URL = "http://opay.rw/8080/";
    String PAYMENT_STATUS = "PaymentServiceProvider/command";

    String CMD_PAYMENT_STATUS = "pamentRequestByRef";

    @POST(PAYMENT_STATUS)
    Call<PaymentStatus> getPaymentStatus(@Header("cmd") String cmd, @Header("access_token") String token, @Body String paymentId);

}
