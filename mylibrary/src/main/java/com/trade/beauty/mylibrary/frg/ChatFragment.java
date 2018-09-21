package com.trade.beauty.mylibrary.frg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.trade.beauty.mylibrary.R;
import com.trade.beauty.mylibrary.adapter.ChatAdapter;
import com.trade.beauty.mylibrary.base.BaseFragment;
import com.trade.beauty.mylibrary.bean.ChatModel;
import com.trade.beauty.mylibrary.bean.ItemModel;
import com.trade.beauty.mylibrary.easyutils.EaseNotifier;
import com.trade.beauty.mylibrary.easyutils.EasyUtil;
import com.trade.beauty.mylibrary.easyutils.emlisenter.MyEMMessageListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzz on 2018/9/20
 */
public class ChatFragment extends BaseFragment {
    // 聊天信息输入框
    private EditText mInputEdit;
    // 发送按钮
    private TextView mSendBtn;
    private TextView tvAdd;
    private LinearLayout layout_bottom;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    // 当前聊天的 ID
    private String mChatId;
    private String currUsername;
    private boolean isShowBottom = false;

    private MyEMMessageListener myEMMessageListener;//通过注册消息监听来接收消息。

    private EaseNotifier easeNotifier;

    ArrayList<ItemModel> models;

    // 当前会话对象
    private EMConversation mConversation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        // 移除消息监听
        EasyUtil.getEmManager().removeMessageListener(myEMMessageListener);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        models = new ArrayList<>();

        easeNotifier = new EaseNotifier(getActivity());

        // 获取当前会话的username(如果是群聊就是群id)
        mChatId = getActivity().getIntent().getStringExtra("ec_chat_id");
        currUsername = EMClient.getInstance().getCurrentUser();
        adapter = new ChatAdapter();
        initView();

        addMsgLisenter();
        initConversation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initView() {
        mInputEdit = (EditText) fragmentView.findViewById(R.id.et);
        mSendBtn = (TextView) fragmentView.findViewById(R.id.tvSend);
        tvAdd = fragmentView.findViewById(R.id.tvAdd);
        layout_bottom = fragmentView.findViewById(R.id.layout_bottom);

        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.recylerView);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // 设置发送按钮的点击事件
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mInputEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    mInputEdit.setText("");
                    // 创建一条新消息，第一个参数为消息内容，第二个为接受者username:为对方用户或者群聊的id
                    EMMessage message = EasyUtil.getEmManager().createTxtSendMessage(content, mChatId);

                    ArrayList<ItemModel> newLists = new ArrayList<>();
                    ChatModel model = new ChatModel();
                    model.setContent(content);
                    model.setIcon("http://img.my.csdn.net/uploads/201508/05/1438760758_6667.jpg");
                    newLists.add(new ItemModel(ItemModel.CHAT_B, model));
                    adapter.addAll(newLists);

                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);

                    hideKeyBorad(mInputEdit);

                    // 调用发送消息的方法
                    EasyUtil.getEmManager().sendMessage(message);
                    // 为消息设置回调
                    message.setMessageStatusCallback(new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            // 消息发送成功，打印下日志，正常操作应该去刷新ui
                            Log.i("yyl", "send message on success");
                        }

                        @Override
                        public void onError(int i, String s) {
                            // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui
                            Log.i("yyl", "send message on error " + i + " - " + s);
                        }

                        @Override
                        public void onProgress(int i, String s) {
                            // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
                        }
                    });
                }
            }
        });

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowBottom = !isShowBottom;
                layout_bottom.setVisibility(isShowBottom ? View.VISIBLE : View.GONE);
//                if (isShowBottom) {
//                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_in);
//                    LayoutAnimationController controller = new LayoutAnimationController(animation);
//                    layout_bottom.setLayoutAnimation(controller);
//                } else {
//                    Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_out);
//                    LayoutAnimationController controller = new LayoutAnimationController(animation);
//                    layout_bottom.setLayoutAnimation(controller);
//                }
            }
        });
    }

    private void hideKeyBorad(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }
