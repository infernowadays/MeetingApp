package com.example.meetingapp.api;

import com.example.meetingapp.models.Event;
import com.example.meetingapp.models.EventRequest;
import com.example.meetingapp.models.LoginData;
import com.example.meetingapp.models.Password;
import com.example.meetingapp.models.RegisterData;
import com.example.meetingapp.models.Token;
import com.example.meetingapp.models.UserProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @Headers("Content-Type: application/json")
    @POST("token_auth/auth")
    Call<Token> login(@Body LoginData loginData);

    @Headers("Content-Type: application/json")
    @POST("token_auth/users")
    Call<UserProfile> users(@Body RegisterData registerData);

    @Headers("Content-Type: application/json")
    @PUT("token_auth/profile/me/")
    Call<UserProfile> updateProfile(@Body UserProfile userProfile);

    @Headers("Content-Type: application/json")
    @GET("token_auth/profile/me/")
    Call<UserProfile> meProfile();

    @Headers("Content-Type: application/json")
    @GET("api/events")
    Call<List<Event>> getEvents(@Query("category") List<String> categories, @Query("me") List<String> roles);

    @Headers("Content-Type: application/json")
    @GET("api/events/{pk}")
    Call<Event> getEvent(@Path("pk") String pk);

    @Headers("Content-Type: application/json")
    @POST("api/events")
    Call<Event> createEvent(@Body Event event);

    @Headers("Content-Type: application/json")
    @PUT("api/events/{pk}")
    Call<Event> updateEvent(@Path("pk") String pk, @Body Event event);

    @Headers("Content-Type: application/json")
    @POST("api/requests")
    Call<EventRequest> sendRequest(@Body EventRequest eventRequest);

    @Headers("Content-Type: application/json")
    @GET("api/requests")
    Call<List<EventRequest>> getRequests();

    @Headers("Content-Type: application/json")
    @POST("token_auth/profile/me/")
    Call<Token> changePassword(@Body Password password);
}
