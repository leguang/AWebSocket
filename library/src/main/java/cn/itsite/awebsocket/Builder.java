package cn.itsite.awebsocket;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author: leguang
 * @e-mail: langmanleguang@qq.com
 * @version: v0.0.0
 * @blog: https://github.com/leguang
 * @time: 2018/7/15 0015 19:26
 * @description:
 */
public class Builder {
    public long reconnectInterval = 1;
    public TimeUnit reconnectIntervalTimeUnit = TimeUnit.SECONDS;
    public boolean isLog = false;
    public String logTag = "WebSocketManager";
    public OkHttpClient client = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .build();
    public SSLSocketFactory sslSocketFactory;
    public X509TrustManager trustManager;
    public String url;
    public Request request;
    public long heartbeatInterval;
    public TimeUnit heartbeatIntervalUnit;
    public String ping;

    /**
     * set your client
     *
     * @param client
     */
    public Builder setClient(OkHttpClient client) {
        this.client = client;
        return this;
    }

    /**
     * wss support
     *
     * @param sslSocketFactory
     * @param trustManager
     */
    public Builder setSSLSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
        this.sslSocketFactory = sslSocketFactory;
        this.trustManager = trustManager;
        return this;
    }

    /**
     * set reconnect interval
     *
     * @param Interval reconncet interval
     * @param timeUnit unit
     * @return
     */
    public Builder setReconnectInterval(long Interval, TimeUnit timeUnit) {
        this.reconnectInterval = Interval;
        this.reconnectIntervalTimeUnit = timeUnit;
        return this;
    }

    public Builder setLog(boolean log) {
        this.isLog = log;
        return this;
    }

    public Builder setLog(boolean isLog, String logTag) {
        this.isLog = isLog;
        this.logTag = logTag;
        return this;
    }

    public Builder setUrl(String url) {
        this.url = url;
        return this;
    }

    public Builder setRequest(Request request) {
        this.request = request;
        return this;
    }

    public Builder setHeartbeat(long period, TimeUnit heartbeatUnit, String ping) {
        this.heartbeatInterval = period;
        this.heartbeatIntervalUnit = heartbeatUnit;
        this.ping = ping;
        return this;
    }

    public WebSocketManager build() {
        return WebSocketManager.getInstance()
                .setRxWebSocket(new RxWebSocket(this));
    }

    public Observable<WebSocketWrapper> buildObservable() {
        return build().getObservable();
    }

    public Observable<WebSocketWrapper> connect() {
        Observable<WebSocketWrapper> observable = buildObservable();
        observable.subscribe();
        return observable;
    }
}
