package com.example.googlemapsproject;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.googlemapsproject.APIs.TokenSingleton;
import com.example.googlemapsproject.Models.Club;
import com.example.googlemapsproject.Models.Fixture;
import com.example.googlemapsproject.Services.ClubRepository;
import com.example.googlemapsproject.Services.FixtureRepository;

import java.util.List;

public class FixtureInputViewModel extends AndroidViewModel {

        private ClubRepository clubRepo;
        private LiveData<List<Club>> clubResponseLiveData;

        public FixtureInputViewModel(@NonNull Application application) {
            super(application);
        }

        public void init(){
            clubRepo = new ClubRepository();
            clubResponseLiveData = clubRepo.getClubResponseLiveData();
        }

        public void getFixtures() {
            clubRepo.getClubs(TokenSingleton.getInstance().getBearerTokenString());

        }

        public LiveData<List<Club>> getFixturesResponseLiveData() {
            return clubResponseLiveData;
        }
}
