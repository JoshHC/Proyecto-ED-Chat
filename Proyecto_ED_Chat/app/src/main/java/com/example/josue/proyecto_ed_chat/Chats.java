package com.example.josue.proyecto_ed_chat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Chats extends Fragment {

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

        return view;
    }

}
