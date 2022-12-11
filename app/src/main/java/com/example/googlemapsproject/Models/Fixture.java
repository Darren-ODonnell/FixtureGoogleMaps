package com.example.googlemapsproject.Models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;


public class Fixture implements Serializable{
    @Expose
    private Long id;
    @Expose
    private Competition competition;
    @Expose
    private Club homeTeam;
    @Expose
    private Club awayTeam;
    @Expose
    private String fixtureDate;
    @Expose
    private String fixtureTime;
    @Expose
    private Integer season;
    @Expose
    private Integer round;

    public Fixture() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public Club getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Club homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Club getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Club awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getFixtureDate() {
        return fixtureDate;
    }

    public void setFixtureDate(String fixtureDate) {
        this.fixtureDate = fixtureDate;
    }

    public String getFixtureTime() {
        return fixtureTime;
    }

    public void setFixtureTime(String fixtureTime) {
        this.fixtureTime = fixtureTime;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    @Override
    public String toString() {
        return this.fixtureDate;
    }
}
