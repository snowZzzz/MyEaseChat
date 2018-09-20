package net.melove.demo.easechat.frg;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroupInfo;

import net.melove.demo.easechat.R;
import net.melove.demo.easechat.act.GroupSearchActivity;
import net.melove.demo.easechat.adapter.FriendListAdapter;
import net.melove.demo.easechat.adapter.GroupPublicListAdapter;
import net.melove.demo.easechat.base.BaseFragment;
import net.melove.demo.easechat.bean.FriendBean;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by zhangzz on 2018/9/19
 */
public class GroupSearchFragment extends BaseFragment implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private BaseQuickAdapter friendAdapter;
    private List<EMGroupInfo> groupsList;

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_group_frgment;
    }

    @Override
    protected void initView() {
        fragmentView.findViewById(R.id.btn_search).setOnClickListener(this);
        mRecyclerView = fragmentView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        friendAdapter = new GroupPublicListAdapter(R.layout.layout_group_public_item);
        friendAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(friendAdapter);

        Task.callInBackground(new Callable<List<EMGroupInfo>>() {
            @Override
            public List<EMGroupInfo> call() throws Exception {
                try {
                    EMCursorResult<EMGroupInfo> result = EMClient.getInstance().groupManager().getPublicGroupsFromServer(20, "");
                    groupsList = result.getData();
                } catch (Exception e){
                    e.getMessage();
                }
                return groupsList;
            }
        }).continueWith(new Continuation<List<EMGroupInfo>, Object>() {
            @Override
            public Object then(Task<List<EMGroupInfo>> task) throws Exception {
                if (groupsList != null && groupsList.size() > 0) {
                    friendAdapter.setNewData(groupsList);
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);


        friendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (groupsList != null && groupsList.size() > 0) {
                    EMGroupInfo emGroupInfo = groupsList.get(position);
                    ((GroupSearchActivity)(getActivity())).changeFrg(emGroupInfo);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                Toast.makeText(getActivity(), "查找群", Toast.LENGTH_LONG).show();
                break;
        }
    }

}
