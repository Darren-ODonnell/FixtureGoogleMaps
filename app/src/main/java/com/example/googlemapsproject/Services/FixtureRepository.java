package com.example.googlemapsproject.Services;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bottomnavigationproper.StringReferences;
import com.example.googlemapsproject.APIs.APIClient;
import com.example.googlemapsproject.APIs.APIInterface;
import com.example.googlemapsproject.Models.Fixture;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FixtureRepository {

    private APIInterface apiInterface;
    private MutableLiveData<List<Fixture>> fixtureResponseLiveData;


    public FixtureRepository(){
        fixtureResponseLiveData = new MutableLiveData<>();

        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    //By default getFixtures should return Fixtures pertaining to Naomh Judes
    public void getFixtures(String token){
        apiInterface.getFixtures(token, StringReferences.JUDES).enqueue(new Callback<List<Fixture>>() {
            @Override
            public void onResponse(Call<List<Fixture>> call, Response<List<Fixture>> response) {
                if (response.body() != null) {
                    fixtureResponseLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Fixture>> call, Throwable t) {
                fixtureResponseLiveData.postValue(null);
            }
        });

    }

    public LiveData<List<Fixture>> getFixturesResponseLiveData() {
        return fixtureResponseLiveData;
    }
}

