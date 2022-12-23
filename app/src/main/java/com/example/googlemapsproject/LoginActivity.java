package com.example.googlemapsproject;

import static com.example.googlemapsproject.MainActivity.API_KEY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.googlemapsproject.APIs.TokenSingleton;
import com.example.googlemapsproject.Models.Login;
import com.example.googlemapsproject.Repositorys.LoginRepository;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginFromInput();
        returnToRegister();

    }

    private void returnToRegister() {
        findViewById(R.id.navigate_to_register).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });
    }

    public void loginFromInput(){
        LoginRepository service = new LoginRepository();

        findViewById(R.id.login).setOnClickListener(v -> {
            String username = getTextFromEditText(R.id.name);
            String password = getTextFromEditText(R.id.password);

            Login loginObj = new Login(username, password);
            service.login(loginObj);

            startActivity(new Intent(getApplicationContext(), FixtureActivity.class));

        });
    }

    private String getTextFromEditText(int id){
        return ((EditText)findViewById(id)).getText().toString();
    }

    public void storeToken(Context context) {
        SharedPreferences settings = context.getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(API_KEY, TokenSingleton.getInstance().getTokenStr());
        // Commit the edits!
        editor.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        storeToken(getApplicationContext());
    }
}
