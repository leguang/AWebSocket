package cn.itsite.awebsocket;

import android.util.Log;

/**
 * @author: leguang
 * @e-mail: langmanleguang@qq.com
 * @version: v0.0.0
 * @blog: https://github.com/leguang
 * @time: 2018/12/19 0019 14:31
 * @description:
 */
public class Utils {
    public static boolean isLog = false;

    public static void log(String tag, String msg) {
        if (isLog) {
            Log.d(tag, msg);
        }
    }
}
