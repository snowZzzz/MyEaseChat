package net.melove.demo.easechat.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.util.EMLog;

import net.melove.demo.easechat.R;
import net.melove.demo.easechat.adapter.FriendListAdapter;
import net.melove.demo.easechat.bean.FriendBean;
import net.melove.demo.easechat.easyutils.EasyUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

public class GroupDetailActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter friendAdapter;
    private String groupId;
    private EMGroup group;

    private List<String> memberList = Collections.synchronizedList(new ArrayList<String>());
    private List<FriendBean> frindLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        groupId = getIntent().getStringExtra("groupId");
        group = EasyUtil.getEmManager().getGroup(groupId);


        mRecyclerView = findViewById(R.id.recylerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        friendAdapter = new FriendListAdapter(R.layout.layout_friends_item);
        friendAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(friendAdapter);

        Task.callInBackground(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                try {
                    memberList.clear();
                    memberList = EasyUtil.getEmManager().getGroupMembers(groupId);
                } catch (Exception e){
                    e.getMessage();
                }
                return memberList;
            }
        }).continueWith(new Continuation<List<String>, Object>() {
            @Override
            public Object then(Task<List<String>> task) throws Exception {
                if (memberList != null && memberList.size() > 0) {
                    for (String str : memberList) {
                        frindLists.add(new FriendBean(str, false));
                    }
                }
                friendAdapter.setNewData(frindLists);
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }
}
