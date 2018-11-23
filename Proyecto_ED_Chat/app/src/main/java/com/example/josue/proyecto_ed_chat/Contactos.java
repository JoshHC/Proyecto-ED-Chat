package com.example.josue.proyecto_ed_chat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class Contactos extends Fragment {

    //consumibles rest api
    JsonPlaceHolderApi jsonPlaceHolderApi;
    private List<Usuario> Contactos = new ArrayList<>();
    ListView Lista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contactos, container, false);

        //instancia retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.233:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        ObtenerUsuarios(view);
        return view;
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
                Lista = (ListView) view.findViewById(R.id.Lista);
                final UsuarioAdapter Adaptador = new UsuarioAdapter(getActivity(), (ArrayList<Usuario>) Contactos);
                Lista.setAdapter(Adaptador);

            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(getActivity().getBaseContext(),"Error " + t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
