package cn.itsite.awebsoket.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import cn.itsite.awebsoket.WebSocketInfo;
import cn.itsite.awebsoket.WsHelper;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {
    private EditText etUrl;
    private Button connect;
    private EditText editText0;
    private Button send0;
    private TextView message0;
    private EditText editText1;
    private Button send1;
    private TextView message1;
    private Button subscribe0;
    private Button subscribe1;
    private Button disconnect0;
    private Button disconnect1;
    private Disposable mDisposable0;
    private Disposable mDisposable1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
    }

    private void initView() {
        etUrl = findViewById(R.id.et_url);
        connect = findViewById(R.id.connect);
        editText0 = findViewById(R.id.editText0);
        editText1 = findViewById(R.id.editText1);
        send0 = findViewById(R.id.send0);
        send1 = findViewById(R.id.send1);
        subscribe0 = findViewById(R.id.subscribe0);
        subscribe1 = findViewById(R.id.subscribe1);
        disconnect0 = findViewById(R.id.disconnect0);
        disconnect1 = findViewById(R.id.disconnect1);
        message0 = findViewById(R.id.message0);
        message1 = findViewById(R.id.message1);
    }

    private void setListener() {
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WsHelper.builder()
                        .setLog(true)
//                        .setHeartbeat(3000, TimeUnit.MILLISECONDS, "")
//                        .setReconnectInterval(3000, TimeUnit.MILLISECONDS)
                        .setUrl(etUrl.getText().toString().trim())
                        .connect();
            }
        });

        send0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText0.getText().toString();
                WsHelper.asyncSend(message);
            }
        });

        send1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editText1.getText().toString();
                WsHelper.asyncSend(message);
            }
        });
        subscribe0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDisposable0 = WsHelper.getObservable()
                        .subscribe(new Consumer<WebSocketInfo>() {
                            @Override
                            public void accept(WebSocketInfo webSocketInfo) {

                                if (webSocketInfo.isOnOpen()) {
//                                    Log.d("MainActivity", " on WebSocket open");
                                } else {

                                    String string = webSocketInfo.getString();
                                    if (string != null) {
                                        message0.setText(Html.fromHtml(string));

                                    }

                                    ByteString byteString = webSocketInfo.getByteString();
                                    if (byteString != null) {
                                        Log.d("MainActivity", "webSocketInfo.getByteString():" + byteString);

                                    }
                                }

                            }
                        });
            }
        });

        subscribe1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDisposable1 = WsHelper.getObservable()
                        .subscribe(new Consumer<WebSocketInfo>() {
                            @Override
                            public void accept(WebSocketInfo webSocketInfo) {

                                if (webSocketInfo.isOnOpen()) {
//                                    Log.d("MainActivity", " on WebSocket open");
                                } else {

                                    String string = webSocketInfo.getString();
                                    if (string != null) {
                                        message1.setText(Html.fromHtml(string));

                                    }

                                    ByteString byteString = webSocketInfo.getByteString();
                                    if (byteString != null) {
                                        Log.d("MainActivity", "webSocketInfo.getByteString():" + byteString);
                                    }
                                }

                            }
                        });
            }
        });

        disconnect0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDisposable0 != null) {
                    mDisposable0.dispose();
                }
                message0.setText("");
            }
        });
        disconnect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDisposable1 != null) {
                    mDisposable1.dispose();
                }
                message1.setText("");
            }
        });
    }
}
