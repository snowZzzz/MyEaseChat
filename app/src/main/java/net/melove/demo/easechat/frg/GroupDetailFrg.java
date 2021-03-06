package net.melove.demo.easechat.frg;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;

import net.melove.demo.easechat.R;

import com.hyphenate.exceptions.HyphenateException;
import com.trade.beauty.mylibrary.base.BaseFragment;
import com.trade.beauty.mylibrary.easyutils.EasyUtil;
import com.trade.beauty.mylibrary.utils.ToastUtils;

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
    private Button btn_join;

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
        btn_join = fragmentView.findViewById(R.id.btn_join);

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
                    if (searchedGroup != null) {
                        tv_group_name.setText(searchedGroup.getGroupName());
                        tv_group_owner.setText(searchedGroup.getOwner());
                        tv_group_desc.setText(searchedGroup.getDescription());
                    }
                    return null;
                }
            }, Task.UI_THREAD_EXECUTOR);
        }


        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task.callInBackground(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        try {
                            EasyUtil.getEmManager().joinGroup(info.getGroupId());
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            ToastUtils.showCenter(getActivity(), "加入失败");
                        }
                        ToastUtils.showCenter(getActivity(), "加入成功");
                        return null;
                    }
                }).continueWith(new Continuation<Void, Object>() {
                    @Override
                    public Object then(Task<Void> task) throws Exception {
                        return null;
                    }
                }, Task.UI_THREAD_EXECUTOR);

            }
        });
    }
}
