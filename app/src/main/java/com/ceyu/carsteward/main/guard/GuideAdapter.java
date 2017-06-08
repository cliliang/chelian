package com.ceyu.carsteward.main.guard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class GuideAdapter extends FragmentStatePagerAdapter {

    private ArrayList<ViewPageItem> mItems;

    public GuideAdapter(FragmentManager fm, ArrayList<ViewPageItem> items) {
        super(fm);
        mItems = items;
    }

    @Override
    public Fragment getItem(int arg0) {
        return getFragment(arg0);
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mItems.get(position).mTitle;
    }

    private Fragment getFragment(int pos) {
        return GuardFragment.newInstance(mItems.get(pos).mArgs);
    }

    public static class ViewPageItem {
        public Bundle mArgs;
        public String mTitle;
    }
}
