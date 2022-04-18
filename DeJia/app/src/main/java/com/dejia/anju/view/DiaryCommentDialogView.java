package com.dejia.anju.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dejia.anju.R;
import com.dejia.anju.api.ReplyApi;
import com.dejia.anju.api.UpLoadUgcImageApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.event.Event;
import com.dejia.anju.model.CommentInfo;
import com.dejia.anju.model.UgcUploadImageInfo;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.DialogUtils;
import com.dejia.anju.utils.GlideEngine;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.utils.Util;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hjq.gson.factory.GsonFactory;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.lzy.okgo.model.HttpParams;


public class DiaryCommentDialogView extends AlertDialog {

    private CommentInfo mDatas;
    //    private List<String> mResults = new ArrayList<>();
    //发送按钮
    @BindView(R.id.bbs_web_sumbit1_bt)
    Button sumbitBt1;
    //输入框
    @BindView(R.id.bbs_web_input_content1_et)
    EditText inputContentEt;
    //输入框整体
    @BindView(R.id.bbs_web_input_content1_ly2)
    LinearLayout iputly2;
    //图片点击按钮
    @BindView(R.id.up_photo_iv)
    ImageView upPhotoIv;
    //选择图片整体
    @BindView(R.id.ll_select_pic)
    LinearLayout ll_select_pic;
    //选择图片整体
    @BindView(R.id.write_que_bbs_add_photo_rly)
    RelativeLayout sCPhotoRly;
    //图片列表整体布局
    @BindView(R.id.bbs_input_photo_ly)
    LinearLayout photoLy;
    //图片列表
    @BindView(R.id.rv_pic)
    RecyclerView rv_pic;
    //图片按钮文字
    @BindView(R.id.tv_photo)
    TextView tv_photo;
    @BindView(R.id.tv_photo_num)
    TextView tv_photo_num;
    private List<LocalMedia> chooseResult;
    private Activity mContext;
    private PicAdapter adapter;
    private PictureWindowAnimationStyle mWindowAnimationStyle;


    public DiaryCommentDialogView(@NonNull Activity context, CommentInfo sumbitPhotoDatas) {
        super(context, R.style.MyDialog1);
        this.mContext = context;
        this.mDatas = sumbitPhotoDatas;
        this.chooseResult = mDatas.getLocalMediaList();
        if (chooseResult == null) {
            chooseResult = new ArrayList<>();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.diary_comment_pop_buttom_dialog_new, null, false);
        ButterKnife.bind(this, view);

        inputContentEt.setFocusable(true);
        inputContentEt.setFocusableInTouchMode(true);
        inputContentEt.requestFocus();

        findView(view);
        initRecycle(chooseResult);
        setContentView(view);

        setCancelable(false);
        setCanceledOnTouchOutside(true);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        setOnShowListener(dialog -> showKeyboard());

        mWindowAnimationStyle = new PictureWindowAnimationStyle();
        mWindowAnimationStyle.ofAllAnimation(R.anim.picture_anim_up_in, R.anim.picture_anim_down_out);
        sCPhotoRly.setOnClickListener(v -> {
            showBottomPop();
        });
        sumbitBt1.setOnClickListener(v -> {
            if (Util.isFastDoubleClick()) {
                return;
            }
            View view1 = getWindow().peekDecorView();
            if (view1 != null) {
                closeKeyboard(view1);
            }
            if (TextUtils.isEmpty(inputContentEt.getText().toString().trim())) {
                ToastUtils.toast(mContext, "内容不能为空").show();
                return;
            }
            if (inputContentEt.getText().toString().trim().length() < 5) {
                ToastUtils.toast(mContext, "至少需要5个字").show();
                return;
            }
            postImg();
        });
    }

    private void postImg() {
        if (adapter != null && adapter.getMediaData() != null && adapter.getMediaData().size() >= 0) {
            HashMap<String, Object> map = new HashMap<>(0);
            HttpParams httpParams = new HttpParams();
            for (int i = 0; i < adapter.getMediaData().size(); i++) {
                httpParams.put(adapter.getMediaData().get(i) + "",
                        new File(adapter.getMediaData().get(i).getCompressPath()));
            }
            new UpLoadUgcImageApi().getCallBack(mContext, map, httpParams, (BaseCallBackListener<ServerData>) serverData -> {
                if ("1".equals(serverData.code)) {
                    List<UgcUploadImageInfo> list = JSONUtil.jsonToArrayList(serverData.data, UgcUploadImageInfo.class);
                    postComment(list);
                } else {
                    ToastUtils.toast(mContext, "图片上传失败请重试").show();
                }
            });
        } else {
            postComment(null);
        }
    }

