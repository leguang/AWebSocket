package cn.itsite.awebsocket;

import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


/**
 * @author: leguang
 * @e-mail: langmanleguang@qq.com
 * @version: v0.0.0
 * @blog: https://github.com/leguang
 * @time: 2018/7/13 0013 10:41
 * @description:
 */
public class RxWebSocket {
    private String logTag = RxWebSocket.class.getSimpleName();
    private OkHttpClient client;
    private Request request;
    private WebSocket webSocket;
    private Observable<WebSocketInfo> observable;
    private boolean isLog;
    private long reconnectInterval = 1;
    private TimeUnit reconnectIntervalTimeUnit = TimeUnit.SECONDS;
    private String url;
    private SSLSocketFactory sslSocketFactory;
    private X509TrustManager trustManager;
    private long heartbeatInterval;
    private TimeUnit heartbeatIntervalUnit;
    private String ping;
    private CompositeDisposable mCompositeDisposable;
    private Scheduler.Worker worker = Schedulers.io().createWorker();
    private Disposable heartbeatDisposable;

    private RxWebSocket() {
    }

    public RxWebSocket(Builder builder) {
        Utils.isLog = builder.isLog;
        reconnectInterval = builder.reconnectInterval;
        reconnectIntervalTimeUnit = builder.reconnectIntervalTimeUnit;
        logTag = builder.logTag;
        client = builder.client;
        sslSocketFactory = builder.sslSocketFactory;
        trustManager = builder.trustManager;
        url = builder.url;
        request = builder.request;
        heartbeatInterval = builder.heartbeatInterval;
        heartbeatIntervalUnit = builder.heartbeatIntervalUnit;
        ping = builder.ping;
        getObservable();
    }

    /**
     * set your client
     *
     * @param client
     */
    public void setClient(OkHttpClient client) {
        if (client == null) {
            throw new NullPointerException(" Are you kidding me ? client == null");
        }
        this.client = client;
    }

