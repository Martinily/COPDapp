package com.funnyseals.app.util;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/12/17
 *     desc   : 限制点击监听只一段时间内只相应一次
 *     version: 1.0
 * </pre>
 */
public class BtnClickLimitUtil {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
}
