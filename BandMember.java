package com.example.vhl2.bandapp3;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Vhl2 on 11/22/2017.
 */

public class BandMember implements Serializable, Comparable<BandMember>{
    private String Name;
    private String Password;
    private Boolean Admin;
    private String Year;
    private String UserName;
    private int points;
    private String instrument;


    public BandMember() {
        Name = "blank";
        Password = "1234";
        Admin = false;
        Year = "freshman";
        UserName = "blank";
        this.points = 0;
        this.instrument = "blank";
    }

    public BandMember(String name, String password, String year, String userName, int points, String instrument) {
        Name = name;
        Password = password;
        Admin = false;
        Year = year;
        UserName = userName;
        this.points = points;
        this.instrument = instrument;
    }

    public BandMember(String name, String password, Boolean admin, String year, String userName, int points, String instrument) {
        Name = name;
        Password = password;
        Admin = admin;
        Year = year;
        UserName = userName;
        this.points = points;
        this.instrument = instrument;
    }


    /**
     * constructor with default points
     * @param name
     * @param password
     * @param admin
     * @param year
     * @param userName
     * @param instrument
     */
    public BandMember(String name, String password, Boolean admin, String year, String userName, String instrument) {
        Name = name;
        Password = password;
        Admin = admin;
        Year = year;
        UserName = userName;
        this.points = 0;
        this.instrument = instrument;
    }



    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public Boolean getAdmin() {
        return Admin;
    }

    public void setAdmin(Boolean admin) {
        Admin = admin;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public void addPoints(int newPoints){
        points += newPoints;
    }

    /**
     * toString method for bandMember objects that shows their user name and points
     * @return String Containing UserNames and Points
     */
    @Override
    public String toString() {
        return  UserName + " Total Points: " + points;
    }


    
    @Override
    public int compareTo(@NonNull BandMember other) {
        if (instrument.compareTo(other.getInstrument()) != 0) {
            return this.instrument.compareTo(other.getInstrument());
        } else {
            if(UserName.compareTo(other.getUserName()) != 0) {
                return this.UserName.compareTo(other.getUserName());
            } else {
                return 0;
            }
        }
    }
}
