package com.oltranz.opay.apiclient;

import com.oltranz.opay.models.customers.CustomerResponse;
import com.oltranz.opay.models.liquidation.LiquidationRequest;
import com.oltranz.opay.models.liquidation.LiquidationResponse;
import com.oltranz.opay.models.login.LoginRequest;
import com.oltranz.opay.models.login.LoginResponse;
import com.oltranz.opay.models.serviceprovider.ProviderValue;
import com.oltranz.opay.models.user.UserDetails;
import com.oltranz.opay.models.user.UserProfile;
import com.oltranz.opay.models.wallet.Balance;
import com.oltranz.opay.models.wallet.Wallet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 8/23/2017.
 */

public interface WalletManagerServices {
    String BASE_URL="http://41.74.172.132:8088/";
    String GET_WALLET = "oltranz/services/wallet/wallets/owner/{merchantId}";
    String GET_BALANCE = "oltranz/services/wallet/transactions/summary/wallet/{walletId}";
    String LIQUIDATE = "oltranz/services/wallet/transactions/liquidate";
    String GET_CUSTOMERS = "oltranz/services/wallet/wallet/clients/{walletId}";
    String PAYMENT_SERVICE = "oltranz/services/wallet/transactions/payments/{walletId}";

    @GET(GET_WALLET)
    Call<List<Wallet>> getWallet(@Path("merchantId")String userId);

    @GET(GET_BALANCE)
    Call<Balance> getBalance(@Path("walletId") String walletId);

    @POST(LIQUIDATE)
    Call<LiquidationResponse> liquidation(@Body LiquidationRequest liquidationRequest);

    @GET(GET_CUSTOMERS)
    Call<CustomerResponse> getCustomers(@Path("walletId") String walletId);

    @GET(PAYMENT_SERVICE)
    Call<List<ProviderValue>> getPaymentValues(@Path("walletId") String walletId);
}
