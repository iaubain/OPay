package com.oltranz.opay.fragments.dashboard;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.oltranz.opay.R;
import com.oltranz.opay.config.Config;
import com.oltranz.opay.models.liquidation.LiquidationRequest;
import com.oltranz.opay.models.liquidation.LiquidationResponse;
import com.oltranz.opay.models.liquidation.LiquidationWallet;
import com.oltranz.opay.models.liquidation.Origin;
import com.oltranz.opay.models.merchant.MerchantDetails;
import com.oltranz.opay.models.user.UserDetails;
import com.oltranz.opay.models.wallet.Balance;
import com.oltranz.opay.models.wallet.Wallet;
import com.oltranz.opay.utilities.DataFactory;
import com.oltranz.opay.utilities.keys.KeyGen;
import com.oltranz.opay.utilities.loader.BalanceLoader;
import com.oltranz.opay.utilities.loader.LiquidationLoader;
import com.oltranz.opay.utilities.loader.WalletLoader;
import com.oltranz.opay.utilities.views.Label;
import com.oltranz.opay.utilities.views.MButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLiquidate} interface
 * to handle interaction events.
 * Use the {@link Liquidate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Liquidate extends Fragment implements BalanceLoader.OnBalanceLoader,
        LiquidationLoader.OnLiquidation {
    private static final String ARG_TOKEN = "token";
    private static final String ARG_MERCHANT = "merchant";
    private static final String ARG_USER = "user";
    private static final String ARG_WALLET = "wallet";

    private String token;
    private MerchantDetails merchantDetails;
    private UserDetails userDetails;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private ProgressDialog progressDialog;

    @BindView(R.id.amount)
    Label amount;
    @BindView(R.id.collected)
    Label collected;
    @BindView(R.id.commission)
    Label commission;
    @BindView(R.id.liquidateButton)
    LinearLayout liquidateButton;

    private Wallet wallet;
    private Balance balance;

    private OnLiquidate mListener;

    public Liquidate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Liquidate.
     */
    public static Liquidate newInstance(String token, String merchant, String user, String wallet) {
        Liquidate fragment = new Liquidate();
        Bundle args = new Bundle();
        args.putString(ARG_TOKEN, token);
        args.putString(ARG_MERCHANT, merchant);
        args.putString(ARG_USER, user);
        args.putString(ARG_WALLET, wallet);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            token = getArguments().getString(ARG_TOKEN);
            merchantDetails = (MerchantDetails) DataFactory.stringToObject(MerchantDetails.class, getArguments().getString(ARG_MERCHANT));
            userDetails = (UserDetails) DataFactory.stringToObject(UserDetails.class, getArguments().getString(ARG_USER));
            wallet = (Wallet) DataFactory.stringToObject(Wallet.class, getArguments().getString(ARG_WALLET));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dash_liquidate, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        mProgress("Loading wallet balance");
        BalanceLoader balanceLoader = new BalanceLoader(token, wallet.getId(), Liquidate.this);
        balanceLoader.startLoading();

        liquidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //liquidate popup
                try{
                    if(Double.valueOf(balance.getDueAmount()) > 0){
                        confirmBox();
                    }else{
                        Toast.makeText(getContext(), "Insufficient funds.", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Liquidation, internal application error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLiquidate) {
            mListener = (OnLiquidate) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void refreshFragment(){
        BalanceLoader balanceLoader = new BalanceLoader(token, wallet.getId(), Liquidate.this);
        balanceLoader.startLoading();
    }

    private void refreshBalance(String title, String message){
        try {
            builder = new AlertDialog.Builder(getContext(), R.style.SimpleBlackDialog);
            builder.setMessage(message != null ? message : "NO_MESSAGE")
                    .setTitle(title);
            builder.setCancelable(false);
            // Add the buttons
            builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mProgress("Loading wallet balance");
                    BalanceLoader balanceLoader = new BalanceLoader(token, wallet.getId(), Liquidate.this);
                    balanceLoader.startLoading();
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), message != null ? message : "NO_MESSAGE", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshLiquidation(String title, String message){
        try {
            builder = new AlertDialog.Builder(getContext(), R.style.SimpleBlackDialog);
            builder.setMessage(message != null ? message : "NO_MESSAGE")
                    .setTitle(title);
            // Add the buttons
            builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mProgress("Requesting liquidation");
                    Origin origin = new Origin(Config.APP_ID, KeyGen.genId());
                    LiquidationWallet liquidationWallet = new LiquidationWallet(balance.getDueAmount(),"", merchantDetails.getId());
                    LiquidationRequest request = new LiquidationRequest(origin,liquidationWallet, userDetails.getUser().getId());

                    LiquidationLoader liquidationLoader = new LiquidationLoader(token, request,Liquidate.this);
                    liquidationLoader.startLoading();
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), message != null ? message : "NO_MESSAGE", Toast.LENGTH_SHORT).show();
        }
    }

    private void popUp(String title, String message){
        try {
            builder = new AlertDialog.Builder(getContext(), R.style.SimpleBlackDialog);
            builder.setMessage(message != null ? message : "NO_MESSAGE")
                    .setTitle(title);
            // Add the buttons
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            dialog = builder.create();
            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), message != null ? message : "NO_MESSAGE", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmBox(){
        try {
            final Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.confirm_box);

            final Label boxTitle = (Label) dialog.findViewById(R.id.dialogTitle);
            ImageView close = (ImageView) dialog.findViewById(R.id.icClose);
            MButton cancel = (MButton) dialog.findViewById(R.id.cancel);
            MButton ok = (MButton) dialog.findViewById(R.id.ok);
            LinearLayout boxContent = (LinearLayout) dialog.findViewById(R.id.dialogContent);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            boxTitle.setText("CONFIRMATION");

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //proceed with liquidation
                    mProgress("Requesting liquidation");
                    Origin origin = new Origin(Config.APP_ID, KeyGen.genUuId());
                    LiquidationWallet liquidationWallet = new LiquidationWallet(balance.getDueAmount(),"", merchantDetails.getId());
                    LiquidationRequest request = new LiquidationRequest(origin,liquidationWallet, userDetails.getUser().getId());

                    LiquidationLoader liquidationLoader = new LiquidationLoader(token, request,Liquidate.this);
                    liquidationLoader.startLoading();

                    dialog.dismiss();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            //setting box content

            Label lbl = new Label(getContext());
            lbl.setText("CLIENT'S ADDRESS");
            lbl.setTextSize(18);
            lbl.setGravity(Gravity.CENTER);
            lbl.setId(View.generateViewId());
            lbl.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            boxContent.addView(lbl);

            Label lblValue = new Label(getContext());
            lblValue.setText(merchantDetails.getAddress()+"\n\n");
            lblValue.setTextSize(27);
            lblValue.setGravity(Gravity.CENTER);
            lblValue.setTextColor(ContextCompat.getColor(getContext(),R.color.colorBlack));
            lblValue.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            lblValue.setId(View.generateViewId());
            lblValue.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            boxContent.addView(lblValue);

            lbl = new Label(getContext());
            lbl.setText("AMOUNT TO LIQUIDATE");
            lbl.setTextSize(18);
            lbl.setGravity(Gravity.CENTER);
            lbl.setId(View.generateViewId());
            lbl.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            boxContent.addView(lbl);

            lblValue = new Label(getContext());
            lblValue.setText(balance.getDueAmount()+"\n\n");
            lblValue.setTextSize(27);
            lblValue.setGravity(Gravity.CENTER);
            lblValue.setTextColor(ContextCompat.getColor(getContext(),R.color.colorBlack));
            lblValue.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            lblValue.setId(View.generateViewId());
            lblValue.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            boxContent.addView(lblValue);

            dialog.show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void mProgress(String message){
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();

        progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(message != null ? message : "NO_MESSAGE");
        progressDialog.show();
    }

    private void dismissProgress(){
        if (progressDialog != null)
            if (progressDialog.isShowing())
                progressDialog.dismiss();
    }

    @Override
    public void onBalance(boolean isLoaded, Object object) {
        dismissProgress();
        if(!isLoaded){
            refreshBalance("Alert", "Failed to load wallet balance due to:"+object);
        }else{
            balance = (Balance) object;
            amount.setText(balance.getDueAmount().isEmpty() ? "0.00" : balance.getDueAmount());
            collected.setText(balance.getTotalAmount().isEmpty() ? "0.00 Rwf" : balance.getTotalAmount()+" Rwf");
            commission.setText(balance.getCommission().isEmpty() ? "0.00 Rwf" : balance.getCommission()+" Rwf");
        }
    }

    @Override
    public void onLiquidation(boolean isLoaded, Object object) {
        dismissProgress();
        if(!isLoaded){
            refreshLiquidation("Alert", "Liquidation request failed due to:"+object);
        }else{
            LiquidationResponse response = (LiquidationResponse) object;
            popUp("Alert", "("+response.getCode()+") "+response.getDescription());
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnLiquidate {

        void onLiquidate(boolean isLiquidate, Object object);
    }
}
