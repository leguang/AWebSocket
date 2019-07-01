package cn.itsite.awebsocket;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
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
public final class WebSocketManager {
    private static WebSocketManager instance;
    private RxWebSocket rxWebSocket;

    private WebSocketManager() {
    }

    public static WebSocketManager getInstance() {
        if (instance == null) {
            synchronized (WebSocketManager.class) {
                if (instance == null) {
                    instance = new WebSocketManager();
                }
            }
        }
        return instance;
    }

    public RxWebSocket getRxWebSocket() {
        return rxWebSocket;
    }

    public WebSocketManager setRxWebSocket(RxWebSocket rxWebSocket) {
        this.rxWebSocket = rxWebSocket;
        return this;
    }

    public WebSocket getWebSocket() {
        if (getInstance().getRxWebSocket() == null) {
            return null;
        }
        return getInstance()
                .getRxWebSocket()
                .getWebSocket();
    }

    public static Observable<WebSocketWrapper> getObservable() {
        if (getInstance().getRxWebSocket() == null) {
            return null;
        }
        return getInstance()
                .getRxWebSocket()
                .getObservable();
    }

    /**
     * 如果url的WebSocket已经打开,可以直接调用这个发送消息.
     *
     * @param message
     */
    public static void send(String message) {
        if (getInstance().getRxWebSocket() != null) {
            getInstance().getRxWebSocket().send(message);
        }
    }

    /**
     * 如果url的WebSocket已经打开,可以直接调用这个发送消息.
     *
     * @param byteString
     */
    public static void send(ByteString byteString) {
        if (getInstance().getRxWebSocket() != null) {
            getInstance().getRxWebSocket().send(byteString);
        }
    }

    /**
     * 不用关心url 的WebSocket是否打开,可以直接发送
     *
     * @param message
     */

    public static void asyncSend(String message) {
        if (getInstance().getRxWebSocket() != null) {
            getInstance().getRxWebSocket().asyncSend(message);
        }
    }

    /**
     * 不用关心url 的WebSocket是否打开,可以直接发送
     *
     * @param byteString
     */
    public static void asyncSend(ByteString byteString) {
        if (getInstance().getRxWebSocket() != null) {
            getInstance().getRxWebSocket().asyncSend(byteString);
        }
    }

    public static void clear() {
        if (getInstance().getRxWebSocket() != null) {
            getInstance().getRxWebSocket().clear();
        }
    }

    public static int subscriberSize() {
        RxWebSocket rxWebSocket = getInstance().getRxWebSocket();
        return rxWebSocket == null ? 0 : rxWebSocket.subscriberSize();
    }

    public static void add(Disposable disposable) {
        if (getInstance().getRxWebSocket() == null) {
            return;
        }
        getInstance().getRxWebSocket().add(disposable);
    }

    public static Builder builder() {
        return new Builder();
    }
}
