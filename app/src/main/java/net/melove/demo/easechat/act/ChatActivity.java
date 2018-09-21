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

public class ChatActivity extends AppCompatActivity{
    FragmentManagerUtil fragmentManagerUtil;

    // 当前聊天的 ID
    private String mChatId;

    private String currUsername;

    ChatFragment chatFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 获取当前会话的username(如果是群聊就是群id)
//        mChatId = getIntent().getStringExtra("ec_chat_id");

        fragmentManagerUtil = new FragmentManagerUtil(this, R.id.layout_frame);
        chatFragment = new ChatFragment();

        fragmentManagerUtil.chAddFrag(chatFragment, "", false);
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
