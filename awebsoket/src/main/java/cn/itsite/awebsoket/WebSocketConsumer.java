package cn.itsite.awebsoket;

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
public abstract class WebSocketConsumer implements Consumer<WebSocketInfo> {

    @Override
    public void accept(WebSocketInfo webSocketInfo) throws Exception {
        if (webSocketInfo.isOnOpen()) {
            onOpen(webSocketInfo.getWebSocket());
        } else if (webSocketInfo.getString() != null) {
            onMessage(webSocketInfo.getString());
        } else if (webSocketInfo.getByteString() != null) {
            onMessage(webSocketInfo.getByteString());
        }
    }

    public abstract void onOpen(@NonNull WebSocket webSocket);

    public abstract void onMessage(@NonNull String text);

    public abstract void onMessage(@NonNull ByteString bytes);
}
