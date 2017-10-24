package com.oltranz.opay.utilities.dashboard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.oltranz.opay.fragments.dashboard.Customers;
import com.oltranz.opay.fragments.dashboard.Graphs;
import com.oltranz.opay.fragments.dashboard.Liquidate;

import java.util.List;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/4/2017.
 */

public class DashAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public DashAdapter(FragmentManager fm, List<Fragment> mFragments) {
        super(fm);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.mFragments.get(position);
    }

    @Override
    public int getCount() {
        return this.mFragments.size();
    }

    public int mPostion (Fragment fragment){
        return this.mFragments.indexOf(fragment);
    }

    public void refreshFragment(Fragment fragment){
        for(Fragment fr : mFragments){
            if(fr.getClass().isInstance(fragment)){
                int position = mFragments.indexOf(fr);
                mFragments.remove(position);
                mFragments.add(position, fragment);
            }
        }
        notifyDataSetChanged();
    }

    public List<Fragment> currentConatainedFragment(){
        return this.mFragments;
    }

    public void refreshDashboard(){
        for(Fragment fragment : mFragments){
            if(fragment instanceof Liquidate){
                Liquidate liquidate = (Liquidate) fragment;
                liquidate.refreshFragment();
            }else if(fragment instanceof Graphs){
                Graphs graphs = (Graphs) fragment;
                graphs.refreshFragment();
            }else if(fragment instanceof Customers){
                Customers customers = (Customers) fragment;
                customers.refreshFragment();
            }
        }
        notifyDataSetChanged();
    }
    public Fragment getLiquidation(){
        for(Fragment fragment : mFragments){
            if(fragment instanceof Liquidate){
                return fragment;
            }
        }
        return null;
    }
}
