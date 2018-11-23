package com.example.josue.proyecto_ed_chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Chats extends Fragment implements AdapterView.OnItemClickListener{

    private List<Contacto> Contactos = new ArrayList<>();
    ListView Lista;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        //Prueba No.1 Contactos
        Contacto Nuevo1 = new Contacto("Chat No.1");
        Contacto Nuevo2 = new Contacto("Chat No.2");
        Contactos.add(Nuevo1);
        Contactos.add(Nuevo2);

        Lista = (ListView) view.findViewById(R.id.Lista);
        final ContactoAdapter Adaptador = new ContactoAdapter(getActivity(), (ArrayList<Contacto>) Contactos);
        Lista.setAdapter(Adaptador);
        Lista.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        //Conversacion Nueva = new Conversacion();
        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor myEditor = myPreferences.edit();
        //Se Guarda el receptor
       /* myEditor.putString("Token", token);
        myEditor.putString("Username",Username.getText().toString());
        myEditor.commit();*/
       //Se Renueva el Token
        //Se redirige a la conversacion
        Intent intent = new Intent(getActivity(), InternalChat.class);
        startActivity(intent);
    }

    }
