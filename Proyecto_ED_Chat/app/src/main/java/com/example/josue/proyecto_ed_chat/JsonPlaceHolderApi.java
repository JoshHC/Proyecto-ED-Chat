package com.example.josue.proyecto_ed_chat;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi
{
    //Para Obtener una Lista de Usuarios
    @GET ("user")
    Call<List<Usuario>> getUsers();

    @GET("user/mensajes")
    Call<List<Conversacion>> getMensajes();

    //Para Obtener un Usuario en Especifico
    @GET ("user/{username}")
    Call<ResponseBody> getUser(@Path("username") String username);

    //Ingresar Usuario, se debe de Usar para Creacion de Nuevos Usuarios
    @POST("user/registro")
     Call<ResponseBody> POSTUser(@Body Usuario NewUser);

    /*@POST("user/login")
    Call<ResponseBody> Login(@Body String NewUser);*/

    //Validacion del Login para el Usuario
    @POST("user/login")
    Call<ResponseBody> Login(@Body UsuarioLogin NewUser);

    //Validacion del Email para el Login
    @POST("user/email")
    Call<ResponseBody> ValidarEmail(@Body Usuario NewUser);

    @GET("user/verify")
    Call<ResponseBody> Verificar(@Header("Authorization") String token);


}
