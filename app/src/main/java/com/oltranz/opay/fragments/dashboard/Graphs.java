package com.oltranz.opay.fragments.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.oltranz.opay.R;
import com.oltranz.opay.models.merchant.MerchantDetails;
import com.oltranz.opay.models.serviceprovider.ProviderValue;
import com.oltranz.opay.models.user.UserDetails;
import com.oltranz.opay.models.wallet.Wallet;
import com.oltranz.opay.utilities.AppFont;
import com.oltranz.opay.utilities.DataFactory;
import com.oltranz.opay.utilities.loader.BalanceLoader;
import com.oltranz.opay.utilities.loader.ProviderPaymentLoader;
import com.oltranz.opay.utilities.views.MyMarkerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnGraphs} interface
 * to handle interaction events.
 * Use the {@link Graphs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Graphs extends Fragment implements OnChartValueSelectedListener,
        ProviderPaymentLoader.OnProvider {
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

    private List<ProviderValue> providerValues = new ArrayList<>();

    private OnGraphs mListener;

    @BindView(R.id.chart1)
    BarChart mChart;

    public Graphs() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Liquidate.
     */
    public static Graphs newInstance(String token, String merchant, String user, String wallet) {
        Graphs fragment = new Graphs();
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
        return inflater.inflate(R.layout.dash_graph, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mChart.setOnChartValueSelectedListener(this);
        mChart.getDescription().setEnabled(false);

//        mChart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawBarShadow(false);

        mChart.setDrawGridBackground(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setTypeface(AppFont.provide(getContext()));
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTypeface(AppFont.provide(getContext()));
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "Amount in RWF";
            }
        });

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(AppFont.provide(getContext()));
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setCenterAxisLabels(true);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        mChart.getAxisRight().setEnabled(false);

        mProgress("Loading charts");
        ProviderPaymentLoader providerPaymentLoader = new ProviderPaymentLoader(token, wallet.getId(), Graphs.this);
        providerPaymentLoader.startLoading();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGraphs) {
            mListener = (OnGraphs) context;
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
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

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

    public void setGraphData() {
        float groupSpace = 0.08f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.2f; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"


        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        Random rand = new Random();
        int n = rand.nextInt(20) + 1;
        float randomMultiplier = n * 100000f;
        for (int i = 0; i < 20; i++) {
            yVals1.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
            yVals2.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
            yVals3.add(new BarEntry(i, (float) (Math.random() * randomMultiplier)));
        }

        BarDataSet set1, set2, set3;

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) mChart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) mChart.getData().getDataSetByIndex(2);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();

        } else {
            // create 4 DataSets
            set1 = new BarDataSet(yVals1, "MTN");
            set1.setColor(Color.rgb(255, 197, 8));
            set2 = new BarDataSet(yVals2, "TIGO");
            set2.setColor(Color.rgb(25, 51, 112));
            set3 = new BarDataSet(yVals3, "AIRTEL");
            set3.setColor(Color.rgb(236, 31, 39));

            BarData data = new BarData(set1, set2, set3);
            data.setValueFormatter(new LargeValueFormatter());
            data.setValueTypeface(AppFont.provide(getContext()));

            mChart.setData(data);
        }

        // specify the width each bar should have
        mChart.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        mChart.getXAxis().setAxisMinimum(0);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        mChart.getXAxis().setAxisMaximum(0 + mChart.getBarData().getGroupWidth(groupSpace, barSpace));
        mChart.groupBars(0, groupSpace, barSpace);
        mChart.invalidate();
    }


    public void setDynamicGraph(List<ProviderValue> providerValues) {
        float groupSpace = 0.08f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.2f; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        /*
        YourData[] dataObjects = ...;

List<Entry> entries = new ArrayList<Entry>();

for (YourData data : dataObjects) {

    // turn your data into Entry objects
    entries.add(new Entry(data.getValueX(), data.getValueY()));
}
         */

        int providerSize = providerValues.size();
//        List<BarEntry> entries = new ArrayList<>();
//        for(ProviderValue providerValue : providerValues){
//            int i = 0;
//            entries.add(new BarEntry(i, Float.parseFloat(providerValue.getValue())));
//            i++;
//        }

        HashMap<String, ArrayList<BarEntry>> vals = new HashMap<>();
        for (ProviderValue providerValue : providerValues) {
            int i = 0;
            ArrayList<BarEntry> val = new ArrayList<>();
            val.add(new BarEntry(i, Float.parseFloat(providerValue.getValue())));
            vals.put(providerValue.getName(), val);
            i++;
        }

        List<BarDataSet> sets = new ArrayList<>();
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            for (ProviderValue value : providerValues) {
                int i = 0;
                BarDataSet dataSet = (BarDataSet) mChart.getData().getDataSetByIndex(i);
                dataSet.setValues(vals.get(value.getName()));
                i++;
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            }
        } else {
            BarData data = new BarData();
            for (ProviderValue value : providerValues) {
                BarDataSet barDataSet = new BarDataSet(vals.get(value.getName()), value.getName());
                if (value.getName().toUpperCase().contains("MTN")) {
                    barDataSet.setColor(Color.rgb(255, 197, 8));
                } else if (value.getName().toUpperCase().contains("TIGO")) {
                    barDataSet.setColor(Color.rgb(25, 51, 112));
                } else if (value.getName().toUpperCase().contains("AIRTEL")) {
                    barDataSet.setColor(Color.rgb(236, 31, 39));
                } else {
                    barDataSet.setColor(getRandomColor());
                }
                data.addDataSet(barDataSet);
            }
            data.setValueFormatter(new LargeValueFormatter());
            data.setValueTypeface(AppFont.provide(getContext()));

            mChart.setData(data);
        }

        // specify the width each bar should have
        mChart.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        mChart.getXAxis().setAxisMinimum(0);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        mChart.getXAxis().setAxisMaximum(0 + mChart.getBarData().getGroupWidth(groupSpace, barSpace));
        if (providerSize > 1)
            mChart.groupBars(0, groupSpace, barSpace);
        mChart.invalidate();
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public void refreshFragment(){
        ProviderPaymentLoader providerPaymentLoader = new ProviderPaymentLoader(token, wallet.getId(), Graphs.this);
        providerPaymentLoader.startLoading();
    }

    @Override
    public void onProvider(boolean isLoaded, Object object, List<ProviderValue> providerValues) {
        dismissProgress();
        if (!isLoaded) {
            Toast.makeText(getContext(), object + "", Toast.LENGTH_SHORT).show();
            return;
        }
        this.providerValues = providerValues;
        //setGraphData();
        setDynamicGraph(providerValues);
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
    public interface OnGraphs {
        void onGraphs(boolean isLiquidate, Object object);
    }
}
