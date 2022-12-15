package com.example.googlemapsproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.googlemapsproject.APIs.TokenSingleton;
import com.example.googlemapsproject.Repositorys.LoginRepository;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "token_key";
    public static final String API_KEY = "jwt_token";
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        validateJWT();

    }

    public void validateJWT(){
        String token = retrieveToken();

        LoginRepository service = new LoginRepository();

        service.getTokenValidity().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    startActivity(new Intent(getApplicationContext(), FixtureActivity.class));
                    TokenSingleton.getInstance().setTokenString(token);
                    storeToken(getApplicationContext());
                }else{
                    buildRegisterLoginScreen();
                }
            }
        });

        if(token != null){
            service.validateJWT(token);
        }else{
            buildRegisterLoginScreen();
        }
    }

    private String retrieveToken() {
        Context context = getApplicationContext();

        settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(API_KEY, null);
    }

    public void buildRegisterLoginScreen(){
        setContentView(R.layout.register_login_screen);

        findViewById(R.id.navigate_to_login).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

//        findViewById(R.id.navigate_to_register).setOnClickListener(v -> {
//            startActivity(new Intent(this, RegisterActivity.class));
//        });
//        findViewById(R.id.login).setOnClickListener(v -> onLogin());
    }

    public void storeToken(Context context) {
        SharedPreferences settings = context.getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(API_KEY, TokenSingleton.getInstance().getTokenStr());
        // Commit the edits!
        editor.commit();
    }

}
