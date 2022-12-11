package com.example.googlemapsproject.APIs;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    //Spare pc in work
    // private static String url = "http://147.252.81.67:8080/";

    //Desktop IP
//    private static String url = "http://192.168.100.151:8080/";

    //Localhost equivalent - Localhost cannot be used explicitly as
    // android picks up localhost as a reference to the emulator itself rather than the Computer

    private static String url = "http://192.168.100.56:8080/";


    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        OkHttpClient client = buildClientWithoutAuth();


        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    private static OkHttpClient buildClientWithAuth(){
        // Logging intercept for further debugging
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Initialising the interceptor to handle the JWT Token for all requests
        Interceptor authInterceptor = chain -> {
            Request newRequest  = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + TokenSingleton.getInstance().getBearerTokenString())
                    .build();
            return chain.proceed(newRequest);
        };

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build();
    }
    private static OkHttpClient buildClientWithoutAuth(){
        // Logging intercept for further debugging
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);



        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

}
