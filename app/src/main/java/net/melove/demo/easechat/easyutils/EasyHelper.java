package net.melove.demo.easechat.easyutils;

import android.content.Context;
import android.content.Intent;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.EMLog;

import net.melove.demo.easechat.act.ChatActivity;
import net.melove.demo.easechat.bean.Constant;
import net.melove.demo.easechat.easyutils.emlisenter.MyContactListener;
import net.melove.demo.easechat.easyutils.emlisenter.MyEMMessageListener;

import java.util.List;

/**
 * Created by zhangzz on 2018/9/17
 * 初始化环信的基本配置
 * 添加各种事件回调
 */
public class EasyHelper {
    private final static String TAG = "EasyHelper";
    public static EasyHelper instance;
    private EaseNotifier easeNotifier;
    private MyEMMessageListener mEMMessageListener;
    private Context mContext;


    public synchronized static EasyHelper getInstance() {
        if (instance == null) {
            instance = new EasyHelper();
        }
        return instance;
    }

    public void init(Context context) {
        this.mContext = context;
        initEMOptions();
        easeNotifier = new EaseNotifier(context);
        addNotfPrivoder();
        registerMessageListener();
    }

    private void addNotfPrivoder() {
        //set options
        easeNotifier.setSettingsProvider(new EaseNotifier.EaseSettingsProvider() {

            @Override
            public boolean isSpeakerOpened() {
                return true;
            }

            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {
                return true;
            }

            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                return true;
            }

            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {
                return true;
            }
        });


        easeNotifier.setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //you can update title here
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //you can update icon here
                return 0;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // be used on notification bar, different text according the message type.
                return message.getFrom() + ": " + "tik";
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                // here you can customize the text.
                // return fromUsersNum + "contacts send " + messageNum + "messages to you";
                return null;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                // you can set what activity you want display when user click the notification
                Intent intent = new Intent(mContext, ChatActivity.class);
                // open calling activity if there is call

                EMMessage.ChatType chatType = message.getChatType();
                if (chatType == EMMessage.ChatType.Chat) { // single chat message
                    intent.putExtra("ec_chat_id", message.getFrom());
                    intent.putExtra("chatType", 1);
                }

                return intent;
            }
        });
    }

    protected void registerMessageListener() {
        mEMMessageListener = new MyEMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
                    // 判断一下是否是会议邀请
                    String confId = message.getStringAttribute(Constant.MSG_ATTR_CONF_ID, "");

                    // in background, do not refresh UI, notify it in notification bar
                    easeNotifier.notify(message);
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "receive command message");
                    //get message body
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action();//获取自定义action
                    //获取扩展属性 此处省略
                    //maybe you need get extension of your message
                    //message.getStringAttribute("");
                    EMLog.d(TAG, String.format("Command：action:%s,message:%s", action,message.toString()));
                }
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                EMLog.d(TAG, "onMessageRecalled:");
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                EMLog.d(TAG, "change:");
                EMLog.d(TAG, "change:" + change);
            }
        };

        EasyUtil.getEmManager().addMessageListener(mEMMessageListener);
    }

    public void initEMOptions() {
        // 调用初始化方法初始化sdk
        EMClient.getInstance().init(mContext, initOptions());
        // 设置开启debug模式
        EMClient.getInstance().setDebugMode(true);

        EMClient.getInstance().contactManager().setContactListener(new MyContactListener(mContext));
    }

    /**
     * SDK初始化的一些配置
     * 关于 EMOptions 可以参考官方的 API 文档
     * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1chat_1_1_e_m_options.html
     */
    private EMOptions initOptions() {
        EMOptions options = new EMOptions();
        // 设置Appkey，如果配置文件已经配置，这里可以不用设置
        // options.setAppKey("lzan13#hxsdkdemo");
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，
        options.setRequireDeliveryAck(true);
        // 设置是否根据服务器时间排序，默认是true
        options.setSortMessageByServerTime(false);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(true);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);
        // 设置google GCM推送id，国内可以不用设置
        // options.setGCMNumber(MLConstants.ML_GCM_NUMBER);
        // 设置集成小米推送的appid和appkey
        // options.setMipushConfig(MLConstants.ML_MI_APP_ID, MLConstants.ML_MI_APP_KEY);

        return options;
    }


}
