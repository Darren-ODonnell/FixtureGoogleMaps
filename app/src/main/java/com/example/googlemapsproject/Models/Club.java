package com.example.googlemapsproject.Models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Club implements Serializable {
    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    private String contactName;
    @Expose
    private String contactEmail;
    @Expose
    private String contactPhone;
    @Expose
    private String pitches;
    @Expose
    private String colours;

    public Club() {
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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getPitches() {
        return pitches;
    }

    public void setPitches(String pitches) {
        this.pitches = pitches;
    }

    public String getColours() {
        return colours;
    }

    public void setColours(String colours) {
        this.colours = colours;
    }
}
