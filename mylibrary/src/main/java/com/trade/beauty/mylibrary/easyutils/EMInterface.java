package com.trade.beauty.mylibrary.easyutils;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import com.trade.beauty.mylibrary.easyutils.emlisenter.MyCallBackImpl;
import com.trade.beauty.mylibrary.easyutils.emlisenter.MyEMMessageListener;

import java.util.List;

/**
 * Created by zhangzz on 2018/9/14
 * 环信各种api定义接口
 */
public interface EMInterface {
    void createAccount(String username, String pwd) throws HyphenateException;//注册

    void login(String userName, String password, MyCallBackImpl callback);//登录

    List<String> getAllContactsFromServer() throws HyphenateException;//获取好友列表

    void addContact(String toAddUsername, String reason) throws HyphenateException;//添加好友

    void acceptInvitation(String username) throws HyphenateException;//同意好友请求

    void declineInvitation(String username) throws HyphenateException;//拒绝好友请求

    void addMessageListener(MyEMMessageListener myEMMessageListener);//通过注册消息监听来接收消息。

    void removeMessageListener(MyEMMessageListener myEMMessageListener);//在不需要的时候移除listener，如在activity的onDestroy()时

    EMConversation getConversation(String username, EMConversation.EMConversationType type, boolean createIfNotExists);//初始化会话对象

    EMMessage createTxtSendMessage(String content, String toChatUsername);//发送文本消息

    void sendMessage(EMMessage message);//发送消息

    void createGroup(String groupName, String desc, String[] allMembers, String reason, EMGroupOptions option) throws HyphenateException;//创建群组

    List<EMGroup> getJoinedGroupsFromServer() throws HyphenateException;//获取群组列表

    List<EMGroup> getAllGroups();//从本地加载群组列表 -需异步处理

    EMGroup getGroup(String groupId);////根据群组ID从本地获取群组基本信息

    EMGroup getGroupFromServer(String groupId, boolean fetchMembers) throws HyphenateException;//根据群组ID从服务器获取群组基本信息, 如果fetchMembers为true，取群组信息的时候也会获取群成员

    void addUsersToGroup(String groupId, String[] newmembers);//群主加人调用此方法

    EMCursorResult<String> fetchGroupMembers(String groupId, String cursor, int pageSize) throws HyphenateException;//获取群成员方法 返回Cursor

    List<String> getGroupMembers(String groupId) throws HyphenateException;//获取群成员方法 返回List

    void joinGroup(String groupid) throws HyphenateException;//如果群开群是自由加入的，即group.isMembersOnly()为false，直接join，需异步处理

    void applyJoinToGroup(String groupid, String joinReason) throws HyphenateException;//需要申请和验证才能加入的，即group.isMembersOnly()为true，调用下面方法， 需异步处理

    EMCursorResult<EMGroupInfo>  getPublicGroupsFromServer(int pagesize, String cursor) throws HyphenateException;//获取公开群列表, pageSize为要取到的群组的数量，cursor用于告诉服务器从哪里开始取

    void logout(boolean unbindToken, MyCallBackImpl callback);//退出登录
}
