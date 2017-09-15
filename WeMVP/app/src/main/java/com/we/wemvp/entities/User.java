package com.we.wemvp.entities;

/**
 * Created by DaniRosas on 14/9/17.
 */

public class User {
    String email;
    String name;
    String surname;
    String country;
    int age;
    boolean online;
    boolean verified;

    public final static boolean ONLINE = true;
    public final static boolean OFFLINE = false;

    public User() {
    }

    public User(String email, boolean online) {
        this.email = email;
        this.online = online;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
