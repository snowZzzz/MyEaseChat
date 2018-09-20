package com.trade.beauty.mylibrary.bean;

/**
 * Created by zhangzz on 2018/9/18
 */
public class FriendBean {
    private String userName;
    private boolean isSelect;

    public FriendBean(String userName, boolean isSelect) {
        this.userName = userName;
        this.isSelect = isSelect;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
