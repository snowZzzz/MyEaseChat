package net.melove.demo.easechat.easyutils;

import android.content.Context;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import net.melove.demo.easechat.easyutils.emlisenter.MyCallBackImpl;
import net.melove.demo.easechat.easyutils.emlisenter.MyEMMessageListener;

import java.util.List;

/**
 * Created by zhangzz on 2018/9/14
 */
public interface EMInterface {
    void initEMOptions(Context context);//sdk的初始化

    void createAccount(String username, String pwd) throws HyphenateException;//注册

    void login(String userName, String password,  MyCallBackImpl callback);//登录

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

    void logout(boolean unbindToken, MyCallBackImpl callback);//退出登录
}
