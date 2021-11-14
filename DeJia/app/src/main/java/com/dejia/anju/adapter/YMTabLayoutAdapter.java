package com.dejia.anju.adapter;

import android.os.Parcelable;
import android.view.ViewGroup;

import com.dejia.anju.base.BaseFragment;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class YMTabLayoutAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "YMTabLayoutAdapter";
    private List<String> titleList;
    private List<BaseFragment> fragmentList;

    public YMTabLayoutAdapter(FragmentManager fm, List<String> mPageTitleList, List<BaseFragment> mFragmentList) {
        super(fm);
        this.titleList = mPageTitleList;
        this.fragmentList = mFragmentList;
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }


}
