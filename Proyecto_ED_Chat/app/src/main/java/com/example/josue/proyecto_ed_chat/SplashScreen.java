package com.example.josue.proyecto_ed_chat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashScreen extends AppCompatActivity {


    private final int DURACION_SPLASH = 3000;
    //consumibles rest api
    JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();


        //INSTANCIA RETROFIT
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.233:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        setContentView(R.layout.activity_splash_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Se Valida el Token, Si ya Vencio se le envia a Login, Sino se le envia a Chats y se deben de cargar sus Datos.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
                SharedPreferences.Editor myEditor = myPreferences.edit();
                String token = myPreferences.getString("Token","no existe");
                VerificarUsuario(token);
            };
        }, DURACION_SPLASH);
    }

    private void VerificarUsuario(String token){
        Call<ResponseBody> call = jsonPlaceHolderApi.Verificar(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                if(response.code() == 403 || response.code() == 500)
                {
                    Toast.makeText(getApplication().getBaseContext(),"La Sesion ha Expirado" ,Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intentc = new Intent(SplashScreen.this, Login.class);
                    startActivity(intentc);

                }else
                {
                    Toast.makeText(getApplication().getBaseContext(),"Su Sesion Sigue Activa" ,Toast.LENGTH_SHORT).show();
                    String token = "";
                    try {
                        token = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Se genera nuevamente el token para que la sesión siga activa.
                    token = token.replace("\"","");
                    SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
                    SharedPreferences.Editor myEditor = myPreferences.edit();
                    //Se Guarda el Token Obtenido
                    myEditor.putString("Token", token);
                    myEditor.commit();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intentc = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intentc);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getApplication().getBaseContext(),"Error " + t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