    private void postComment(List<UgcUploadImageInfo> list) {
        Map<String, Object> maps = new HashMap<>();
        if (list != null && list.size() > 0) {
            List imgList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                HashMap map = new HashMap(0);
                map.put("img", list.get(i).getPost_img_url());
                imgList.add(map);
            }
            maps.put("image", GsonFactory.getSingletonGson().toJson(imgList).toString());
        }
        maps.put("content", inputContentEt.getText().toString().trim());
        maps.put("article_id", adapter.getCommentInfo().getArticle_id());
        maps.put("reply_id", adapter.getCommentInfo().getReply_id());
        maps.put("at_user_id", adapter.getCommentInfo().getUid());
        new ReplyApi().getCallBack(mContext, maps, (BaseCallBackListener<ServerData>) serverData -> {
            if ("1".equals(serverData.code)) {
                EventBus.getDefault().post(new Event<>(8, serverData.data));
//                ToastUtils.toast(mContext, "发表成功").show();
                dismiss();
            } else {
                ToastUtils.toast(mContext, serverData.message).show();
            }
        });
    }

    public void findView(View view) {
        iputly2.setOnClickListener(arg0 -> {
            if (Util.isFastDoubleClick()) {
                return;
            }
            sCPhotoRly.setVisibility(View.VISIBLE);
            showKeyboard();
        });

        inputContentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    sumbitBt1.setTextColor(Color.parseColor("#CCCCCC"));
                    sumbitBt1.setEnabled(false);
                } else {
                    sumbitBt1.setTextColor(Color.parseColor("#0095FF"));
                    sumbitBt1.setEnabled(true);
                }
            }
        });

        if (mDatas != null && !TextUtils.isEmpty(mDatas.getContent()) && inputContentEt != null) {
            inputContentEt.setText(mDatas.getContent());
            inputContentEt.setSelection(mDatas.getContent().length());
        }
        this.setOnCancelListener(dialog -> {
            if (mDatas != null) {
                mDatas.setContent(inputContentEt.getText().toString().trim());
//                if (adapter != null && adapter.getCommentInfo() != null && adapter.getCommentInfo().getImage() != null) {
//                    mDatas.setImage(adapter.getCommentInfo().getImage());
//                }
                if (adapter != null && adapter.getMediaData() != null && adapter.getMediaData().size() > 0) {
                    mDatas.setLocalMediaList(adapter.getMediaData());
                }
                EventBus.getDefault().post(new Event<>(9, mDatas));
            }
        });
    }


    /**
     * 图片选择按钮是隐藏
     *
     * @param isGone:true:隐藏，false:显示
     */
    public void setPicturesChooseGone(boolean isGone) {
        if (isGone) {
            ll_select_pic.setVisibility(View.GONE);
            photoLy.setVisibility(View.GONE);
        } else {
            ll_select_pic.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 初始化图片列表形式
     */
    public void initRecycle(List<LocalMedia> chooseResult) {
//        linearLayoutManager.setReverseLayout(true);//布局反向
//        linearLayoutManager.setStackFromEnd(true);//数据反向
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rv_pic.setLayoutManager(linearLayoutManager);
        adapter = new PicAdapter(mContext, mDatas, chooseResult);
        rv_pic.setAdapter(adapter);
        if (adapter != null && adapter.getMediaData() != null) {
            tv_photo_num.setText(adapter.getMediaData().size() + "");
        }
//        if (mDatas != null && mDatas.getImage() != null && mDatas.getImage().size() > 0) {
//            mResults = mDatas.getImage();
//        }
        if (chooseResult.size() == 0) {
            photoLy.setVisibility(View.GONE);
        } else {
            if (chooseResult.size() >= 9) {
                tv_photo.setTextColor(Color.parseColor("#CCCCCC"));
            } else {
                tv_photo.setTextColor(Color.parseColor("#666666"));
            }
            photoLy.setVisibility(View.VISIBLE);
        }
    }

    class PicAdapter extends RecyclerView.Adapter<PicAdapter.ViewHolder> {

        private Context context;
        private CommentInfo data;
        private List<LocalMedia> localMediaLst;

        public PicAdapter(Context context, CommentInfo data, List<LocalMedia> list) {
            this.data = data;
            this.context = context;
            this.localMediaLst = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_published_grida1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            if (position == localMediaLst.size() && position <= (9 - 1)) {
                //添加图片
                holder.item_grida_image.setController(Fresco.newDraweeControllerBuilder().setUri("res://mipmap/" + R.mipmap.add).setAutoPlayAnimations(true).build());
                holder.item_grida_bt.setVisibility(View.GONE);
                holder.item_grida_image.setOnClickListener(v -> showBottomPop());
            } else {
                holder.item_grida_image.setController(Fresco.newDraweeControllerBuilder().setUri("file://" + adapter.getMediaData().get(position).getCompressPath()).setAutoPlayAnimations(true).build());
                holder.item_grida_bt.setVisibility(View.VISIBLE);
            }
            holder.item_grida_bt.setOnClickListener(v -> {
//                data.getImage().remove(position);
                localMediaLst.remove(position);
                tv_photo_num.setText(localMediaLst.size() + "");
                if (localMediaLst.size() >= 9) {
                    tv_photo.setTextColor(Color.parseColor("#CCCCCC"));
                } else {
                    tv_photo.setTextColor(Color.parseColor("#666666"));
                }
                if (localMediaLst.size() == 0) {
                    tv_photo_num.setText("0");
                    photoLy.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            if (localMediaLst == null || localMediaLst.size() == 0) {
                return 1;
            } else {
                return this.localMediaLst.size() >= 9 ? 9 : this.localMediaLst.size() + 1;
            }
        }

//        public List<String> getData() {
//            return data.getImage();
//        }

        public CommentInfo getCommentInfo() {
            return data;
        }

        public List<LocalMedia> getMediaData() {
            return localMediaLst;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private SimpleDraweeView item_grida_image;
            private Button item_grida_bt;

            public ViewHolder(View itemView) {
                super(itemView);
                item_grida_image = itemView.findViewById(R.id.item_grida_image);
                item_grida_bt = itemView.findViewById(R.id.item_grida_bt);
            }
        }
    }


    //照片 拍照选择
    private void showBottomPop() {
        DialogUtils.showSelectPicDialog(mContext, new DialogUtils.CallBack5() {
            @Override
            public void onAlbumClick() {
                DialogUtils.closeDialog();
                //相册选择
                PictureSelector.create(mContext)
                        .openGallery(PictureMimeType.ofImage())
                        .isCamera(false)
                        .isAndroidQTransform(true)
                        .theme(R.style.picture_WeChat_style)
                        .isWeChatStyle(true)
                        .selectionMode(PictureConfig.MULTIPLE)
                        .selectionData(adapter.getMediaData())
                        .maxSelectNum(9)
                        .setPictureWindowAnimationStyle(mWindowAnimationStyle)
                        .isEnableCrop(false)
                        .isCompress(true)// 是否压缩
                        .compressQuality(60)// 图片压缩后输出质量 0~ 100
                        .isZoomAnim(true)
                        .withAspectRatio(1, 1)
                        .circleDimmedLayer(false)
                        .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                        .cutOutQuality(90)// 裁剪输出质量 默认100
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
//                        .isOpenClickSound(true)// 是否开启点击声音
                        .imageEngine(GlideEngine.createGlideEngine())
                        .forResult(PictureConfig.CHOOSE_REQUEST_COMMENT);
            }

            @Override
            public void onCreamrClick() {
                DialogUtils.closeDialog();
                //拍照
                PictureSelector.create(mContext)
                        .openCamera(PictureMimeType.ofImage())
                        .isAndroidQTransform(true)
                        .isEnableCrop(true)
                        .isCompress(true)// 是否压缩
                        .compressQuality(60)// 图片压缩后输出质量 0~ 100
                        .withAspectRatio(1, 1)
                        .circleDimmedLayer(false)
                        .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                        .cutOutQuality(90)// 裁剪输出质量 默认100
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .imageEngine(GlideEngine.createGlideEngine())
                        .forResult(PictureConfig.REQUEST_CAMERA_COMMENT);
            }
        });
    }

    /**
     * 显示
     */
    public void showDialog() {
        show();
    }

    public void setmResults(List<LocalMedia> imgList) {
        this.chooseResult = imgList;
        initRecycle(chooseResult);
    }


    /**
     * 打开键盘
     */
    public void showKeyboard() {
        inputContentEt.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }, 200);
    }

    /**
     * 关闭键盘
     *
     * @param view
     */
    private void closeKeyboard(View view) {
        EventBus.getDefault().post(new Event<>(8, ""));
        InputMethodManager inputmanger = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
