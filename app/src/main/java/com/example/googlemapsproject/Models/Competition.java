package com.example.googlemapsproject.Models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Competition implements Serializable {
    @Expose
    private Long id;

    @Expose
    private String name;

    @Expose
    private Integer season;

    public Competition() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }
}
