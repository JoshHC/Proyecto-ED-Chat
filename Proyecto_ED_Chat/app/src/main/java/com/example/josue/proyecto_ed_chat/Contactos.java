package com.example.josue.proyecto_ed_chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Contactos extends Fragment {

    //consumibles rest api
    JsonPlaceHolderApi jsonPlaceHolderApi;
    private List<Usuario> Contactos = new ArrayList<>();
    ListView Lista;
    android.support.v4.app.FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contactos, container, false);

        //instancia retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.233:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        //SE VALIDA EL TOKEN
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor myEditor = myPreferences.edit();
        String token = myPreferences.getString("Token","no existe");
        VerificarUsuario(token);

        ObtenerUsuarios(view);
        return view;
    }

    private void CrearConversacion(Conversacion Nueva)
    {
        Call<Conversacion> call = jsonPlaceHolderApi.POSTConversacion(Nueva);
        call.enqueue(new Callback<Conversacion>() {
            @Override
            public void onResponse(Call<Conversacion> call, Response<Conversacion> response) {

                Toast.makeText(getActivity().getBaseContext(),"Cargando Conversacion",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Conversacion> call, Throwable t) {

                Toast.makeText(getActivity().getBaseContext(),"Error No se pudo crear la conversacion " + t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ObtenerUsuarios(final View view){

        Call<List<Usuario>> call = jsonPlaceHolderApi.getUsers();
        call.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {


                if(!response.isSuccessful()){
                    Toast.makeText(getActivity().getBaseContext(),"Code: " + response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }

                //GENERAR NUEVO TOKEN
                Contactos = response.body();
                List<Usuario> Temporal = new ArrayList<>();
                SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                String Usuario = myPreferences.getString("Username","No Encontrado");
                for (Usuario i:Contactos)
                {
                    if(i.getUsername().equals(Usuario) == false)
                    {
                        Temporal.add(i);
                    }
                }
                Lista = (ListView) view.findViewById(R.id.Lista);
                final UsuarioAdapter Adaptador = new UsuarioAdapter(getActivity(), (ArrayList<Usuario>) Temporal);
                Lista.setAdapter(Adaptador);
                Lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        //Se Guarda el receptor
                        TextView NombreReceptor = view.findViewById(R.id.Name);
                        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                        SharedPreferences.Editor myEditor = myPreferences.edit();

                        for (Usuario x:Contactos)
                        {
                            if(x.getName().equals(NombreReceptor.getText().toString()))
                            {
                                myEditor.putString("UsernameR",x.getUsername());
                            }

                        }
                        String Usuario = myPreferences.getString("Username","No Encontrado");
                        //Se Renueva el Token
                        String token = myPreferences.getString("Token","no existe");
                        VerificarUsuario(token);

                        myEditor.putString("Receptor", NombreReceptor.getText().toString());
                        myEditor.putString("Emisor", Usuario);
                        myEditor.commit();

                        List<Mensaje> Auxiliar = new ArrayList<>();
                        Conversacion Nueva = new Conversacion(Usuario, NombreReceptor.getText().toString(),Auxiliar);
                        CrearConversacion(Nueva);

                        //Se redirige a la conversacion
                        Intent intentc = new Intent(getActivity(), InternalChat.class);
                        startActivity(intentc);
                    }
                });

            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(),"Error " + t.getMessage(),Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity().getBaseContext(),"La Sesion ha Expirado" ,Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intentc = new Intent(getContext(), Login.class);
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
                    SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor myEditor = myPreferences.edit();
                    //Se Guarda el Token Obtenido
                    myEditor.putString("Token", token);
                    myEditor.commit();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(getActivity().getBaseContext(),"Error " + t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
