package com.oltranz.opay.apiclient;

import com.oltranz.opay.models.login.LoginRequest;
import com.oltranz.opay.models.login.LoginResponse;
import com.oltranz.opay.models.merchant.MerchantDetails;
import com.oltranz.opay.models.payment.PaymentRequest;
import com.oltranz.opay.models.payment.PaymentResponse;
import com.oltranz.opay.models.signup.registermerchant.RegisterMerchantRequest;
import com.oltranz.opay.models.signup.registermerchant.RegisterMerchantResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/11/2017.
 */

public interface MerchantServices {
    String BASE_URL="http://opay.rw/";
    String GET_MERCHANT = "8080/ProvisioningApp/command";
    String REQUEST_PAYMENT = "8080/PaymentServiceProvider/command";
    String CREATE_MERCHANT = "8080/ProvisioningApp/command";

    String CMD_GET_MERCHANT = "getRequestorMerchant";
    String CMD_PAYMENT = "requestPayment";
    String CMD_NEW_MERCHANT = "newMerchant";
    String SIGNATURE = "2ere623jher7er7899";

    @POST(GET_MERCHANT)
    Call<MerchantDetails> getMerchant(@Header("cmd") String cmd, @Header("access_token") String token);

    @POST(REQUEST_PAYMENT)
    Call<PaymentResponse> requestPayment(@Header("cmd") String cmd, @Header("access_token") String token, @Body PaymentRequest paymentRequest);

    @POST(CREATE_MERCHANT)
    Call<RegisterMerchantResponse> registerMerchant(@Header("cmd") String cmd, @Header("system-signature") String token, @Body RegisterMerchantRequest request);

}
