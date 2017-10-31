package com.oltranz.opay.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.oltranz.opay.R;
import com.oltranz.opay.models.history.HistoryLiquidation;
import com.oltranz.opay.models.merchant.MerchantDetails;
import com.oltranz.opay.models.user.UserDetails;
import com.oltranz.opay.models.wallet.Wallet;
import com.oltranz.opay.utilities.DataFactory;
import com.oltranz.opay.utilities.adapters.LiquidationAdapter;
import com.oltranz.opay.utilities.loader.HistoryLiquidationLoader;
import com.oltranz.opay.utilities.views.Label;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLiquidationHistoryFragment} interface
 * to handle interaction events.
 * Use the {@link HistoryLiquidationFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryLiquidationFrag extends Fragment implements HistoryLiquidationLoader.OnLiquidationHistory,
        LiquidationAdapter.OnLiquidationHistoryAdapter {
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
    private DatePickerDialog datePickerDialog;

    private List<HistoryLiquidation> mHistory = new ArrayList<>();
    private LiquidationAdapter adapter;
    private HistoryLiquidationLoader loader;
    private String startDate;
    private String endDate;
    private boolean isTo = false;
    private Calendar tempFrom;
    private Calendar tempTo;

    @BindView(R.id.swipeHolder)
    SwipeRefreshLayout swipe;
    @BindView(R.id.historyHolder)
    RecyclerView mHistoryHolder;
    @BindView(R.id.from)
    Label from;
    @BindView(R.id.to)
    Label to;

    private OnLiquidationHistoryFragment mListener;

    public HistoryLiquidationFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HistoryLiquidationFrag.
     */
    public static HistoryLiquidationFrag newInstance(String token, String merchant, String user, String wallet) {
        HistoryLiquidationFrag fragment = new HistoryLiquidationFrag();
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
        return inflater.inflate(R.layout.history_liquidation, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if(wallet == null){
            mListener.onLiquidationHistoryFragment(false, "Wallet data are empty");
            return;
        }
        mProgress("Loading history");
        Date date = new Date();
        tempFrom = new GregorianCalendar();
        tempFrom.setTimeInMillis(date.getTime());
        tempTo = new GregorianCalendar();
        tempTo.setTimeInMillis(date.getTime());
        try {
            startDate = DataFactory.formatDate(date);
            endDate = DataFactory.formatDate(date);
        } catch (Exception e) {
            e.printStackTrace();
            startDate = date.toString();
            endDate = date.toString();
        }

        from.setText(startDate);
        to.setText(endDate);
        loader = new HistoryLiquidationLoader(token, wallet.getId(), startDate, endDate, HistoryLiquidationFrag.this);
        loader.startLoading();

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Date date = new Date();
                if(tempFrom == null || tempTo == null){
                    try {
                        startDate = DataFactory.formatDate(date);
                        endDate = DataFactory.formatDate(date);
                    } catch (Exception e) {
                        e.printStackTrace();
                        startDate = date.toString();
                        endDate = date.toString();
                    }
                }else{
                    try {
                        startDate = DataFactory.formatDate(tempFrom.getTime());
                        endDate = DataFactory.formatDate(tempTo.getTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                        startDate = date.toString();
                        endDate = date.toString();
                    }
                }


                from.setText(startDate);
                to.setText(endDate);
                if (loader == null)
                    loader = new HistoryLiquidationLoader(token, wallet.getId(), startDate, endDate, HistoryLiquidationFrag.this);

                loader.startLoading();
            }
        });

        final Calendar calendar = Calendar.getInstance();
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                tempFrom = new GregorianCalendar(year, month, dayOfMonth);
                                try{
                                    from.setText(DataFactory.formatDate(tempFrom.getTime()));
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                datePickerDialog.dismiss();
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                tempTo = new GregorianCalendar(year, month, dayOfMonth);
                                try{
                                    to.setText(DataFactory.formatDate(tempTo.getTime()));
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                try {
                                    if(tempFrom.getTimeInMillis() <= tempTo.getTimeInMillis()){
                                        mProgress("Loading history");
                                        loader = new HistoryLiquidationLoader(token,
                                                wallet.getId(),
                                                DataFactory.formatDate(tempFrom.getTime()),
                                                DataFactory.formatDate(tempTo.getTime()),
                                                HistoryLiquidationFrag.this);
                                        loader.startLoading();
                                    }else{
                                        Toast.makeText(getContext(), "Start date is higher than end date", Toast.LENGTH_SHORT).show();
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                    Toast.makeText(getContext(), "Error setting date", Toast.LENGTH_SHORT).show();
                                }

                                datePickerDialog.dismiss();
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLiquidationHistoryFragment) {
            mListener = (OnLiquidationHistoryFragment) context;
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
    public void onLiquidationHistoryAdapter(boolean isInteraction, HistoryLiquidation historyLiquidation) {

    }

    @Override
    public void onLiquidationHistory(boolean isLoaded, Object object, List<HistoryLiquidation> providerValues) {
        dismissProgress();
        if(swipe != null && swipe.isRefreshing())
            swipe.setRefreshing(false);
        if (!isLoaded) {
            Toast.makeText(getContext(), "Pull down to refresh. Error: " + object, Toast.LENGTH_SHORT).show();
        } else {
            if (adapter != null)
                adapter.refreshAdapter(providerValues);
            else {
                adapter = new LiquidationAdapter(getContext(), providerValues, HistoryLiquidationFrag.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                mHistoryHolder.setLayoutManager(mLayoutManager);
                mHistoryHolder.setHasFixedSize(true);
                mHistoryHolder.setItemAnimator(new DefaultItemAnimator());
                mHistoryHolder.setAdapter(adapter);
            }
        }
    }

    public void filter(String charSequence){
        if(adapter != null && adapter.getItemCount() > 1){
            adapter.filter(charSequence);
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
    public interface OnLiquidationHistoryFragment {
        void onLiquidationHistoryFragment(boolean isLoaded, Object object);
    }
}
