package com.example.meetingapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meetingapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketActivity extends AppCompatActivity {

    private Button start;
    private TextView output;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_socket);
        start = findViewById(R.id.start);
        output = findViewById(R.id.output);
        client = new OkHttpClient();
        start.setOnClickListener(view -> start());
    }

    private void start() {
        Request request = new Request.Builder()
                .url("ws://10.0.2.2:8000/ws/chat/")
                .addHeader("Authorization", "Token asasddfgdhjkhlkjhkgjhf")
                .build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                output.setText(output.getText().toString() + "\n\n" + txt);
            }
        });
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, @NonNull Response response) {
            JSONObject jsonString = null;
            try {
                jsonString = new JSONObject().put("message", "Hello World!");
            } catch (JSONException e) {
                e.printStackTrace();
            }


//            webSocket.send(String.valueOf(jsonString));
//            webSocket.send("What's up ?");
//            webSocket.send(ByteString.decodeHex("deadbeef"));
//            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
            output("Receiving : " + text);
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, @NonNull String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(@NonNull WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage());
        }
    }
}
