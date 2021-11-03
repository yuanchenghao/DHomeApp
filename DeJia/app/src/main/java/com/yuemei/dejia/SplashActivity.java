package com.yuemei.dejia;


import com.yuemei.dejia.base.BaseActivity;
import com.yuemei.dejia.base.Constants;
import com.yuemei.dejia.utils.DialogUtils;
import com.yuemei.dejia.utils.KVUtils;
import com.zhangyue.we.x2c.ano.Xml;

import static com.yuemei.dejia.net.BuildConfig.registeredInterface;

//启动页
public class SplashActivity extends BaseActivity {
    @Xml(layouts = "activity_splash")
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        Constants.APP_STATUS = Constants.APP_STATUS_NORMAL;
        int activeCount = KVUtils.getInstance().decodeInt("is_first_active", 0);
        KVUtils.getInstance().encode("is_first_active", activeCount + 1);
        KVUtils.getInstance().decodeInt("privacy_agreement", 0);
        //接口注册
        registeredInterface();
        //版本判断 小于6.0直接跳过  或者 非首次 （为了处理合规问题 多次频繁请求权限）
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || (KVUtils.getInstance().decodeInt("is_first_active") != 1)){
//            Init();
//        } else {
//            mPermsissionData.add(new PermsissionData(R.drawable.phone_state_img, "电话信息", "用于提升账号安全，降低盗号及交易中可能遇到的风险", Manifest.permission.READ_PHONE_STATE));
////            String firstLoad = Cfg.loadStr(mContext, "isFirstLoad", "");
////            if (TextUtils.isEmpty(firstLoad)) {
////                Cfg.saveStr(mContext, "isFirstLoad", "yes");
////                mPermsissionData.add(new PermsissionData(R.drawable.location_img, "定位信息", "用于获得当前地区优惠商品和促销活动", Manifest.permission.ACCESS_COARSE_LOCATION));
////            }
//            mPermsissionData.add(new PermsissionData(R.drawable.external_img, "存储空间", "用于缓存列表，商品图片等信息，将加快您的访问速度并节省流量", Manifest.permission.WRITE_EXTERNAL_STORAGE));
//            mPermissionManager = new PermissionManager(this, new PermissionManager.PermissionUtilsInter() {
//                @Override
//                public List<PermsissionData> getApplyPermissions() {
//                    return mPermsissionData;
//                }
//
//                @Override
//                public void goInit() {
//                    Init();
//                }
//            });
//            boolean isOk = mPermissionManager.checkPermission();
//            if (isOk) {
//                Init();
//            }
//        }
        if (KVUtils.getInstance().decodeInt("privacy_agreement", 0) == 0) {
            DialogUtils.showCancellationDialog(mContext, "同意", "不同意", new DialogUtils.CallBack2() {
                @Override
                public void onYesClick() {
                    KVUtils.getInstance().encode("privacy_agreement", 1);
                    MainActivity.invoke(mContext);
                    finish();
                }

                @Override
                public void onNoClick() {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            });
        } else {
            MainActivity.invoke(mContext);
            finish();
        }
    }

    @Override
    protected void initData() {

    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Init();
//    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PermissionManager.REQUEST_SETTING_CODE) {
//            Init();
//        }
//    }

}