//
//    private OnAddLisenterEvent mOnAddLisenterEvent;
//
//    public interface OnAddLisenterEvent {
//        void addEvent();
//    }
//
//    public void setOnAddLisenterEvent(OnAddLisenterEvent onAddLisenterEvent) {
//        this.mOnAddLisenterEvent = onAddLisenterEvent;
//    }

    public void addMsgLisenter() {
        myEMMessageListener = new MyEMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                super.onMessageReceived(messages);
                // 循环遍历当前收到的消息
                for (EMMessage message : messages) {
                    Log.i("yyl", "收到新消息:" + message);
                    if (message.getFrom().equals(mChatId)) {
                        // 设置消息为已读
                        mConversation.markMessageAsRead(message.getMsgId());
                        // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                        Message msg = mHandler.obtainMessage();
                        msg.what = 0;
                        msg.obj = message;
                        mHandler.sendMessage(msg);
                    } else {
                        // TODO 如果消息不是当前会话的消息发送通知栏通知
                        easeNotifier.notify(message);
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                super.onCmdMessageReceived(messages);
                for (int i = 0; i < messages.size(); i++) {
                    // 透传消息
                    EMMessage cmdMessage = messages.get(i);
                    EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
                    Log.i("yyl", "收到 CMD 透传消息" + body.action());
                }
            }
        };

        // 添加消息监听
        EasyUtil.getEmManager().addMessageListener(myEMMessageListener);
    }

    /**
     * 初始化会话对象，并且根据需要加载更多消息
     */
    public void initConversation() {

        /**
         * 初始化会话对象，这里有三个参数么，
         * 第一个表示会话的当前聊天的 useranme 或者 groupid
         * 第二个是会话类型可以为空
         * 第三个表示如果会话不存在是否创建
         */
        mConversation = EasyUtil.getEmManager().getConversation(mChatId, null, true);
////获取此会话的所有消息
//        List<EMMessage> messages = conversation.getAllMessages();
        // 设置当前会话未读数为 0
        mConversation.markAllMessagesAsRead();
        int count = mConversation.getAllMessages().size();
        if (count < mConversation.getAllMsgCount() && count < 20) {
            // 获取已经在列表中的最上边的一条消息id
            String msgId = mConversation.getAllMessages().get(0).getMsgId();
            // 分页加载更多消息，需要传递已经加载的消息的最上边一条消息的id，以及需要加载的消息的条数
            mConversation.loadMoreMsgFromDB(msgId, 20 - count);
        }
        // 打开聊天界面获取最后一条消息内容并显示
        if (mConversation.getAllMessages().size() > 0) {
            List<EMMessage> emMessages = mConversation.getAllMessages();
            for (EMMessage message : emMessages) {
                ChatModel model = new ChatModel();
                EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                if (!message.getFrom().equals(currUsername)) {
                    model.setContent(body.getMessage());
                    model.setIcon("http://img.my.csdn.net/uploads/201508/05/1438760758_3497.jpg");
                    models.add(new ItemModel(ItemModel.CHAT_A, model));
                } else {
                    model.setContent(body.getMessage());
                    model.setIcon("http://img.my.csdn.net/uploads/201508/05/1438760758_6667.jpg");
                    models.add(new ItemModel(ItemModel.CHAT_B, model));
                }
                adapter.replaceAll(models);
                recyclerView.scrollToPosition(models.size() - 1);
            }
        }
    }

    /**
     * 自定义实现Handler，主要用于刷新UI操作
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    EMMessage message = (EMMessage) msg.obj;
                    // 这里只是简单的demo，也只是测试文字消息的收发，所以直接将body转为EMTextMessageBody去获取内容
                    EMTextMessageBody body = (EMTextMessageBody) message.getBody();

                    ArrayList<ItemModel> newLists = new ArrayList<>();
                    ChatModel model = new ChatModel();
                    model.setContent(body.getMessage());
                    model.setIcon("http://img.my.csdn.net/uploads/201508/05/1438760758_3497.jpg");
                    newLists.add(new ItemModel(ItemModel.CHAT_A, model));
                    adapter.addAll(newLists);
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);

                    break;
            }
        }
    };

}
