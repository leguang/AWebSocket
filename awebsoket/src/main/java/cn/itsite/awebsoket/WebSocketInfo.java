package cn.itsite.awebsoket;

import okhttp3.WebSocket;
import okio.ByteString;

/**
 * @author: leguang
 * @e-mail: langmanleguang@qq.com
 * @version: v0.0.0
 * @blog: https://github.com/leguang
 * @time: 2018/7/13 0013 10:41
 * @description:
 */
public class WebSocketInfo {
    private WebSocket mWebSocket;
    private String mString;
    private ByteString mByteString;
    private boolean onOpen;
    private boolean onReconnect;

    private WebSocketInfo() {
    }

    WebSocketInfo(WebSocket webSocket, boolean onOpen) {
        mWebSocket = webSocket;
        this.onOpen = onOpen;
    }

    WebSocketInfo(WebSocket webSocket, String mString) {
        mWebSocket = webSocket;
        this.mString = mString;
    }

    WebSocketInfo(WebSocket webSocket, ByteString byteString) {
        mWebSocket = webSocket;
        mByteString = byteString;
    }

    static WebSocketInfo createReconnect() {
        WebSocketInfo socketInfo = new WebSocketInfo();
        socketInfo.onReconnect = true;
        return socketInfo;
    }

    public WebSocket getWebSocket() {
        return mWebSocket;
    }

    public void setWebSocket(WebSocket webSocket) {
        mWebSocket = webSocket;
    }

    public String getString() {
        return mString;
    }

    public void setString(String string) {
        this.mString = string;
    }

    public ByteString getByteString() {
        return mByteString;
    }

    public void setByteString(ByteString byteString) {
        mByteString = byteString;
    }

    public boolean isOnOpen() {
        return onOpen;
    }

    public boolean isOnReconnect() {
        return onReconnect;
    }
}
