package com.example.meetingapp.api;

import com.example.meetingapp.models.Event;
import com.example.meetingapp.models.Login;
import com.example.meetingapp.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserClient {

    @Headers("Content-Type: application/json")
    @POST("token_auth/auth")
    Call<User> login(@Body Login login);

    @Headers("Content-Type: application/json")
    @GET("api/events")
    Call<List<Event>> getEvents(@Header("Authorization") String authToken);
}
