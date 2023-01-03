package com.example.googlemapsproject.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import retrofit2.http.Part;

public class Club implements Serializable, Parcelable {
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

    @Override
    public String toString() {
        return pitches;
    }

    public Club (Parcel parcel) {
        this.id = parcel.readLong();
        this.name = parcel.readString();
        this.contactName = parcel.readString();
        this.contactEmail = parcel.readString();
        this.pitches = parcel.readString();
        this.colours = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Required method to write to Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(contactName);
        dest.writeString(contactEmail);
        dest.writeString(pitches);
        dest.writeString(colours);
    }

    // Method to recreate a Question from a Parcel
    public static Creator<Club> CREATOR = new Creator<Club>() {

        @Override
        public Club createFromParcel(Parcel source) {
            return new Club(source);
        }

        @Override
        public Club[] newArray(int size) {
            return new Club[size];
        }

    };
}
