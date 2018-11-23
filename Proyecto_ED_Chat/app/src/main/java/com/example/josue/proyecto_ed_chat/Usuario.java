package com.example.josue.proyecto_ed_chat;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Usuario {

    @SerializedName("name")
    private String Name;
    @SerializedName("lastname")
    private String Lastname;
    @SerializedName("birthday")
    private String Birthday;
    @SerializedName("phone")
    private String Phone;
    @SerializedName("email")
    private String Email;
    @SerializedName("username")
    private String Username;
    @SerializedName("password")
    private String Password;

    public Usuario(String name, String lastname, String birthday, String phone, String email, String username, String password) {
        Name = name;
        Lastname = lastname;
        Birthday = birthday;
        Phone = phone;
        Email = email;
        Username = username;
        Password = password;
    }


    public String getName() {
        return Name;
    }

    public String getLastname() {
        return Lastname;
    }

    public String getBirthday() {
        return Birthday;
    }

    public String getPhone() {
        return Phone;
    }

    public String getEmail() {
        return Email;
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }


}
