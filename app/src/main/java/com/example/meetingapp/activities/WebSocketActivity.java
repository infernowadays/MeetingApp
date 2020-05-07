package com.example.meetingapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.meetingapp.R;
import com.example.meetingapp.api.Api;
import com.example.meetingapp.api.RetrofitClient;
import com.example.meetingapp.models.Category;
import com.example.meetingapp.models.Event;
import com.example.meetingapp.models.EventRequest;
import com.example.meetingapp.models.Test2;
import com.example.meetingapp.models.Test3;
import com.example.meetingapp.utils.PreferenceUtils;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

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

    private static final String webSocketUrl = "ws://10.0.2.2:8000/ws/chat/";
    private static final String authorization = "Authorization";
    private Button start;
    private Button send;
    Event event;
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
                .url(webSocketUrl)
                .addHeader(authorization, "Token 8e11d55f3e867fe88ffece459dd648dbd7c6703d")
                .build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }


    private void send(){
        Call<EventRequest> call = RetrofitClient
                .getInstance(PreferenceUtils.getToken(Objects.requireNonNull(this)))
                .getApi()
                .sendRequest(new EventRequest("yk3LBB78EAgnIiumrCjMs13Nlsh2", "yk3LBB78EAgnIiumrCjMs13Nlsh2", 66));

        call.enqueue(new Callback<EventRequest>() {
            @Override
            public void onResponse(Call<EventRequest> call, retrofit2.Response<EventRequest> response) {

            }

            @Override
            public void onFailure(Call<EventRequest> call, Throwable t) {

            }
        });
    }

    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                output.setText(output.getText().toString() + "\n\n" + txt);
            }
        });
    }

    public final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
            JSONObject jsonString = null;
            try {
                jsonString = new JSONObject().put("message", "Hello World!");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            webSocket.send(String.valueOf(jsonString));
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
            Gson gson = new Gson();
            Test2 test2 = gson.fromJson(text, Test2.class);
            output("Receiving : " + text);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, @NonNull String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
        }

        @Override
        public void onFailure(@NonNull WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage());
//            webSocket.close(NORMAL_CLOSURE_STATUS, null);
        }
    }
}
