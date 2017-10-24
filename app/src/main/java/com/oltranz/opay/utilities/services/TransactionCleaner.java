package com.oltranz.opay.utilities.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.oltranz.opay.models.payment.PaymentRequest;
import com.oltranz.opay.models.status.PaymentStatus;
import com.oltranz.opay.utilities.DataFactory;
import com.oltranz.opay.utilities.loader.PaymentStatusLoader;
import com.oltranz.opay.utilities.recall.PaymentCashe;
import com.oltranz.opay.utilities.recall.RecallModel;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class TransactionCleaner extends IntentService implements PaymentStatusLoader.OnPaymentStatus {
    public static final String POST_TRANSACTIONS = "POST_TRANSACTIONS";
    public static final String PAYMENT_BROADCAST_FILTER = "com.oltranz.opay.utilities.services.TransactionCleaner.CLEAN";
    public static final String PAYMENT_BROADCAST_ACTION = "REFRESH_PAYMENT";

    public static final String PAYMENT_STATUS = "payment_status";
    public static final String PAYMENT_DATA = "payment_data";
    private PaymentRequest paymentRequest;

    public TransactionCleaner() {
        super("TransactionCleaner");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (POST_TRANSACTIONS.equals(action)) {
                List<RecallModel> recallModels = PaymentCashe.getEntry();
                Log.d("TransactionCleaner", "Pending transaction number: " + recallModels.size());
                if (!recallModels.isEmpty()) {
                    for (RecallModel recallModel : recallModels) {
                        postTransaction(recallModel.getToken(), recallModel.getPaymentRequest());
                    }
                }
            }
        }
    }

    private void postTransaction(String token, PaymentRequest paymentRequest) {
        Log.d("TransactionCleaner", "Posting transaction token: " + token + " payment request: " + DataFactory.objectToString(paymentRequest));
        PaymentStatusLoader paymentStatusLoader = new PaymentStatusLoader(TransactionCleaner.this, TransactionCleaner.this, paymentRequest, token);
        paymentStatusLoader.startLoading();
    }

    private void sendBroadcast(PaymentRequest paymentRequest, PaymentStatus paymentStatus) {
        try {
            Intent intent = new Intent(PAYMENT_BROADCAST_FILTER);
            intent.setAction(PAYMENT_BROADCAST_FILTER);
            Bundle bundle = new Bundle();
            bundle.putString(PAYMENT_STATUS, DataFactory.objectToString(paymentStatus));
            bundle.putString(PAYMENT_DATA, DataFactory.objectToString(paymentRequest));
            intent.putExtras(bundle);
            LocalBroadcastManager.getInstance(TransactionCleaner.this).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentStatus(boolean isLoaded, Object object, PaymentRequest paymentRequest) {
        if (!isLoaded) {
            try {
                PaymentCashe.removeValue(paymentRequest.getRequestRef());
                Toast.makeText(TransactionCleaner.this, "Failed to check payment status. Go to history", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                PaymentStatus status = (PaymentStatus) object;
                if (status.getBody().getStatus().equals("100") || (!status.getBody().getStatus().equals("101") && !status.getBody().getStatus().equals("102")))
                    PaymentCashe.removeValue(paymentRequest.getRequestRef());

                sendBroadcast(paymentRequest, status);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
