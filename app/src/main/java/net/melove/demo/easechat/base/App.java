package net.melove.demo.easechat.base;

import com.trade.beauty.mylibrary.base.BaseApplication;

/**
 * Created by zhangzz on 2018/9/20
 */
public class App extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        // 调用初始化方法初始化sdk
        EasyHelper.getInstance().init(mContext);
    }
}
