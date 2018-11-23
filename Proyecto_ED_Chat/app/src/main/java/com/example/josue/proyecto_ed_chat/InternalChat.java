package com.example.josue.proyecto_ed_chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class InternalChat extends Fragment {

    ImageButton Envio;
    TextView Texto;
    ListView Lista;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.internalchat, container, false);

        Envio = view.findViewById(R.id.btnenvio);
        Texto = view.findViewById(R.id.editText);

        SharedPreferences myPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor myEditor = myPreferences.edit();
        final String Nombre = myPreferences.getString("Username","no existe");
        final boolean Status = false;

        Envio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(Texto.getText().equals("") == false) {
                    Mensaje Nuevo = new Mensaje(Texto.getText().toString(),Nombre,Status);
                    Lista = (ListView) view.findViewById(R.id.messages_view);
                    //SE OBTIENE LA CONVERSACION Y LA LISTA DE MENSAJES Y SE INSERTA EL NUEVO MENSAJE
                    //final ConversacionAdapter Adaptador = new UsuarioAdapter(getActivity(), (ArrayList<Mensaje>) Contactos);
                    //Lista.setAdapter(Adaptador);

                }else{
                    Toast.makeText(getActivity().getBaseContext(),"No Hay ningun mensaje para enviar",Toast.LENGTH_SHORT).show();
            }
        }});
        return view;
    }

}
