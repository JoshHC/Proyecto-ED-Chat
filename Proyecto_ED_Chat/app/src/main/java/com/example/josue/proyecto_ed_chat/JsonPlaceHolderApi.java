package com.example.josue.proyecto_ed_chat;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi
{
    //Para Obtener una Lista de Usuarios
    @GET ("user")
    Call<List<Usuario>> getUsers();

    //Ingresar/Obtener la lista de conversaciones
    @POST("user/conversacion")
    Call<Conversacion> POSTConversacion(@Body Conversacion NewConversacion);

    //Ingresar/Obtener la lista de conversaciones
    @PUT    ("user/mensajes")
    Call<Conversacion> POSTMenssajes(@Body Conversacion NewConversacion);

    @GET("user/all/conversations")
    Call<List<Conversacion>> GetMensajes();

    //Para Obtener un Usuario en Especifico
    @GET ("user/{username}")
    Call<ResponseBody> getUser(@Path("username") String username);

    //Ingresar Usuario, se debe de Usar para Creacion de Nuevos Usuarios
    @POST("user/registro")
     Call<ResponseBody> POSTUser(@Body Usuario NewUser);

    //Validacion del Login para el Usuario
    @POST("user/login")
    Call<ResponseBody> Login(@Body UsuarioLogin NewUser);

    //Validacion del Email para el Login
    @POST("user/email")
    Call<ResponseBody> ValidarEmail(@Body Usuario NewUser);

    @GET("user/verify")
    Call<ResponseBody> Verificar(@Header("Authorization") String token);


}
