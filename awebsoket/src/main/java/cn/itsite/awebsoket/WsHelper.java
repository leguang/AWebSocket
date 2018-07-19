package cn.itsite.awebsoket;

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
public final class WsHelper {
    private static WsHelper instance;
    private RxWebSocket rxWebSocket;

    private WsHelper() {
    }

    public static WsHelper getInstance() {
        if (instance == null) {
            synchronized (WsHelper.class) {
                if (instance == null) {
                    instance = new WsHelper();
                }
            }
        }
        return instance;
    }

    public RxWebSocket getRxWebSocket() {
        return rxWebSocket;
    }

    public WsHelper setRxWebSocket(RxWebSocket rxWebSocket) {
        this.rxWebSocket = rxWebSocket;
        return this;
    }

    public WebSocket getWebSocket() {
        return getInstance()
                .getRxWebSocket()
                .getWebSocket();
    }

    public static Observable<WebSocketInfo> getObservable() {
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
        getInstance().getRxWebSocket().send(message);
    }

    /**
     * 如果url的WebSocket已经打开,可以直接调用这个发送消息.
     *
     * @param byteString
     */
    public static void send(ByteString byteString) {
        getInstance().getRxWebSocket().send(byteString);
    }

    /**
     * 不用关心url 的WebSocket是否打开,可以直接发送
     *
     * @param message
     */

    public static void asyncSend(String message) {
        getInstance().getRxWebSocket().asyncSend(message);
    }

    /**
     * 不用关心url 的WebSocket是否打开,可以直接发送
     *
     * @param byteString
     */
    public static void asyncSend(ByteString byteString) {
        getInstance().getRxWebSocket().asyncSend(byteString);
    }

    public static void clear() {
        getInstance().getRxWebSocket().clear();
    }

    public static int subscriberSize() {
        return getInstance().getRxWebSocket().subscriberSize();
    }

    public void add(Disposable disposable) {
        getInstance().getRxWebSocket().add(disposable);
    }

    public static Builder builder() {
        return new Builder();
    }

//    public static final class Builder {
//        protected long reconnectInterval = 1;
//        protected TimeUnit reconnectIntervalTimeUnit = TimeUnit.SECONDS;
//        protected boolean isLog = false;
//        protected String logTag = "WsHelper";
//        protected OkHttpClient client = new OkHttpClient.Builder()
//                .addInterceptor(new HttpHeaderInterceptor())
//                .retryOnConnectionFailure(true)
//                .build();
//        protected SSLSocketFactory sslSocketFactory;
//        protected X509TrustManager trustManager;
//        protected String url;
//        protected long period;
//        protected TimeUnit unit;
//
//        /**
//         * set your client
//         *
//         * @param client
//         */
//        public Builder setClient(OkHttpClient client) {
//            this.client = client;
//            return this;
//        }
//
//        /**
//         * wss support
//         *
//         * @param sslSocketFactory
//         * @param trustManager
//         */
//        public Builder setSSLSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
//            this.sslSocketFactory = sslSocketFactory;
//            this.trustManager = trustManager;
//            return this;
//        }
//
//        /**
//         * set reconnect interval
//         *
//         * @param Interval reconncet interval
//         * @param timeUnit unit
//         * @return
//         */
//        public Builder setReconnectInterval(long Interval, TimeUnit timeUnit) {
//            this.reconnectInterval = Interval;
//            this.reconnectIntervalTimeUnit = timeUnit;
//            return this;
//
//        }
//
//        public Builder setLog(boolean log) {
//            this.isLog = log;
//            return this;
//        }
//
//        public Builder setShowLog(boolean isLog, String logTag) {
//            this.isLog = isLog;
//            this.logTag = logTag;
//            return this;
//        }
//
//        public Builder setUrl(String url) {
//            this.url = url;
//            return this;
//        }
//
//        public Builder setHeartbeat(long period, TimeUnit unit) {
//            this.period = period;
//            this.unit = unit;
//            return this;
//        }
//
//        public RxWebSocket build() {
//            RxWebSocket instance = RxWebSocket.getInstance();
//            instance.setLog(this.isLog, this.logTag);
//            instance.setClient(this.client);
//            instance.setUrl(this.url);
//            instance.setReconnectInterval(this.reconnectInterval, this.reconnectIntervalTimeUnit);
//            if (this.sslSocketFactory != null && this.trustManager != null) {
//                instance.setSSLSocketFactory(this.sslSocketFactory, this.trustManager);
//            }
//
//            instance.heartbeat(period, unit);
//            return instance;
//        }
//
//        public Observable<WebSocketInfo> buildObservable() {
//            return build().getObservable();
//        }
//
//        public RxWebSocket connect() {
//            RxWebSocket instance = build();
//            instance.getObservable().subscribe();
//            return instance;
//        }
//    }
}
