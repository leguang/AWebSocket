package cn.itsite.awebsoket;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;

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
    public String logTag = "WsHelper";
    public OkHttpClient client = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .build();
    public SSLSocketFactory sslSocketFactory;
    public X509TrustManager trustManager;
    public String url;
    public long heartbeatInterval;
    public TimeUnit heartbeatIntervalUnit;

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

    public Builder setShowLog(boolean isLog, String logTag) {
        this.isLog = isLog;
        this.logTag = logTag;
        return this;
    }

    public Builder setUrl(String url) {
        this.url = url;
        return this;
    }

    public Builder setHeartbeat(long period, TimeUnit heartbeatUnit) {
        this.heartbeatInterval = period;
        this.heartbeatIntervalUnit = heartbeatUnit;
        return this;
    }

    public WsHelper build() {
        return WsHelper.getInstance()
                .setRxWebSocket(new RxWebSocket(this));
    }

    public Observable<WebSocketInfo> buildObservable() {
        return build().getObservable();
    }

    public void connect() {
        buildObservable().subscribe();
    }
}
