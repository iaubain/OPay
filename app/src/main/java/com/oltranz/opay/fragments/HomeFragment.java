package com.oltranz.opay.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.oltranz.opay.R;
import com.oltranz.opay.config.Config;
import com.oltranz.opay.fragments.dashboard.Customers;
import com.oltranz.opay.fragments.dashboard.Graphs;
import com.oltranz.opay.fragments.dashboard.Liquidate;
import com.oltranz.opay.models.merchant.MerchantDetails;
import com.oltranz.opay.models.payment.PaymentRequest;
import com.oltranz.opay.models.payment.PaymentResponse;
import com.oltranz.opay.models.user.UserDetails;
import com.oltranz.opay.models.wallet.Wallet;
import com.oltranz.opay.utilities.DataFactory;
import com.oltranz.opay.utilities.dashboard.DashAdapter;
import com.oltranz.opay.utilities.keys.KeyGen;
import com.oltranz.opay.utilities.loader.PaymentLoader;
import com.oltranz.opay.utilities.loader.WalletLoader;
import com.oltranz.opay.utilities.recall.PaymentCashe;
import com.oltranz.opay.utilities.views.EditHelper;
import com.oltranz.opay.utilities.views.Label;
import com.oltranz.opay.utilities.views.MButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnHomeFragment} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements
        Liquidate.OnLiquidate,
        Graphs.OnGraphs,
        Customers.OnCustomer,
        PaymentLoader.OnPaymentRequest,
        WalletLoader.OnWalletLoader{
    private static final String TOKEN_PARAM = "token";
    private static final String MERCHANT_PARAM = "merchant";
    private static final String USER_PARAM = "user";

    private String token;
    private String merchant;
    private String user;

    private MerchantDetails merchantDetails;
    private UserDetails userDetails;
    private Wallet wallet;

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.paymentButton)
    CardView payButton;
    @BindView(R.id.pageIndicator)
    TabLayout pageIndicator;
    private DashAdapter dashAdapter;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private ProgressDialog progressDialog;
    private Dialog mDialog;
    private PaymentRequest paymentRequest;

    private OnHomeFragment mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(String token, String merchant, String user) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(TOKEN_PARAM, token);
        args.putString(MERCHANT_PARAM, merchant);
        args.putString(USER_PARAM, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            token = getArguments().getString(TOKEN_PARAM);
            merchant = getArguments().getString(MERCHANT_PARAM);
            user = getArguments().getString(USER_PARAM);
            merchantDetails = (MerchantDetails) DataFactory.stringToObject(MerchantDetails.class, getArguments().getString(MERCHANT_PARAM));
            userDetails = (UserDetails) DataFactory.stringToObject(UserDetails.class, getArguments().getString(USER_PARAM));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mProgress("Loading wallet information");
        WalletLoader walletLoader = new WalletLoader(token, merchantDetails.getId(), HomeFragment.this);
        walletLoader.startLoading();

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentBox(v);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeFragment) {
            mListener = (OnHomeFragment) context;
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

    @Override
    public void onLiquidate(boolean isLiquidate, Object object) {

    }

    @Override
    public void onGraphs(boolean isLiquidate, Object object) {

    }

    @Override
    public void onCustomer(Uri uri) {

    }

    private void refreshWallet(String title, String message){
        try {
            builder = new AlertDialog.Builder(getContext(), R.style.SimpleBlackDialog);
            builder.setMessage(message != null ? message : "NO_MESSAGE")
                    .setTitle(title);
            builder.setCancelable(false);
            // Add the buttons
            builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mProgress("Loading wallet information");
                    WalletLoader walletLoader = new WalletLoader(token, merchantDetails.getId(), HomeFragment.this);
                    walletLoader.startLoading();
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

    private List<Fragment> getDashboardFragment() {
        List<Fragment> fList = new ArrayList<Fragment>();
        fList.add(Liquidate.newInstance(token, merchant, user, DataFactory.objectToString(wallet)));
        fList.add(Graphs.newInstance(token, merchant, user, DataFactory.objectToString(wallet)));
        fList.add(Customers.newInstance(token, merchant, user, DataFactory.objectToString(wallet)));
        return fList;
    }

    private void paymentBox(View v) {
        if (v.getId() != R.id.paymentButton)
            return;
        try {
            if (mDialog != null && mDialog.isShowing())
                mDialog.dismiss();
            mDialog = new Dialog(getContext());
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.setContentView(R.layout.payment);

            final EditHelper phone = (EditHelper) mDialog.findViewById(R.id.phone);
            final EditHelper amount = (EditHelper) mDialog.findViewById(R.id.amount);
            MButton cancel = (MButton) mDialog.findViewById(R.id.cancel);
            MButton ok = (MButton) mDialog.findViewById(R.id.ok);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDialog != null && mDialog.isShowing())
                        mDialog.dismiss();
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDialog != null && mDialog.isShowing()) {
                        if (TextUtils.isEmpty(phone.getText().toString()) || !DataFactory.isValidPhone(phone.getText().toString())) {
                            phone.requestFocus();
                            phone.setError("Invalid number");
                        } else if (TextUtils.isEmpty(amount.getText().toString())) {
                            amount.requestFocus();
                            amount.setError("Invalid amount");
                        } else {
                            //proceed with request payment
                            try{
                                String phoneNumber = DataFactory.formatPhone(phone.getText().toString(), getContext());
                                if(phoneNumber != null && !phoneNumber.isEmpty()){
                                    phone.setText(phoneNumber);
                                }else{
                                    phone.setError("Invalid phone");
                                    return;
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                phone.setError("Invalid number");
                                return;
                            }

                            try {
                                PaymentRequest paymentRequest = new PaymentRequest();
                                paymentRequest.setPayingAccountId(phone.getText().toString());
                                paymentRequest.setAmount(amount.getText().toString());
                                paymentRequest.setRequestRef(KeyGen.genId());
                                paymentMode(paymentRequest);
                                mDialog.dismiss();
                            } catch (Exception e) {
                                phone.setError(e.getMessage());
                            }
                        }
                    }
                }
            });
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Application internal error", Toast.LENGTH_SHORT).show();
        }
    }

    private void paymentMode(final PaymentRequest paymentRequest) {
        try {
            final Dialog dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.payment_mode);

            final Label boxTitle = (Label) dialog.findViewById(R.id.dialogTitle);
            ImageView close = (ImageView) dialog.findViewById(R.id.icClose);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            boxTitle.setText("Payment Methods");


            ImageButton mtn = (ImageButton) dialog.findViewById(R.id.mtn);
            ImageButton tigo = (ImageButton) dialog.findViewById(R.id.tigo);
            ImageButton airtel = (ImageButton) dialog.findViewById(R.id.airtel);

            mtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paymentRequest.setPayingSpId(Config.MTN);
                    requestPayment(paymentRequest);
                    dialog.dismiss();
                }
            });

            tigo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paymentRequest.setPayingSpId(Config.TIGO);
                    requestPayment(paymentRequest);
                    dialog.dismiss();
                }
            });

            airtel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    paymentRequest.setPayingSpId(Config.AIRTEL);
                    requestPayment(paymentRequest);
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Application internal error", Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshDashBoard() {
        dashAdapter.refreshDashboard();

        for (int i = 0; i < pageIndicator.getTabCount() - 1; i++) {
            View tab = ((ViewGroup) pageIndicator.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(0, 0, 30, 0);
            tab.requestLayout();
        }
    }

    private void requestPayment(PaymentRequest paymentRequest) {
        PaymentCashe.addValue(token, paymentRequest);
        this.paymentRequest = paymentRequest;
        mProgress("Requesting payment");
        PaymentLoader paymentLoader = new PaymentLoader(HomeFragment.this, getContext(), paymentRequest, token);
        paymentLoader.startLoading();
    }

    private void customBox(String title, String message) {
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

    private void mProgress(String message) {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();

        progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.setMessage(message != null ? message : "NO_MESSAGE");
        progressDialog.show();
    }

    private void dismissProgress() {
        if (progressDialog != null)
            if (progressDialog.isShowing())
                progressDialog.dismiss();
    }

    private void updateMessage(String message) {
        if (progressDialog != null)
            if (progressDialog.isShowing())
                progressDialog.setMessage(message != null ? message : "NO_MESSAGE");
    }

    @Override
    public void onPaymentRequest(boolean isLoaded, Object object) {
        dismissProgress();
        if (!isLoaded) {
            customBox("Alert", "Failed to request the payment due to:" + object);
            PaymentCashe.removeValue(paymentRequest.getRequestRef());
        } else {
            PaymentResponse paymentResponse = (PaymentResponse) object;
            customBox("Alert", "(" + paymentResponse.getCode() + ")" + paymentResponse.getDescription());
        }
    }

    @Override
    public void onWalletLoader(boolean isLoaded, Object object) {
        dismissProgress();
        if(!isLoaded){
            refreshWallet("Alert", "Getting wallet information failed due to:"+object);
        }else{
            wallet = (Wallet) object;
            mListener.onHomeWalletData(wallet);
            List<Fragment> fragments = getDashboardFragment();
            dashAdapter = new DashAdapter(getChildFragmentManager(), fragments);
            viewPager.setAdapter(dashAdapter);
            pageIndicator.setupWithViewPager(viewPager, true);
            viewPager.setOffscreenPageLimit(2);
            viewPager.setPadding(60, 0, 60, 0);
            viewPager.setClipToPadding(false);
            viewPager.setPageMargin(30);

            for (int i = 0; i < pageIndicator.getTabCount() - 1; i++) {
                View tab = ((ViewGroup) pageIndicator.getChildAt(0)).getChildAt(i);
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
                p.setMargins(0, 0, 30, 0);
                tab.requestLayout();
            }
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
    public interface OnHomeFragment {
        void onHomeFragment(boolean isGoTo, Object goTo, String message);
        void onHomeWalletData(Wallet wallet);
    }
}
