package net.melove.demo.easechat.easyutils;

/**
 * Created by zhangzz on 2018/9/14
 */
public class EasyUtil {
    /* 使用入口：获取EasyUI操作管理类实例
     * @return
     */
    public static EMInterface getEmManager() {
        if (EasyManagerSet.umManager == null) {
            EMInterfaceImpl.initEMInstance();
        }
        return EasyManagerSet.umManager;
    }

    public final static class EasyManagerSet {
        private static EMInterface umManager;

        public static void setUmManager(EMInterface manager) {
            EasyManagerSet.umManager = manager;
        }
    }
}
