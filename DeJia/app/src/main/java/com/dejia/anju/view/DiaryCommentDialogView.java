package com.dejia.anju.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
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
import com.dejia.anju.model.CommentInfo;
import com.dejia.anju.utils.GlideEngine;
import com.dejia.anju.utils.ToastUtils;
import com.dejia.anju.utils.Util;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.hjq.gson.factory.GsonFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;


//显示 app 评论弹框功能改为 H5调用原生配合实现。
//        方法名：showAppCommentAlert
//        参数： json对象
//        参数格式，例如：{
//            showImgBtn: false// 是否显示上传图片按钮，（楼中楼不显示上传图片按钮）
//            reply_id: 123 // 评论id
//            image: [] // 评论上传的图片
//            article_id: 20347, //  文章Id =》  长图文
//            uid: 7, // 当前登录人的用户 id
//
//        }
//
//// app提交成功之后，回调该方法，h5做显示处理。
//        方法名：showCommentResults
//        参数： json 对象
//        参数格式，提交成功后，侯老板返回的data数据即可。
//
//        只需要把成功（code==1）之后的data数据返回给我就行，如果错误，弹出对应提示就行，不需要调用 h5 的显示方法

public class DiaryCommentDialogView extends AlertDialog {

    private CommentInfo mDatas;
    private ArrayList<String> mResults = new ArrayList<>();
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
    @BindView(R.id.write_que_bbs_add_photo_rly_container)
    LinearLayout sCPhotoRlyContainer;
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
    private Activity mContext;
    private float dp;
    private String sumbitContent2;
    private PicAdapter adapter;
    private PictureWindowAnimationStyle mWindowAnimationStyle;
    public static final int REQUEST_CODE = 732;
    private boolean isPhoto = false;
    ArrayList<JSONObject> typeData = new ArrayList<>();           //图片上传完成后返回的图片地址集合。key是本地存储路径，vle是服务器连接
    ArrayList<String> mResultsFinal = new ArrayList<>();           //图片压缩完成后图片本地地址集合。key是本地存储路径，vle是压缩后的本地路径
    public static final int IMAG_PROGRESS = 10;     //照片进度
    public static final int IMAG_SUCCESS = 11;     //照片上传成功
    public static final int IMAG_FAILURE = 12;     //照片上传失败
    private Handler mHandler = new MyHandler(this);
    private String mKey;
    private Gson mGson;

    private static class MyHandler extends Handler {
        private final WeakReference<DiaryCommentDialogView> mActivity;

        public MyHandler(DiaryCommentDialogView activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            DiaryCommentDialogView theActivity = mActivity.get();
            if (theActivity != null) {
                switch (msg.what) {
                    case IMAG_PROGRESS:
                        break;
                    case IMAG_SUCCESS:
                        String imgUrl = (String) msg.obj;
                        int imageWidth = msg.arg1;
                        int imageHeight = msg.arg2;
                        JSONObject jsonObject = theActivity.setJson(imgUrl, imageWidth, imageHeight);
                        theActivity.typeData.add(jsonObject);
                        break;
                    case IMAG_FAILURE:
                        break;
                }
            }
        }
    }

    private JSONObject setJson(String imgUrl, int imageWidth, int imageHeight) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("width", imageWidth + "");
            jsonObject.put("height", imageHeight + "");
            jsonObject.put("img", imgUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public DiaryCommentDialogView(@NonNull Activity context, CommentInfo sumbitPhotoDatas) {
        super(context, R.style.MyDialog1);
        this.mContext = context;
        this.mDatas = sumbitPhotoDatas;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.diary_comment_pop_buttom_dialog_new, null, false);
        ButterKnife.bind(this, view);

        mGson = GsonFactory.getSingletonGson();
        inputContentEt.setFocusable(true);
        inputContentEt.setFocusableInTouchMode(true);
        inputContentEt.requestFocus();

        findView(view);
        initBottHttp();
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
    }

