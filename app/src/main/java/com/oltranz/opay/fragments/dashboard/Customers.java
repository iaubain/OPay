package com.oltranz.opay.fragments.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oltranz.opay.R;
import com.oltranz.opay.models.customers.CustomerResponse;
import com.oltranz.opay.models.merchant.MerchantDetails;
import com.oltranz.opay.models.user.UserDetails;
import com.oltranz.opay.models.wallet.Wallet;
import com.oltranz.opay.utilities.DataFactory;
import com.oltranz.opay.utilities.loader.CustomerLoader;
import com.oltranz.opay.utilities.loader.ProviderPaymentLoader;
import com.oltranz.opay.utilities.views.Label;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnCustomer} interface
 * to handle interaction events.
 * Use the {@link Customers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Customers extends Fragment implements CustomerLoader.OnCustomerLoader {
    private static final String ARG_TOKEN = "token";
    private static final String ARG_MERCHANT = "merchant";
    private static final String ARG_USER = "user";
    private static final String ARG_WALLET = "wallet";

    private String token;
    private MerchantDetails merchantDetails;
    private UserDetails userDetails;
    private Wallet wallet;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private ProgressDialog progressDialog;

    @BindView(R.id.counter)
    Label counter;

    private OnCustomer mListener;

    public Customers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Users.
     */
    public static Customers newInstance(String token, String merchant, String user, String wallet) {
        Customers fragment = new Customers();
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
        return inflater.inflate(R.layout.dash_customers, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        mProgress("Loading customers");
        CustomerLoader customerLoader = new CustomerLoader(wallet.getId(),Customers.this);
        customerLoader.startLoading();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCustomer) {
            mListener = (OnCustomer) context;
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
        CustomerLoader customerLoader = new CustomerLoader(wallet.getId(),Customers.this);
        customerLoader.startLoading();
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

    @Override
    public void onCustomerLoader(boolean isLoaded, Object object) {
        dismissProgress();
        if(!isLoaded)
            counter.setText("~0");
        CustomerResponse customerResponse = (CustomerResponse) object;
        counter.setText(customerResponse.getTotal());
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
    public interface OnCustomer {
        void onCustomer(Uri uri);
    }
}
