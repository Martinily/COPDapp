package com.funnyseals.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <pre>
 *     author : marin
 *     time   : 2018/11/27
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public enum imeUtil {
    ;

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("M月d日", Locale.CHINESE);

    private static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("H:m", Locale.CHINESE);

    /**
     * 一天的毫秒数
     */
    private static long DATE_LONG = 24 * 60 * 60 * 1000;

    /**
     * 获取格式化时间
     * 一天以内返回时间,一天以外返回日期
     */
    public static String getFormatTime(long time) {
        long now = new Date().getTime();

        if ((now - time) < DATE_LONG) {
            return TIME_FORMAT.format(new Date(time));
        }

        return DATE_FORMAT.format(new Date(time));
    }
}
