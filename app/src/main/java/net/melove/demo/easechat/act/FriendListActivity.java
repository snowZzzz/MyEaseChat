package net.melove.demo.easechat.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hyphenate.chat.EMClient;

import net.melove.demo.easechat.R;
import net.melove.demo.easechat.adapter.FriendListAdapter;

import com.trade.beauty.mylibrary.bean.FriendBean;
import com.trade.beauty.mylibrary.easyutils.EasyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

public class FriendListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter friendAdapter;
    List<String> usernames = null;
    List<FriendBean> frindLists = null;
    private Button btn_sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        btn_sure = findViewById(R.id.btn_sure);

        mRecyclerView = findViewById(R.id.rv_friend_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        friendAdapter = new FriendListAdapter(R.layout.layout_friends_item);
        friendAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(friendAdapter);

        frindLists = new ArrayList<>();

        Task.callInBackground(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                usernames = EasyUtil.getEmManager().getAllContactsFromServer();
                return usernames;
            }
        }).continueWith(new Continuation<List<String>, Object>() {
            @Override
            public Object then(Task<List<String>> task) throws Exception {
//                ec_tv_invite_friend.setText(task.getResult().toString());
                if (usernames != null && usernames.size() > 0) {
                    for (String str : usernames) {
                        frindLists.add(new FriendBean(str, false));
                    }
                }
                friendAdapter.setNewData(frindLists);
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<FriendBean> frinds = friendAdapter.getData();
                List<String> vars = new ArrayList<>();
                if (frinds != null && frinds.size() > 0) {
                    for (FriendBean friendBean : frinds) {
                        if (friendBean.isSelect()) {
                            vars.add(friendBean.getUserName());
                        }
                    }
                }

                vars.add(EMClient.getInstance().getCurrentUser());
                over(vars);
            }
        });

        friendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(FriendListActivity.this, ChatActivity.class);
                intent.putExtra("ec_chat_id", frindLists.get(position).getUserName());
                startActivity(intent);
            }
        });
    }

    void over(List<String> vars) {
        setResult(RESULT_OK, new Intent().putExtra("newmembers", vars.toArray(new String[vars.size()])));
        finish();
    }
}
