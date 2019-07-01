package cn.itsite.awebsocket;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: leguang
 * @e-mail: langmanleguang@qq.com
 * @version: v0.0.0
 * @blog: https://github.com/leguang
 * @time: 2018/7/13 0013 10:41
 * @description:
 */
public abstract class GsonSubscriber<T> extends WebSocketSubscriber {
    private static final Gson GSON = new Gson();
    protected Type type;


    public GsonSubscriber() {
        analysisType();
    }

    private void analysisType() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("No generics found!");
        }
        ParameterizedType type = (ParameterizedType) superclass;
        this.type = type.getActualTypeArguments()[0];
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onMessage(String text) {
        Observable.just(text)
                .map(new Function<String, T>() {
                    @Override
                    public T apply(String s) throws Exception {
                        try {
                            return GSON.fromJson(s, type);
                        } catch (JsonSyntaxException e) {
                            return GSON.fromJson(GSON.fromJson(s, String.class), type);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        onMessage(t);
                    }
                });

    }

    protected abstract void onMessage(T t);
}
