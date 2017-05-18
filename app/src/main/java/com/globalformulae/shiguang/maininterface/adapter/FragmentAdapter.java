package com.globalformulae.shiguang.maininterface.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by ZXG on 2017/3/3.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    List<Fragment> mFragments;
    List<String> mTitles;
    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> lists) {
        super(fm);
        mFragments=fragments;
        mTitles=lists;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
