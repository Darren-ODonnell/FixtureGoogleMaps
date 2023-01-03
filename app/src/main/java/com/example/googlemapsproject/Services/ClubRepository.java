package com.example.googlemapsproject.Services;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.googlemapsproject.APIs.APIClient;
import com.example.googlemapsproject.APIs.APIInterface;
import com.example.googlemapsproject.Models.Club;
import com.example.googlemapsproject.Models.Fixture;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClubRepository {

    private APIInterface apiInterface;
    private MutableLiveData<List<Club>> clubResponseLiveData;


    public ClubRepository(){
        clubResponseLiveData = new MutableLiveData<>();

        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    //By default getFixtures should return Fixtures pertaining to Naomh Judes
    public void getClubs(String token){
        apiInterface.getClubs(token).enqueue(new Callback<List<Club>>() {
            @Override
            public void onResponse(Call<List<Club>> call, Response<List<Club>> response) {
                if (response.body() != null) {
                    clubResponseLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Club>> call, Throwable t) {
                clubResponseLiveData.postValue(null);
            }
        });

    }

    public LiveData<List<Club>> getClubResponseLiveData() {
        return clubResponseLiveData;
    }


}

