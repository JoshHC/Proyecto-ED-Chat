package com.example.josue.proyecto_ed_chat;

import com.google.gson.annotations.SerializedName;

public class UsuarioLogin {

    @SerializedName("username")
    private String Username;
    @SerializedName("password")
    private String Password;

    public UsuarioLogin(String username, String password)
    {
        Username = username;
        Password = password;
    }
}


