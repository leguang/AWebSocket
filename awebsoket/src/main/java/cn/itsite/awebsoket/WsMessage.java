package cn.itsite.awebsoket;

import android.util.Log;

import com.google.gson.GsonBuilder;

/**
 * @author: leguang
 * @e-mail: langmanleguang@qq.com
 * @version: v0.0.0
 * @blog: https://github.com/leguang
 * @time: 2018/7/13 0013 19:48
 * @description: 请求和响应都是统一格式，因此封装出一个消息对象。
 */
public class WsMessage<T> {
    public static final String URI_CONNECT = "v1/connect";
    public static final String URI_DISCONNECT = "v1/disconnect";
    public static final String URI_HEARTBEAT = "v1/heartbeat";
    public static final String URI_RECEIVE = "v1/receive";
    public static final String URI_NOTIFY = "v1/notify";
    public static final String URI_ADVERTISE = "v1/advertise";
    public static final String URI_IM = "v1/im";
    public static final String URI_EXTRA = "v1/extra";

    public String message;
    public String uid;
    public String uri;
    public int code;
    public T data;

    public WsMessage() {
    }

    public WsMessage(String uri) {
        this.uri = uri;
    }

    public WsMessage(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static WsMessage getInstance() {
        return new WsMessage();
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        String json = new GsonBuilder().setPrettyPrinting().create().toJson(this);
        Log.e("WsMessage-->", json);
        return json;
    }

    public static String heartbeat() {
        return new WsMessage<>(URI_HEARTBEAT).toString();
    }
}
