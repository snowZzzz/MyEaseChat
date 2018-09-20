package net.melove.demo.easechat.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import net.melove.demo.easechat.R;
import net.melove.demo.easechat.adapter.ConverAdapter;

import com.trade.beauty.mylibrary.easyutils.EasyUtil;
import com.trade.beauty.mylibrary.easyutils.emlisenter.MyEMMessageListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ConversationActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    BaseQuickAdapter converAdapter;
    MyEMMessageListener myEMMessageListener;
    List<EMConversation> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        mRecyclerView = findViewById(R.id.rv_force_record);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        converAdapter = new ConverAdapter(R.layout.layout_records_item);
        converAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(converAdapter);

        converAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (list != null && list.size() > 0) {
                    Intent intent = new Intent(ConversationActivity.this, ChatActivity.class);
                    intent.putExtra("ec_chat_id", list.get(position).conversationId());
                    startActivity(intent);
                }
            }
        });

        myEMMessageListener = new MyEMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                super.onMessageReceived(messages);
                // notify new message
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list.clear();
                        list = loadConversationList();
                        converAdapter.setNewData(list);
                    }
                });
            }
        };


        findViewById(R.id.tv_get_convertions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        EasyUtil.getEmManager().addMessageListener(myEMMessageListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EasyUtil.getEmManager().removeMessageListener(myEMMessageListener);
    }

    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

}
