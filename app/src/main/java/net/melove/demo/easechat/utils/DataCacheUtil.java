package net.melove.demo.easechat.utils;

import android.content.Context;

import com.google.gson.reflect.TypeToken;

import net.melove.demo.easechat.bean.InviteMessage;
import net.melove.demo.easechat.bean.User;

import java.util.List;

/**
 * Created by zzz on 2018/9/4.
 * 本地缓存sp的工具类
 */

public class DataCacheUtil {

    private SharedPreferencesUtils mSharedPreferences;
    private static DataCacheUtil mInstance;

    /**
     * 通过单例模式实例化对象
     * @param context
     */
    private DataCacheUtil(Context context){
        mSharedPreferences=new SharedPreferencesUtils(context);
    }

    /**
     * 调用构造函数时，传入context.getApplicationContext()
     * @param context
     * @return
     */
    public static DataCacheUtil getInstance(Context context){
        if (mInstance==null){
            synchronized (DataCacheUtil.class){
                if (mInstance==null){
                    mInstance=new DataCacheUtil(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    /**
     * 保存单个用户数据存储
     * @param str
     */
    public void saveString(String key, String str){
        mSharedPreferences.saveObject(key,str);
    }

    /**
     * 获取单个用户数据
     * @return
     */
    public String getString(String key){
        return mSharedPreferences.getString(key, "");
    }

    /**
     * 保存单个用户数据存储
     * @param user
     */
    public void saveUser(User user){
        mSharedPreferences.saveObject("user_info",user);
    }

    /**
     * 获取单个用户数据
     * @return
     */
    public User getUser(){
        return mSharedPreferences.getObject("user_info",User.class);
    }


    /**
     * 保存List<User>对象
     * @param users
     */
    public void saveUserList(List<User> users){
        mSharedPreferences.saveList("user_list_info",users);
    }

    /**
     * 获取List<User>对象
     * @return
     */
    public List<User> getUserList(){
        return  mSharedPreferences.getList("user_list_info",new TypeToken<List<User>>(){});
    }

    /**
     * 保存List<InviteMessage>对象
     * @param users
     */
    public void saveInviteMsgList(List<InviteMessage> users){
        mSharedPreferences.saveList("ivmsg_list_info",users);
    }

    /**
     * 获取List<InviteMessage>对象
     * @return
     */
    public List<InviteMessage> getInviteMsgList(){
        return  mSharedPreferences.getList("ivmsg_list_info",new TypeToken<List<InviteMessage>>(){});
    }
}
