package com.example.meetingapp.api;

import com.example.meetingapp.models.Event;
import com.example.meetingapp.models.EventRequest;
import com.example.meetingapp.models.Login;
import com.example.meetingapp.models.Test3;
import com.example.meetingapp.models.Token;
import com.example.meetingapp.models.User;
import com.example.meetingapp.models.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    @Headers("Content-Type: application/json")
    @POST("token_auth/auth")
    Call<Token> login(@Body Login login);

    @Headers("Content-Type: application/json")
    @POST("token_auth/users")
    Call<User> users(@Body Users users);

    @Headers("Content-Type: application/json")
    @GET("api/events")
    Call<List<Event>> getEvents();

    @Headers("Content-Type: application/json")
    @GET("api/events/{pk}")
    Call<Event> getEvent(@Path("pk") String pk);

    @Headers("Content-Type: application/json")
    @POST("api/events")
    Call<Event> createEvent(@Body Event event);

    @Headers("Content-Type: application/json")
    @POST("api/requests")
    Call<EventRequest> sendRequest(@Body EventRequest eventRequest);
}