    void findView(View view) {
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
                    sumbitBt1.setTextColor(Color.parseColor("#FF527F"));
                    sumbitBt1.setEnabled(true);
                }
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
            sCPhotoRlyContainer.setVisibility(View.GONE);
            photoLy.setVisibility(View.GONE);
        } else {
            sCPhotoRlyContainer.setVisibility(View.VISIBLE);
        }
    }

    private void initBottHttp() {
        try {
            sCPhotoRly.setOnClickListener(v -> {
                showBottomPop();
            });
            // 滑动图片
            dp = mContext.getResources().getDimension(R.dimen.dp);
            gridviewInit();
            // 提交
            sumbitBt1.setOnClickListener(arg0 -> {
                if (Util.isFastDoubleClick()) {
                    return;
                }
                View view = getWindow().peekDecorView();
                if (view != null) {
                    closeKeyboard(view);
                }
                sumbitContent2 = inputContentEt.getText().toString().trim();
                if (sumbitContent2.length() > 0 && !"".equals(sumbitContent2)) {
                    if (sumbitContent2.length() > 1) {
//                            if (Util.isLoginAndBind(mContext)) {
//                                if (!isPhoto) {
//                                    inputContentEt.setText("");
//                                    postFileQue();
//                                    dismiss();
//                                } else {
//                                    if (typeData.size() == mResultsFinal.size()) {
//                                        inputContentEt.setText("");
//                                        postFileQue();
//                                        dismiss();
//                                    } else {
//                                        ToastUtils.toast(mContext, "图片上传中请稍后...").show();
//                                    }
//                                }
//                            }
                    } else {
                        ToastUtils.toast(mContext, "亲，内容至少要大于1个字哟！").show();
                    }
                } else {
                    ToastUtils.toast(mContext, "内容不能为空").show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化图片列表形式
     */
    public void gridviewInit() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        rv_pic.setLayoutManager(linearLayoutManager);
        adapter = new PicAdapter(mContext, mResults);
        rv_pic.setAdapter(adapter);
        if (mResults.size() == 0) {
            photoLy.setVisibility(View.GONE);
        } else {
            if (mResults.size() >= 9) {
                tv_photo.setTextColor(Color.parseColor("#CCCCCC"));
            } else {
                tv_photo.setTextColor(Color.parseColor("#666666"));
            }
            photoLy.setVisibility(View.VISIBLE);
        }
    }

    class PicAdapter extends RecyclerView.Adapter<PicAdapter.ViewHolder> {

        private Context context;
        private List<String> data;

        public PicAdapter(Context context, List<String> data) {
            this.context = context;
            this.data = data;

        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_published_grida1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.item_grida_image.setController(Fresco.newDraweeControllerBuilder().setUri(mResults.get(position)).setAutoPlayAnimations(true).build());
            holder.tv_index.setText(position + 1 + "/" + data.size());
            holder.item_grida_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mResults.remove(position);
                    if (mResults.size() >= 9) {
//                        upPhotoIv.setBackgroundResource(R.drawable.pic_gray_dialog);
                        tv_photo.setTextColor(Color.parseColor("#CCCCCC"));
                    } else {
//                        upPhotoIv.setBackgroundResource(R.drawable.pic_dialog);
                        tv_photo.setTextColor(Color.parseColor("#666666"));
                    }
                    if (mResults.size() == 0) {
                        photoLy.setVisibility(View.GONE);
                    }
                    notifyDataSetChanged();
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    String paths = mResults.get(position);
//                    Intent it = new Intent(mContext, EditImageActivity.class);
//                    it.putExtra(EditImageActivity.FILE_PATH, paths);
//                    File outputFile = FileUtils.getEmptyFile("yuemei" + System.currentTimeMillis() + ".jpg");
//                    it.putExtra(EditImageActivity.EXTRA_OUTPUT, outputFile.getAbsolutePath());
//                    it.putExtra("pos", position + "");
//                    mContext.startActivityForResult(it, 9);
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private SimpleDraweeView item_grida_image;
            private Button item_grida_bt;
            private TextView tv_index;

            public ViewHolder(View itemView) {
                super(itemView);
                item_grida_image = itemView.findViewById(R.id.item_grida_image);
                item_grida_bt = itemView.findViewById(R.id.item_grida_bt);
                tv_index = itemView.findViewById(R.id.tv_index);
            }
        }
    }


    public void upLoadFile() {
        if (mResults.size() > 0) {
            for (int i = 0; i < mResults.size(); i++) {
                // 压缩图片
                String pathS = Environment.getExternalStorageDirectory().toString() + "/YueMei";
                File path1 = new File(pathS);// 建立这个路径的文件或文件夹
                if (!path1.exists()) {// 如果不存在，就建立文件夹
                    path1.mkdirs();
                }
                File file = new File(path1, "yuemei_" + i + System.currentTimeMillis() + ".JPEG");
                String desPath = file.getPath();
//                FileUtils.compressPicture(mResults.get(i), desPath);
//                int[] mImageWidthHeight = FileUtils.getImageWidthHeight(desPath);
//                mKey = QiNuConfig.getKey();
//                if (i == mResults.size() - 1) {
//                    MyUploadImage.getMyUploadImage(mContext, mHandler, desPath, true).upCommentsList(mKey, mImageWidthHeight);
//                } else {
//                    MyUploadImage.getMyUploadImage(mContext, mHandler, desPath, false).upCommentsList(mKey, mImageWidthHeight);
//                }
                mResultsFinal.add(desPath);
            }
        }
    }

    //头像选择
    private void showBottomPop() {
        SelectUserAvatarPopWindow selectUserAvatarPopWindow = new SelectUserAvatarPopWindow(mContext);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        selectUserAvatarPopWindow.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp1 = getWindow().getAttributes();
            lp1.alpha = 1f;
            getWindow().setAttributes(lp1);
        });
        selectUserAvatarPopWindow.showAtLocation(mContext.getWindow().getDecorView().findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
        selectUserAvatarPopWindow.setOnTextClickListener(new SelectUserAvatarPopWindow.OnTextClickListener() {
            @Override
            public void onTextClick() {
                //拍照
                PictureSelector.create(mContext)
                        .openCamera(PictureMimeType.ofImage())
                        .isAndroidQTransform(true)
                        .isEnableCrop(true)
                        .isCompress(true)// 是否压缩
                        .compressQuality(60)// 图片压缩后输出质量 0~ 100
                        .withAspectRatio(1, 1)
                        .circleDimmedLayer(true)
                        .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                        .cutOutQuality(90)// 裁剪输出质量 默认100
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        .imageEngine(GlideEngine.createGlideEngine())
                        .forResult(PictureConfig.REQUEST_CAMERA);
            }

            @Override
            public void onTextClick2() {
                //相册选择
                PictureSelector.create(mContext)
                        .openGallery(PictureMimeType.ofImage())
                        .isCamera(false)
                        .isAndroidQTransform(true)
                        .theme(R.style.picture_WeChat_style)
                        .isWeChatStyle(true)
                        .selectionMode(PictureConfig.SINGLE)
                        .setPictureWindowAnimationStyle(mWindowAnimationStyle)
                        .isEnableCrop(true)
                        .isCompress(true)// 是否压缩
                        .compressQuality(60)// 图片压缩后输出质量 0~ 100
                        .isZoomAnim(true)
                        .withAspectRatio(1, 1)
                        .circleDimmedLayer(true)
                        .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                        .cutOutQuality(90)// 裁剪输出质量 默认100
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
//                        .isOpenClickSound(true)// 是否开启点击声音
                        .imageEngine(GlideEngine.createGlideEngine())
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });
    }


    /**
     * 上传图片文件
     */
    public void postFileQue() {
//        HashMap<String, Object> maps = new HashMap<>();
//        maps.put("userid", mDatas.getUserid());
//        maps.put("docid", mDatas.get_id());
//        maps.put("qid", mDatas.getQid());
//        maps.put("cid", mDatas.getCid());
//        maps.put("type", "1");
//        maps.put("askorshare", mDatas.getAskorshare());
//        maps.put("content", mGson.toJson(new ForumTextData(sumbitContent2)));
//        maps.put("visibility", mDatas.getVisibility());
//        if (typeData.size() > 0) {
//            maps.put("image", typeData.toString());
//        }
//        new SumbitPhotoApi().getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
//            @Override
//            public void onSuccess(ServerData serverData) {
//                if (!serverData.isOtherCode) {
//                    ToastUtils.toast(mContext, serverData.message).show();
//                }
//                if ("1".equals(serverData.code)) {
//                    try {
//                        SumbitPhotoBean sumbitPhotoBean = JSONUtil.TransformSingleBean(serverData.data, SumbitPhotoBean.class);
//                        if (onSubmitClickListener != null) {
//                            onSubmitClickListener.onSubmitClick(sumbitPhotoBean.get_id(), mResultsFinal, sumbitContent2);
//                        }
//                        Utils.sumitHttpCode(mContext, "7");
//                        inputContentEt.setText("");
//                        // 清理图片缓存
//                        mResults.clear();
//                        typeData.clear();
//                        gridviewInit();
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        });
    }

    //提交接口回调
    public interface OnSubmitClickListener {
        void onSubmitClick(String id, ArrayList<String> imgLists, String content);
    }

    private OnSubmitClickListener onSubmitClickListener;

    public void setOnSubmitClickListener(OnSubmitClickListener onSubmitClickListener) {
        this.onSubmitClickListener = onSubmitClickListener;
    }

    /**
     * 显示
     */
    public void showDialog() {
        show();
    }

    public void setmResults(ArrayList<String> mResults) {
        this.mResults = mResults;
        upLoadFile();
        isPhoto = true;

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
        InputMethodManager inputmanger = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
