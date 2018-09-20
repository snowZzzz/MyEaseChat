package net.melove.demo.easechat.frg;

import android.os.Bundle;
import android.widget.TextView;

import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;

import net.melove.demo.easechat.R;
import com.trade.beauty.mylibrary.base.BaseFragment;
import com.trade.beauty.mylibrary.easyutils.EasyUtil;

import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by zhangzz on 2018/9/19
 */
public class GroupDetailFrg extends BaseFragment {
    private TextView tv_group_name;
    private TextView tv_group_owner;
    private TextView edit_reason;
    private TextView tv_group_desc;

    EMGroupInfo info;
    public static EMGroup searchedGroup;

    public static GroupDetailFrg getInstance(EMGroupInfo info) {
        GroupDetailFrg groupDetailFrg = new GroupDetailFrg();
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", info);
        groupDetailFrg.setArguments(bundle);
        return groupDetailFrg;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_group_detail_frgment;
    }

    @Override
    protected void initView() {
        info = (EMGroupInfo) getArguments().getSerializable("info");

        tv_group_name = fragmentView.findViewById(R.id.tv_group_name);
        tv_group_owner = fragmentView.findViewById(R.id.tv_group_owner);
        edit_reason = fragmentView.findViewById(R.id.edit_reason);
        tv_group_desc = fragmentView.findViewById(R.id.tv_group_desc);

        if (info != null) {
            Task.callInBackground(new Callable<EMGroup>() {
                @Override
                public EMGroup call() throws Exception {
                    try {
                        searchedGroup = EasyUtil.getEmManager().getGroupFromServer(info.getGroupId(), false);
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    return searchedGroup;
                }
            }).continueWith(new Continuation<EMGroup, Object>() {
                @Override
                public Object then(Task<EMGroup> task) throws Exception {
                    if (searchedGroup != null ) {
                        tv_group_name.setText(searchedGroup.getGroupName());
                        tv_group_owner.setText(searchedGroup.getOwner());
                        tv_group_desc.setText(searchedGroup.getDescription());
                    }
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR);
        }
    }
}
