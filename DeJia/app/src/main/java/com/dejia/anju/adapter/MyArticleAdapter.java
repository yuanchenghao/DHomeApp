package com.dejia.anju.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.dejia.anju.R;
import com.dejia.anju.api.ZanApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.mannger.WebUrlJumpManager;
import com.dejia.anju.model.MyArticleInfo;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.SpanUtil;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.utils.Util;
import com.dejia.anju.view.YMGridLayoutManager;
import com.dejia.anju.view.YMLinearLayoutManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_TYPE_ONE = 1;       //长图文无图类型
    private final int ITEM_TYPE_TOW = 2;       //长图文一张图类型
    private final int ITEM_TYPE_THTEE = 3;     //长图文多张图类型（2张之上）
    private final int ITEM_TYPE_FOUR = 4;      //短图文类型
    private LayoutInflater mInflater;
    private Activity mContext;
    private List<MyArticleInfo> mDatas;

    public MyArticleAdapter(Activity context, List<MyArticleInfo> datas) {
        this.mContext = context;
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        //区分类型
        if (mDatas.get(position).getArticle_type() == 1) {
            //长图文
            if (mDatas.get(position).getImg() == null || mDatas.get(position).getImg().size() == 0) {
                //无图类型
                return ITEM_TYPE_ONE;
            } else if (mDatas.get(position).getImg() != null && mDatas.get(position).getImg().size() == 1) {
                //一张图类型
                return ITEM_TYPE_TOW;
            } else {
                //多图类型
                return ITEM_TYPE_THTEE;
            }
        } else {
            //短图文类型
            return ITEM_TYPE_FOUR;
        }
    }


    @Override
    public int getItemCount() {
        int size = 0;
        if (mDatas != null) {
            size = mDatas.size();
        }
        return size;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_ONE:
                //长图文无图类型
                return new Type1ViewHolder(mInflater.inflate(R.layout.item_home_type1, parent, false));
            case ITEM_TYPE_TOW:
                //长图文一张图类型
                return new Type2ViewHolder(mInflater.inflate(R.layout.item_home_type2, parent, false));
            case ITEM_TYPE_THTEE:
                //长图文多张图类型（2张之上）
                return new Type3ViewHolder(mInflater.inflate(R.layout.item_home_type3, parent, false));
            default:
                //短图文类型
                return new Type4ViewHolder(mInflater.inflate(R.layout.item_home_type3, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Type1ViewHolder) {
            setType1View((Type1ViewHolder) holder, mDatas, position);
        } else if (holder instanceof Type2ViewHolder) {
            setType2View((Type2ViewHolder) holder, mDatas, position);
        } else if (holder instanceof Type3ViewHolder) {
            setType3View((Type3ViewHolder) holder, mDatas, position);
        } else {
            setType4View((Type4ViewHolder) holder, mDatas, position);
        }
    }

    private void setType1View(Type1ViewHolder type1View, List<MyArticleInfo> mDatas, int position) {
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getUser_img())) {
            type1View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri(mDatas.get(position).getUser_data().getUser_img()).setAutoPlayAnimations(true).build());
        } else {
            type1View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getNickname())) {
            type1View.tv_name.setText(Util.toDBC(mDatas.get(position).getUser_data().getNickname()));
        } else {
            type1View.tv_name.setText("");
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getTime_set())) {
            type1View.tv_time.setText(Util.toDBC(mDatas.get(position).getTime_set()));
        } else {
            type1View.tv_time.setText("");
        }
        type1View.tv_follow.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mDatas.get(position).getTitle())) {
            if (mDatas.get(position).getBuilding() != null
                    && mDatas.get(position).getBuilding().size() > 0
                    && !TextUtils.isEmpty(mDatas.get(position).getBuilding().get(0).getName())) {
                type1View.tv_context.setMovementMethod(LinkMovementMethod.getInstance());
                type1View.tv_context.setHighlightColor(Color.TRANSPARENT);
                StringBuffer stringBuffer = new StringBuffer(mDatas.get(position).getBuilding().get(0).getName());
                stringBuffer.append("|").append(mDatas.get(position).getTitle());
                SpannableStringBuilder ssb = new SpannableStringBuilder(stringBuffer);
                //设置分割线
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.title_segmentation);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                ssb.setSpan(new SpanUtil.CenterSpaceImageSpan(drawable, SizeUtils.dp2px(5), SizeUtils.dp2px(5)),
                        mDatas.get(position).getBuilding().get(0).getName().length(),
                        mDatas.get(position).getBuilding().get(0).getName().length() + 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //设置点击和颜色
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        if (!TextUtils.isEmpty(mDatas.get(position).getBuilding().get(0).getUrl())) {
                            WebUrlJumpManager.getInstance().invoke(mContext, mDatas.get(position).getBuilding().get(0).getUrl(), null);
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        //取消下划线
                        ds.setUnderlineText(false);
                        //设置颜色
                        ds.setColor(Color.parseColor("#18619A"));
                    }
                }, 0, mDatas.get(position).getBuilding().get(0).getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        if (!TextUtils.isEmpty(mDatas.get(position).getUrl())) {
                            WebUrlJumpManager.getInstance().invoke(mContext, mDatas.get(position).getUrl(), null);
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        //取消下划线
                        ds.setUnderlineText(false);
                        //设置颜色
                        ds.setColor(Color.parseColor("#1C2125"));
                    }
                }, mDatas.get(position).getBuilding().get(0).getName().length(), stringBuffer.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                type1View.tv_context.setText(ssb);
            } else {
                type1View.tv_context.setText(Util.toDBC(mDatas.get(position).getTitle()));
            }
        } else {
            type1View.tv_context.setText("");
        }
        if (mDatas.get(position).getBuilding() != null && mDatas.get(position).getBuilding().size() > 0) {
            YMLinearLayoutManager layoutManager = new YMLinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
            type1View.rv_build.setLayoutManager(layoutManager);
            BuildAdapter buildAdapter = new BuildAdapter(mContext, mDatas.get(position).getBuilding());
            type1View.rv_build.setAdapter(buildAdapter);
            type1View.ll_location.setVisibility(View.VISIBLE);
        } else {
            type1View.ll_location.setVisibility(View.GONE);
        }
        type1View.tv_user_type_flag.setVisibility(View.GONE);
        type1View.user_type.setVisibility(View.GONE);
    }

    private void setType2View(Type2ViewHolder type2View, List<MyArticleInfo> mDatas, int position) {
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getUser_img())) {
            type2View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri(mDatas.get(position).getUser_data().getUser_img()).setAutoPlayAnimations(true).build());
        } else {
            type2View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getNickname())) {
            type2View.tv_name.setText(Util.toDBC(mDatas.get(position).getUser_data().getNickname()));
        } else {
            type2View.tv_name.setText("");
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getTime_set())) {
            type2View.tv_time.setText(Util.toDBC(mDatas.get(position).getTime_set()));
        } else {
            type2View.tv_time.setText("");
        }
        type2View.tv_follow.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mDatas.get(position).getTitle())) {
            if (mDatas.get(position).getBuilding() != null
                    && mDatas.get(position).getBuilding().size() > 0
                    && !TextUtils.isEmpty(mDatas.get(position).getBuilding().get(0).getName())) {
                type2View.tv_context.setMovementMethod(LinkMovementMethod.getInstance());
                type2View.tv_context.setHighlightColor(Color.TRANSPARENT);
                StringBuffer stringBuffer = new StringBuffer(mDatas.get(position).getBuilding().get(0).getName());
                stringBuffer.append("|").append(mDatas.get(position).getTitle());
                SpannableStringBuilder ssb = new SpannableStringBuilder(stringBuffer);
                //设置分割线
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.title_segmentation);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                ssb.setSpan(new SpanUtil.CenterSpaceImageSpan(drawable, SizeUtils.dp2px(5), SizeUtils.dp2px(5)),
                        mDatas.get(position).getBuilding().get(0).getName().length(),
                        mDatas.get(position).getBuilding().get(0).getName().length() + 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //设置点击和颜色
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        if (!TextUtils.isEmpty(mDatas.get(position).getBuilding().get(0).getUrl())) {
                            WebUrlJumpManager.getInstance().invoke(mContext, mDatas.get(position).getBuilding().get(0).getUrl(), null);
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        //取消下划线
                        ds.setUnderlineText(false);
                        //设置颜色
                        ds.setColor(Color.parseColor("#18619A"));
                    }
                }, 0, mDatas.get(position).getBuilding().get(0).getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        if (!TextUtils.isEmpty(mDatas.get(position).getUrl())) {
                            WebUrlJumpManager.getInstance().invoke(mContext, mDatas.get(position).getUrl(), null);
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        //取消下划线
                        ds.setUnderlineText(false);
                        //设置颜色
                        ds.setColor(Color.parseColor("#1C2125"));
                    }
                }, mDatas.get(position).getBuilding().get(0).getName().length(), stringBuffer.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                type2View.tv_context.setText(ssb);
            } else {
                type2View.tv_context.setText(Util.toDBC(mDatas.get(position).getTitle()));
            }
        } else {
            type2View.tv_context.setText("");
        }
        if (mDatas.get(position).getBuilding() != null && mDatas.get(position).getBuilding().size() > 0) {
            YMLinearLayoutManager layoutManager = new YMLinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
            type2View.rv_build.setLayoutManager(layoutManager);
            BuildAdapter buildAdapter = new BuildAdapter(mContext, mDatas.get(position).getBuilding());
            type2View.rv_build.setAdapter(buildAdapter);
            type2View.ll_location.setVisibility(View.VISIBLE);
        } else {
            type2View.ll_location.setVisibility(View.GONE);
        }
        if (mDatas.get(position).getImg().size() > 0 && !TextUtils.isEmpty(mDatas.get(position).getImg().get(0).getImg())) {
            type2View.iv_img.setController(Fresco.newDraweeControllerBuilder().setUri(mDatas.get(position).getImg().get(0).getImg()).setAutoPlayAnimations(true).build());
        }
        type2View.tv_user_type_flag.setVisibility(View.GONE);
        type2View.user_type.setVisibility(View.GONE);
    }

    private void setType3View(Type3ViewHolder type3View, List<MyArticleInfo> mDatas, int position) {
        type3View.ll_comment_zan.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getUser_img())) {
            type3View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri(mDatas.get(position).getUser_data().getUser_img()).setAutoPlayAnimations(true).build());
        } else {
            type3View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getNickname())) {
            type3View.tv_name.setText(Util.toDBC(mDatas.get(position).getUser_data().getNickname()));
        } else {
            type3View.tv_name.setText("");
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getTime_set())) {
            type3View.tv_time.setText(Util.toDBC(mDatas.get(position).getTime_set()));
        } else {
            type3View.tv_time.setText("");
        }
        type3View.tv_follow.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mDatas.get(position).getTitle())) {
            if (mDatas.get(position).getBuilding() != null
                    && mDatas.get(position).getBuilding().size() > 0
                    && !TextUtils.isEmpty(mDatas.get(position).getBuilding().get(0).getName())) {
                type3View.tv_context.setMovementMethod(LinkMovementMethod.getInstance());
                type3View.tv_context.setHighlightColor(Color.TRANSPARENT);
                StringBuffer stringBuffer = new StringBuffer(mDatas.get(position).getBuilding().get(0).getName());
                stringBuffer.append("|").append(mDatas.get(position).getTitle());
                SpannableStringBuilder ssb = new SpannableStringBuilder(stringBuffer);
                //设置分割线
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.title_segmentation);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                ssb.setSpan(new SpanUtil.CenterSpaceImageSpan(drawable, SizeUtils.dp2px(5), SizeUtils.dp2px(5)),
                        mDatas.get(position).getBuilding().get(0).getName().length(),
                        mDatas.get(position).getBuilding().get(0).getName().length() + 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //设置点击和颜色
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        if (!TextUtils.isEmpty(mDatas.get(position).getBuilding().get(0).getUrl())) {
                            WebUrlJumpManager.getInstance().invoke(mContext, mDatas.get(position).getBuilding().get(0).getUrl(), null);
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        //取消下划线
                        ds.setUnderlineText(false);
                        //设置颜色
                        ds.setColor(Color.parseColor("#18619A"));
                    }
                }, 0, mDatas.get(position).getBuilding().get(0).getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        if (!TextUtils.isEmpty(mDatas.get(position).getUrl())) {
                            WebUrlJumpManager.getInstance().invoke(mContext, mDatas.get(position).getUrl(), null);
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        //取消下划线
                        ds.setUnderlineText(false);
                        //设置颜色
                        ds.setColor(Color.parseColor("#1C2125"));
                    }
                }, mDatas.get(position).getBuilding().get(0).getName().length(), stringBuffer.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                type3View.tv_context.setText(ssb);
            } else {
                type3View.tv_context.setText(Util.toDBC(mDatas.get(position).getTitle()));
            }
        } else {
            type3View.tv_context.setText("");
        }
        if (mDatas.get(position).getBuilding() != null && mDatas.get(position).getBuilding().size() > 0) {
            YMLinearLayoutManager layoutManager = new YMLinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
            type3View.rv_build.setLayoutManager(layoutManager);
            BuildAdapter buildAdapter = new BuildAdapter(mContext, mDatas.get(position).getBuilding());
            type3View.rv_build.setAdapter(buildAdapter);
            type3View.ll_location.setVisibility(View.VISIBLE);
        } else {
            type3View.ll_location.setVisibility(View.GONE);
        }
        type3View.tv_user_type_flag.setVisibility(View.GONE);
        type3View.user_type.setVisibility(View.GONE);
        if (mDatas.get(position).getImg() != null && mDatas.get(position).getImg().size() > 0) {
            YMGridLayoutManager gridLayoutManager = new YMGridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false);
            HomeItemImgAdapter homeItemImgAdapter = new HomeItemImgAdapter(mContext, mDatas.get(position).getImg(), ScreenUtils.getScreenWidth(), "3");
            type3View.rv_img.setLayoutManager(gridLayoutManager);
            type3View.rv_img.setAdapter(homeItemImgAdapter);
            type3View.rv_img.setVisibility(View.VISIBLE);
            homeItemImgAdapter.setListener(mList -> {
                if (!TextUtils.isEmpty(mDatas.get(position).getUrl())) {
                    WebUrlJumpManager.getInstance().invoke(mContext, mDatas.get(position).getUrl(), null);
                }
            });
        } else {
            type3View.rv_img.setVisibility(View.GONE);
        }
    }

    private void setType4View(Type4ViewHolder type4View, List<MyArticleInfo> mDatas, int position) {
        type4View.tv_comment_num.setText(mDatas.get(position).getReply_num() + "");
        type4View.tv_zan_num.setText(mDatas.get(position).getAgree_num() + "");
        type4View.ll_comment_zan.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getUser_img())) {
            type4View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri(mDatas.get(position).getUser_data().getUser_img()).setAutoPlayAnimations(true).build());
        } else {
            type4View.iv_person.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.icon_default).setAutoPlayAnimations(true).build());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getUser_data().getNickname())) {
            type4View.tv_name.setText(Util.toDBC(mDatas.get(position).getUser_data().getNickname()));
        } else {
            type4View.tv_name.setText("");
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getTime_set())) {
            type4View.tv_time.setText(Util.toDBC(mDatas.get(position).getTime_set()));
        } else {
            type4View.tv_time.setText("");
        }
        type4View.tv_follow.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(mDatas.get(position).getTitle())) {
            if (mDatas.get(position).getBuilding() != null
                    && mDatas.get(position).getBuilding().size() > 0
                    && !TextUtils.isEmpty(mDatas.get(position).getBuilding().get(0).getName())) {
                type4View.tv_context.setMovementMethod(LinkMovementMethod.getInstance());
                type4View.tv_context.setHighlightColor(Color.TRANSPARENT);
                StringBuffer stringBuffer = new StringBuffer(mDatas.get(position).getBuilding().get(0).getName());
                stringBuffer.append("|").append(mDatas.get(position).getTitle());
                SpannableStringBuilder ssb = new SpannableStringBuilder(stringBuffer);
                //设置分割线
                Drawable drawable = mContext.getResources().getDrawable(R.drawable.title_segmentation);
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                ssb.setSpan(new SpanUtil.CenterSpaceImageSpan(drawable, SizeUtils.dp2px(5), SizeUtils.dp2px(5)),
                        mDatas.get(position).getBuilding().get(0).getName().length(),
                        mDatas.get(position).getBuilding().get(0).getName().length() + 1,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //设置点击和颜色
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        if (!TextUtils.isEmpty(mDatas.get(position).getBuilding().get(0).getUrl())) {
                            WebUrlJumpManager.getInstance().invoke(mContext, mDatas.get(position).getBuilding().get(0).getUrl(), null);
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        //取消下划线
                        ds.setUnderlineText(false);
                        //设置颜色
                        ds.setColor(Color.parseColor("#18619A"));
                    }
                }, 0, mDatas.get(position).getBuilding().get(0).getName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        if (!TextUtils.isEmpty(mDatas.get(position).getUrl())) {
                            WebUrlJumpManager.getInstance().invoke(mContext, mDatas.get(position).getUrl(), null);
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        //取消下划线
                        ds.setUnderlineText(false);
                        //设置颜色
                        ds.setColor(Color.parseColor("#1C2125"));
                    }
                }, mDatas.get(position).getBuilding().get(0).getName().length(), stringBuffer.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                type4View.tv_context.setText(ssb);
            } else {
                type4View.tv_context.setText(Util.toDBC(mDatas.get(position).getTitle()));
            }
        } else {
            type4View.tv_context.setText("");
        }
        if (mDatas.get(position).getBuilding() != null && mDatas.get(position).getBuilding().size() > 0) {
            YMLinearLayoutManager layoutManager = new YMLinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
            type4View.rv_build.setLayoutManager(layoutManager);
            BuildAdapter buildAdapter = new BuildAdapter(mContext, mDatas.get(position).getBuilding());
            type4View.rv_build.setAdapter(buildAdapter);
            type4View.ll_location.setVisibility(View.VISIBLE);
        } else {
            type4View.ll_location.setVisibility(View.GONE);
        }
        type4View.tv_user_type_flag.setVisibility(View.GONE);
        type4View.user_type.setVisibility(View.GONE);
        if (mDatas.get(position).getImg() != null && mDatas.get(position).getImg().size() > 0) {
            if (mDatas.get(position).getImg().size() == 1) {
                YMLinearLayoutManager ymLinearLayoutManager = new YMLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                HomeItemImgAdapter homeItemImgAdapter = new HomeItemImgAdapter(mContext, mDatas.get(position).getImg(), ScreenUtils.getScreenWidth(), "4");
                type4View.rv_img.setLayoutManager(ymLinearLayoutManager);
                type4View.rv_img.setAdapter(homeItemImgAdapter);
                homeItemImgAdapter.setListener(mList -> {
                    if (!TextUtils.isEmpty(mDatas.get(position).getUrl())) {
                        WebUrlJumpManager.getInstance().invoke(mContext, mDatas.get(position).getUrl(), null);
                    }
                });
            } else {
                YMGridLayoutManager gridLayoutManager = new YMGridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false);
                HomeItemImgAdapter homeItemImgAdapter = new HomeItemImgAdapter(mContext, mDatas.get(position).getImg(), ScreenUtils.getScreenWidth(), "4");
                type4View.rv_img.setLayoutManager(gridLayoutManager);
                type4View.rv_img.setAdapter(homeItemImgAdapter);
                homeItemImgAdapter.setListener(mList -> {
                    if (!TextUtils.isEmpty(mDatas.get(position).getUrl())) {
                        WebUrlJumpManager.getInstance().invoke(mContext, mDatas.get(position).getUrl(), null);
                    }
                });
            }
            type4View.rv_img.setVisibility(View.VISIBLE);
        } else {
            type4View.rv_img.setVisibility(View.GONE);
        }
    }

    class Type1ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView iv_person;
        TextView tv_name;
        TextView tv_user_type_flag;
        TextView tv_time;
        TextView user_type;
        TextView tv_follow;
        TextView tv_context;
        LinearLayout ll_location;
        RecyclerView rv_build;

        Type1ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_person = itemView.findViewById(R.id.iv_person);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_user_type_flag = itemView.findViewById(R.id.tv_user_type_flag);
            tv_time = itemView.findViewById(R.id.tv_time);
            user_type = itemView.findViewById(R.id.user_type);
            tv_follow = itemView.findViewById(R.id.tv_follow);
            tv_context = itemView.findViewById(R.id.tv_context);
            ll_location = itemView.findViewById(R.id.ll_location);
            rv_build = itemView.findViewById(R.id.rv_build);
            itemView.setOnClickListener(v -> mEventListener.onItemListener(v, mDatas.get(getLayoutPosition()), getLayoutPosition()));
        }
    }

    class Type2ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView iv_person;
        SimpleDraweeView iv_img;
        TextView tv_name;
        TextView tv_user_type_flag;
        TextView tv_time;
        TextView user_type;
        TextView tv_follow;
        TextView tv_context;
        TextView tv_des;
        LinearLayout ll_location;
        RecyclerView rv_build;

        Type2ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_person = itemView.findViewById(R.id.iv_person);
            iv_img = itemView.findViewById(R.id.iv_img);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_user_type_flag = itemView.findViewById(R.id.tv_user_type_flag);
            tv_time = itemView.findViewById(R.id.tv_time);
            user_type = itemView.findViewById(R.id.user_type);
            tv_follow = itemView.findViewById(R.id.tv_follow);
            tv_context = itemView.findViewById(R.id.tv_context);
            tv_des = itemView.findViewById(R.id.tv_des);
            ll_location = itemView.findViewById(R.id.ll_location);
            rv_build = itemView.findViewById(R.id.rv_build);
            itemView.setOnClickListener(v -> mEventListener.onItemListener(v, mDatas.get(getLayoutPosition()), getLayoutPosition()));
        }
    }

    class Type3ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView iv_person;
        TextView tv_name;
        TextView tv_user_type_flag;
        TextView tv_time;
        TextView user_type;
        TextView tv_follow;
        TextView tv_context;
        LinearLayout ll_location;
        RecyclerView rv_build;
        RecyclerView rv_img;
        LinearLayout ll_comment_zan;

        Type3ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_person = itemView.findViewById(R.id.iv_person);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_user_type_flag = itemView.findViewById(R.id.tv_user_type_flag);
            tv_time = itemView.findViewById(R.id.tv_time);
            user_type = itemView.findViewById(R.id.user_type);
            tv_follow = itemView.findViewById(R.id.tv_follow);
            tv_context = itemView.findViewById(R.id.tv_context);
            ll_location = itemView.findViewById(R.id.ll_location);
            rv_build = itemView.findViewById(R.id.rv_build);
            rv_img = itemView.findViewById(R.id.rv_img);
            ll_comment_zan = itemView.findViewById(R.id.ll_comment_zan);
            itemView.setOnClickListener(v -> mEventListener.onItemListener(v, mDatas.get(getLayoutPosition()), getLayoutPosition()));
        }
    }

    class Type4ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView iv_person;
        TextView tv_name;
        TextView tv_user_type_flag;
        TextView tv_time;
        TextView user_type;
        TextView tv_follow;
        TextView tv_context;
        LinearLayout ll_location;
        RecyclerView rv_build;
        RecyclerView rv_img;
        ImageView iv_comment_num;
        TextView tv_comment_num;
        ImageView iv_zan_num;
        TextView tv_zan_num;
        LinearLayout ll_comment_zan;

        Type4ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_person = itemView.findViewById(R.id.iv_person);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_user_type_flag = itemView.findViewById(R.id.tv_user_type_flag);
            tv_time = itemView.findViewById(R.id.tv_time);
            user_type = itemView.findViewById(R.id.user_type);
            tv_follow = itemView.findViewById(R.id.tv_follow);
            tv_context = itemView.findViewById(R.id.tv_context);
            ll_location = itemView.findViewById(R.id.ll_location);
            rv_build = itemView.findViewById(R.id.rv_build);
            rv_img = itemView.findViewById(R.id.rv_img);
            iv_comment_num = itemView.findViewById(R.id.iv_comment_num);
            tv_comment_num = itemView.findViewById(R.id.tv_comment_num);
            iv_zan_num = itemView.findViewById(R.id.iv_zan_num);
            tv_zan_num = itemView.findViewById(R.id.tv_zan_num);
            ll_comment_zan = itemView.findViewById(R.id.ll_comment_zan);
            itemView.setOnClickListener(v -> mEventListener.onItemListener(v, mDatas.get(getLayoutPosition()), getLayoutPosition()));
            tv_zan_num.setOnClickListener(v -> {
                HashMap<String, Object> maps = new HashMap<>(0);
                //点赞类型（1文章点赞 ，2评论点赞）
                maps.put("agree_type", "1");
                //文章id 或者 评论id
                maps.put("ugc_or_reply_id", mDatas.get(getLayoutPosition()).getId());
                if ("0".equals(mDatas.get(getLayoutPosition()).getIs_agree())) {
                    //没点过赞
                    new ZanApi().getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
                        @Override
                        public void onSuccess(ServerData serverData) {
                            if ("1".equals(serverData.code)) {
                                ToastUtils.toast(mContext, "点赞成功").show();
                                mDatas.get(getLayoutPosition()).setAgree_num(Integer.parseInt(mDatas.get(getLayoutPosition()).getAgree_num()) + 1 + "");
                                mDatas.get(getLayoutPosition()).setIs_agree("1");
                                notifyItemChanged(getLayoutPosition());
                            }
                        }
                    });
                } else {
                    new ZanApi().getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
                        if ("1".equals(serverData.code)) {
                            ToastUtils.toast(mContext, "取消赞成功").show();
                            mDatas.get(getLayoutPosition()).setIs_agree("0");
                            if (Integer.parseInt(mDatas.get(getLayoutPosition()).getAgree_num()) > 0) {
                                mDatas.get(getLayoutPosition()).setAgree_num(Integer.parseInt(mDatas.get(getLayoutPosition()).getAgree_num()) - 1 + "");
                            }
                            notifyItemChanged(getLayoutPosition());
                        }
                    });
                }
            });
            iv_zan_num.setOnClickListener(v -> {
                HashMap<String, Object> maps = new HashMap<>(0);
                //点赞类型（1文章点赞 ，2评论点赞）
                maps.put("agree_type", "1");
                //文章id 或者 评论id
                maps.put("ugc_or_reply_id", mDatas.get(getLayoutPosition()).getId());
                if ("0".equals(mDatas.get(getLayoutPosition()).getIs_agree())) {
                    //没点过赞
                    new ZanApi().getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
                        if ("1".equals(serverData.code)) {
                            ToastUtils.toast(mContext, "点赞成功").show();
                            mDatas.get(getLayoutPosition()).setAgree_num(Integer.parseInt(mDatas.get(getLayoutPosition()).getAgree_num()) + 1 + "");
                            mDatas.get(getLayoutPosition()).setIs_agree("1");
                            notifyItemChanged(getLayoutPosition());
                        }
                    });
                } else {
                    new ZanApi().getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
                        if ("1".equals(serverData.code)) {
                            ToastUtils.toast(mContext, "取消赞成功").show();
                            mDatas.get(getLayoutPosition()).setIs_agree("0");
                            if (Integer.parseInt(mDatas.get(getLayoutPosition()).getAgree_num()) > 0) {
                                mDatas.get(getLayoutPosition()).setAgree_num(Integer.parseInt(mDatas.get(getLayoutPosition()).getAgree_num()) - 1 + "");
                            }
                            notifyItemChanged(getLayoutPosition());
                        }
                    });
                }
            });
        }
    }


    /**
     * 追加数据
     */
    public void addData(List<MyArticleInfo> data) {
        int size = mDatas.size();
        mDatas.addAll(data);
        notifyItemRangeInserted(size, mDatas.size() - size);
    }


    private EventListener mEventListener;

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

    public interface EventListener {
        //        void onItemFollowListener(View v, MyArticleInfo data, int pos);
        void onItemListener(View v, MyArticleInfo data, int pos);
    }
}
