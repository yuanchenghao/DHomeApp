package com.dejia.anju.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.dejia.anju.R;
import com.dejia.anju.base.BaseFragment;
import com.github.chrisbanes.photoview.PhotoView;
import com.zhangyue.we.x2c.ano.Xml;

import butterknife.BindView;

/**
 * @author ych
 * 大图浏览页面
 */
public class PictureSlideFragment extends BaseFragment {
    private String url;
    @BindView(R.id.photo_view)
    PhotoView photo_view;

    public static PictureSlideFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString("url", url);
        PictureSlideFragment fragment = new PictureSlideFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Xml(layouts = "fragment_picture")
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_picture;
    }

    @Override
    protected void initView(View view) {
        url = getArguments().getString("url");
        if (!TextUtils.isEmpty(url)) {
            Glide.with(mContext).load(url).into(photo_view);
        }
    }

    @Override
    protected void initData(View view) {

    }
}
