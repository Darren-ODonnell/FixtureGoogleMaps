package com.example.googlemapsproject.APIs;

import com.example.googlemapsproject.Models.Club;
import com.example.googlemapsproject.Models.Fixture;
import com.example.googlemapsproject.Models.Login;
import com.example.googlemapsproject.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

    @POST("api/auth/login")
    Call<User> login(@Body Login login);

    @GET("api/auth/checkToken")
    Call<Boolean> checkToken(@Query("token") String token);

    @GET("fixture/findByClub")
    Call<List<Fixture>> getFixtures(@Header("Authorization") String accessToken, @Query("name") String name);

    @GET("club/list")
    Call<List<Club>> getClubs(@Header("Authorization") String accessToken);

    @GET("club/findByName")
    Call<Club> getClubByName(@Header("Authorization") String accessToken);

}
