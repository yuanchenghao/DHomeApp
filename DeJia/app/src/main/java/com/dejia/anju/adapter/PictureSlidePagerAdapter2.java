package com.dejia.anju.adapter;

import com.dejia.anju.fragment.PictureSlideFragment;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PictureSlidePagerAdapter2 extends FragmentStatePagerAdapter {
    private List<String> urlList;

    public PictureSlidePagerAdapter2(FragmentManager fm, List<String> list) {
        super(fm);
        this.urlList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return PictureSlideFragment.newInstance(urlList.get(position));
    }

    @Override
    public int getCount() {
        return urlList.size();
    }
}