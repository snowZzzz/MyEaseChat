package net.melove.demo.easechat.act;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import net.melove.demo.easechat.R;
import net.melove.demo.easechat.adapter.ChatAdapter;
import net.melove.demo.easechat.bean.ChatModel;
import net.melove.demo.easechat.bean.ItemModel;
import net.melove.demo.easechat.easyutils.EaseNotifier;
import net.melove.demo.easechat.easyutils.EasyUtil;
import net.melove.demo.easechat.easyutils.emlisenter.MyEMMessageListener;

import java.util.ArrayList;
import java.util.List;

public class ChatGroupActivity extends AppCompatActivity {

    // 聊天信息输入框
    private EditText mInputEdit;
    // 发送按钮
    private TextView mSendBtn;
    private Button btn_members;
    private TextView tv_group_name;

    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    ArrayList<ItemModel> models;

    // 当前聊天的 ID
    private String mChatId;

    private String groupName;
    // 当前会话对象
    private EMConversation mConversation;

    private MyEMMessageListener myEMMessageListener;//通过注册消息监听来接收消息。

    private EaseNotifier easeNotifier;

    private String currUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group);

        // 获取当前会话的username(如果是群聊就是群id)
        mChatId = getIntent().getStringExtra("ec_chat_group_id");
        groupName = getIntent().getStringExtra("ec_chat_group_name");

        currUsername = EMClient.getInstance().getCurrentUser();

        models = new ArrayList<>();

        initAddMsgLis();
        initView();
        initConversation();

        easeNotifier = new EaseNotifier(this);
    }


    /**
     * 初始化界面
     */
    private void initView() {
        mInputEdit = (EditText) findViewById(R.id.et);
        mSendBtn = (TextView) findViewById(R.id.tvSend);
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        btn_members = findViewById(R.id.btn_members);
        tv_group_name = findViewById(R.id.tv_group_name);
        tv_group_name.setText(groupName);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter = new ChatAdapter());

        // 设置发送按钮的点击事件
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mInputEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    mInputEdit.setText("");
                    // 创建一条新消息，第一个参数为消息内容，第二个为接受者username:为对方用户或者群聊的id
                    EMMessage message = EasyUtil.getEmManager().createTxtSendMessage(content, mChatId);
                    message.setChatType(EMMessage.ChatType.GroupChat);
                    //如果是群聊，设置chattype，默认是单聊
//                    if (chatType == CHATTYPE_GROUP)
//                        message.setChatType(ChatType.GroupChat);

                    // 将新的消息内容和时间加入到下边
//                    mContentText.setText(mContentText.getText()
//                        + "\n发送："
//                        + content
//                        + " - time: "
//                        + message.getMsgTime());
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

        btn_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatGroupActivity.this, GroupDetailActivity.class);
                intent.putExtra("groupId", mChatId);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化会话对象，并且根据需要加载更多消息
     */
    private void initConversation() {

        /**
         * 初始化会话对象，这里有三个参数么，
         * 第一个表示会话的当前聊天的 useranme 或者 groupid
         * 第二个是会话类型可以为空
         * 第三个表示如果会话不存在是否创建
         */
        mConversation = EasyUtil.getEmManager().getConversation(mChatId, null, true);
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
                if (message.getTo().equals(mChatId) && !message.getFrom().equals(currUsername)) {
                    model.setContent(body.getMessage());
                    model.setIcon("http://img.my.csdn.net/uploads/201508/05/1438760758_3497.jpg");
                    models.add(new ItemModel(ItemModel.CHAT_A, model));
                } else if (message.getTo().equals(mChatId)){
                    model.setContent(body.getMessage());
                    model.setIcon("http://img.my.csdn.net/uploads/201508/05/1438760758_6667.jpg");
                    models.add(new ItemModel(ItemModel.CHAT_B, model));
                }
//                message = mConversation.getLastMessage();
                // 将消息内容和时间显示出来
//            mContentText.setText(
//                "聊天记录：" + body.getMessage() + " - time: " + mConversation.getLastMessage()
//                    .getMsgTime());
            }

            adapter.replaceAll(models);
            recyclerView.scrollToPosition(models.size() - 1);
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
                    // 将新的消息内容和时间加入到下边
//                mContentText.setText(mContentText.getText()
//                    + "\n接收："
//                    + body.getMessage()
//                    + " - time: "
//                    + message.getMsgTime());
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // 添加消息监听
        EasyUtil.getEmManager().addMessageListener(myEMMessageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 移除消息监听
        EasyUtil.getEmManager().removeMessageListener(myEMMessageListener);
    }

    private void initAddMsgLis() {
        myEMMessageListener = new MyEMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                super.onMessageReceived(messages);
                // 循环遍历当前收到的消息
                for (EMMessage message : messages) {
                    Log.i("yyl", "收到新消息:" + message);
                    if (message.getTo().equals(mChatId)) {
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
    }

    private void hideKeyBorad(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }
}
