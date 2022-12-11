package com.example.googlemapsproject;
public class User {
    private int id;
    private String email;
    private String accessToken;
    private final String tokenType = "Bearer";


    public User(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType(){
        return this.tokenType;
    }
}

