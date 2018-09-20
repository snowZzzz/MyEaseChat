package net.melove.demo.easechat.act;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import net.melove.demo.easechat.R;
import com.trade.beauty.mylibrary.bean.ChatModel;
import com.trade.beauty.mylibrary.bean.ItemModel;
import com.trade.beauty.mylibrary.easyutils.EaseNotifier;
import com.trade.beauty.mylibrary.easyutils.EasyUtil;
import com.trade.beauty.mylibrary.easyutils.emlisenter.MyEMMessageListener;
import com.trade.beauty.mylibrary.frg.ChatFragment;
import com.trade.beauty.mylibrary.utils.FragmentManagerUtil;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements ChatFragment.OnAddLisenterEvent{
    FragmentManagerUtil fragmentManagerUtil;

    // 当前聊天的 ID
    private String mChatId;

    private MyEMMessageListener myEMMessageListener;//通过注册消息监听来接收消息。

    private EaseNotifier easeNotifier;

    ArrayList<ItemModel> models;

    // 当前会话对象
    private EMConversation mConversation;

    private String currUsername;

    ChatFragment chatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 获取当前会话的username(如果是群聊就是群id)
        mChatId = getIntent().getStringExtra("ec_chat_id");
        currUsername = EMClient.getInstance().getCurrentUser();

        fragmentManagerUtil = new FragmentManagerUtil(this, R.id.layout_frame);
        chatFragment = new ChatFragment();

        fragmentManagerUtil.chAddFrag(chatFragment, "", false);
        models = new ArrayList<>();

        easeNotifier = new EaseNotifier(this);

        chatFragment.setOnAddLisenterEvent(this);
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
                    chatFragment.addLists(newLists);
                    break;
            }
        }
    };

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
                chatFragment.refresh(models);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 移除消息监听
        EasyUtil.getEmManager().removeMessageListener(myEMMessageListener);
    }

    @Override
    public void addEvent() {
        addMsgLisenter();
        initConversation();
    }
}
