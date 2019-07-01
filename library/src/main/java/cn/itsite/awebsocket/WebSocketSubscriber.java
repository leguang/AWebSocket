package cn.itsite.awebsocket;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
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

public abstract class WebSocketSubscriber implements Observer<WebSocketWrapper> {
    private boolean hasOpened;
    protected Disposable disposable;

    @Override
    public final void onSubscribe(Disposable disposable) {
        this.disposable = disposable;
    }

    @Override
    public final void onNext(@NonNull WebSocketWrapper webSocketWrapper) {
        if (webSocketWrapper.isOnOpen()) {
            hasOpened = true;
            onOpen(webSocketWrapper.getWebSocket());
        } else if (webSocketWrapper.getString() != null) {
            onMessage(webSocketWrapper.getString());
        } else if (webSocketWrapper.getByteString() != null) {
            onMessage(webSocketWrapper.getByteString());
        } else if (webSocketWrapper.isOnReconnect()) {
            onReconnect();
        } else if (webSocketWrapper.isOnClosing()) {
            onClosing();
        }
    }

    protected void onOpen(@NonNull WebSocket webSocket) {
    }

    protected void onMessage(@NonNull String text) {
    }

    protected void onMessage(@NonNull ByteString byteString) {
    }

    protected void onReconnect() {
    }

    protected void onClosing() {
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    public void dispose() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
