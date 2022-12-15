package com.example.googlemapsproject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.googlemapsproject.APIs.TokenSingleton;
import com.example.googlemapsproject.Models.Fixture;
import com.example.googlemapsproject.Services.FixtureRepository;

import java.util.List;

public class ClubLocationViewModel extends AndroidViewModel {

    private FixtureRepository fixtureRepository;
    private LiveData<List<Fixture>> fixtureResponseLiveData;

    public ClubLocationViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        fixtureRepository = new FixtureRepository();
        fixtureResponseLiveData = fixtureRepository.getFixturesResponseLiveData();
    }

    public void getFixtures() {
        fixtureRepository.getFixtures(TokenSingleton.getInstance().getBearerTokenString());

    }

    public LiveData<List<Fixture>> getFixturesResponseLiveData() {
        return fixtureResponseLiveData;
    }
}
