package com.example.josue.proyecto_ed_chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.Scaledrone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private TextView mTextMessage;
    private ListView listviewresult;
    //consumibles rest api
    JsonPlaceHolderApi jsonPlaceHolderApi;
    android.support.v4.app.FragmentManager fragmentManager=getSupportFragmentManager();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Contactos:
               fragmentManager.beginTransaction().replace(R.id.contenedor, new Contactos()).commit();
                return true;
            case R.id.Chats:
                fragmentManager.beginTransaction().replace(R.id.contenedor, new Chats()).commit();
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opcioneschat, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //INSTANCIA DE RETROFIT
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.233:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        //SE VALIDA EL TOKEN
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        String token = myPreferences.getString("Token","no existe");
        VerificarUsuario(token);

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
                    Intent intentc = new Intent(MainActivity.this, Login.class);
                    startActivity(intentc);

                }else
                {
                    String token = "";
                    try {
                        token = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //Se genera nuevamente el token para que la sesión siga activa.
                    token = token.replace("\"","");
                    SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor myEditor = myPreferences.edit();
                    //Se Guarda el Token Obtenido
                    myEditor.putString("Token", token);
                    myEditor.commit();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getApplication().getBaseContext(),"Error " + t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


}
