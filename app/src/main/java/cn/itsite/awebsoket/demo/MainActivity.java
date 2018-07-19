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
    private EditText editText0;
    private Button send0;
    private TextView textview0;
    private EditText editText1;
    private Button send1;
    private TextView textview1;
    private Button centect0;
    private Button centect1;
    private Button discentect0;
    private Button discentect1;
    private Disposable mDisposable0;
    private Disposable mDisposable1;
    private String url = "wss://push.biotifo.com/mol_wallet_push/core/mola3fb777fed444e97a2ed9624fcebf244";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
        WsHelper.builder()
                .setLog(true)
                .setHeartbeat(3000, TimeUnit.MILLISECONDS)
                .setReconnectInterval(3000, TimeUnit.MILLISECONDS)
                .setUrl(url)
//                .build();
                .connect();
    }

    private void initView() {
        editText0 = findViewById(R.id.editText0);
        editText1 = findViewById(R.id.editText1);
        send0 = findViewById(R.id.send0);
        send1 = findViewById(R.id.send1);
        centect0 = findViewById(R.id.centect0);
        centect1 = findViewById(R.id.centect1);
        discentect0 = findViewById(R.id.discentect0);
        discentect1 = findViewById(R.id.discentect1);
        textview0 = findViewById(R.id.textview0);
        textview1 = findViewById(R.id.textview1);
    }

    private void setListener() {
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
        centect0.setOnClickListener(new View.OnClickListener() {
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
                                        textview0.setText(Html.fromHtml(string));

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

        centect1.setOnClickListener(new View.OnClickListener() {
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
                                        textview1.setText(Html.fromHtml(string));

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

        discentect0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDisposable0.dispose();
                textview0.setText("");
            }
        });
        discentect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDisposable1.dispose();
                textview1.setText("");

            }
        });
    }
}
