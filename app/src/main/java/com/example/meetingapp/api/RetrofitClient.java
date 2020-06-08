package com.example.meetingapp.api;

import android.annotation.SuppressLint;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    //    private static final String BASE_URL = "http://10.0.2.2:8000/";
    private static final String BASE_URL = "http://104.248.247.195:80/";
    private static String TOKEN;
    private static RetrofitClient instance;
    private static boolean needsHeader = true;
    private Retrofit retrofit;

    private RetrofitClient() {
        /* Unsafe */
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @SuppressLint("TrustAllX509TrustManager")
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @SuppressLint("TrustAllX509TrustManager")
                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        // Install the all-trusting trust manager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            Objects.requireNonNull(sslContext).init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        // Create an ssl socket factory with our all-trusting manager\
        SSLSocketFactory sslSocketFactory = Objects.requireNonNull(sslContext).getSocketFactory();
        /* End Unsafe */

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(Objects.requireNonNull(sslSocketFactory), (X509TrustManager) trustAllCerts[0])
                .hostnameVerifier((hostname, session) -> true)
                .addInterceptor(
                        chain -> {
                            Request original = chain.request();

                            Request.Builder requestBuilder = original.newBuilder()
                                    .method(original.method(), original.body());

                            if (needsHeader) {
                                requestBuilder.addHeader("Authorization", TOKEN);
                            }

                            Request request = requestBuilder.build();
                            okhttp3.Response response = chain.proceed(request);

                            switch (response.code()) {
                                case 404:
                                    break;
                                case 500:
                                    break;
                                default:

                                    break;
                            }

                            return response;
                        }
                )
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static void setToken(String token) {
        TOKEN = "Token " + token;
    }

    public static void needsHeader(boolean needs) {
        needsHeader = needs;
    }

    public static synchronized RetrofitClient getInstance(String token) {
        if (instance == null) {
            instance = new RetrofitClient();
            setToken(token);
        }
        return instance;
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }
}