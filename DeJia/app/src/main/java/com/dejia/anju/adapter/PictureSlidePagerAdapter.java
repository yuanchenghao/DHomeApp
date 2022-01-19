package com.dejia.anju.adapter;

import com.dejia.anju.fragment.PictureSlideFragment;
import com.dejia.anju.model.BuildingImgInfo;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PictureSlidePagerAdapter extends FragmentStatePagerAdapter {
    private List<BuildingImgInfo> urlList;

    public PictureSlidePagerAdapter(FragmentManager fm, List<BuildingImgInfo> list) {
        super(fm);
        this.urlList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return PictureSlideFragment.newInstance(urlList.get(position).getImg().getImg());
    }

    @Override
    public int getCount() {
        return urlList.size();
    }
}