    public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
        client = getClient().newBuilder()
                .sslSocketFactory(sslSocketFactory, trustManager)
                .build();
    }

    public void setHeartbeatInterval(long heartbeatInterval, TimeUnit heartbeatIntervalUnit) {
        this.heartbeatInterval = heartbeatInterval;
        this.heartbeatIntervalUnit = heartbeatIntervalUnit;
    }

    /**
     * 日志开关
     *
     * @param isLog
     */
    public void setLog(boolean isLog) {
        this.isLog = isLog;
    }

    /**
     * 设置日志开关和TAG
     *
     * @param showLog
     * @param logTag
     */
    public void setLog(boolean showLog, String logTag) {
        setLog(showLog);
        this.logTag = logTag;
    }

    /**
     * 设置间隔可单位
     *
     * @param interval
     * @param timeUnit
     */
    public void setReconnectInterval(long interval, TimeUnit timeUnit) {
        this.reconnectInterval = interval;
        this.reconnectIntervalTimeUnit = timeUnit;
    }

    /**
     * @param timeout  The WebSocket will be reconnected after the specified time interval is not "onMessage",
     *                 在指定时间间隔后没有收到消息就会重连WebSocket,为了适配小米平板,因为小米平板断网后,不会发送错误通知
     * @param timeUnit unit
     * @return
     */
    public Observable<WebSocketInfo> getObservable(final long timeout, final TimeUnit timeUnit) {
        if (observable == null) {
            observable = Observable.create(new WebSocketOnSubscribe())
                    //自动重连
                    .timeout(timeout, timeUnit)
                    .retry(new Predicate<Throwable>() {
                        @Override
                        public boolean test(Throwable throwable) throws Exception {
                            return throwable instanceof IOException || throwable instanceof TimeoutException;
                        }
                    })
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            add(disposable);
                        }
                    })
                    .doOnDispose(new Action() {
                        @Override
                        public void run() throws Exception {
                            Utils.log(logTag, "OnDispose");
                        }
                    })
                    .doOnNext(new Consumer<WebSocketInfo>() {
                        @Override
                        public void accept(WebSocketInfo webSocketInfo) throws Exception {
                            if (webSocketInfo.isOnOpen()) {
                                webSocket = webSocketInfo.getWebSocket();
                            }
                        }
                    })
                    .share()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            if (webSocket != null) {
                observable = observable.startWith(new WebSocketInfo(webSocket, true));
            }
        }
        return observable;
    }

    public Observable<WebSocketInfo> getObservable() {
        return getObservable(30, TimeUnit.DAYS);
    }

    public Observable<String> getWebSocketString() {
        return getObservable()
                .filter(new Predicate<WebSocketInfo>() {
                    @Override
                    public boolean test(@NonNull WebSocketInfo webSocketInfo) throws Exception {
                        return webSocketInfo.getString() != null;
                    }
                })
                .map(new Function<WebSocketInfo, String>() {
                    @Override
                    public String apply(@NonNull WebSocketInfo webSocketInfo) throws Exception {
                        return webSocketInfo.getString();
                    }
                });
    }

    public Observable<ByteString> getWebSocketByteString() {
        return getObservable()
                .filter(new Predicate<WebSocketInfo>() {
                    @Override
                    public boolean test(@NonNull WebSocketInfo webSocketInfo) throws Exception {
                        return webSocketInfo.getByteString() != null;
                    }
                })
                .map(new Function<WebSocketInfo, ByteString>() {
                    @Override
                    public ByteString apply(WebSocketInfo webSocketInfo) throws Exception {
                        return webSocketInfo.getByteString();
                    }
                });
    }

    public Observable<WebSocket> getWebSocketObservable() {
        return getObservable()
                .map(new Function<WebSocketInfo, WebSocket>() {
                    @Override
                    public WebSocket apply(@NonNull WebSocketInfo webSocketInfo) throws Exception {
                        return webSocketInfo.getWebSocket();
                    }
                });
    }

    public Scheduler.Worker getWorker() {
        return worker;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    /**
     * 如果url的WebSocket已经打开,可以直接调用这个发送消息.
     *
     * @param message
     */
    public void send(String message) {
        if (webSocket != null) {
            webSocket.send(message);
        } else {
            throw new IllegalStateException("The WebSokcet not open");
        }
    }

    /**
     * 如果url的WebSocket已经打开,可以直接调用这个发送消息.
     *
     * @param byteString
     */
    public void send(ByteString byteString) {
        if (webSocket != null) {
            webSocket.send(byteString);
        } else {
            throw new IllegalStateException("The WebSokcet not open");
        }
    }

    /**
     * 不用关心url 的WebSocket是否打开,可以直接发送
     */
    public void asyncSend(final String message) {
        worker.schedule(new Runnable() {
            @Override
            public void run() {
                if (isLog) {
                    Log.d(logTag, " send-->" + message + "-->Thread-->" + Thread.currentThread().getName());
                }

                if (webSocket != null) {
                    webSocket.send(message);
                }
            }
        });
    }

    /**
     * 不用关心url 的WebSocket是否打开,可以直接发送
     *
     * @param byteString
     */
    public void asyncSend(final ByteString byteString) {
        worker.schedule(new Runnable() {
            @Override
            public void run() {
                if (isLog) {
                    Log.d(logTag, " send-->" + byteString.utf8() + "-->Thread-->" + Thread.currentThread().getName());
                }

                if (webSocket != null) {
                    webSocket.send(byteString);
                }
            }
        });
    }

    public void stopHeartbeat() {
        if (heartbeatDisposable != null) {
            heartbeatDisposable.dispose();
        }
    }

    public void heartbeat() {
        heartbeat(heartbeatInterval, heartbeatIntervalUnit, ping);
    }

    public void heartbeat(long period, TimeUnit unit, final String ping) {
        if (period == 0) {
            return;
        }

        if (heartbeatDisposable != null) {
            heartbeatDisposable.dispose();
        }

        heartbeatDisposable = Observable.interval(period, period, unit)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) {
                        asyncSend(ping);
                    }
                });
        add(heartbeatDisposable);
    }

    private Request getRequest(String url) {
        if (request == null) {
            request = new Request.Builder()
                    .url(url)
                    .build();
        }
        return request;
    }

    private OkHttpClient getClient() {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .build();
        }
        client.dispatcher().cancelAll();
        return client;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private final class WebSocketOnSubscribe implements ObservableOnSubscribe<WebSocketInfo> {

        @Override
        public void subscribe(@NonNull ObservableEmitter<WebSocketInfo> emitter) throws Exception {
            if (webSocket != null) {
                //降低重连频率
                if (!"main".equals(Thread.currentThread().getName())) {
                    long ms = reconnectIntervalTimeUnit.toMillis(reconnectInterval);
                    SystemClock.sleep(ms == 0 ? 5000 : ms);
                    emitter.onNext(WebSocketInfo.createReconnect());
                }
            }

            initWebSocket(emitter);
        }

        private void initWebSocket(final ObservableEmitter<WebSocketInfo> emitter) {
            webSocket = getClient().newWebSocket(getRequest(url), new WebSocketListener() {
                @Override
                public void onOpen(final WebSocket webSocket, Response response) {
                    heartbeat();
                    Utils.log(logTag, "onOpen-->" + response.toString());
                    if (!emitter.isDisposed()) {
                        emitter.onNext(new WebSocketInfo(webSocket, true));
                    }
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    Utils.log(logTag, "onMessage-->" + text);
                    if (!emitter.isDisposed()) {
                        emitter.onNext(new WebSocketInfo(webSocket, text));
                    }
                }

                @Override
                public void onMessage(WebSocket webSocket, ByteString bytes) {
                    Utils.log(logTag, "onMessage-->" + bytes.toString());
                    if (!emitter.isDisposed()) {
                        emitter.onNext(new WebSocketInfo(webSocket, bytes));
                    }
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                    Utils.log(logTag, "onFailure-->" + "Throwable-->" + t.toString()
                                    + "WebSocket-->" + webSocket.request().toString()
                            /* + "Response-->" + response.toString()*/);
                    if (!emitter.isDisposed()) {
                        emitter.onError(t);
                    }
                }

                @Override
                public void onClosing(WebSocket webSocket, int code, String reason) {
                    Utils.log(logTag, "onClosing-->" + "WebSocket-->" + webSocket.request().toString()
                            + "code-->" + code + "reason-->" + reason);
                    webSocket.close(1000, null);

                    if (!emitter.isDisposed()) {
                        WebSocketInfo webSocketInfo = new WebSocketInfo(webSocket);
                        webSocketInfo.setOnClosing(true);
                        emitter.onNext(webSocketInfo);
                    }
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    Utils.log(logTag, "onClosed-->" + "WebSocket-->" + webSocket.request().toString()
                            + "code-->" + code + "reason-->" + reason);
                    if (!emitter.isDisposed()) {
                        emitter.onComplete();
                    }
                }
            });
            emitter.setCancellable(new Cancellable() {
                @Override
                public void cancel() throws Exception {
                    webSocket.close(3000, "close WebSocket");
                    Utils.log(logTag, url + " --> cancel");
                }
            });
        }
    }

    /**
     * 单纯的Observables 和 Subscribers管理
     *
     * @param disposable
     */
    public void add(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    /**
     * 单个presenter生命周期结束，取消订阅
     */
    public void clear() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
            heartbeatDisposable = null;
            mCompositeDisposable = null;
        }

        if (webSocket != null) {
            webSocket.cancel();
            webSocket = null;
        }
    }

    public int subscriberSize() {
        return mCompositeDisposable == null ? 0 : mCompositeDisposable.size();
    }
}
