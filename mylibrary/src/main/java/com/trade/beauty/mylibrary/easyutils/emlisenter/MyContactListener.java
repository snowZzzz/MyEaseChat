package com.trade.beauty.mylibrary.easyutils.emlisenter;

import android.content.Context;

import com.hyphenate.EMContactListener;

import com.trade.beauty.mylibrary.bean.InviteMessage;
import com.trade.beauty.mylibrary.utils.DataCacheUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzz on 2018/9/15
 * <p>
 * 监听好友状态事件
 */
public class MyContactListener implements EMContactListener {
    private Context mContext;

    public MyContactListener(Context context) {
        this.mContext = context;
    }

    @Override
    public void onContactAdded(String username) {
        //增加了联系人时回调此方法
        username.trim();
    }

    @Override
    public void onContactDeleted(String username) {
        //被删除时回调此方法
        username.trim();
    }

    @Override
    public void onContactInvited(String username, String reason) {
        //收到好友邀请
        List<InviteMessage> msgs = DataCacheUtil.getInstance(mContext).getInviteMsgList();
        if (msgs == null || msgs.size() < 1) {
            msgs = new ArrayList<>();
        } else {
            for (InviteMessage inviteMessage : msgs) {
                if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
                    //已添加为好友 此时提示好友存在
                }
            }
        }
        // save invitation as message
        InviteMessage msg = new InviteMessage();
        msg.setFrom(username);
        msg.setTime(System.currentTimeMillis());
        msg.setReason(reason);
        // set invitation status
        msg.setStatus(InviteMessage.InviteMessageStatus.BEINVITEED);
        msgs.add(msg);
        DataCacheUtil.getInstance(mContext).saveInviteMsgList(msgs);
    }

    @Override
    public void onFriendRequestAccepted(String username) {
        //好友请求被同意
        username.trim();
    }

    @Override
    public void onFriendRequestDeclined(String username) {
        //好友请求被拒绝
        username.trim();
    }
}