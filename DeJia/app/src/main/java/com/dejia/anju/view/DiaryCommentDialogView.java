//package com.dejia.anju.view;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.Matrix;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.text.Editable;
//import android.text.Spannable;
//import android.text.SpannableString;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.text.style.ImageSpan;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.SimpleAdapter;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.bitmap.CenterCrop;
//import com.dejia.anju.R;
//import com.dejia.anju.api.base.BaseCallBackListener;
//import com.dejia.anju.net.ServerData;
//import com.dejia.anju.utils.JSONUtil;
//import com.dejia.anju.utils.ToastUtils;
//import com.dejia.anju.utils.Util;
//import com.google.gson.Gson;
//import com.hjq.gson.factory.GsonFactory;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.lang.ref.WeakReference;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.viewpager.widget.PagerAdapter;
//import androidx.viewpager.widget.ViewPager;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//import com.dejia.anju.R;
//
//public class DiaryCommentDialogView extends AlertDialog {
//
//    private SumbitPhotoData mDatas;
//    private ArrayList<String> mResults = new ArrayList<>();
//    @BindView(R.id.bbs_web_sumbit1_bt)
//    Button sumbitBt1;
//    @BindView(R.id.bbs_web_input_content1_et)
//    EditText inputContentEt;
//    @BindView(R.id.bbs_web_input_content1_ly2)
//    LinearLayout iputly2;
//    @BindView(R.id.up_photo_iv)
//    ImageView upPhotoIv;
//    @BindView(R.id.write_que_bbs_add_photo_rly_container)
//    LinearLayout sCPhotoRlyContainer;
//    @BindView(R.id.write_que_bbs_add_photo_rly)
//    RelativeLayout sCPhotoRly;
//    @BindView(R.id.bbs_input_photo_ly)
//    LinearLayout photoLy;
//    @BindView(R.id.set_switch_photo_rly1)
//    RelativeLayout content1;
//    @BindView(R.id.set_switch_photo_rly2)
//    RelativeLayout content2;
//    @BindView(R.id.rv_pic)
//    RecyclerView rv_pic;
//    @BindView(R.id.tv_photo)
//    TextView tv_photo;
//    private Activity mContext;
//    public Pattern emoji;
//    private float dp;
//    private String sumbitContent2;
//    private PicAdapter adapter;
//    public static final int REQUEST_CODE = 732;
//    private boolean isPhoto = false;
//    ArrayList<JSONObject> typeData = new ArrayList<>();           //图片上传完成后返回的图片地址集合。key是本地存储路径，vle是服务器连接
//    ArrayList<String> mResultsFinal = new ArrayList<>();           //图片压缩完成后图片本地地址集合。key是本地存储路径，vle是压缩后的本地路径
//    public static final int IMAG_PROGRESS = 10;     //照片进度
//    public static final int IMAG_SUCCESS = 11;     //照片上传成功
//    public static final int IMAG_FAILURE = 12;     //照片上传失败
//    private Handler mHandler = new MyHandler(this);
//    private String mKey;
//    private Gson mGson;
//
//    private static class MyHandler extends Handler {
//        private final WeakReference<DiaryCommentDialogView> mActivity;
//
//        public MyHandler(DiaryCommentDialogView activity) {
//            mActivity = new WeakReference<>(activity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            DiaryCommentDialogView theActivity = mActivity.get();
//            if (theActivity != null) {
//                switch (msg.what) {
//                    case IMAG_PROGRESS:
//                        break;
//                    case IMAG_SUCCESS:
//                        String imgUrl = (String) msg.obj;
//                        int imageWidth = msg.arg1;
//                        int imageHeight = msg.arg2;
//                        JSONObject jsonObject = theActivity.setJson(imgUrl, imageWidth, imageHeight);
//                        theActivity.typeData.add(jsonObject);
//                        break;
//                    case IMAG_FAILURE:
//                        break;
//                }
//            }
//        }
//    }
//
//    private JSONObject setJson(String imgUrl, int imageWidth, int imageHeight) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("width", imageWidth + "");
//            jsonObject.put("height", imageHeight + "");
//            jsonObject.put("img", imgUrl);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsonObject;
//    }
//
//    public DiaryCommentDialogView(@NonNull Activity context, SumbitPhotoData sumbitPhotoDatas) {
//        super(context, R.style.MyDialog1);
//        this.mContext = context;
//        this.mDatas = sumbitPhotoDatas;
//        emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initView();
//    }
//
//
//    private void initView() {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.diary_comment_pop_buttom_dialog_new, null, false);
//        ButterKnife.bind(this, view);
//
//        mGson = GsonFactory.getSingletonGson();
//        inputContentEt.setFocusable(true);
//        inputContentEt.setFocusableInTouchMode(true);
//        inputContentEt.requestFocus();
//
//        findView(view);
//        initBottHttp();
//        setContentView(view);           //这行一定要写在前面
//
//        setCancelable(false);    //点击外部不可dismiss
//        setCanceledOnTouchOutside(true);
//        Window window = this.getWindow();
//        window.setGravity(Gravity.BOTTOM);
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setAttributes(params);
//        //解决dilaog中EditText无法弹出输入的问题
//        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        setOnShowListener(new OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialog) {
//                showKeyboard();
//            }
//        });
//    }
//
//    void findView(View view) {
//        iputly2.setOnClickListener(arg0 -> {
//            if (Util.isFastDoubleClick()) {
//                return;
//            }
//            sCPhotoRly.setVisibility(View.VISIBLE);
//            content1.setVisibility(View.GONE);
//            content2.setVisibility(View.GONE);
//            showKeyboard();
//        });
//
//        inputContentEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() == 0) {
//                    sumbitBt1.setTextColor(Color.parseColor("#CCCCCC"));
//                    sumbitBt1.setEnabled(false);
//                } else {
//                    sumbitBt1.setTextColor(Color.parseColor("#FF527F"));
//                    sumbitBt1.setEnabled(true);
//                }
//            }
//        });
//    }
//
//
//    /**
//     * 图片选择按钮是隐藏
//     *
//     * @param isGone:true:隐藏，false:显示
//     */
//    public void setPicturesChooseGone(boolean isGone) {
//        if (isGone) {
//            sCPhotoRlyContainer.setVisibility(View.GONE);
//            photoLy.setVisibility(View.GONE);
//        } else {
//            sCPhotoRlyContainer.setVisibility(View.VISIBLE);
//        }
//    }
//
//    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
//        // 获取这个图片的宽和高
//        float width = bgimage.getWidth();
//        float height = bgimage.getHeight();
//        // 创建操作图片用的matrix对象
//        Matrix matrix = new Matrix();
//        // 计算宽高缩放率
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//        // 缩放图片动作
//        matrix.postScale(scaleWidth, scaleHeight);
//        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
//        return bitmap;
//    }
//
//    private void initBottHttp() {
//        try {
//            initRly();
//            // 滑动图片
//            dp = mContext.getResources().getDimension(R.dimen.dp);
//            gridviewInit();
//            // 提交
//            sumbitBt1.setOnClickListener(arg0 -> {
//                if (Util.isFastDoubleClick()) {
//                    return;
//                }
//                View view = getWindow().peekDecorView();
//                if (view != null) {
//                    closeKeyboard(view);
//                }
//                sumbitContent2 = inputContentEt.getText().toString().trim();
//                Matcher matcher = emoji.matcher(sumbitContent2);
//                if (matcher.find()) {
//                    ToastUtils.toast(mContext, "暂不支持表情输入").show();
//                } else {
//                    if (sumbitContent2.length() > 0 && !"".equals(sumbitContent2)) {
//                        if (sumbitContent2.length() > 1) {
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
//                        } else {
//                            ToastUtils.toast(mContext, "亲，内容至少要大于1个字哟！").show();
//                        }
//                    } else {
//                        ToastUtils.toast(mContext, "内容不能为空").show();
//                    }
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 初始化图片列表形式
//     */
//    public void gridviewInit() {
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
//        rv_pic.setLayoutManager(linearLayoutManager);
//        adapter = new PicAdapter(mContext, mResults);
//        rv_pic.setAdapter(adapter);
//        if (mResults.size() == 0) {
//            photoLy.setVisibility(View.GONE);
//        } else {
//            if (mResults.size() >= 9) {
//                upPhotoIv.setBackgroundResource(R.drawable.pic_gray_dialog);
//                tv_photo.setTextColor(Color.parseColor("#CCCCCC"));
//            } else {
//                upPhotoIv.setBackgroundResource(R.drawable.pic_dialog);
//                tv_photo.setTextColor(Color.parseColor("#666666"));
//            }
//            photoLy.setVisibility(View.VISIBLE);
//        }
//    }
//
//    void toXIangce(int pos) {
//        if (pos == mResults.size()) {
//            String sdcardState = Environment.getExternalStorageState();
//
//            if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
//
//                // start multiple photos selector
//                Intent intent = new Intent(mContext, ImagesSelectorActivity.class);
//                // max number of images to be selected
//                intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 4);
//                // min size of image which will be shown; to filter tiny images (mainly icons)
//                intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
//                // show camera or not
//                intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
//                // pass current selected images as the initial value
//                intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
//                // start the selector
//                mContext.startActivityForResult(intent, REQUEST_CODE);
//            } else {
//                ToastUtils.toast(mContext, "sdcard已拔出，不能选择照片").show();
//            }
//        } else {
//            String paths = mResults.get(pos);
//            Intent it = new Intent(mContext, EditImageActivity.class);
//            it.putExtra(EditImageActivity.FILE_PATH, paths);
//            File outputFile = FileUtils.getEmptyFile("yuemei" + System.currentTimeMillis() + ".jpg");
//            it.putExtra(EditImageActivity.EXTRA_OUTPUT, outputFile.getAbsolutePath());
//            it.putExtra("pos", pos + "");
//            mContext.startActivityForResult(it, 9);
//        }
//    }
//
//
//    public class PicAdapter extends RecyclerView.Adapter<PicAdapter.ViewHolder> {
//
//        private Context context;
//        private List<String> data;
//
//        public PicAdapter(Context context, List<String> data) {
//            this.context = context;
//            this.data = data;
//
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(context).inflate(R.layout.item_published_grida1, parent, false);
//            return new ViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
//            Glide.with(mContext)
//                    .load(mResults.get(position))
//                    .transform(new CenterCrop(mContext), new GlideRoundTransform(mContext, Utils.dip2px(4)))
//                    .into(holder.item_grida_image);
//            holder.tv_index.setText(position + 1 + "/" + data.size());
//            holder.item_grida_bt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mResults.remove(position);
//                    if (mResults.size() >= 9) {
//                        upPhotoIv.setBackgroundResource(R.drawable.pic_gray_dialog);
//                        tv_photo.setTextColor(Color.parseColor("#CCCCCC"));
//                    } else {
//                        upPhotoIv.setBackgroundResource(R.drawable.pic_dialog);
//                        tv_photo.setTextColor(Color.parseColor("#666666"));
//                    }
//                    if (mResults.size() == 0) {
//                        photoLy.setVisibility(View.GONE);
//                    }
//                    notifyDataSetChanged();
//                }
//            });
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String paths = mResults.get(position);
//                    Intent it = new Intent(mContext, EditImageActivity.class);
//                    it.putExtra(EditImageActivity.FILE_PATH, paths);
//                    File outputFile = FileUtils.getEmptyFile("yuemei" + System.currentTimeMillis() + ".jpg");
//                    it.putExtra(EditImageActivity.EXTRA_OUTPUT, outputFile.getAbsolutePath());
//                    it.putExtra("pos", position + "");
//                    mContext.startActivityForResult(it, 9);
//
//                }
//            });
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return data.size();
//        }
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            private ImageView item_grida_image;
//            private Button item_grida_bt;
//            private TextView tv_index;
//
//            public ViewHolder(View itemView) {
//                super(itemView);
//                item_grida_image = itemView.findViewById(R.id.item_grida_image);
//                item_grida_bt = itemView.findViewById(R.id.item_grida_bt);
//                tv_index = itemView.findViewById(R.id.tv_index);
//            }
//        }
//    }
//
//
//    public void initRly() {
//        if (TextUtils.isEmpty(mDatas.getNickname())) {
//            inputContentEt.setHint("来都来了，不评论一句吗");
//        } else {
//            inputContentEt.setHint("回复:" + mDatas.getNickname());
//        }
//
//        sCPhotoRly.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if ("0".equals(mDatas.getAskorshare())) {
//                    content1.setVisibility(View.VISIBLE);
//                }
//                //版本判断
//                if (Build.VERSION.SDK_INT >= 23) {
//                    Acp.getInstance(mContext).request(new AcpOptions.Builder().setPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).build(), new AcpListener() {
//                        @Override
//                        public void onGranted() {
//                            selectImg();
//                        }
//
//                        @Override
//                        public void onDenied(List<String> permissions) {
//
//                        }
//                    });
//                } else {
//                    selectImg();
//                }
//            }
//        });
//    }
//
//    private void selectImg() {
//        String sdcardState = Environment.getExternalStorageState();
//        if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
//            // start multiple photos selector
//            Intent intent = new Intent(mContext, ImagesSelectorActivity.class);
//            // max number of images to be selected
//            intent.putExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, 9);
//            // min size of image which will be shown; to filter tiny images (mainly icons)
//            intent.putExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, 100000);
//            // show camera or not
//            intent.putExtra(SelectorSettings.SELECTOR_SHOW_CAMERA, true);
//            // pass current selected images as the initial value
//            intent.putStringArrayListExtra(SelectorSettings.SELECTOR_INITIAL_SELECTED_LIST, mResults);
//            // start the selector
//            mContext.startActivityForResult(intent, REQUEST_CODE);
//        } else {
//            MyToast.yuemeiToast(mContext, "sdcard已拔出，不能选择照片").show();
//        }
//    }
//
//    void upLoadFile() {
//        if (mResults.size() > 0) {
//            for (int i = 0; i < mResults.size(); i++) {
//                // 压缩图片
//                String pathS = Environment.getExternalStorageDirectory().toString() + "/YueMei";
//                File path1 = new File(pathS);// 建立这个路径的文件或文件夹
//                if (!path1.exists()) {// 如果不存在，就建立文件夹
//                    path1.mkdirs();
//                }
//                File file = new File(path1, "yuemei_" + i + System.currentTimeMillis() + ".JPEG");
//                String desPath = file.getPath();
//                FileUtils.compressPicture(mResults.get(i), desPath);
//                int[] mImageWidthHeight = FileUtils.getImageWidthHeight(desPath);
//                mKey = QiNuConfig.getKey();
//                if (i == mResults.size() - 1) {
//                    MyUploadImage.getMyUploadImage(mContext, mHandler, desPath, true).upCommentsList(mKey, mImageWidthHeight);
//                } else {
//                    MyUploadImage.getMyUploadImage(mContext, mHandler, desPath, false).upCommentsList(mKey, mImageWidthHeight);
//                }
//                mResultsFinal.add(desPath);
//            }
//        }
//    }
//
//
//    /**
//     * 上传图片文件
//     */
//    public void postFileQue() {
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
//    }
//
//    //提交接口回调
//    public interface OnSubmitClickListener {
//        void onSubmitClick(String id, ArrayList<String> imgLists, String content);
//    }
//
//    private OnSubmitClickListener onSubmitClickListener;
//
//    public void setOnSubmitClickListener(OnSubmitClickListener onSubmitClickListener) {
//        this.onSubmitClickListener = onSubmitClickListener;
//    }
//
//    /**
//     * 显示
//     */
//    public void showDialog() {
//        show();
//    }
//
//    public void setmResults(ArrayList<String> mResults) {
//        this.mResults = mResults;
//        upLoadFile();
//        isPhoto = true;
//
//    }
//
//
//    /**
//     * 打开键盘
//     */
//    public void showKeyboard() {
//        inputContentEt.postDelayed(() -> {
//            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        }, 200);
//    }
//
//    /**
//     * 关闭键盘
//     *
//     * @param view
//     */
//    private void closeKeyboard(View view) {
//        InputMethodManager inputmanger = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
//    }
//
//}
