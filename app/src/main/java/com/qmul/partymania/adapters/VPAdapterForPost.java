package com.qmul.partymania.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qmul.partymania.fragments.ShareToHall;
import com.qmul.partymania.fragments.ShareToMoment;

/**
 * VPAdapterForPost
 * <p>
 * Created by bb on 2017/3/7.
 */

public class VPAdapterForPost extends FragmentPagerAdapter {

    private ShareToMoment shareToMoment;
    private ShareToHall shareToHall;

    private String[] tabTittle;

    public VPAdapterForPost(FragmentManager fm, String[] tabTittle) {
        super(fm);
        this.tabTittle = tabTittle;
    }


    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return shareToMoment = new ShareToMoment();
        }
        if (position == 1) {
            return shareToHall = new ShareToHall();
        }
        return shareToMoment = new ShareToMoment();
    }

    @Override
    public int getCount() {
        return tabTittle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTittle[position];
    }


    public ShareToMoment getShareToMoment() {
        return shareToMoment;
    }

    public ShareToHall getShareToHall() {
        return shareToHall;
    }
}
