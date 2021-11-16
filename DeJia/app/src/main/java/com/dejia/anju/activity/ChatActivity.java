package com.dejia.anju.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.dejia.anju.R;
import com.dejia.anju.api.ChatIndexApi;
import com.dejia.anju.api.ChatSendApi;
import com.dejia.anju.api.GetMessageApi;
import com.dejia.anju.api.base.BaseCallBackListener;
import com.dejia.anju.base.BaseActivity;
import com.dejia.anju.model.ChatIndexInfo;
import com.dejia.anju.model.ChatMessageInfo;
import com.dejia.anju.model.MessageBean;
import com.dejia.anju.net.ServerData;
import com.dejia.anju.utils.JSONUtil;
import com.dejia.anju.utils.SoftKeyBoardListener;
import com.dejia.anju.utils.Util;
import com.dejia.anju.view.PullLoadMoreRecyclerView;
import com.dejia.anju.webSocket.IMManager;
import com.dejia.anju.webSocket.MessageCallBack;
import com.zhangyue.we.x2c.ano.Xml;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

//私信页面
public class ChatActivity extends BaseActivity implements View.OnClickListener, MessageCallBack {
    @BindView(R.id.rl_title)
    RelativeLayout rl_title;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.content_lv)
    PullLoadMoreRecyclerView content_lv;
    @BindView(R.id.ll_input)
    LinearLayout ll_input;
    @BindView(R.id.mess_et)
    EditText mess_et;
    @BindView(R.id.mess_send)
    Button mess_send;
    @BindView(R.id.keyboard_content)
    LinearLayout keyboard_content;
    @BindView(R.id.fl_root)
    FrameLayout fl_root;
    private boolean isFlag = false;
    //页码
    private int page;
    //最后一条消息时间
    private String msgtime;
    //获取页面信息
    private ChatIndexApi chatIndexApi;
    //获取私信消息
    private GetMessageApi getMessageApi;
    //发私信
    private ChatSendApi chatSendApi;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) rl_title.getLayoutParams();
        layoutParams.topMargin = statusbarHeight;
        rl_title.setLayoutParams(layoutParams);

        ViewGroup.MarginLayoutParams l = (ViewGroup.MarginLayoutParams) content_lv.getLayoutParams();
        l.topMargin = statusbarHeight + SizeUtils.dp2px(50);
        content_lv.setLayoutParams(l);

        ll_input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isFlag) {
                    Util.showKeyBoard(mContext, ll_input);
                    keyboard_content.setVisibility(View.GONE);
                    isFlag = true;
                }
                return false;
            }
        });
        mess_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    mess_send.setVisibility(View.VISIBLE);
                } else {
                    mess_send.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SoftKeyBoardListener.setListener(mContext, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                keyboard_content.setVisibility(View.GONE);
            }

            @Override
            public void keyBoardHide(int height) {

            }
        });

    }

    @Override
    protected void initData() {
        IMManager.setMessageCallBack(this);
        setMultiOnClickListener(ll_back);
        getChatIndexInfo();
    }

    //获取页面信息
    private void getChatIndexInfo() {
        Map<String, Object> maps = new HashMap<String,Object>();
        maps.put("id","1");
        chatIndexApi = new ChatIndexApi();
        chatIndexApi.getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
            @Override
            public void onSuccess(ServerData serverData) {
                if("1".equals(serverData.code)){
                    ChatIndexInfo chatIndexInfo = JSONUtil.TransformSingleBean(serverData.data,ChatIndexInfo.class);
                    tv_name.setText(chatIndexInfo.getTitle());
                    tv_type.setText(chatIndexInfo.getSubtitle());
                }
            }
        });
    }

    //获取私信信息
    private void getMessageInfo(){
        Map<String, Object> maps = new HashMap<String,Object>();
        maps.put("id","1");
        maps.put("page",page);
        maps.put("msgtime",msgtime);
        getMessageApi = new GetMessageApi();
        getMessageApi.getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
            @Override
            public void onSuccess(ServerData serverData) {
                if("1".equals(serverData.code)){
                    ChatMessageInfo chatMessageInfo = JSONUtil.TransformSingleBean(serverData.data,ChatMessageInfo.class);
                }
            }
        });
    }

    //发送私信消息
    private void sendMessage(){
        Map<String, Object> maps = new HashMap<String,Object>();
        maps.put("id","1");
        maps.put("class_id","1");//类型
        maps.put("content","发送的内容发送的内容");
        chatSendApi = new ChatSendApi();
        chatSendApi.getCallBack(mContext, maps, new BaseCallBackListener<ServerData>() {
            @Override
            public void onSuccess(ServerData serverData) {
                if("1".equals(serverData.code)){

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    public void receiveMessage(MessageBean.DataBean dataBean, String group_id) {

    }

    @Override
    public void onFocusCallBack(String txt) {

    }
}
