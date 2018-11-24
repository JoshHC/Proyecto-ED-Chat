package com.example.josue.proyecto_ed_chat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ConversacionAdapter extends BaseAdapter
{
    private static LayoutInflater inflater = null;
    List<Mensaje> messages = new ArrayList<Mensaje>();
    Activity activity;

    public ConversacionAdapter(Activity activity, ArrayList<Mensaje> items) {
        this.activity = activity;
        this.messages = items;

        //Inicialización del inflater
        inflater =(LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
    }

    public void add(Mensaje message) {
        this.messages.add(message);
        notifyDataSetChanged(); // to render the list we need to notify
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View Emisor = inflater.inflate(R.layout.my_message,null);
        final View Receptor = inflater.inflate(R.layout.their_message,null);
        TextView mensajeEmisor = (TextView) Emisor.findViewById(R.id.message_body);
        TextView mensajeReceptor = (TextView) Receptor.findViewById(R.id.message_body);
        TextView nombreReceptor = (TextView) Receptor.findViewById(R.id.name);
        Mensaje message = messages.get(position);

        if(message.isPertenecealusuariologeado() == true){

            SDES Cifrado = new SDES();

            String ClaveCifrado = GenerarClaveCifrado(message.getEmisor(), message.getReceptor());
            String mensaje = Cifrado.Descifrar(message.getMensaje(), ClaveCifrado);

            mensajeEmisor.setText(mensaje);
            return Emisor;
        }else{
            mensajeReceptor.setText(message.getMensaje());
            nombreReceptor.setText(message.getReceptor());
            return Receptor;
        }
    }

    // Genera la llave para el Cifrado de Punto a Punto
    private String GenerarClaveCifrado(String Emisor, String Receptor)
    {
        Emisor = Emisor.toUpperCase();
        Receptor = Receptor.toUpperCase();

        String[] Temp = new String[10];
        char[] emisor = Emisor.toCharArray();
        char[] receptor = Receptor.toCharArray();

        // El menor Nombre siempre va de primero
        if(Emisor.compareTo(Receptor) < 0)
        {
            for(int i = 0; i < 5; i++)
            {
                try
                {
                    Temp[i] = String.valueOf(emisor[i]);
                }
                catch (Exception e)
                {
                    Temp[i] = "0";
                }
            }

            for(int i = 5; i < 10; i++)
            {
                try
                {
                    Temp[i] = String.valueOf(receptor[i-5]);
                }
                catch (Exception e)
                {
                    Temp[i] = "0";
                }
            }

        }
        else
        {
            for(int i = 0; i < 5; i++)
            {
                try
                {
                    Temp[i] = String.valueOf(receptor[i]);
                }
                catch (Exception e)
                {
                    Temp[i] = "0";
                }
            }

            for(int i = 5; i < 10; i++)
            {
                try
                {
                    Temp[i] = String.valueOf(emisor[i-5]);
                }
                catch (Exception e)
                {
                    Temp[i] = "0";
                }
            }
        }

        String Clave = "";

        // Si es consonante es 1 y si es vocal 0
        for(int i = 0; i < 10; i++)
        {
            if (Temp[i].equals("A") || Temp[i].equals("E") || Temp[i].equals("I") || Temp[i].equals("O") || Temp[i].equals("U"))
                Clave += "0";
            else
                Clave += "1";
        }

        return Clave;
    }

    }
