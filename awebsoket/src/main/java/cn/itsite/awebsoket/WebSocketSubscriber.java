package cn.itsite.awebsoket;

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

public abstract class WebSocketSubscriber implements Observer<WebSocketInfo> {
    private boolean hasOpened;
    protected Disposable disposable;

    @Override
    public final void onSubscribe(Disposable disposable) {
        this.disposable = disposable;
    }

    @Override
    public final void onNext(@NonNull WebSocketInfo webSocketInfo) {
        if (webSocketInfo.isOnOpen()) {
            hasOpened = true;
            onOpen(webSocketInfo.getWebSocket());
        } else if (webSocketInfo.getString() != null) {
            onMessage(webSocketInfo.getString());
        } else if (webSocketInfo.getByteString() != null) {
            onMessage(webSocketInfo.getByteString());
        } else if (webSocketInfo.isOnReconnect()) {
            onReconnect();
        } else if (webSocketInfo.isOnClosing()) {
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
