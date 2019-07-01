package cn.itsite.awebsocket;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
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
public abstract class WebSocketConsumer implements Consumer<WebSocketWrapper> {

    @Override
    public void accept(WebSocketWrapper webSocketWrapper) throws Exception {
        if (webSocketWrapper.isOnOpen()) {
            onOpen(webSocketWrapper.getWebSocket());
        } else if (webSocketWrapper.getString() != null) {
            onMessage(webSocketWrapper.getString());
        } else if (webSocketWrapper.getByteString() != null) {
            onMessage(webSocketWrapper.getByteString());
        }
    }

    public abstract void onOpen(@NonNull WebSocket webSocket);

    public abstract void onMessage(@NonNull String text);

    public abstract void onMessage(@NonNull ByteString bytes);
}
