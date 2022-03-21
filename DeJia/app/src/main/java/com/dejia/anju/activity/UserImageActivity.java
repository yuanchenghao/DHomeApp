package com.dejia.anju.activity;

import android.os.Build;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.dejia.anju.R;
import com.dejia.anju.base.BaseActivity;
import com.github.chrisbanes.photoview.PhotoView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;


import butterknife.BindView;

/**
 * 文 件 名: UserImageActivity
 * 创 建 人: 原成昊
 * 创建日期: 2022/3/21 13:14
 * 邮   箱: 188897876@qq.com
 * 修改备注：
 */

public class UserImageActivity extends BaseActivity {
    @BindView(R.id.iv_person)
    PhotoView iv_person;
    @BindView(R.id.iv_close)
    ImageView iv_close;
    @BindView(R.id.rl)
    RelativeLayout rl;
    private String userImg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_img;
    }

    @Override
    protected void initView() {
        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarDarkMode(this);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rl.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        rl.setLayoutParams(layoutParams);
        //禁止双击放大
        iv_person.setOnDoubleTapListener(null);
        userImg = getIntent().getStringExtra("imgUrl");
        if (!TextUtils.isEmpty(userImg)) {
            Glide.with(mContext).load(userImg).into(iv_person);
        }
        iv_close.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mContext.finishAfterTransition();
            } else {
                mContext.finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

}
