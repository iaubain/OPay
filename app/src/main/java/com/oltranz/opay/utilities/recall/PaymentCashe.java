package com.oltranz.opay.utilities.recall;

import com.oltranz.opay.models.payment.PaymentRequest;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/11/2017.
 */

public class PaymentCashe {
    public static final LinkedHashMap<String, RecallModel> payments = new LinkedHashMap<>();
    public static boolean isContained(String id){
        return payments.containsValue(id);
    }
    public static PaymentRequest addValue(String token, PaymentRequest paymentRequest){
        payments.put(paymentRequest.getRequestRef(), new RecallModel(token, paymentRequest));
        RecallModel recallModel = payments.get(paymentRequest.getRequestRef());
        return recallModel.getPaymentRequest();
    }
    public static RecallModel removeValue(String id){
        return payments.remove(id);
    }
    public static PaymentRequest getPayment(String id){
        RecallModel recallModel = payments.get(id);
        return recallModel != null ? recallModel.getPaymentRequest() : null;
    }
    public static List<PaymentRequest> getAllPayments(){
        List<PaymentRequest> paymentRequests = new ArrayList<>();
        for (Map.Entry entry : payments.entrySet()){
            paymentRequests.add(((RecallModel) entry.getValue()).getPaymentRequest());
        }
        return paymentRequests;
    }

    public static List<RecallModel> getEntry(){
        List<RecallModel> recallModels = new ArrayList<>();
        for (Map.Entry entry : payments.entrySet()){
            recallModels.add((RecallModel) entry.getValue());
        }
        return recallModels;
    }
}
