package com.example.meetingapp.api;

import com.example.meetingapp.models.Chat;
import com.example.meetingapp.models.CommonMessage;
import com.example.meetingapp.models.Complaint;
import com.example.meetingapp.models.EmailConfirmation;
import com.example.meetingapp.models.Event;
import com.example.meetingapp.models.LoginData;
import com.example.meetingapp.models.MegaCategory;
import com.example.meetingapp.models.Message;
import com.example.meetingapp.models.Password;
import com.example.meetingapp.models.PrivateMessage;
import com.example.meetingapp.models.ProfilePhoto;
import com.example.meetingapp.models.RegisterData;
import com.example.meetingapp.models.RequestGet;
import com.example.meetingapp.models.RequestSend;
import com.example.meetingapp.models.Ticket;
import com.example.meetingapp.models.Token;
import com.example.meetingapp.models.UserProfile;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    // Authentication
    @Headers("Content-Type: application/json")
    @POST("token_auth/auth/")
    Call<Token> login(@Body LoginData loginData);

    @Headers("Content-Type: application/json")
    @POST("token_auth/users/")
    Call<UserProfile> users(@Body RegisterData registerData);

    @Headers("Content-Type: application/json")
    @POST("token_auth/profile/me/")
    Call<Token> changePassword(@Body Password password);

    // User Profile
    @Headers("Content-Type: application/json")
    @PUT("token_auth/profile/me/")
    Call<UserProfile> updateProfile(@Body UserProfile userProfile);

    @Headers("Content-Type: application/json")
    @GET("token_auth/profile/me/")
    Call<UserProfile> meProfile();

    @Headers("Content-Type: application/json")
    @GET("token_auth/profile/{pk}/")
    Call<UserProfile> getUserProfile(@Path("pk") String pk);

    @Multipart
    @POST("token_auth/profile/upload/")
    Call<ProfilePhoto> uploadFile(@PartMap Map<String, RequestBody> params);

    // Events
    @Headers("Content-Type: application/json")
    @POST("api/events/")
    Call<Event> createEvent(@Body Event event);

    @Headers("Content-Type: application/json")
    @PUT("api/events/{pk}/")
    Call<Event> updateEvent(@Path("pk") String pk, @Body Event event);

    @Headers("Content-Type: application/json")
    @GET("api/events/{pk}/")
    Call<Event> getEvent(@Path("pk") String pk);

    @Headers("Content-Type: application/json")
    @GET("api/events/")
    Call<List<Event>> getEvents(@Query("category") List<String> categories,
                                @Query("me") List<String> roles);

    // Tickets
    @Headers("Content-Type: application/json")
    @GET("api/tickets/")
    Call<List<Ticket>> getTickets(@Query("category") List<String> categories,
                                  @Query("me") List<String> roles);

    @Headers("Content-Type: application/json")
    @POST("api/tickets/")
    Call<Ticket> createTicket(@Body Ticket ticket);

    @Headers("Content-Type: application/json")
    @GET("api/tickets/{pk}/")
    Call<Ticket> getTicket(@Path("pk") String pk);

    @Headers("Content-Type: application/json")
    @PUT("api/tickets/{pk}/")
    Call<Ticket> updateTicket(@Path("pk") String pk, @Body Ticket ticket);

    // Requests
    @Headers("Content-Type: application/json")
    @POST("api/requests/")
    Call<RequestGet> sendRequest(@Body RequestSend eventRequest);

    @Headers("Content-Type: application/json")
    @PUT("api/requests/{pk}/")
    Call<RequestGet> answerRequest(@Path("pk") String pk, @Body RequestSend decision);

    @Headers("Content-Type: application/json")
    @GET("api/requests/")
    Call<List<RequestGet>> getRequests();

    // Moderator
    @Headers("Content-Type: application/json")
    @GET("api/complaints/")
    Call<List<Complaint>> getComplaints();

    @Headers("Content-Type: application/json")
    @GET("api/complaints/{pk}/")
    Call<Complaint> getComplaint(@Path("pk") String pk);

    // Messages
    @Headers("Content-Type: application/json")
    @POST("api/messages/")
    Call<CommonMessage> sendMessage(@Body Message message);

    @Headers("Content-Type: application/json")
    @GET("api/messages/{event_id}/")
    Call<List<CommonMessage>> getEventMessages(@Path("event_id") String event_id);

    @Headers("Content-Type: application/json")
    @POST("api/private-messages/")
    Call<PrivateMessage> sendPrivateMessage(@Body PrivateMessage privateMessage);

    @Headers("Content-Type: application/json")
    @GET("api/private-messages/{user_id}/")
    Call<List<CommonMessage>> getPrivateMessage(@Path("user_id") String user_id);

    @Headers("Content-Type: application/json")
    @GET("api/chats/")
    Call<List<Chat>> getEventChats();

    // Categories
    @Headers("Content-Type: application/json")
    @GET("api/categories/")
    Call<List<MegaCategory>> getCategories();

    // Email Confirmation
    @Headers("Content-Type: application/json")
    @POST("api/generate_code/")
    Call<Void> generateCode(@Body EmailConfirmation email);

    @Headers("Content-Type: application/json")
    @POST("api/check_code/")
    Call<Void> checkCode(@Body EmailConfirmation emailConfirmation);

    @Headers("Content-Type: application/json")
    @PUT("token_auth/tokens/")
    Call<Void> updateFirebaseToken(@Body String firebaseToken);
}
