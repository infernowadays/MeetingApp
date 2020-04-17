package com.example.meetingapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meetingapp.R;
import com.example.meetingapp.api.Api;
import com.example.meetingapp.models.Test2;
import com.example.meetingapp.models.Test3;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebSocketActivity extends AppCompatActivity {

    private Button start;
    private Button send;

    private TextView output;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_socket);
        start = findViewById(R.id.start);
        send = findViewById(R.id.send);
        output = findViewById(R.id.output);
        start.setOnClickListener(view -> start());
        send.setOnClickListener(v -> send());
    }

    private void start() {
        client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("ws://fuck123123.herokuapp.com/ws/chat/")
                .addHeader("Authorization", "Token 123124fwefwhtrhergee1t2y4")
                .build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    private void send(){

//        Retrofit.Builder builder = new Retrofit.Builder()
//                .baseUrl("https://fuck123123.herokuapp.com/")
//                .addConverterFactory(GsonConverterFactory.create());
//
//        Retrofit retrofit = builder.build();
//        Api userClient = retrofit.create(Api.class);
//
//        Test3 test3 = new Test3("1", "1");
//        Call<String> call = userClient.sendRequest(test3, "Token 123124fwefwhtrhergee1t2y4");
//
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//                int a = 5;
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });
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
            Gson gson = new Gson();
            Test2 test2 = gson.fromJson(text, Test2.class);
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
//            webSocket.close(NORMAL_CLOSURE_STATUS, null);
//            start();
        }
    }
}
