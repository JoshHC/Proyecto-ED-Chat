package com.example.josue.proyecto_ed_chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InternalChat extends AppCompatActivity {

    //consumibles rest api
    JsonPlaceHolderApi jsonPlaceHolderApi;

    private SwipeRefreshLayout swipeRefreshLayout;

    ListView Lista;
    EditText editText;
    ImageButton BotonEnvio;
    String Emisor;
    String Receptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internalchat);

        //INSTANCIA DE RETROFIT
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.233:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        //INICICALIZACION DE VARIABLES
        Lista = findViewById(R.id.messages_view);
        editText = findViewById(R.id.editText);
        BotonEnvio = findViewById(R.id.btnenvio);
        getSupportActionBar().setTitle(Receptor);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        //SE VALIDA EL TOKEN
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor myEditor = myPreferences.edit();
        String token = myPreferences.getString("Token","no existe");
        VerificarUsuario(token);

        //SE OBTIENEN EL EMISOR Y EL RECEPTOR GUARDADOS EN MEMORIA
        Emisor = myPreferences.getString("Emisor","No Encontrado");
        Receptor= myPreferences.getString("Receptor","No Encontrado");

        Mensaje Nuevo = new Mensaje(editText.getText().toString(),Emisor, Receptor,true);
        List<Mensaje> Auxiliar = new ArrayList<>();
        Auxiliar.add(Nuevo);
        final Conversacion Nueva = new Conversacion(Emisor, Receptor,Auxiliar);

        //SE GESTIONA Y SE MANDA A ACTUALIZAR LA CONVERSACION COMPLETA
        GestiondeConversacion();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Esto se ejecuta cada vez que se realiza el gesto
                GestiondeConversacion();
            }
        });

        BotonEnvio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Prueba = editText.getText().toString();
                Mensaje Nuevo = new Mensaje(editText.getText().toString(),Emisor,Receptor,true);
                List<Mensaje> Auxiliar = new ArrayList<>();
                Auxiliar.add(Nuevo);
                //SE AÑADEN MENSAJES A LA CONVERSACION, SE ENVIAN
                final Conversacion Nueva = new Conversacion(Emisor, Receptor,Auxiliar);
                GestiondeConversacion(Nueva);
            }
        });

    }

    private void GestiondeConversacion()
    {
        Call<List<Conversacion>> call = jsonPlaceHolderApi.GetMensajes();
        call.enqueue(new Callback<List<Conversacion>>() {
            @Override
            public void onResponse(Call<List<Conversacion>> call, Response<List<Conversacion>> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getApplication().getBaseContext(),"Error no se ha podido obtener la conversacion " + response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Conversacion> Nueva = response.body();
                Lista = (ListView) findViewById(R.id.messages_view);
                List<Mensaje> ListaEnvio = new ArrayList<>();
                for (Conversacion i:Nueva)
                {
                    //SI EL EMISOR Y EL RECEPTOR SON IGUALES QUIERE DECIR QUE CORRESPONDE A LA CONVERSACION
                    if(i.getEmisor().equals(Emisor) == true && i.getReceptor().equals(Receptor) == true)
                    {
                        ListaEnvio = i.getMensajes();
                    }
                }

                for (Mensaje t:ListaEnvio)
                {
                    t.setReceptor(Receptor);
                }

                String receptor = Receptor;
                //SI LA LISTA ESTA VACIA NO SE ENVIA NADA
                if(ListaEnvio.size() != 0) {
                    final ConversacionAdapter Adaptador = new ConversacionAdapter(InternalChat.this, (ArrayList<Mensaje>) ListaEnvio);
                    Lista.setAdapter(Adaptador);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Conversacion>> call, Throwable t) {
                Toast.makeText(getApplication().getBaseContext(),"Error no se ha podido obtener la conversacion ",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void GestiondeConversacion(final Conversacion NewConversation)
    {
        Call<List<Conversacion>> call = jsonPlaceHolderApi.GetMensajes();
        call.enqueue(new Callback<List<Conversacion>>() {
            @Override
            public void onResponse(Call<List<Conversacion>> call, Response<List<Conversacion>> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(getApplication().getBaseContext(),"Error no se ha podido obtener la conversacion " + response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Conversacion> Nueva = response.body();
                List<Mensaje> ListadeMensajes = new ArrayList<>();
                Lista = (ListView) findViewById(R.id.messages_view);
                for (Conversacion i:Nueva)
                {
                    if(i.getEmisor().equals(Emisor) == true && i.getReceptor().equals(Receptor) == true)
                    {
                        ListadeMensajes = i.getMensajes();
                        ListadeMensajes.add(NewConversation.getMensajes().get(0));
                        i.setMensajes(ListadeMensajes);
                        EnviarMensaje(i);
                    }
                }

                for (Mensaje t:ListadeMensajes)
                {
                    t.setReceptor(Receptor);
                }

                final ConversacionAdapter Adaptador = new ConversacionAdapter(InternalChat.this, (ArrayList<Mensaje>) ListadeMensajes);
                Lista.setAdapter(Adaptador);
            }

            @Override
            public void onFailure(Call<List<Conversacion>> call, Throwable t) {
                Toast.makeText(getApplication().getBaseContext(),"Error no se ha podido obtener la conversacion ",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void EnviarMensaje(Conversacion Nueva)
    {
        Call<Conversacion> call = jsonPlaceHolderApi.POSTMenssajes(Nueva);
        call.enqueue(new Callback<Conversacion>() {
            @Override
            public void onResponse(Call<Conversacion> call, Response<Conversacion> response) {

                Toast.makeText(getApplication().getBaseContext(),"Envio Exitoso ",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Conversacion> call, Throwable t) {
            }
        });
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
                    Intent intentc = new Intent(InternalChat.this, Login.class);
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
                    SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(InternalChat.this);
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
