package net.melove.demo.easechat.easyutils;

import android.content.Context;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

import net.melove.demo.easechat.easyutils.emlisenter.MyCallBackImpl;
import net.melove.demo.easechat.easyutils.emlisenter.MyContactListener;
import net.melove.demo.easechat.easyutils.emlisenter.MyEMMessageListener;

import java.util.List;

/**
 * Created by zhangzz on 2018/9/14
 * 环信各种api接口实现代理类 方便不同工具库的封装
 */
public class EMInterfaceImpl implements EMInterface {
    public static EMInterfaceImpl instance;

    public static void initEMInstance() {
        if (instance == null) {
            synchronized (EMInterfaceImpl.class) {
                if (instance == null) {
                    instance = new EMInterfaceImpl();
                    EasyUtil.EasyManagerSet.setUmManager(instance);
                }
            }
        }
    }

    /**
     * 注册用户名会自动转为小写字母，所以建议用户名均以小写注册
     *
     * @param username
     * @param pwd
     */
    @Override
    public void createAccount(String username, String pwd) throws HyphenateException {
        EMClient.getInstance().createAccount(username, pwd);//同步方法
    }

    /**
     * 登录方法
     *
     * @param userName
     * @param password
     * @param callback
     */
    @Override
    public void login(String userName, String password, MyCallBackImpl callback) {
        EMClient.getInstance().login(userName, password, callback);
    }

    /**
     * 获取好友列表
     *
     * @return
     * @throws HyphenateException
     */
    @Override
    public List<String> getAllContactsFromServer() throws HyphenateException {
        List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
        return usernames;
    }

    /**
     * 查找好友
     *
     * @return
     * @throws HyphenateException 参数为要添加的好友的username和添加理由
     */
    @Override
    public void addContact(String toAddUsername, String reason) throws HyphenateException {
        EMClient.getInstance().contactManager().addContact(toAddUsername, reason);
    }

    /**
     * 同意好友请求
     * @param username
     */
    @Override
    public void acceptInvitation(String username) throws HyphenateException {
        EMClient.getInstance().contactManager().acceptInvitation(username);
    }

    /**
     * 拒绝好友请求
     * @param username
     */
    @Override
    public void declineInvitation(String username) throws HyphenateException {
        EMClient.getInstance().contactManager().declineInvitation(username);
    }

    /**
     * 通过注册消息监听来接收消息。
     * @param myEMMessageListener
     */
    @Override
    public void addMessageListener(MyEMMessageListener myEMMessageListener) {
        EMClient.getInstance().chatManager().addMessageListener(myEMMessageListener);
    }

    /**
     * 在不需要的时候移除listener，如在activity的onDestroy()时
     * @param myEMMessageListener
     */
    @Override
    public void removeMessageListener(MyEMMessageListener myEMMessageListener) {
        EMClient.getInstance().chatManager().removeMessageListener(myEMMessageListener);
    }

    /**
     * 初始化会话对象，这里有三个参数么，
     * 第一个表示会话的当前聊天的 useranme 或者 groupid
     * 第二个是会话类型可以为空
     * 第三个表示如果会话不存在是否创建
     */
    @Override
    public EMConversation getConversation(String username, EMConversation.EMConversationType type, boolean createIfNotExists) {

        return EMClient.getInstance().chatManager().getConversation(username, null, createIfNotExists);
    }

    /**
     * //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id
     * @param content
     * @param toChatUsername
     * @return
     */
    @Override
    public EMMessage createTxtSendMessage(String content, String toChatUsername) {
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        return message;
    }

    /**
     * 发送消息
     * @param message
     */
    @Override
    public void sendMessage(EMMessage message) {
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 创建群组
     * @param groupName 群组名称
     * @param desc 群组简介
     * @param allMembers 群组初始成员，如果只有自己传空数组即可
     * @param reason 邀请成员加入的reason
     * @param option 群组类型选项，可以设置群组最大用户数(默认200)及群组类型
     *               option.inviteNeedConfirm表示邀请对方进群是否需要对方同意，默认是需要用户同意才能加群的。
     *               option.extField创建群时可以为群组设定扩展字段，方便个性化订制。
     * @return 创建好的group
     * @throws HyphenateException
     */
    @Override
    public void createGroup(String groupName, String desc, String[] allMembers, String reason, EMGroupOptions option) throws HyphenateException {
        EMClient.getInstance().groupManager().createGroup(groupName, desc, allMembers, reason, option);
    }

    /**
     * 从服务器获取自己加入的和创建的群组列表，此api获取的群组sdk会自动保存到内存和db。
     * 需异步处理;
     * @return
     */
    @Override
    public List<EMGroup> getJoinedGroupsFromServer() throws HyphenateException {
        return EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
    }

    /**
     * 从本地加载群组列表
     * @return
     */
    @Override
    public List<EMGroup> getAllGroups() {
        return EMClient.getInstance().groupManager().getAllGroups();
    }

    /**
     * 调用sdk的退出登录方法
     *
     * @param unbindToken 表示是否解绑推送的token 没有使用推送或者被踢都要传false
     * @param callback
     */
    @Override
    public void logout(boolean unbindToken, MyCallBackImpl callback) {
        EMClient.getInstance().logout(false, callback);
    }
}
