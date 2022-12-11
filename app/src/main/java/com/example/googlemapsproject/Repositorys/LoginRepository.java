package com.example.googlemapsproject.Repositorys;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.googlemapsproject.APIs.APIClient;
import com.example.googlemapsproject.APIs.APIInterface;
import com.example.googlemapsproject.APIs.TokenSingleton;
import com.example.googlemapsproject.Models.Login;
import com.example.googlemapsproject.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    private MutableLiveData<Boolean> validToken;
    private MutableLiveData<String> tokenLiveData;
    private APIInterface apiInterface;

    public LoginRepository(){
        apiInterface = APIClient.getClient().create(APIInterface.class);
        validToken = new MutableLiveData<>();
        tokenLiveData = new MutableLiveData<>();
    }


    public void login(Login login){
        Call<User> call = apiInterface.login(login);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {

                if(response.isSuccessful()){
                    assert response.body() != null;
                    User user = response.body();
                    String token = user.getAccessToken();
                    tokenLiveData.postValue(token);
                    validToken.postValue(true);
                    TokenSingleton.getInstance().setTokenString(token);
                }else{
//                    Toast.makeText(getApplicationContext(), "Login not correct :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                call.cancel();
//                Toast.makeText(getApplicationContext(), "error :(", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void validateJWT(String token){
        validToken.postValue(false);
        Call<Boolean> call = apiInterface.checkToken(token);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body().booleanValue()){
                    validToken.postValue(true);
                    TokenSingleton.getInstance().setTokenString(token);
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                validToken.postValue(false);
            }
        });
    }

    public MutableLiveData<Boolean> getTokenValidity(){
        return validToken;
    }
}
